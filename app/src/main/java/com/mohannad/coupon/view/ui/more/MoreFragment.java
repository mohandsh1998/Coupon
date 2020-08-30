package com.mohannad.coupon.view.ui.more;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.databinding.FragmentMoreBinding;
import com.mohannad.coupon.utils.BaseFragment;
import com.mohannad.coupon.view.ui.auth.changepassword.ChangePasswordActivity;
import com.mohannad.coupon.view.ui.auth.signup.SignUpActivity;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.help.HelpActivity;
import com.mohannad.coupon.view.ui.setting.SettingActivity;
import com.mohannad.coupon.view.ui.splash.SplashActivity;
import com.mohannad.coupon.view.ui.usedcoupon.UsedCouponActivity;

public class MoreFragment extends BaseFragment {

    private MoreViewModel mViewModel;
    private FragmentMoreBinding binding;
    private StorageSharedPreferences sharedPreferences;

    public static MoreFragment newInstance() {
        return new MoreFragment();
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
        sharedPreferences = new StorageSharedPreferences(requireContext());
        mViewModel = new ViewModelProvider(requireActivity()).get(MoreViewModel.class);
        binding.setMoreViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        binding.lyOpenInstagram.setOnClickListener(v -> {
            openInstagram();
        });
        binding.lyOpenSnapchat.setOnClickListener(v -> {
            openSnapChat();
        });
        binding.lyOpenTelegram.setOnClickListener(v -> {
            openTelegram();
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
        final String appPackageName = "com.instagram.android";
        final boolean isAppInstalled = isAppAvailable(requireActivity().getApplicationContext(), appPackageName);
        Uri uri = Uri.parse(sharedPreferences.getIntstagram());
        if (isAppInstalled) {
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage(appPackageName);
            startActivity(likeIng);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
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

    private void openSnapChat() {
        final String appPackageName = "com.snapchat.android";
        final boolean isAppInstalled = isAppAvailable(requireActivity().getApplicationContext(), appPackageName);
        Uri uri = Uri.parse(sharedPreferences.getSnapChat());
        if (isAppInstalled) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(appPackageName);
            startActivity(intent);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    private void openTelegram() {
        final String appPackageName = "org.telegram.messenger";
        final boolean isAppInstalled = isAppAvailable(requireActivity().getApplicationContext(), appPackageName);
        Uri uri = Uri.parse(sharedPreferences.getTelegram());
        if (isAppInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND, uri);
            myIntent.setPackage(appPackageName);
            requireContext().startActivity(Intent.createChooser(myIntent, "Share with"));
        } else {
            showDefaultDialog(binding.lyContainer, getString(R.string.telegram_not_installed));
        }
    }

    /**
     * Indicates whether the specified app ins installed and can used as an intent. This
     * method checks the package manager for installed packages that can
     * respond to an intent with the specified app. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param appName The name of the package you want to check
     * @return True if app is installed
     */
    public static boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}