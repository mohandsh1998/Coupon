package com.mohannad.coupon.view.ui.store.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.utils.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class StoresViewModel extends BaseViewModel {
    private HomeRepository homeRepository;
    public  MutableLiveData<ArrayList<StoreResponse.Store>> stores = new MutableLiveData<>();
    private StorageSharedPreferences mSharedPreferences;

    public StoresViewModel(@NonNull Application application) {
        super(application);
        homeRepository = HomeRepository.newInstance();
        mSharedPreferences = new StorageSharedPreferences(application);
        // call getStores
        getStores();
    }

    // this method will call getStores from repository to get stores from server
    private void getStores() {
        // display loading
        dataLoading.setValue(true);
        // call getStores from repository
        homeRepository.getStores(mSharedPreferences.getLanguage(), mSharedPreferences.getCountryID(), new ResponseServer<LiveData<StoreResponse>>() {
            @Override
            public void onSuccess(boolean status, int code, LiveData<StoreResponse> response) {
                // hide loading
                dataLoading.setValue(false);
                // check if status success
                if (status) {
                    if (response != null && response.getValue() != null) {
                        if (response.getValue().isStatus()) {
                            // after success to get categories will need to edit the value stored in a categoriesTabs to update tabs(UI).
                            stores.setValue(response.getValue().getStores());
                        }
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

}