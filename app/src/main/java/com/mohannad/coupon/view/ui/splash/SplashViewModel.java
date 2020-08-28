package com.mohannad.coupon.view.ui.splash;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.repository.AddCouponRepository;
import com.mohannad.coupon.repository.HomeRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Utils;

import java.util.List;

public class SplashViewModel extends BaseViewModel {
    private AddCouponRepository addCouponRepository;
    MutableLiveData<List<CountryResponse.Country>> countries = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;
    public SplashViewModel(@NonNull Application application) {
        super(application);
        addCouponRepository = AddCouponRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getCountries();
    }

    // this method will call getCountries from repository to get all countries from server
    public void getCountries() {
        dataLoading.setValue(true);
        addCouponRepository.getCountries(sharedPreferences.getLanguage(),
                new ResponseServer<LiveData<CountryResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<CountryResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                countries.setValue(response.getValue().getCountries());
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