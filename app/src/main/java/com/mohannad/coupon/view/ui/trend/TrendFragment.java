package com.mohannad.coupon.view.ui.trend;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.TrendFragmentBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.home.TrendAdapter;
import com.mohannad.coupon.view.ui.product.ProductsActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TrendFragment extends BaseFragment {

    private TrendViewModel mViewModel;
    private TrendFragmentBinding trendFragmentBinding;

    public static TrendFragment newInstance() {
        TrendFragment fragment = new TrendFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        trendFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.trend_fragment, container, false);
        return trendFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TrendViewModel.class);
        mViewModel.getTrends();
        trendFragmentBinding.setTrendViewModel(mViewModel);
        trendFragmentBinding.setLifecycleOwner(this);

        TrendAdapter trendAdapter = new TrendAdapter(getContext(), new ArrayList<>(), new TrendAdapter.TrendClickListener() {
            @Override
            public void openProductActivity(int position, Coupon trend) {
                if (!TextUtils.isEmpty(trend.getBestSelling())) {
                    openBrowser(trend.getBestSelling());
                } else if (trend.getCouponsCount() != 0) {
                  startActivity(new Intent(getContext(), TrendCouponsActivity.class).putExtra("idTrend", trend.getId()));
                } else {
                    // open products activity
                    Intent intent = new Intent(getContext(), ProductsActivity.class);
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
        trendFragmentBinding.rvCouponsFragmentTrend.setAdapter(trendAdapter);

        mViewModel.trendsLiveData.observe(getViewLifecycleOwner(), trendAdapter::addAll);
        mViewModel.toastMessageFailed.observe(getViewLifecycleOwner(), msg -> {
            showAlertDialog(trendFragmentBinding.lyContainer, msg);
        });
    }

}