package com.mohannad.coupon.view.ui.usedcoupon;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.utils.BaseActivity;

public class UsedCouponActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_coupon);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
    }
}