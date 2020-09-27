package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.SearchResponse;
import com.mohannad.coupon.data.model.UsedCouponResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {
    private final String TAG = getClass().getSimpleName();
    private static SearchRepository searchRepository;

    public static SearchRepository newInstance() {
        if (searchRepository == null)
            searchRepository = new SearchRepository();
        return searchRepository;
    }

    public void searchCoupon(String lang, String token, String tokenDevice, int countryId, String word, int page, ResponseServer<LiveData<SearchResponse>> responseServer) {
        MutableLiveData<SearchResponse> resultsCoupons = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchCoupon(lang, token, tokenDevice, countryId, word, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                resultsCoupons.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), resultsCoupons);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Search onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void filterCoupon(String lang, String token, String tokenDevice, int countryId, Integer idStore, Integer idCompany, String filterSpecific, int page, ResponseServer<LiveData<SearchResponse>> responseServer) {
        MutableLiveData<SearchResponse> resultsCoupons = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.filterCoupons(lang, token, tokenDevice, countryId, idStore, idCompany, filterSpecific, page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                resultsCoupons.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), resultsCoupons);
            }

            @Override
            public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Search onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
