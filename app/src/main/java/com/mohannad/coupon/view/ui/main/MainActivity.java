package com.mohannad.coupon.view.ui.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mohannad.coupon.R;
import com.mohannad.coupon.callback.ICommunicateMainActivity;
import com.mohannad.coupon.databinding.ActivityMainBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.ui.home.HomeFragment;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity implements ICommunicateMainActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_trend, R.id.navigation_deal
                , R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.imgFilter.setOnClickListener(v -> {
            if (getForegroundFragment() instanceof HomeFragment) {
                HomeFragment homeFragment = (HomeFragment) getForegroundFragment();
                homeFragment.showFilterSheet();
            }
        });
    }

    public Fragment getForegroundFragment() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    public void onInteractionHomeFragment() {
        changeToolbarAndStatusBar(R.color.gray7, binding.toolbar);
        binding.imgFilter.setVisibility(View.VISIBLE);
        binding.tvTitleToolbar.setVisibility(View.GONE);
        binding.tvTitleToolbar.setText(getString(R.string.title_home));
    }

    @Override
    public void onInteractionFavoriteFragment() {
        changeToolbarAndStatusBar(R.color.pink, binding.toolbar);
        binding.imgFilter.setVisibility(View.GONE);
        binding.tvTitleToolbar.setVisibility(View.VISIBLE);
        binding.tvTitleToolbar.setText(getString(R.string.title_favorite));
    }

    @Override
    public void onInteractionTrendFragment() {
        changeToolbarAndStatusBar(R.color.pink, binding.toolbar);
        binding.imgFilter.setVisibility(View.GONE);
        binding.tvTitleToolbar.setVisibility(View.VISIBLE);
        binding.tvTitleToolbar.setText(getString(R.string.title_trend));
    }

    @Override
    public void onInteractionDealFragment() {
        changeToolbarAndStatusBar(R.color.pink, binding.toolbar);
        binding.imgFilter.setVisibility(View.GONE);
        binding.tvTitleToolbar.setVisibility(View.VISIBLE);
        binding.tvTitleToolbar.setText(getString(R.string.title_deal));
    }

    @Override
    public void onInteractionMoreFragment() {
        changeToolbarAndStatusBar(R.color.pink, binding.toolbar);
        binding.imgFilter.setVisibility(View.GONE);
        binding.tvTitleToolbar.setVisibility(View.VISIBLE);
        binding.tvTitleToolbar.setText(getString(R.string.title_more));
    }
}