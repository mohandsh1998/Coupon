package com.mohannad.coupon.view.ui.usedcoupon;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.ActivityHelpBinding;
import com.mohannad.coupon.databinding.ActivityUsedCouponBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.home.CouponsAdapter;
import com.mohannad.coupon.view.adapter.usedcoupon.UsedCouponsAdapter;
import com.mohannad.coupon.view.ui.auth.login.LoginActivity;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.help.HelpViewModel;
import com.mohannad.coupon.view.ui.product.ProductsActivity;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import java.util.ArrayList;

public class UsedCouponActivity extends BaseActivity {
    private StorageSharedPreferences storageSharedPreferences;
    ArrayList<Coupon> coupons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUsedCouponBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_used_coupon);
        UsedCouponsViewModel model = new ViewModelProvider(this).get(UsedCouponsViewModel.class);
        binding.setUsedCouponViewModel(model);
        binding.setLifecycleOwner(this);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }

        storageSharedPreferences = new StorageSharedPreferences(this);
        // initialization an adapter for coupons
        UsedCouponsAdapter couponsAdapter = new UsedCouponsAdapter(this, coupons, new UsedCouponsAdapter.CouponClickListener() {
            @Override
            public void copyCoupon(int position, Coupon coupon) {
                // copy code coupon
                copyText(coupon.getCouponCode());
                // show dialog
                showDefaultDialog(binding.lyContainer, getString(R.string.coupon_was_copied));
//                homeViewModel.copyCoupon(coupon.getId());
            }

            @Override
            public void shopNowCoupon(int position, Coupon coupon) {
                startActivity(new Intent(UsedCouponActivity.this, WebViewActivity.class).putExtra("url", coupon.getLink()));
            }

            @Override
            public void answerQuestion(int position, Coupon coupon, boolean answer) {
                if (!answer) {
                    startActivity(new Intent(UsedCouponActivity.this, ContactUsActivity.class));
                }
            }


            @Override
            public void shareCoupon(int position, Coupon coupon) {
                shareText("Title : " + coupon.getCompanyName() + "\n Description : " + coupon.getDesc());
            }

            @Override
            public void addToFavoriteCoupon(int position, Coupon coupon) {
                model.addOrRemoveCouponFavorite(coupon.getId());
            }
        });
        binding.rvCoupons.setAdapter(couponsAdapter);
        binding.rvCoupons.setHasFixedSize(true);
        model.usedCoupons.observe(this, couponsAdapter::addAll);
        // display success msg
        model.toastMessageSuccess.observe(this, msg -> {
            showDefaultDialog(binding.lyContainer, msg);
        });
        // display error msg
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
    }
}