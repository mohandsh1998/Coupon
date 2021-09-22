package com.mohannad.coupon.view.ui.splash;

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
import com.mohannad.coupon.repository.SettingRepository;
import com.mohannad.coupon.utils.BaseViewModel;
import com.mohannad.coupon.utils.Constants;

import java.util.List;

public class SplashViewModel extends BaseViewModel {
    private SettingRepository settingRepository;
    public MutableLiveData<Boolean> successTokenDevice = new MutableLiveData<>();
    MutableLiveData<List<CountryResponse.Country>> countries = new MutableLiveData<>();
    StorageSharedPreferences sharedPreferences;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        settingRepository = SettingRepository.newInstance();
        sharedPreferences = new StorageSharedPreferences(getApplication());
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

    // this method will call getSetting from repository to get setting app from server
    public void getSetting() {
        dataLoading.setValue(true);
        settingRepository.getSetting(sharedPreferences.getLanguage(),
                new ResponseServer<LiveData<SettingResponse>>() {
                    @Override
                    public void onSuccess(boolean status, int code, LiveData<SettingResponse> response) {
                        dataLoading.setValue(false);
                        if (response != null && response.getValue() != null) {
                            if (response.getValue().isStatus()) {
                                success.setValue(true);
                                sharedPreferences.saveIntstagram(response.getValue().getSetting().getInstagram());
                                sharedPreferences.saveSnapChat(response.getValue().getSetting().getSnapchat());
                                sharedPreferences.saveTelegram(response.getValue().getSetting().getTelegram());
                                sharedPreferences.saveWhatsUp(response.getValue().getSetting().getWhatsapp());
                                sharedPreferences.saveAdsTitle(response.getValue().getSetting().getTitleAds());
                            }
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        success.setValue(false);
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