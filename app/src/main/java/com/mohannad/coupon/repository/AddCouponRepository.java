package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class AddCouponRepository {
    private final String TAG = getClass().getSimpleName();
    private static AddCouponRepository addCouponRepository;

    public static AddCouponRepository newInstance() {
        if (addCouponRepository == null)
            addCouponRepository = new AddCouponRepository();
        return addCouponRepository;
    }

    public void suggestionCoupon(String lang, String email, int countryId,
                                 String couponCode, int companyId, String mobile, String desc,
                                 ResponseServer<MessageResponse> responseServer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.suggestionCoupon(lang, email, countryId, couponCode, companyId, mobile, desc).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                responseServer.onSuccess(response.isSuccessful(), response.code(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "AddCoupon onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
