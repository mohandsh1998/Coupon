package com.mohannad.coupon.view.ui.deal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.FragmentDealBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.deal.DealAdapter;
import com.mohannad.coupon.view.adapter.deal.SlideAdsAdapter;
import com.mohannad.coupon.view.adapter.help.HelpAdapter;
import com.mohannad.coupon.view.ui.help.HelpViewModel;

import java.util.ArrayList;

public class DealFragment extends BaseFragment {

    FragmentDealBinding binding;

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
        DealAdapter dealAdapter = new DealAdapter(requireContext(), new ArrayList<>());
        binding.dealsRv.setAdapter(dealAdapter);
        binding.dealsRv.setHasFixedSize(true);
        binding.dealsRv.setNestedScrollingEnabled(false);
        SlideAdsAdapter slideAdsAdapter = new SlideAdsAdapter(requireActivity(), new ArrayList<>());
        binding.viewPagerAdsFragmentDeal.setAdapter(slideAdsAdapter);
        new TabLayoutMediator(binding.tabDots, binding.viewPagerAdsFragmentDeal,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();
        dealViewModel.deals.observe(requireActivity(), dealAdapter::addAll);
        dealViewModel.adsDeal.observe(requireActivity(), slideAdsAdapter::addAll);
        dealViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
//            showAlertDialog(msg);
        });
    }
}