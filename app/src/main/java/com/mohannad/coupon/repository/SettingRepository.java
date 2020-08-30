package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.SettingResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingRepository {
    private final String TAG = getClass().getSimpleName();
    private static SettingRepository settingRepository;

    public static SettingRepository newInstance() {
        if (settingRepository == null)
            settingRepository = new SettingRepository();
        return settingRepository;
    }

    // this method will using to get countries from SERVER SIDE
    public void getCountries(String lang, ResponseServer<LiveData<CountryResponse>> responseServer) {
        MutableLiveData<CountryResponse> countries = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCountries(lang).enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(@NonNull Call<CountryResponse> call, @NonNull Response<CountryResponse> response) {
                countries.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), countries);
            }

            @Override
            public void onFailure(@NonNull Call<CountryResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Countries onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to get setting app from SERVER SIDE
    public void getSetting(String lang, ResponseServer<LiveData<SettingResponse>> responseServer) {
        MutableLiveData<SettingResponse> setting = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getSetting(lang).enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(@NonNull Call<SettingResponse> call, @NonNull Response<SettingResponse> response) {
                setting.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), setting);
            }

            @Override
            public void onFailure(@NonNull Call<SettingResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Setting onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
