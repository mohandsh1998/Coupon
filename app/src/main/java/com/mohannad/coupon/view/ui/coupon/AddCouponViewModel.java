package com.mohannad.coupon.view.ui.coupon;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.AddCouponRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.repository.SettingRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Utils;

import java.util.List;

public class AddCouponViewModel extends BaseViewModel {
    // Store name
    public MutableLiveData<String> storeName = new MutableLiveData<>();
    // Offer
    public MutableLiveData<String> offer = new MutableLiveData<>();
    // code coupon
    public MutableLiveData<String> couponCode = new MutableLiveData<>();
    // description
    public MutableLiveData<String> description = new MutableLiveData<>();
    // store link
    public MutableLiveData<String> storeLink = new MutableLiveData<>();
    // email user
    public MutableLiveData<String> email = new MutableLiveData<>();
    // mobile number
    public MutableLiveData<String> whatsUp = new MutableLiveData<>();
    // id country
    public Integer idCountry = null;


    MutableLiveData<List<CountryResponse.Country>> countries = new MutableLiveData<>();

    // error messages that will show if there problem in coupon data when adding the coupon
    public MutableLiveData<Boolean> errorStore = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorOffer = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorCoupon = new MutableLiveData<>();
    public MutableLiveData<String> errorWhatsUp = new MutableLiveData<>();
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();

    private SettingRepository settingRepository;
    private AddCouponRepository addCouponRepository;

    StorageSharedPreferences sharedPreferences;

    public AddCouponViewModel(@NonNull Application application) {
        super(application);
        addCouponRepository = AddCouponRepository.newInstance();
        settingRepository = SettingRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getCountries();
    }

    // this method will call getCountries from repository to get all countries from server
    public void getCountries() {
        dataLoading.setValue(true);
        settingRepository.getCountries(sharedPreferences.getLanguage(),
                new ResponseServer<LiveData<CountryResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<CountryResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                countries.setValue(response.getValue().getCountries());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

    public void onClick() {
        if (validation()) {
            dataLoading.setValue(true);
            suggestionCoupon();
        }
    }


    public void country(int idCountry) {
        this.idCountry = idCountry;
    }

    public void suggestionCoupon() {
        // call suggestionCoupon from repository
        addCouponRepository.suggestionCoupon(sharedPreferences.getLanguage(), storeName.getValue(), offer.getValue(), couponCode.getValue(), description.getValue(), storeLink.getValue(), email.getValue(), idCountry, whatsUp.getValue(), new ResponseServer<MessageResponse>() {
            @Override
            public void onSuccess(boolean status, int code, MessageResponse response) {
                // hide loading
                dataLoading.setValue(false);
                // if success when connection
                if (status) {
                    if (response.isStatus()) {
                        // success to send coupon
                        toastMessageSuccess.setValue(response.getMessage());
                        success.setValue(true);
                        storeName.setValue(null);
                        storeLink.setValue(null);
                        offer.setValue(null);
                        email.setValue(null);
                        couponCode.setValue(null);
                        whatsUp.setValue(null);
                        description.setValue(null);
                    } else {
                        // failed to send coupon
                        success.setValue(false);
                        toastMessageFailed.setValue(response.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                // hide loading
                dataLoading.setValue(false);
                success.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });

    }

    private boolean validation() {
        boolean valid = true;

        if (TextUtils.isEmpty(storeName.getValue())) {
            errorStore.setValue(true);
            valid = false;
        } else {
            errorStore.setValue(false);
        }

        if (TextUtils.isEmpty(offer.getValue())) {
            errorOffer.setValue(true);
            valid = false;
        } else {
            errorOffer.setValue(false);
        }

        if (TextUtils.isEmpty(couponCode.getValue())) {
            errorCoupon.setValue(true);
            valid = false;
        } else {
            errorCoupon.setValue(false);
        }

        if (!TextUtils.isEmpty(email.getValue()) && !Utils.isEmailValid(email.getValue())) {
            errorEmail.setValue(getApplication().getString(R.string.email_invalid));
            valid = false;
        } else {
            errorEmail.setValue(null);
        }

        if (TextUtils.isEmpty(whatsUp.getValue())) {
            errorWhatsUp.setValue(getApplication().getString(R.string.this_filed_is_empty));
            valid = false;
        } else if (!Utils.isMobileValid(whatsUp.getValue())) {
            errorWhatsUp.setValue(getApplication().getString(R.string.mobile_invalid));
            valid = false;
        } else {
            errorWhatsUp.setValue(null);
        }

        return valid;
    }
}