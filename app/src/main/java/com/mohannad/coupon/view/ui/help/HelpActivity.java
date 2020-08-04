package com.mohannad.coupon.view.ui.help;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityHelpBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.help.HelpAdapter;

import java.util.ArrayList;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
        HelpAdapter helpAdapter = new HelpAdapter(new ArrayList<>());
        binding.rvHelpItems.setAdapter(helpAdapter);
        HelpViewModel model = new ViewModelProvider(this).get(HelpViewModel.class);
        binding.setHelpViewModel(model);
        binding.setLifecycleOwner(this);

        model.helpContents.observe(this, helpAdapter::addAll);
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(msg);
        });
    }
}