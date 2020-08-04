package com.mohannad.coupon.view.ui.auth.login;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityLoginBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.main.MainActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel model = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLoginViewModel(model);
        binding.setLifecycleOwner(this);
        model.success.observe(this, success -> {
            if (success) startActivity(new Intent(this, MainActivity.class));
        });
        model.toastMessageSuccess.observe(this, msg -> {
            showSuccessDialog(msg);
        });
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(msg);
        });

    }
}