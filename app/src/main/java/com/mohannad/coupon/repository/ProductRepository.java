package com.mohannad.coupon.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.ProductsResponse;
import com.mohannad.coupon.data.network.ApiClient;
import com.mohannad.coupon.data.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepository {
    private final String TAG = getClass().getSimpleName();
    private static ProductRepository productRepository;

    public static ProductRepository newInstance() {
        if (productRepository == null)
            productRepository = new ProductRepository();
        return productRepository;
    }

    public void getAllProductsCategory(String lang, String token, String tokenDevice, int idTitle, int idCategory, ResponseServer<LiveData<ProductsResponse>> responseServer) {
        MutableLiveData<ProductsResponse> products = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllProductsCategory(lang, token, tokenDevice, idTitle, idCategory).enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductsResponse> call, @NonNull Response<ProductsResponse> response) {
                products.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), products);
            }

            @Override
            public void onFailure(@NonNull Call<ProductsResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Product onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void getAllProductsCompany(String lang, String token, String tokenDevice, int idTitle, int idCompany, ResponseServer<LiveData<ProductsResponse>> responseServer) {
        MutableLiveData<ProductsResponse> products = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllProductsCompany(lang, token, tokenDevice, idTitle, idCompany).enqueue(new Callback<ProductsResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductsResponse> call, @NonNull Response<ProductsResponse> response) {
                products.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), products);
            }

            @Override
            public void onFailure(@NonNull Call<ProductsResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Product onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    // this method will using to add or remove the product to favorite on SERVER SIDE
    public void addOrRemoveProductFavorite(String lang, String token, String tokenDevice, int idProduct, ResponseServer<LiveData<MessageResponse>> responseServer) {
        MutableLiveData<MessageResponse> message = new MutableLiveData<>();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.addOrRemoveProductFavorite(lang, token, tokenDevice, idProduct).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                message.setValue(response.body());
                responseServer.onSuccess(response.isSuccessful(), response.code(), message);
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Product onFailure" + call.toString());
                responseServer.onFailure(t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
