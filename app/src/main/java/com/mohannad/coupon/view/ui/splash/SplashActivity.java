package com.mohannad.coupon.view.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.databinding.ActivitySettingBinding;
import com.mohannad.coupon.databinding.ActivitySplashBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.main.MainActivity;

public class SplashActivity extends BaseActivity {
    StorageSharedPreferences mStorageSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySplashBinding settingBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        mStorageSharedPreferences = new StorageSharedPreferences(this);
        SplashViewModel model = new ViewModelProvider(this).get(SplashViewModel.class);
        model.getSetting();
        model.success.observe(this, success -> {
            // Checking for first time launch
            if (!mStorageSharedPreferences.isFirstTimeLaunch()) {
                launchSplashActivity();
            } else {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
        });

//        final Handler handler = new Handler();
//        handler.postDelayed(() -> {
//            // Checking for first time launch
//            if (!mStorageSharedPreferences.isFirstTimeLaunch()) {
//                launchSplashActivity();
//                finish();
//            } else {
//                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
//            }
//        }, 2000);

        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(settingBinding.lyContainer, msg);
        });
    }

    private void launchSplashActivity() {
        mStorageSharedPreferences.setFirstTimeLaunch(false);
        // check if user selected country or not
        if (!TextUtils.isEmpty(mStorageSharedPreferences.getCountryName()) && mStorageSharedPreferences.getCountryID() != -1)
            // if selected -> open main activity
            startActivity(new Intent(this, MainActivity.class));
        else {
            // if not -> open Language And Country activity to select by user
            startActivity(new Intent(SplashActivity.this, LanguageAndCountryActivity.class));
        }
        finish();
    }
}