package com.mohannad.coupon.view.ui.trend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CopyCouponResponse;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.CouponsTrendResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.TrendResponse;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.repository.TrendRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class TrendViewModel extends BaseViewModel {
    TrendRepository trendRepository;
    HomeRepository homeRepository;
    MutableLiveData<List<Coupon>> trendsLiveData = new MutableLiveData<>();
    MutableLiveData<List<Coupon>> couponTrendsLiveData = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;
    // add to favorite
    public MutableLiveData<Boolean> successAddOrRemoveToFavorite = new MutableLiveData<>();

    public TrendViewModel(@NonNull Application application) {
        super(application);
        trendRepository = TrendRepository.newInstance();
        homeRepository = HomeRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
    }

    public void getTrends() {
        dataLoading.setValue(true);
        trendRepository.getTrends(sharedPreferences.getLanguage(), sharedPreferences.getTokenFCM(), sharedPreferences.getCountryID(), new ResponseServer<LiveData<TrendResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<TrendResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        trendsLiveData.setValue(response.getValue().getTitles());
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

    public void getCouponTrends(int trendID) {
        dataLoading.setValue(true);
        trendRepository.trendCoupon(sharedPreferences.getLanguage(),sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), sharedPreferences.getCountryID(), trendID,new ResponseServer<LiveData<CouponsTrendResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CouponsTrendResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        couponTrendsLiveData.setValue(response.getValue().getCoupons());
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

    // this method will call copyCoupon  from repository to increase the number of times the coupon is copied on SERVER
    public void copyCoupon(int idCoupon) {
        // call copyCoupon from repository
        homeRepository.copyCoupon(sharedPreferences.getLanguage(), sharedPreferences.getAuthToken(), idCoupon, new ResponseServer<LiveData<CopyCouponResponse>>() {
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
        homeRepository.reviewCoupon(sharedPreferences.getLanguage(), sharedPreferences.getAuthToken(), idCoupon, isGood, new ResponseServer<LiveData<MessageResponse>>() {
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
        homeRepository.addOrRemoveCouponFavorite(sharedPreferences.getLanguage(),
                sharedPreferences.getAuthToken(), sharedPreferences.getTokenFCM(), idCoupon, new ResponseServer<LiveData<MessageResponse>>() {
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
