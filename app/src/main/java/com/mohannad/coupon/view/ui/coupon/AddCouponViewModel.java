package com.mohannad.coupon.view.ui.coupon;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.repository.AddCouponRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Utils;

import java.util.List;

public class AddCouponViewModel extends BaseViewModel {
    AddCouponRepository addCouponRepository;
    MutableLiveData<List<CountryResponse.Country>> countries = new MutableLiveData<>();
    public String email;
    public String whatsUp;
    public String store;
    public String brand;
    public String description;
    public String coupon;
    // error messages that will show when add coupon
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorWhatsUp = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorStore = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorBrand = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorDescription = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorCoupon = new MutableLiveData<>();

    public AddCouponViewModel(@NonNull Application application) {
        super(application);
        addCouponRepository = AddCouponRepository.newInstance();
        getCountries();
    }

    // this method will call getCountries from repository to get all countries from server
    public void getCountries() {
        dataLoading.setValue(true);
        addCouponRepository.getCountries(getApplication().getString(R.string.lang),
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

        }
    }
    private boolean validation() {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            errorEmail.setValue(getApplication().getString(R.string.this_filed_is_empty));
            valid = false;
        } else if (!Utils.isEmailValid(email)) {
            errorEmail.setValue(getApplication().getString(R.string.email_invalid));
            valid = false;
        } else {
            errorEmail.setValue(null);
        }
        if (TextUtils.isEmpty(whatsUp)) {
            errorWhatsUp.setValue(getApplication().getString(R.string.this_filed_is_empty));
            valid = false;
        } else if (!Utils.isMobileValid(whatsUp)) {
            errorWhatsUp.setValue(getApplication().getString(R.string.mobile_invalid));
            valid = false;
        } else {
            errorWhatsUp.setValue(null);
        }
        if (TextUtils.isEmpty(store)) {
            errorStore.setValue(true);
            valid = false;
        } else {
            errorStore.setValue(false);
        }
        if (TextUtils.isEmpty(brand)) {
            errorBrand.setValue(true);
            valid = false;
        } else {
            errorBrand.setValue(false);
        }
        if (TextUtils.isEmpty(description)) {
            errorDescription.setValue(true);
            valid = false;
        } else {
            errorDescription.setValue(false);
        }
        if (TextUtils.isEmpty(coupon)) {
            errorCoupon.setValue(true);
            valid = false;
        } else {
            errorCoupon.setValue(false);
        }
        return valid;
    }
}