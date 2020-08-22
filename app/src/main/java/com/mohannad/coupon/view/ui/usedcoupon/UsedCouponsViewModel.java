package com.mohannad.coupon.view.ui.usedcoupon;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.UsedCouponResponse;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.repository.UsedCouponRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class UsedCouponsViewModel extends BaseViewModel {
    private HomeRepository homeRepository;
    UsedCouponRepository usedCouponRepository;
    MutableLiveData<List<Coupon>> usedCoupons = new MutableLiveData<>();
    private StorageSharedPreferences sharedPreferences;

    public UsedCouponsViewModel(@NonNull Application application) {
        super(application);
        homeRepository = HomeRepository.newInstance();
        usedCouponRepository = UsedCouponRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(application);
        getUsedCoupons();
    }
    // this method will call getUsedCoupons from repository to get all used coupons by user from server
    private void getUsedCoupons() {
        dataLoading.setValue(true);
        usedCouponRepository.getUsedCoupons(getApplication().getString(R.string.lang), sharedPreferences.getAuthToken(), new ResponseServer<LiveData<UsedCouponResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<UsedCouponResponse> response) {
                dataLoading.setValue(false);
                if (response != null && response.getValue() != null) {
                    if (response.getValue().isStatus()) {
                        usedCoupons.setValue(response.getValue().getCoupons());
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
        homeRepository.addOrRemoveCouponFavorite(getApplication().getString(R.string.lang), sharedPreferences.getAuthToken(), idCoupon, new ResponseServer<LiveData<MessageResponse>>() {
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