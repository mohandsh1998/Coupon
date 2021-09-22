package com.mohannad.coupon.view.ui.trend;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.TrendActivityBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.home.TrendAdapter;
import com.mohannad.coupon.view.ui.product.ProductsActivity;

import java.util.ArrayList;

public class TrendActivity extends BaseActivity {

    private TrendViewModel mViewModel;
    private TrendActivityBinding trendActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trendActivityBinding = DataBindingUtil.setContentView(this, R.layout.trend_activity);
        mViewModel = new ViewModelProvider(this).get(TrendViewModel.class);
        mViewModel.getTrends();
        trendActivityBinding.setTrendViewModel(mViewModel);
        trendActivityBinding.setLifecycleOwner(this);

        TrendAdapter trendAdapter = new TrendAdapter(this, new ArrayList<>(), sharedPreferences.getThemeMode(), new TrendAdapter.TrendClickListener() {
            @Override
            public void openProductActivity(int position, Coupon trend) {
                if (!TextUtils.isEmpty(trend.getBestSelling())) {
                    openBrowser(trend.getBestSelling());
                } else if (trend.getCouponsCount() != 0) {
                    startActivity(new Intent(getBaseContext(), TrendCouponsActivity.class).putExtra("idTrend", trend.getId()));
                } else {
                    // open products activity
                    Intent intent = new Intent(getBaseContext(), ProductsActivity.class);
                    // id title in items
                    intent.putExtra("idTitle", trend.getId());
                    intent.putExtra("title", trend.getTitle());
                    // check when click on item title to get products for category or company
                    if (trend.getCategoryId() != 0) {
                        // this will send type and id category to products activity and get the products for category
                        intent.putExtra("type", "category");
                        intent.putExtra("idCategory", trend.getCategoryId());
                    } else {
                        // this will send type and id company to products activity and get the products for company
                        intent.putExtra("type", "company");
                        intent.putExtra("idCompany", trend.getCompanyId());
                    }
                    startActivity(intent);
                }
            }
        });
        trendActivityBinding.rvCouponsFragmentTrend.setAdapter(trendAdapter);

        mViewModel.trendsLiveData.observe(this, trendAdapter::addAll);
        mViewModel.toastMessageFailed.observe(this, msg -> {
            showAlertDialog(trendActivityBinding.lyContainer, msg);
        });

        trendActivityBinding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}