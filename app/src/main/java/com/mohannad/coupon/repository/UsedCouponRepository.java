package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.UsedCouponResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsedCouponRepository {
    private final String TAG = getClass().getSimpleName();
    private static UsedCouponRepository usedCouponRepository;

    public static UsedCouponRepository newInstance() {
        if (usedCouponRepository == null)
            usedCouponRepository = new UsedCouponRepository();
        return usedCouponRepository;
    }

    public void getUsedCoupons(String lang, String token, ResponseServer<LiveData<UsedCouponResponse>> responseServer) {
        MutableLiveData<UsedCouponResponse> usedCoupons = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getUsedCoupons(lang, token).enqueue(new Callback<UsedCouponResponse>() {
            @Override
            public void onResponse(@NonNull Call<UsedCouponResponse> call, @NonNull Response<UsedCouponResponse> response) {
                usedCoupons.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), usedCoupons);
            }

            @Override
            public void onFailure(@NonNull Call<UsedCouponResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Used Coupon onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
