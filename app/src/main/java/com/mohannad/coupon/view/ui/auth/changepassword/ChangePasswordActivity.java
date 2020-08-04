package com.mohannad.coupon.view.ui.auth.changepassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.utils.BaseActivity;

public class ChangePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
    }
}