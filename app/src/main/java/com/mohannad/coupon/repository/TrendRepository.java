package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.TrendResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendRepository {
    private final String TAG = getClass().getSimpleName();
    private static TrendRepository trendRepository;

    public static TrendRepository newInstance() {
        if (trendRepository == null)
            trendRepository = new TrendRepository();
        return trendRepository;
    }

    public void getTrends(String lang, String deviceToken,
                          ResponseServer<LiveData<TrendResponse>> responseServer) {
        MutableLiveData<TrendResponse> trendResponseMutableLiveData = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getTrends(lang, deviceToken).enqueue(new Callback<TrendResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrendResponse> call, @NonNull Response<TrendResponse> response) {
                trendResponseMutableLiveData.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), trendResponseMutableLiveData);
            }

            @Override
            public void onFailure(@NonNull Call<TrendResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "getTrends onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
