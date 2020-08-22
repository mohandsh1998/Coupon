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

public class ChangePasswordRepository {
    private final String TAG = getClass().getSimpleName();
    private static ChangePasswordRepository changePasswordRepository;

    public static ChangePasswordRepository newInstance() {
        if (changePasswordRepository == null)
            changePasswordRepository = new ChangePasswordRepository();
        return changePasswordRepository;
    }

    public void changePassword(String lang, String token, String oldPassword, String newPassword,
                               String confirmNewPassword, ResponseServer<MessageResponse> responseServer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.changePassword(lang, token, oldPassword, newPassword, confirmNewPassword).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                responseServer.onSuccess(response.isSuccessful(), response.code(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "ChangePassword onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
