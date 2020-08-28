package com.mohannad.coupon.view.ui.coupon;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.AddCouponRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Utils;

import java.util.List;

public class AddCouponViewModel extends BaseViewModel {
    AddCouponRepository addCouponRepository;
    private HomeRepository homeRepository;
    MutableLiveData<List<CountryResponse.Country>> countries = new MutableLiveData<>();
    // email user
    public String email;
    // mobile number
    public String whatsUp;
    // id country
    public Integer idCountry = null;
    // id category
    public Integer idCategory = null;
    // id company
    public Integer idCompany = null;
    // description
    public String description;
    // code coupon
    public String coupon;
    // error messages that will show if there problem in coupon data when adding the coupon
    public MutableLiveData<String> errorEmail = new MutableLiveData<>();
    public MutableLiveData<String> errorWhatsUp = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorCategory = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorCompany = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorDescription = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorCoupon = new MutableLiveData<>();
    // categories that will show in spinner
    MutableLiveData<List<CategoriesResponse.Category>> categories = new MutableLiveData<>();
    // companies that will show in spinner
    MutableLiveData<List<CompaniesResponse.Company>> companies = new MutableLiveData<>();
    // tag finish loading
    boolean countryFinish = false, categoryFinish = false, companyFinish = false;
    StorageSharedPreferences sharedPreferences;
    public AddCouponViewModel(@NonNull Application application) {
        super(application);
        addCouponRepository = AddCouponRepository.newInstance();
        homeRepository = HomeRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getCountries();
        getCategories();
    }

    // this method will call getCountries from repository to get all countries from server
    public void getCountries() {
        dataLoading.setValue(true);
        addCouponRepository.getCountries(sharedPreferences.getLanguage(),
                new ResponseServer<LiveData<CountryResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<CountryResponse> response) {
                        countryFinish = true;
                        if (categoryFinish && companyFinish)
                            dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                countries.setValue(response.getValue().getCountries());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        countryFinish = true;
                        if (categoryFinish && companyFinish)
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

    // Categories
    // this method will call getCategoriesTabs from repository to get categories from server
    private void getCategories() {
        // show loading
        dataLoading.setValue(true);
        // call getCategoriesTabs from repository
        homeRepository.getCategoriesTabs(sharedPreferences.getLanguage(), new ResponseServer<LiveData<CategoriesResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CategoriesResponse> response) {
                categoryFinish = true;
                if (countryFinish && companyFinish)
                    // hide loading
                    dataLoading.setValue(false);
                // check if status success
                if (status) {
                    if (response != null && response.getValue() != null) {
                        if (response.getValue().isStatus()) {
                            // after success to get categories will need to edit the value stored in a categories to update spinner(UI).
                            categories.setValue(response.getValue().getCategories());
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                categoryFinish = true;
                if (countryFinish && companyFinish)
                    // hide loading
                    dataLoading.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }

    // this method will call getCompanies from repository to get companies to category from server
    public void getCompanies(int idCategory) {
        // show loading
        dataLoading.setValue(true);
        // call getCompanies from repository
        homeRepository.getCompanies(sharedPreferences.getLanguage(), 1, idCategory, new ResponseServer<LiveData<CompaniesResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CompaniesResponse> response) {
                companyFinish = true;
                if (countryFinish && categoryFinish)
                    // hide loading
                    dataLoading.setValue(false);
                // hide loading
                dataLoading.setValue(false);
                // check if status success
                if (status) {
                    if (response != null && response.getValue() != null) {
                        if (response.getValue().isStatus()) {
                            // after success to get companies will need to edit the value stored in a companies to update UI.
                            companies.setValue(response.getValue().getCompanies());
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                companyFinish = true;
                if (countryFinish && categoryFinish)
                    // hide loading
                    dataLoading.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }

    public void category(int idCategory) {
        this.idCategory = idCategory;
    }

    public void company(int idCompany) {
        this.idCompany = idCompany;
    }

    public void country(int idCountry) {
        this.idCountry = idCountry;
    }

    public void suggestionCoupon() {
        // call suggestionCoupon from repository
        addCouponRepository.suggestionCoupon(sharedPreferences.getLanguage(), email, idCountry,
                coupon, idCompany, whatsUp, description, new ResponseServer<MessageResponse>() {
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
        if (idCategory == null) {
            errorCategory.setValue(true);
            valid = false;
        } else {
            errorCategory.setValue(false);
        }
        if (idCompany == null) {
            errorCompany.setValue(true);
            valid = false;
        } else {
            errorCompany.setValue(false);
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