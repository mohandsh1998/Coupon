package com.mohannad.coupon.view.ui.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.ProductsResponse;
import com.mohannad.coupon.repository.ProductRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class ProductsViewModel extends BaseViewModel {
    ProductRepository productRepository;
    MutableLiveData<List<ProductsResponse.Product>> products = new MutableLiveData<>();
    StorageSharedPreferences storageSharedPreferences;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        productRepository = ProductRepository.newInstance();
        storageSharedPreferences = new StorageSharedPreferences(getApplication());
    }

    // this method will call getAllProductsCategory from repository to get all products to category(idCategory) from server
    public void getProductsCategory(int idTitle, int idCategory) {
        // show loading for products
        dataLoading.setValue(true);
        // call getAllProductsCategory from repository
        productRepository.getAllProductsCategory(storageSharedPreferences.getLanguage(),
                storageSharedPreferences.getAuthToken(), storageSharedPreferences.getTokenFCM(), idTitle, idCategory,
                new ResponseServer<LiveData<ProductsResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<ProductsResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                products.setValue(response.getValue().getProducts());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        // hide loading
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

    // this method will call getAllProductsCompany from repository to get all products to company(idCompany) from server
    public void getProductsCompany(int idTitle, int idCompany) {
        // show loading for products
        dataLoading.setValue(true);
        // call getAllProductsCompany from repository
        productRepository.getAllProductsCompany(storageSharedPreferences.getLanguage(),
                storageSharedPreferences.getAuthToken(), storageSharedPreferences.getTokenFCM(), idTitle, idCompany,
                new ResponseServer<LiveData<ProductsResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<ProductsResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                products.setValue(response.getValue().getProducts());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        // hide loading
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

    // this method will call addOrRemoveProductFavorite from repository to add or remove the product to favorite on server
    public void addOrRemoveProductFavorite(int idProduct) {
        // call addOrRemoveProductFavorite from repository
        productRepository.addOrRemoveProductFavorite(storageSharedPreferences.getLanguage(),
                storageSharedPreferences.getAuthToken(), storageSharedPreferences.getTokenFCM(), idProduct, new ResponseServer<LiveData<MessageResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
                // check if status success
                if (status) {
                    if (response != null && response.getValue() != null) {
                        if (response.getValue().isStatus()) {
                            toastMessageSuccess.setValue(response.getValue().getMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(String message) {
                // show error msg
                toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
            }
        });
    }
}
