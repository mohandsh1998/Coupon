package com.mohannad.coupon.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> empty = new MutableLiveData<>();
    public MutableLiveData<Boolean> success = new MutableLiveData<>();
    public MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();
    public MutableLiveData<String> toastMessageSuccess = new MutableLiveData<>();
    public MutableLiveData<String> toastMessageFailed = new MutableLiveData<>();
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }
}
