package com.mohannad.coupon.view.ui.auth.login;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityLoginBinding;
import com.mohannad.coupon.databinding.ForgetPasswordBottomSheetDialogBinding;
import com.mohannad.coupon.databinding.SuccessForgetPasswordBottomSheetDialogBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.view.ui.auth.signup.SignUpActivity;
import com.mohannad.coupon.view.ui.main.view.MainActivity;

public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        changeToolbarAndStatusBar(R.color.gray7, null);
        model = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLoginViewModel(model);
        binding.setLifecycleOwner(this);
        binding.tvSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUpActivity.class)));
        binding.tvForgetPassword.setOnClickListener(v -> showResetPasswordSheet());
        model.success.observe(this, success -> {
            if (success) startActivity(new Intent(this, MainActivity.class));
        });
        model.toastMessageSuccess.observe(this, msg -> {
            showDefaultDialog(binding.lyContainer, msg);
        });
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
        updateTheme();
    }

    private void showResetPasswordSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        ForgetPasswordBottomSheetDialogBinding sheetView = ForgetPasswordBottomSheetDialogBinding.inflate(LayoutInflater.from(this));
        bottomSheet.setContentView(sheetView.getRoot());
        sheetView.setLifecycleOwner(this);
        sheetView.setLoginViewModel(model);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(((View) sheetView.getRoot().getParent()));
        behavior.setSkipCollapsed(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheet.show();
        model.successResetPassword.observe(this, success -> {
            if (success) {
                bottomSheet.cancel();
                openGmailSheet();
            }
            ;
        });
    }

    private void openGmailSheet() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        SuccessForgetPasswordBottomSheetDialogBinding sheetView = SuccessForgetPasswordBottomSheetDialogBinding.inflate(LayoutInflater.from(this));
        bottomSheet.setContentView(sheetView.getRoot());
        sheetView.setLifecycleOwner(this);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(((View) sheetView.getRoot().getParent()));
        behavior.setSkipCollapsed(true);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheet.show();
        sheetView.btnOpenGmail.setOnClickListener(v -> {
            bottomSheet.cancel();
            openGmail();
        });
    }

    private void updateTheme() {
        switch (sharedPreferences.getThemeMode()) {
            case Constants.MODERN_THEME:
                binding.btnLogin.setBackgroundResource(R.drawable.shape_blue_15dp);
                binding.etEmailActivityLogin.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etEmailActivityLogin.setTextColor(getResources().getColor(R.color.black));
                binding.etPasswordActivityLogin.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etPasswordActivityLogin.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.LIGHT_THEME:
                binding.btnLogin.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                binding.etEmailActivityLogin.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etEmailActivityLogin.setTextColor(getResources().getColor(R.color.black));
                binding.etPasswordActivityLogin.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etPasswordActivityLogin.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.DARK_THEME:
                binding.btnLogin.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                binding.etEmailActivityLogin.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etEmailActivityLogin.setTextColor(getResources().getColor(R.color.white));
                binding.etPasswordActivityLogin.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etPasswordActivityLogin.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void openGmail() {
        Intent intent = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_EMAIL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "Email"));
    }
}