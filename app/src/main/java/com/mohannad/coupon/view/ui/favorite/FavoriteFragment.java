package com.mohannad.coupon.view.ui.favorite;

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


import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.FragmentFavoriteBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.adapter.favorite.FavoriteAdapter;
import com.mohannad.coupon.view.ui.deal.DealViewModel;
import com.mohannad.coupon.view.ui.home.HomeViewModel;

import java.util.ArrayList;

public class FavoriteFragment extends BaseFragment {

    FragmentFavoriteBinding fragmentFavoriteBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentFavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        return fragmentFavoriteBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FavoriteViewModel favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        fragmentFavoriteBinding.setFavoriteViewModel(favoriteViewModel);
        fragmentFavoriteBinding.setLifecycleOwner(this);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(requireContext(), new ArrayList<>());
        fragmentFavoriteBinding.rvCouponsFragmentFavorite.setAdapter(favoriteAdapter);
        favoriteViewModel.favorites.observe(requireActivity(), favoriteAdapter::addAll);
        // display error msg
        favoriteViewModel.toastMessageFailed.observe(requireActivity(), msg -> {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }
}