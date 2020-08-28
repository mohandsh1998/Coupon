package com.mohannad.coupon.view.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.main.MainActivity;

public class WelcomeActivity extends BaseActivity {
    StorageSharedPreferences mStorageSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mStorageSharedPreferences = new StorageSharedPreferences(this);
    }

    public void start(View view) {
        mStorageSharedPreferences.setFirstTimeLaunch(false);
        startActivity(new Intent(this, LanguageAndCountryActivity.class));
    }
}