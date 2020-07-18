package com.mohannad.coupon.view.ui.more;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.utils.BaseViewModel;

public class MoreViewModel extends BaseViewModel {
    public StorageSharedPreferences mSharedPreferences;
    public MoreViewModel(@NonNull Application application) {
        super(application);
        mSharedPreferences = new StorageSharedPreferences(application);
    }
}