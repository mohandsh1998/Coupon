package com.mohannad.coupon.view.ui.contactus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityChangePasswordBinding;
import com.mohannad.coupon.databinding.ActivityContactUsBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.view.ui.auth.changepassword.ChangePasswordViewModel;

public class ContactUsActivity extends BaseActivity {
    ActivityContactUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.contact_us);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        ContactUsViewModel model = new ViewModelProvider(this).get(ContactUsViewModel.class);
        binding.setContactUsViewModel(model);
        binding.setLifecycleOwner(this);
        model.toastMessageSuccess.observe(this, this::showDialog);
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
        binding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
        updateTheme();
    }

    private void updateTheme() {
        switch (sharedPreferences.getThemeMode()) {
            case Constants.MODERN_THEME:
                binding.btnSend.setBackgroundResource(R.drawable.shape_blue_15dp);
                binding.etEmail.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etEmail.setTextColor(getResources().getColor(R.color.black));
                binding.etTitle.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etTitle.setTextColor(getResources().getColor(R.color.black));
                binding.etDescription.setBackgroundResource(R.drawable.shape_gray_radius_15dp);
                binding.etDescription.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.LIGHT_THEME:
                binding.btnSend.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                binding.etEmail.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etEmail.setTextColor(getResources().getColor(R.color.black));
                binding.etTitle.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etTitle.setTextColor(getResources().getColor(R.color.black));
                binding.etDescription.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.etDescription.setTextColor(getResources().getColor(R.color.black));
                break;
            case Constants.DARK_THEME:
                binding.btnSend.setBackgroundResource(R.drawable.shape_gradient_blue_15dp);
                binding.etEmail.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etEmail.setTextColor(getResources().getColor(R.color.white));
                binding.etTitle.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etTitle.setTextColor(getResources().getColor(R.color.white));
                binding.etDescription.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.etDescription.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
}