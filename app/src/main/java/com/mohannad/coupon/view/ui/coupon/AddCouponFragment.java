package com.mohannad.coupon.view.ui.coupon;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.FragmentAddCouponBinding;
import com.mohannad.coupon.view.adapter.spinner.SpinnerAdapter;
import com.mohannad.coupon.view.ui.favorite.FavoriteViewModel;

import java.util.ArrayList;

public class AddCouponFragment extends Fragment {
    private FragmentAddCouponBinding addCouponBinding;
    private AddCouponViewModel mViewModel;

    public static AddCouponFragment newInstance() {
        return new AddCouponFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        addCouponBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_coupon, container, false);
        return addCouponBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddCouponViewModel.class);
        addCouponBinding.setAddCouponViewModel(mViewModel);
        addCouponBinding.setLifecycleOwner(this);
        SpinnerAdapter adapter = new SpinnerAdapter(requireContext(), new ArrayList<>());
        addCouponBinding.spinnerCountries.setAdapter(adapter);
        mViewModel.countries.observe(requireActivity(), countries -> {
            adapter.addAll(countries);
        });
    }

}