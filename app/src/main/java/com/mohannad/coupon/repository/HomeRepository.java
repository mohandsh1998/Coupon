package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CopyCouponResponse;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository {
    private final String TAG = getClass().getSimpleName();
    private static HomeRepository homeRepository;

    public static HomeRepository newInstance() {
        if (homeRepository == null)
            homeRepository = new HomeRepository();
        return homeRepository;
    }

    // this method will using to get stores from SERVER SIDE
    public void getStores(String lang, int idCountry,
                          ResponseServer<LiveData<StoreResponse>> responseServer) {
        MutableLiveData<StoreResponse> stores = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getStores(lang, idCountry).enqueue(new Callback<StoreResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoreResponse> call, @NonNull Response<StoreResponse> response) {
                stores.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), stores);
            }

            @Override
            public void onFailure(@NonNull Call<StoreResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to get companies for store from SERVER SIDE
    public void getCompanies(String lang, int idCountry, int idCategory, ResponseServer<LiveData<CompaniesResponse>> responseServer) {
        MutableLiveData<CompaniesResponse> companies = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCompanies(lang, idCountry, idCategory).enqueue(new Callback<CompaniesResponse>() {
            @Override
            public void onResponse(@NonNull Call<CompaniesResponse> call, @NonNull Response<CompaniesResponse> response) {
                companies.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), companies);
            }

            @Override
            public void onFailure(@NonNull Call<CompaniesResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to get all coupons for category from SERVER SIDE
    public void getAllCouponsStore(String lang, int idCountry, String token, String tokenDevice, int idCategory, int page, ResponseServer<LiveData<CouponHomeResponse>> responseServer) {
        MutableLiveData<CouponHomeResponse> coupons = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllCouponsCategory(lang, idCountry, token, tokenDevice, idCategory, page).enqueue(new Callback<CouponHomeResponse>() {
            @Override
            public void onResponse(@NonNull Call<CouponHomeResponse> call, @NonNull Response<CouponHomeResponse> response) {
                coupons.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), coupons);
            }

            @Override
            public void onFailure(@NonNull Call<CouponHomeResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to get all coupons for company from SERVER SIDE
    public void getAllCouponsCompany(String lang, int idCountry, String token, String tokenDevice, int idCompany, int page, ResponseServer<LiveData<CouponHomeResponse>> responseServer) {
        MutableLiveData<CouponHomeResponse> coupons = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllCouponsCompany(lang, idCountry, token, tokenDevice, idCompany, page).enqueue(new Callback<CouponHomeResponse>() {
            @Override
            public void onResponse(@NonNull Call<CouponHomeResponse> call, @NonNull Response<CouponHomeResponse> response) {
                coupons.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), coupons);
            }

            @Override
            public void onFailure(@NonNull Call<CouponHomeResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to increase the number of times the coupon is copied on SERVER SIDE
    public void copyCoupon(String lang, String token, int idCoupon, ResponseServer<LiveData<CopyCouponResponse>> responseServer) {
        MutableLiveData<CopyCouponResponse> couponCopied = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.copyCoupon(lang, token, idCoupon).enqueue(new Callback<CopyCouponResponse>() {
            @Override
            public void onResponse(@NonNull Call<CopyCouponResponse> call, @NonNull Response<CopyCouponResponse> response) {
                couponCopied.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), couponCopied);
            }

            @Override
            public void onFailure(@NonNull Call<CopyCouponResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will used to review coupon on SERVER SIDE
    public void reviewCoupon(String lang, String token, int idCoupon, int isGood, ResponseServer<LiveData<MessageResponse>> responseServer) {
        MutableLiveData<MessageResponse> reviewCoupon = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.reviewCoupon(lang, token, idCoupon, isGood).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                reviewCoupon.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), reviewCoupon);
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to add or remove the coupon to favorite on SERVER SIDE
    public void addOrRemoveCouponFavorite(String lang, String token, String tokenDevice, int idCoupon, ResponseServer<LiveData<MessageResponse>> responseServer) {
        MutableLiveData<MessageResponse> message = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.addOrRemoveCouponFavorite(lang, token, tokenDevice, idCoupon).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                message.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), message);
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Home onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
