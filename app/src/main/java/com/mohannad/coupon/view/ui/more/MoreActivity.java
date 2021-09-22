package com.mohannad.coupon.view.ui.more;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.mohannad.coupon.BuildConfig;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.local.StorageSharedPreferences;
import com.mohannad.coupon.databinding.ActivityMoreBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.utils.Constants;
import com.mohannad.coupon.view.ui.auth.changepassword.ChangePasswordActivity;
import com.mohannad.coupon.view.ui.auth.login.LoginActivity;
import com.mohannad.coupon.view.ui.contactus.ContactUsActivity;
import com.mohannad.coupon.view.ui.coupon.AddCouponActivity;
import com.mohannad.coupon.view.ui.favorite.FavoriteActivity;
import com.mohannad.coupon.view.ui.setting.SettingActivity;
import com.mohannad.coupon.view.ui.splash.SplashActivity;
import com.mohannad.coupon.view.ui.trend.TrendActivity;
import com.mohannad.coupon.view.ui.usedcoupon.UsedCouponActivity;

public class MoreActivity extends BaseActivity {

    private MoreViewModel mViewModel;
    private ActivityMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_more);
        mViewModel = new ViewModelProvider(this).get(MoreViewModel.class);
        binding.setMoreViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        binding.lyOpenSnapchat.setOnClickListener(v -> openSnapChat());
        binding.lyOpenWhatsUp.setOnClickListener(v -> openWhatsUp());
        binding.lyOpenTelegram.setOnClickListener(v -> openTelegram());
        binding.lyShareApp.setOnClickListener(v -> shareText("Hey check out my app \n Google play: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                + "\n AppStore : " + "https://app.store"));
        binding.lyRegisterNow.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        binding.lyLogout.setOnClickListener(v -> {
            mViewModel.mSharedPreferences.logout();
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        binding.lyAddCoupon.setOnClickListener(v -> {
            startActivity(new Intent(this, AddCouponActivity.class));
        });
        binding.lyHelp.setOnClickListener(v -> {
            startActivity(new Intent(this, ContactUsActivity.class));
        });

        binding.lySettingApp.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });
        binding.lyUsedCoupon.setOnClickListener(v -> {
            startActivity(new Intent(this, UsedCouponActivity.class));
        });
        binding.lyTrend.setOnClickListener(v -> {
            startActivity(new Intent(this, TrendActivity.class));
        });
        binding.lyFavorite.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoriteActivity.class));
        });
        binding.lyChangePassword.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
        binding.lyPrivacyPolicy.setOnClickListener(v -> {
            openBrowser(Constants.PRIVACY_POLICIES_URL + sharedPreferences.getLanguage());
        });
        binding.lyReviewApp.setOnClickListener(v -> {
            openBrowser("https://play.google.com/store/apps/details?id=" + getPackageName());
        });
        binding.imgBackArrow.setOnClickListener(v -> {
            onBackPressed();
        });
        updateTheme();
    }

    private void updateTheme() {
        switch (sharedPreferences.getThemeMode()) {
            case Constants.MODERN_THEME:
                binding.lyAddCoupon.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyUsedCoupon.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lySettingApp.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyHelp.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyPrivacyPolicy.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyRegisterNow.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyTrend.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyFavorite.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyChangePassword.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                binding.lyLogout.setBackgroundResource(R.drawable.shape_stroke_gray_15dp);
                break;
            case Constants.LIGHT_THEME:
                binding.lyAddCoupon.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyUsedCoupon.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lySettingApp.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyHelp.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyPrivacyPolicy.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyRegisterNow.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyTrend.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyFavorite.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyChangePassword.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                binding.lyLogout.setBackgroundResource(R.drawable.shape_white_radius_15dp);
                break;
            case Constants.DARK_THEME:
                binding.lyAddCoupon.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyUsedCoupon.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lySettingApp.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyHelp.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyPrivacyPolicy.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyRegisterNow.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyTrend.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyFavorite.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyChangePassword.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                binding.lyLogout.setBackgroundResource(R.drawable.shape_black_radius_15dp);
                break;
        }
    }

    private void openInstagram() {
        final String appPackageName = "com.instagram.android";
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appPackageName);
        Uri uri = Uri.parse(sharedPreferences.getIntstagram());
        if (isAppInstalled) {
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
            likeIng.setPackage(appPackageName);
            startActivity(likeIng);
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

    private void openSnapChat() {
        final String appPackageName = "com.snapchat.android";
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appPackageName);
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
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appPackageName);
        Uri uri = Uri.parse(sharedPreferences.getTelegram());
        if (isAppInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_SEND, uri);
            myIntent.setPackage(appPackageName);
            startActivity(Intent.createChooser(myIntent, "Share with"));
        } else {
            showDefaultDialog(binding.lyContainer, getString(R.string.telegram_not_installed));
        }
    }

    private void openWhatsUp() {
        final String appPackageName = "com.whatsapp";
        final boolean isAppInstalled = isAppAvailable(getApplicationContext(), appPackageName);
        Uri uri = Uri.parse(sharedPreferences.getWhatsUp());
        if (isAppInstalled) {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
            myIntent.setPackage(appPackageName);
            startActivity(Intent.createChooser(myIntent, "Share with"));
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