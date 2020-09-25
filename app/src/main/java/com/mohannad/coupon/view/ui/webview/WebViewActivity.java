package com.mohannad.coupon.view.ui.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityWebViewBinding;
import com.mohannad.coupon.utils.BaseActivity;

public class WebViewActivity extends BaseActivity {

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