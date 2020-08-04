package com.mohannad.coupon.view.ui.more;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mohannad.coupon.BuildConfig;
import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.FragmentMoreBinding;
import com.mohannad.coupon.view.ui.auth.changepassword.ChangePasswordActivity;
import com.mohannad.coupon.view.ui.auth.signup.SignUpActivity;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.help.HelpActivity;
import com.mohannad.coupon.view.ui.setting.SettingActivity;
import com.mohannad.coupon.view.ui.splash.SplashActivity;
import com.mohannad.coupon.view.ui.usedcoupon.UsedCouponActivity;

public class MoreFragment extends Fragment {

    private MoreViewModel mViewModel;
    private FragmentMoreBinding binding;

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_more, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MoreViewModel.class);
        binding.setMoreViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        binding.lyOpenInstagram.setOnClickListener(v -> {
            openInstagram();
        });
        binding.lyShareApp.setOnClickListener(v -> {
            shareApp();
        });
        binding.lyRegisterNow.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SignUpActivity.class));
        });
        binding.lyLogout.setOnClickListener(v -> {
            mViewModel.mSharedPreferences.logout();
            Intent intent = new Intent(requireContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
        binding.lyHelp.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), HelpActivity.class));
        });
        binding.lyContactUs.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ContactUsActivity.class));
        });
        binding.lySettingApp.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingActivity.class));
        });
        binding.lyUsedCoupon.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), UsedCouponActivity.class));
        });
        binding.lyChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
        });

    }

    private void openInstagram() {
        Uri uri = Uri.parse("https://www.instagram.com/mohand_shbair");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/mohand_shbair")));
        }
    }

    private void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app \n Google play: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                        + "\n AppStore : " + "https://app.store");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}