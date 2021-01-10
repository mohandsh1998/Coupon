package com.mohannad.coupon.view.ui.trend;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ICommunicateMainActivity;
import com.mohannad.coupon.databinding.FragmentFavoriteBinding;
import com.mohannad.coupon.databinding.TrendFragmentBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.ui.favorite.FavoriteViewModel;

import org.jetbrains.annotations.NotNull;

public class TrendFragment extends BaseFragment {

    private TrendViewModel mViewModel;
    private TrendFragmentBinding trendFragmentBinding;
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
        mListener.onInteractionTrendFragment();
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
        trendFragmentBinding.setTrendViewModel(mViewModel);
        trendFragmentBinding.setLifecycleOwner(this);
    }

}