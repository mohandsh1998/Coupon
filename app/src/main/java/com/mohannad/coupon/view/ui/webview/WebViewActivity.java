package com.mohannad.coupon.view.ui.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityWebViewBinding;
import com.mohannad.coupon.utils.BaseActivity;

public class WebViewActivity extends BaseActivity {
    private ActivityWebViewBinding webViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        initWebView();
        webViewBinding.webview.loadUrl(getIntent().getStringExtra("url"));
        webViewBinding.btnBackToAppActivityWebView.setOnClickListener(v -> {
            finish();
        });
    }


    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        webViewBinding.webview.getSettings().setJavaScriptEnabled(true);
        webViewBinding.webview.setWebViewClient(new MyWebViewClient());
        webViewBinding.webview.addJavascriptInterface(new WebViewInterface(), "JSInterface");
    }

    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }
    }

    public static class WebViewInterface {

        public WebViewInterface() {
        }

        @JavascriptInterface
        public void returnApp(boolean status, String message, String token, String username, String email) {
            Log.d("web call", status + " : " + message + " : " + token + " : " + username + " : " + email);

        }
    }
}