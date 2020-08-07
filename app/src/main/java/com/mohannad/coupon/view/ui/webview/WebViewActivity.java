package com.mohannad.coupon.view.ui.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityWebViewBinding webViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        webViewBinding.webview.loadUrl(getIntent().getStringExtra("url"));
        webViewBinding.btnBackToAppActivityWebView.setOnClickListener(v -> {
            finish();
        });
    }
}