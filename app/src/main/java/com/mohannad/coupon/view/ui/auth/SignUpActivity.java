package com.mohannad.coupon.view.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivitySignUpBinding;
import com.mohannad.coupon.utils.BaseActivity;
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
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
        model.toastMessageFailed.observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

    }
}