package com.mohannad.coupon.view.ui.deal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.ActivityDealCouponsBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.usedcoupon.UsedCouponsAdapter;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.search.SearchViewModel;
import com.mohannad.coupon.view.ui.usedcoupon.UsedCouponActivity;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import java.util.ArrayList;

public class DealCouponsActivity extends BaseActivity {
    ArrayList<Coupon> coupons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDealCouponsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_deal_coupons);
        DealViewModel model = new ViewModelProvider(this).get(DealViewModel.class);
        binding.setDealViewModel(model);
        binding.setLifecycleOwner(this);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
        model.getDealCoupons(getIntent().getIntExtra("idDeal", -1));
        // initialization an adapter for coupons
        UsedCouponsAdapter couponsAdapter = new UsedCouponsAdapter(this, coupons, new UsedCouponsAdapter.CouponClickListener() {
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
//                startActivity(new Intent(DealCouponsActivity.this, WebViewActivity.class).putExtra("url", coupon.getLink()));
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
                    startActivity(new Intent(DealCouponsActivity.this, ContactUsActivity.class));
                }
            }

            @Override
            public void bestSelling(int position, Coupon coupon) {
                openBrowser(coupon.getBestSellingTitle());
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
        model.coupons.observe(this, couponsAdapter::addAll);
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