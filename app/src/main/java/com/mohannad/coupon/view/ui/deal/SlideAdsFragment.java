package com.mohannad.coupon.view.ui.deal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.FragmentSlideAdsBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.ui.webview.WebViewActivity;

public class SlideAdsFragment extends BaseFragment {
    private static final String ARG_IMAGE = "image";
    private static final String ARG_LINK = "link";

    // TODO: Rename and change types of parameters
    private String mImage;
    private String mLink;

    public static SlideAdsFragment newInstance(String img, String link) {
        SlideAdsFragment fragment = new SlideAdsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, img);
        args.putString(ARG_LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImage = getArguments().getString(ARG_IMAGE);
            mLink = getArguments().getString(ARG_LINK);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSlideAdsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slide_ads, container, false);
        loadImage(getContext(), mImage, binding.imgAds);
        binding.tvWebsite.setOnClickListener(v -> {
//            startActivity(new Intent(requireContext(), WebViewActivity.class).putExtra("url", mLink));
            openBrowser(mLink);
        });
        return binding.getRoot();
    }
}