package com.mohannad.coupon.view.ui.splash;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.WindowManager;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.databinding.ActivitySplashBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.main.view.MainActivity;
import com.mohannad.coupon.view.ui.store.view.StoresActivity;

public class SplashActivity extends BaseActivity {
    StorageSharedPreferences mStorageSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ActivitySplashBinding settingBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        changeToolbarAndStatusBar(R.color.gray7, null);
        mStorageSharedPreferences = new StorageSharedPreferences(this);
        SplashViewModel model = new ViewModelProvider(this).get(SplashViewModel.class);
        model.success.observe(this, success -> {
            // Checking for first time launch
            if (!mStorageSharedPreferences.isFirstTimeLaunch()) {
                launchSplashActivity();
            } else {
                startActivity(new Intent(this, LanguageAndCountryActivity.class));
                finish();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(model::getSetting, 2500);

        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(settingBinding.lyContainer, msg);
        });
    }

    private void launchSplashActivity() {
        mStorageSharedPreferences.setFirstTimeLaunch(false);
        // check if user selected country or not
        if (!TextUtils.isEmpty(mStorageSharedPreferences.getCountryName()) && mStorageSharedPreferences.getCountryID() != -1)
            // if selected -> open main activity
            startActivity(new Intent(this, StoresActivity.class));
        else {
            // if not -> open Language And Country activity to select by user
            startActivity(new Intent(SplashActivity.this, LanguageAndCountryActivity.class));
        }
        finish();
    }
}