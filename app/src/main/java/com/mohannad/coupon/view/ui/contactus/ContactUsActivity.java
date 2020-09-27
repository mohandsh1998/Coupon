package com.mohannad.coupon.view.ui.contactus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityChangePasswordBinding;
import com.mohannad.coupon.databinding.ActivityContactUsBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.auth.changepassword.ChangePasswordViewModel;

public class ContactUsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.contact_us);
        ActivityContactUsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        ContactUsViewModel model = new ViewModelProvider(this).get(ContactUsViewModel.class);
        binding.setContactUsViewModel(model);
        binding.setLifecycleOwner(this);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
        model.toastMessageSuccess.observe(this, this::showDialog);
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
    }
}