package com.mohannad.coupon.view.ui.favorite;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.repository.FavoriteRepository;
import com.mohannad.coupon.repository.HelpRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.List;

public class FavoriteViewModel extends BaseViewModel {
    FavoriteRepository favoriteRepository;
    MutableLiveData<List<FavoriteResponse.Favorite>> favorites = new MutableLiveData<>();
    StorageSharedPreferences storageSharedPreferences;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = FavoriteRepository.newInstance();
        storageSharedPreferences = new StorageSharedPreferences(getApplication());
        getFavorite();
    }

    public void getFavorite() {
        dataLoading.setValue(true);
        favoriteRepository.getFavorites(getApplication().getString(R.string.lang), storageSharedPreferences.getAuthToken(),
                new ResponseServer<LiveData<FavoriteResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<FavoriteResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                favorites.setValue(response.getValue().getFavorites());
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
}