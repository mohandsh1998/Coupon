package com.mohannad.coupon.view.ui.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ResponseServer;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.SettingResponse;
import com.mohannad.coupon.repository.AddCouponRepository;
import com.mohannad.coupon.repository.SettingRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Constants;

import java.util.List;

public class SettingViewModel extends BaseViewModel {
    private SettingRepository settingRepository;
    public MutableLiveData<Boolean> successTokenDevice = new MutableLiveData<>();
    MutableLiveData<List<CountryResponse.Country>> countries = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        settingRepository = SettingRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
        getCountries();
    }

    // this method will call getCountries from repository to get all countries from server
    public void getCountries() {
        dataLoading.setValue(true);
        settingRepository.getCountries(sharedPreferences.getLanguage(),
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

    // this method will call addTokenDevice from repository to add token device on server
    public void addTokenDevice() {
        dataLoading.setValue(true);
        settingRepository.addTokenDevice(sharedPreferences.getLanguage(), sharedPreferences.getTokenFCM(), Constants.DEVICE_OS, sharedPreferences.getStatusNotification(),
                new ResponseServer<LiveData<MessageResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<MessageResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                successTokenDevice.setValue(true);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        successTokenDevice.setValue(false);
                        dataLoading.setValue(false);
                        // show error msg
                        toastMessageFailed.setValue(getApplication().getString(R.string.problem_when_try_to_connect));
                    }
                });
    }
}