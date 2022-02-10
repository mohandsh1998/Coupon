package com.mohannad.coupon.view.ui.usedcoupon;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.ActivityUsedCouponBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.usedcoupon.UsedCouponsAdapter;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;

import java.util.ArrayList;

public class UsedCouponActivity extends BaseActivity {
    private StorageSharedPreferences storageSharedPreferences;
    ArrayList<Coupon> coupons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.used_coupons);
        ActivityUsedCouponBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_used_coupon);
        UsedCouponsViewModel model = new ViewModelProvider(this).get(UsedCouponsViewModel.class);
        binding.setUsedCouponViewModel(model);
        binding.setLifecycleOwner(this);

        storageSharedPreferences = new StorageSharedPreferences(this);
        // initialization an adapter for coupons
        UsedCouponsAdapter couponsAdapter = new UsedCouponsAdapter(this, coupons, sharedPreferences.getThemeMode(), new UsedCouponsAdapter.CouponClickListener() {
            @Override
            public void copyCoupon(int position, Coupon coupon) {
                // copy code coupon
                copyText(coupon.getCouponCode());
                // show dialog
                showDefaultDialog(binding.lyContainer, getString(R.string.coupon_was_copied));
                model.copyCoupon(coupon.getId());
            }

            @Override
            public void shopNowCoupon(int position, Coupon coupon) {
//                startActivity(new Intent(UsedCouponActivity.this, WebViewActivity.class).putExtra("url", coupon.getLink()));
                openBrowser(coupon.getLink());
            }

            @Override
            public void answerQuestion(int position, Coupon coupon, boolean answer) {
                   /*
                  answer
                  1- yes
                  0- no
                  */
                model.reviewCoupon(coupon.getId(), answer ? 1 : 0);
                if (!answer) {
                    startActivity(new Intent(UsedCouponActivity.this, ContactUsActivity.class).
                            putExtra(ContactUsActivity.COUPON_DETAILS, "Company name " + coupon.getCompanyName()
                                    + " , Code : " + coupon.getCouponCode()));
                }
            }

            @Override
            public void bestSelling(int position, Coupon coupon) {
                openBrowser(coupon.getBestSelling());
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

        binding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}