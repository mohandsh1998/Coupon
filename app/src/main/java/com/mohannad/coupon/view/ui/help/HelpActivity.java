package com.mohannad.coupon.view.ui.help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.utils.BaseActivity;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
    }
}