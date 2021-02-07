package com.mohannad.coupon.view.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.databinding.ActivityWelcomeBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.main.MainActivity;

public class WelcomeActivity extends BaseActivity {
    StorageSharedPreferences mStorageSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWelcomeBinding activityWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        changeToolbarAndStatusBar(R.color.gray7, null);
        Glide.with(this)
                .load(R.drawable.logo)
                .into(activityWelcomeBinding.imgLogo);
        mStorageSharedPreferences = new StorageSharedPreferences(this);
    }

    public void start(View view) {
        mStorageSharedPreferences.setFirstTimeLaunch(false);
        startActivity(new Intent(this, LanguageAndCountryActivity.class));
        finish();
    }
}