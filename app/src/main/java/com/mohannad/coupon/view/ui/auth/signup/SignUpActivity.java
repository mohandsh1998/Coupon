package com.mohannad.coupon.view.ui.auth.signup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivitySignUpBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.auth.login.LoginActivity;
import com.mohannad.coupon.view.ui.main.MainActivity;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        SignUpViewModel model = new ViewModelProvider(this).get(SignUpViewModel.class);
        binding.setSignUpViewModel(model);
        binding.setLifecycleOwner(this);
        model.success.observe(this, success -> {
            if (success) startActivity(new Intent(this, MainActivity.class));
        });
        model.toastMessageSuccess.observe(this, msg -> {
            showSuccessDialog(binding.lyContainer, msg);
        });
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
        binding.tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
}