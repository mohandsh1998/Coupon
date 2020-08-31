package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    private final String TAG = getClass().getSimpleName();
    private static LoginRepository loginRepository;

    public static LoginRepository newInstance() {
        if (loginRepository == null)
            loginRepository = new LoginRepository();
        return loginRepository;
    }

    public void login(String lang, String email, String password,
                      String fcmToken, String deviceType, int allowNotification, ResponseServer<AuthResponse> responseServer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.loginUser(lang, email, password, fcmToken, deviceType, allowNotification).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                responseServer.onSuccess(response.isSuccessful(), response.code(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Login onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void resetPassword(String lang, String email, ResponseServer<MessageResponse> responseServer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.resetPassword(lang, email).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                responseServer.onSuccess(response.isSuccessful(), response.code(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Reset onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
