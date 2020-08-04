package com.mohannad.coupon.view.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.view.ui.main.MainActivity;
import com.tapadoo.alerter.Alerter;

public class SplashActivity extends AppCompatActivity {
    StorageSharedPreferences mStorageSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Checking for first time launch
        mStorageSharedPreferences = new StorageSharedPreferences(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mStorageSharedPreferences.isFirstTimeLaunch()) {
                    launchSplashActivity();
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                }
            }
        }, 2000);

    }

    private void launchSplashActivity() {
        mStorageSharedPreferences.setFirstTimeLaunch(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}