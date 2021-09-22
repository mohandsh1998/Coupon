package com.mohannad.coupon.view.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CopyCouponResponse;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class HomeViewModel extends BaseViewModel {
    private HomeRepository homeRepository;
    // loading tabs
    public MutableLiveData<Boolean> dataLoadingTabs = new MutableLiveData<>();
    // loading companies
    public MutableLiveData<Boolean> dataLoadingCompanies = new MutableLiveData<>();
    // loading coupons
    public MutableLiveData<Boolean> dataLoadingCoupons = new MutableLiveData<>();
    // add to favorite
    public MutableLiveData<Boolean> successAddOrRemoveToFavorite = new MutableLiveData<>();
    // categories that will show in tabs
    MutableLiveData<List<StoreResponse.Store>> categoriesTabs = new MutableLiveData<>();
    // Companies that will show in category
    MutableLiveData<List<CompaniesResponse.Company>> companies = new MutableLiveData<>();
    // coupons that will show in category
    MutableLiveData<List<Coupon>> coupons = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    public int currentPageCoupons;
    private StorageSharedPreferences mSharedPreferences;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepository = HomeRepository.newInstance();
        mSharedPreferences = new StorageSharedPreferences(application);
        // call getCategoriesTabs
        getCategoriesTabs();
    }

    // Categories --> Tabs
    // this method will call getCategoriesTabs from repository to get categories tabs from server
    private void getCategoriesTabs() {
        // show loading for tab
        dataLoadingTabs.setValue(true);
        // call getCategoriesTabs from repository
        homeRepository.getStores(mSharedPreferences.getLanguage(), mSharedPreferences.getCountryID(), new ResponseServer<LiveData<StoreResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<StoreResponse> response) {
                // hide loading
                dataLoadingTabs.setValue(false);
                // check if status success
                if (status) {
                    if (response != null && response.getValue() != null) {
                        if (response.getValue().isStatus()) {
                            // after success to get categories will need to edit the value stored in a categoriesTabs to update tabs(UI).
                            categoriesTabs.setValue(response.getValue().getStores());
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                // hide loading
                dataLoadingTabs.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }

    // this method will call getCompanies from repository to get companies to category from server
    public void getCompanies(int idCategory) {
        // show loading for companies
        dataLoadingCompanies.setValue(true);
        // call getCompanies from repository
        homeRepository.getCompanies(mSharedPreferences.getLanguage(), mSharedPreferences.getCountryID(), idCategory, new ResponseServer<LiveData<CompaniesResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CompaniesResponse> response) {
                // hide loading
                dataLoadingCompanies.setValue(false);
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
                // hide loading
                dataLoadingCompanies.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }

    // this method will call getAllCouponsStore from repository to get all coupons to store from server
    public void getAllCouponsStore(int idCategory, int page) {
        currentPageCoupons = page;
        // show loading for coupons
        dataLoadingCoupons.setValue(true);
        // call getAllCouponsCategory from repository
        homeRepository.getAllCouponsStore(mSharedPreferences.getLanguage(), mSharedPreferences.getCountryID(), mSharedPreferences.getAuthToken(), mSharedPreferences.getTokenFCM(), idCategory, page, new ResponseServer<LiveData<CouponHomeResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CouponHomeResponse> response) {
                // hide loading
                dataLoadingCoupons.setValue(false);
                // check if status success
                if (status) {
                    if (response != null && response.getValue() != null) {
                        if (response.getValue().isStatus()) {
                            // after success to get companies will need to edit the value stored in a companies to update UI.
                            coupons.setValue(response.getValue().getItems().getCoupons());
                            isLastPage.setValue(page == response.getValue().getItems().getLastPage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                // hide loading
                dataLoadingCoupons.setValue(false);
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }

    // this method will call getAllCouponsCompany from repository to get all coupons to company from server
    public void getCouponsCompany(int idCompany, int page) {
        currentPageCoupons = page;
        // show loading for coupons
        dataLoadingCoupons.setValue(true);
        // call getAllCouponsCompany from repository
        homeRepository.getAllCouponsCompany(mSharedPreferences.getLanguage(), mSharedPreferences.getCountryID(),
                mSharedPreferences.getAuthToken(), mSharedPreferences.getTokenFCM(), idCompany, page, new ResponseServer<LiveData<CouponHomeResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<CouponHomeResponse> response) {
                        // hide loading
                        dataLoadingCoupons.setValue(false);
                        // check if status success
                        if (status) {
                            if (response != null && response.getValue() != null) {
                                if (response.getValue().isStatus()) {
                                    // after success to get companies will need to edit the value stored in a companies to update UI.
                                    coupons.setValue(response.getValue().getItems().getCoupons());
                                    isLastPage.setValue(page == response.getValue().getItems().getLastPage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        // hide loading
                        dataLoadingCoupons.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

    // this method will call copyCoupon  from repository to increase the number of times the coupon is copied on SERVER
    public void copyCoupon(int idCoupon) {
        // call copyCoupon from repository
        homeRepository.copyCoupon(mSharedPreferences.getLanguage(), mSharedPreferences.getAuthToken(), idCoupon, new ResponseServer<LiveData<CopyCouponResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CopyCouponResponse> response) {
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    // this method will call reviewCoupon  from repository to review coupon on SERVER
    public void reviewCoupon(int idCoupon, int isGood) {
        // call reviewCoupon from repository
        homeRepository.reviewCoupon(mSharedPreferences.getLanguage(), mSharedPreferences.getAuthToken(), idCoupon, isGood, new ResponseServer<LiveData<MessageResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    // this method will call addOrRemoveCouponFavorite from repository to add or remove the coupon to favorite on server
    public void addOrRemoveCouponFavorite(int idCoupon) {
        // call addOrRemoveCouponFavorite from repository
        homeRepository.addOrRemoveCouponFavorite(mSharedPreferences.getLanguage(),
                mSharedPreferences.getAuthToken(), mSharedPreferences.getTokenFCM(), idCoupon, new ResponseServer<LiveData<MessageResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
                        // check if status success
                        if (status) {
                            if (response != null && response.getValue() != null) {
                                if (response.getValue().isStatus()) {
                                    successAddOrRemoveToFavorite.setValue(true);
//                            toastMessageSuccess.setValue(response.getValue().getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        successAddOrRemoveToFavorite.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

}