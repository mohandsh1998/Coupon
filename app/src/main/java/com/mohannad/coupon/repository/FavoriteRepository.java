package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRepository {
    private final String TAG = getClass().getSimpleName();
    private static FavoriteRepository favoriteRepository;

    public static FavoriteRepository newInstance() {
        if (favoriteRepository == null)
            favoriteRepository = new FavoriteRepository();
        return favoriteRepository;
    }

    public void getFavorites(String lang, String tokenUser, ResponseServer<LiveData<FavoriteResponse>> responseServer) {
        MutableLiveData<FavoriteResponse> favoriteData = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getFavorites(lang, tokenUser).enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(@NonNull Call<FavoriteResponse> call, @NonNull Response<FavoriteResponse> response) {
                favoriteData.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), favoriteData);
            }

            @Override
            public void onFailure(@NonNull Call<FavoriteResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Help onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
