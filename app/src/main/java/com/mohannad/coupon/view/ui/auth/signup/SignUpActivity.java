package com.mohannad.coupon.view.ui.auth.signup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivitySignUpBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.view.ui.main.view.MainActivity;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        changeToolbarAndStatusBar(R.color.gray7, null);
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
        updateTheme();
    }

    private void updateTheme() {
        switch (sharedPreferences.getThemeMode()) {
            case Constants.MODERN_THEME:
                binding.btnSignup.setBackgroundResource(R.drawable.shape_blue_15dp);
                binding.etFullNameActivitySignup.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etFullNameActivitySignup.setTextColor(getResources().getColor(R.color.black));
                binding.etEmailActivitySignup.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etEmailActivitySignup.setTextColor(getResources().getColor(R.color.black));
                binding.etPasswordActivitySignup.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etPasswordActivitySignup.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.LIGHT_THEME:
                binding.btnSignup.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                binding.etFullNameActivitySignup.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etFullNameActivitySignup.setTextColor(getResources().getColor(R.color.black));
                binding.etEmailActivitySignup.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etEmailActivitySignup.setTextColor(getResources().getColor(R.color.black));
                binding.etPasswordActivitySignup.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etPasswordActivitySignup.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.DARK_THEME:
                binding.btnSignup.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                binding.etFullNameActivitySignup.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etFullNameActivitySignup.setTextColor(getResources().getColor(R.color.white));
                binding.etEmailActivitySignup.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etEmailActivitySignup.setTextColor(getResources().getColor(R.color.white));
                binding.etPasswordActivitySignup.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etPasswordActivitySignup.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
}