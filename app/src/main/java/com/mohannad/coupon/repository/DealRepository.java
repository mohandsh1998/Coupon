package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.DealCouponsResponse;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.SearchResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealRepository {
    private final String TAG = getClass().getSimpleName();
    private static DealRepository dealRepository;

    public static DealRepository newInstance() {
        if (dealRepository == null)
            dealRepository = new DealRepository();
        return dealRepository;
    }

    public void getDeals(String lang, int idCountry, int page, ResponseServer<LiveData<DealResponse>> responseServer) {
        MutableLiveData<DealResponse> deals = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getDeals(lang, idCountry, page).enqueue(new Callback<DealResponse>() {
            @Override
            public void onResponse(@NonNull Call<DealResponse> call, @NonNull Response<DealResponse> response) {
                deals.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), deals);
            }

            @Override
            public void onFailure(@NonNull Call<DealResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Help onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void dealCoupons(String lang, String token, String tokenDevice, int countryId, int idDeal, ResponseServer<LiveData<DealCouponsResponse>> responseServer) {
        MutableLiveData<DealCouponsResponse> resultsCoupons = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.dealCoupons(lang, token, tokenDevice, countryId, idDeal).enqueue(new Callback<DealCouponsResponse>() {
            @Override
            public void onResponse(@NonNull Call<DealCouponsResponse> call, @NonNull Response<DealCouponsResponse> response) {
                resultsCoupons.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), resultsCoupons);
            }

            @Override
            public void onFailure(@NonNull Call<DealCouponsResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Deal Coupons onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
