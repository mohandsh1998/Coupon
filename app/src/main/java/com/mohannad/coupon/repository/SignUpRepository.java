package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Header;

public class SignUpRepository {
    private final String TAG = getClass().getSimpleName();
    private static SignUpRepository signUpRepository;

    public static SignUpRepository newInstance() {
        if (signUpRepository == null)
            signUpRepository = new SignUpRepository();
        return signUpRepository;
    }

    public void signUp(String lang, String name, String email, String password,
                                                String fcmToken, String deviceType, int allowNotification, ResponseServer<AuthResponse> responseServer) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.registerUser(lang, name, email, password, fcmToken, deviceType, allowNotification).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                responseServer.onSuccess(response.isSuccessful(), response.code(), response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Sign Up onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
