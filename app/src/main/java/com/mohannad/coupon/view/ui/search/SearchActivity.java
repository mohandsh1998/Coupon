package com.mohannad.coupon.view.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.ActivitySearchBinding;
import com.mohannad.coupon.databinding.ActivityUsedCouponBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.PaginationListener;
import com.mohannad.coupon.view.adapter.usedcoupon.UsedCouponsAdapter;
import com.mohannad.coupon.view.ui.auth.login.LoginActivity;
import com.mohannad.coupon.view.ui.usedcoupon.UsedCouponActivity;
import com.mohannad.coupon.view.ui.usedcoupon.UsedCouponsViewModel;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    // current page for pagination
    private int mCurrentPage = 1;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    // search word
    private String word;
    private StorageSharedPreferences storageSharedPreferences;
    private SearchViewModel model;
    ArrayList<Coupon> coupons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySearchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        model = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.setSearchViewModel(model);
        binding.setLifecycleOwner(this);
        // remove shadow in actionbar and change arrow color
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_back_arrow));
            getSupportActionBar().setElevation(0);
        }
        if (getIntent().hasExtra("word")) {
            // search word will enter by user
            word = getIntent().getStringExtra("word");
        }
        fetchCoupons();
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
                startActivity(new Intent(SearchActivity.this, WebViewActivity.class).putExtra("url", coupon.getLink()));
            }

            @Override
            public void answerQuestion(int position, Coupon coupon, boolean answer) {

            }


            @Override
            public void shareCoupon(int position, Coupon coupon) {

            }

            @Override
            public void addToFavoriteCoupon(int position, Coupon coupon) {
                model.addOrRemoveCouponFavorite(coupon.getId());
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rvCoupons.setLayoutManager(linearLayoutManager);
        binding.rvCoupons.setAdapter(couponsAdapter);
        binding.rvCoupons.setHasFixedSize(true);
        binding.rvCoupons.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                ++mCurrentPage;
               fetchCoupons();
            }

            @Override
            public boolean isLastPage() {
                return mIsLastPage;
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        });

        model.resultCoupons.observe(this, couponsAdapter::addAll);

        model.dataLoading.observe(this, loading -> {
            this.mIsLoading = loading;
        });

        model.isLastPage.observe(this, lastPage -> {
            this.mIsLastPage = lastPage;
        });

        // display success msg
        model.toastMessageSuccess.observe(this, msg -> {
            showDefaultDialog(binding.lyContainer, msg);
        });
        // display error msg
        model.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
    }

    private void fetchCoupons() {
        model.searchCoupons(word, mCurrentPage);
    }

}