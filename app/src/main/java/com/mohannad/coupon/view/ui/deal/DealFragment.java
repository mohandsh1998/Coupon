package com.mohannad.coupon.view.ui.deal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ICommunicateMainActivity;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.databinding.FragmentDealBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.utils.PaginationListener;
import com.mohannad.coupon.view.adapter.deal.DealAdapter;
import com.mohannad.coupon.view.adapter.deal.SlideAdsAdapter;
import com.mohannad.coupon.view.adapter.help.HelpAdapter;
import com.mohannad.coupon.view.ui.help.HelpViewModel;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DealFragment extends BaseFragment {

    private FragmentDealBinding binding;
    // current page for pagination
    private int mCurrentPage = 1;
    private boolean mIsLastPage;
    private boolean mIsLoading;
    private ICommunicateMainActivity mListener;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        if (context instanceof ICommunicateMainActivity) {
            mListener = (ICommunicateMainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ICommunicateHomeActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.onInteractionDealFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deal, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DealViewModel dealViewModel = new ViewModelProvider(this).get(DealViewModel.class);
        binding.setDealViewModel(dealViewModel);
        binding.setLifecycleOwner(this);
        dealViewModel.getDeals(mCurrentPage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        SlideAdsAdapter slideAdsAdapter = new SlideAdsAdapter(requireActivity(), new ArrayList<>());
        DealAdapter dealAdapter = new DealAdapter(requireContext(), slideAdsAdapter, new ArrayList<>(), new DealAdapter.DealClickListener() {
            @Override
            public void openCoupon(DealResponse.DealItem dealItem) {
                startActivity(new Intent(requireContext(), DealCouponsActivity.class).putExtra("idDeal", dealItem.getId()));
            }

            @Override
            public void openDeal(DealResponse.DealItem dealItem) {
                startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("url", dealItem.getLink()));
            }
        });
        binding.dealsRv.setHasFixedSize(true);
        binding.dealsRv.setAdapter(dealAdapter);
        dealViewModel.deals.observe(requireActivity(), dealAdapter::addAll);
        dealViewModel.adsDeal.observe(requireActivity(), slideAdsAdapter::addAll);
        dealViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            showAlertDialog(binding.lyContainer, msg);
        });
        binding.dealsRv.addOnScrollListener(new PaginationListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                ++mCurrentPage;
                dealViewModel.getDeals(mCurrentPage);
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

        dealViewModel.dataLoading.observe(requireActivity(), loading -> {
            this.mIsLoading = loading;
        });

        dealViewModel.isLastPage.observe(requireActivity(), lastPage -> {
            this.mIsLastPage = lastPage;
        });

    }
}