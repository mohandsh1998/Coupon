package com.mohannad.coupon.view.ui.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CopyCouponResponse;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.FavoriteRepository;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.repository.ProductRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class FavoriteViewModel extends BaseViewModel {
    FavoriteRepository favoriteRepository;
    ProductRepository productRepository;
    HomeRepository homeRepository;
    MutableLiveData<List<FavoriteResponse.Favorite>> favorites = new MutableLiveData<>();
    StorageSharedPreferences storageSharedPreferences;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = FavoriteRepository.newInstance();
        productRepository = ProductRepository.newInstance();
        homeRepository = HomeRepository.newInstance();
        storageSharedPreferences = new StorageSharedPreferences(getApplication());
        getFavorite();
    }

    public void getFavorite() {
        dataLoading.setValue(true);
        favoriteRepository.getFavorites(storageSharedPreferences.getLanguage(),
                storageSharedPreferences.getAuthToken(), storageSharedPreferences.getTokenFCM(),
                new ResponseServer<LiveData<FavoriteResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<FavoriteResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                favorites.setValue(response.getValue().getFavorites());
                                if (response.getValue().getFavorites().size() == 0)
                                    empty.setValue(true);
                                else empty.setValue(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }

    // this method will call addOrRemoveCouponFavorite from repository to add or remove the coupon to favorite on server
    public void addOrRemoveCouponFavorite(int idCoupon) {
        // call addOrRemoveCouponFavorite from repository
        homeRepository.addOrRemoveCouponFavorite(getApplication().getString(R.string.lang),
                storageSharedPreferences.getAuthToken(), storageSharedPreferences.getTokenFCM(), idCoupon, new ResponseServer<LiveData<MessageResponse>>() {
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

    // this method will call addOrRemoveProductFavorite from repository to add or remove the product to favorite on server
    public void addOrRemoveProductFavorite(int idProduct) {
        // call addOrRemoveProductFavorite from repository
        productRepository.addOrRemoveProductFavorite(getApplication().getString(R.string.lang),
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


    // this method will call copyCoupon  from repository to increase the number of times the coupon is copied on SERVER
    public void copyCoupon(int idCoupon) {
        // call copyCoupon from repository
        homeRepository.copyCoupon(storageSharedPreferences.getLanguage(), storageSharedPreferences.getAuthToken(), idCoupon, new ResponseServer<LiveData<CopyCouponResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<CopyCouponResponse> response) {
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }

    // this method will call reviewCoupon  from repository to review coupon on SERVER
    public void reviewCoupon(int idCoupon, int isGood) {
        // call reviewCoupon from repository
        homeRepository.reviewCoupon(storageSharedPreferences.getLanguage(), storageSharedPreferences.getAuthToken(), idCoupon, isGood, new ResponseServer<LiveData<MessageResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
            }

            @Override
            public void onFailure(String message) {
            }
        });
    }
}