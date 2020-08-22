package com.mohannad.coupon.view.ui.image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityImageBinding;
import com.mohannad.coupon.utils.BaseActivity;

public class ImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityImageBinding imageBinding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        loadImage(this, getIntent().getStringExtra("imageUrl"), imageBinding.imgProduct);
    }
}