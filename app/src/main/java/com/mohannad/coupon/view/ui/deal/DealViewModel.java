package com.mohannad.coupon.view.ui.deal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.DealCouponsResponse;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.UsedCouponResponse;
import com.mohannad.coupon.repository.DealRepository;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class DealViewModel extends BaseViewModel {
    private DealRepository dealRepository;
    private HomeRepository homeRepository;
    MutableLiveData<List<DealResponse.DealItem>> deals = new MutableLiveData<>();
    MutableLiveData<List<DealResponse.DealsAds>> adsDeal = new MutableLiveData<>();
    MutableLiveData<List<Coupon>> coupons = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLastPage = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;
    public int currentPageDeals;

    public DealViewModel(@NonNull Application application) {
        super(application);
        dealRepository = DealRepository.newInstance();
        homeRepository = HomeRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
    }

    public void getDeals(int page) {
        currentPageDeals = page;
        dataLoading.setValue(true);
        dealRepository.getDeals(sharedPreferences.getLanguage(), sharedPreferences.getCountryID(), page, new ResponseServer<LiveData<DealResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<DealResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        deals.setValue(response.getValue().getDeal().getDealItems());
                        adsDeal.setValue(response.getValue().getDealsAds());
                        isLastPage.setValue(page == response.getValue().getDeal().getLastPage());
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

    // this method will call dealCoupons from repository to get all coupons for deal from server
    public void getDealCoupons(int idDeal) {
        dataLoading.setValue(true);
        dealRepository.dealCoupons(sharedPreferences.getLanguage(),
                sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), sharedPreferences.getCountryID(), idDeal, new ResponseServer<LiveData<DealCouponsResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<DealCouponsResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                coupons.setValue(response.getValue().getCoupons());
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

    // this method will call addOrRemoveCouponFavorite from repository to add or remove the coupon to favorite on server
    public void addOrRemoveCouponFavorite(int idCoupon) {
        // call addOrRemoveCouponFavorite from repository
        homeRepository.addOrRemoveCouponFavorite(sharedPreferences.getLanguage(),
                sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), idCoupon, new ResponseServer<LiveData<MessageResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
                        // check if status success
                        if (status) {
                            if (response != null && response.getValue() != null) {
                                if (response.getValue().isStatus()) {
                                    toastMessageSuccess.setValue(response.getValue().getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

}
