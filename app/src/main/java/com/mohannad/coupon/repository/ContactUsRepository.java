package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsRepository {
    private final String TAG = getClass().getSimpleName();
    private static ContactUsRepository contactUsRepository;

    public static ContactUsRepository newInstance() {
        if (contactUsRepository == null)
            contactUsRepository = new ContactUsRepository();
        return contactUsRepository;
    }

    public void contactUs(String lang, String subject, String email, String content, ResponseServer<MessageResponse> responseServer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.contactUs(lang, subject, email, content).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                responseServer.onSuccess(response.isSuccessful(), response.code(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Contact us onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
