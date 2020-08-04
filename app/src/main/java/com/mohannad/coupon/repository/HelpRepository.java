package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpRepository {
    private final String TAG = getClass().getSimpleName();
    private static HelpRepository helpRepository;

    public static HelpRepository newInstance() {
        if (helpRepository == null)
            helpRepository = new HelpRepository();
        return helpRepository;
    }

    public void getHelpContent(String lang, ResponseServer<LiveData<HelpResponse>> responseServer) {
        MutableLiveData<HelpResponse> helpContent = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getHelpContents(lang).enqueue(new Callback<HelpResponse>() {
            @Override
            public void onResponse(@NonNull Call<HelpResponse> call, @NonNull Response<HelpResponse> response) {
                helpContent.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), helpContent);
            }

            @Override
            public void onFailure(@NonNull Call<HelpResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Help onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
