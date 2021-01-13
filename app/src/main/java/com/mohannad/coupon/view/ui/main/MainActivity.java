package com.mohannad.coupon.view.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.mohannad.coupon.R;
import com.mohannad.coupon.databinding.ActivityMainBinding;
import com.mohannad.coupon.utils.BaseActivity;
import com.mohannad.coupon.view.adapter.home.TabFragmentAdapter;
import com.mohannad.coupon.view.ui.deal.DealFragment;
import com.mohannad.coupon.view.ui.favorite.FavoriteFragment;
import com.mohannad.coupon.view.ui.home.HomeFragment;
import com.mohannad.coupon.view.ui.more.MoreFragment;
import com.mohannad.coupon.view.ui.trend.TrendFragment;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private int indicatorWidth;
    private int valueOldPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        valueOldPos = -1;
        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(HomeFragment.newInstance(), "");
        adapter.addFragment(FavoriteFragment.newInstance(), "");
        adapter.addFragment(TrendFragment.newInstance(), "");
        adapter.addFragment(DealFragment.newInstance(), "");
        adapter.addFragment(MoreFragment.newInstance(), "");
        binding.viewPager.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.viewPager);

        changeStatusBar(R.color.gray7);
        loadSelectedTabAnimation(0);

        //Determine indicator width at runtime
        binding.tab.post(() -> {
            indicatorWidth = binding.tab.getWidth() / binding.tab.getTabCount();
            RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) binding.indicator.getLayoutParams();
            indicatorParams.width = indicatorWidth;
            binding.indicator.setLayoutParams(indicatorParams);
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) binding.indicator.getLayoutParams();
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.setMarginStart((int) translationOffset);
                binding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    changeStatusBar(R.color.gray7);
                } else if (position == 1) {
                    changeStatusBar(R.color.pink);
                } else if (position == 2) {
                    changeStatusBar(R.color.pink);
                } else if (position == 3) {
                    changeStatusBar(R.color.pink);
                } else if (position == 4) {
                    changeStatusBar(R.color.pink);
                }
                loadSelectedTabAnimation(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    private void loadSelectedTabAnimation(int tabPos) {
        if (valueOldPos != -1) {
            if (valueOldPos == 0) {
                binding.ivSelectedHome.animate().alpha(0.0f);
                binding.ivSelectedHome.setVisibility(View.INVISIBLE);
                binding.txtHome.setAlpha(0.4f);
            } else if (valueOldPos == 1) {
                binding.ivSelectFavirote.animate().alpha(0.0f);
                binding.ivSelectFavirote.setVisibility(View.INVISIBLE);
                binding.txtFavorite.setAlpha(0.4f);
            } else if (valueOldPos == 2) {
                binding.ivSelectFire.animate().alpha(0.0f);
                binding.ivSelectFire.setVisibility(View.INVISIBLE);
                binding.txtTrending.setAlpha(0.4f);
            } else if (valueOldPos == 3) {
                binding.ivSelectDeal.animate().alpha(0.0f);
                binding.ivSelectDeal.setVisibility(View.INVISIBLE);
                binding.txtDeal.setAlpha(0.4f);
            } else if (valueOldPos == 4) {
                binding.ivSelectMore.animate().alpha(0.0f);
                binding.ivSelectMore.setVisibility(View.INVISIBLE);
                binding.txtMore.setAlpha(0.4f);
            }
        }
        if (tabPos == 0) {
            binding.ivSelectedHome.animate().alpha(1.0f);
            binding.ivSelectedHome.setVisibility(View.VISIBLE);
            binding.txtHome.setAlpha(1.0f);
        } else if (tabPos == 1) {
            binding.ivSelectFavirote.animate().alpha(1.0f);
            binding.ivSelectFavirote.setVisibility(View.VISIBLE);
            binding.txtFavorite.setAlpha(1.0f);
        } else if (tabPos == 2) {
            binding.ivSelectFire.animate().alpha(1.0f);
            binding.ivSelectFire.setVisibility(View.VISIBLE);
            binding.txtTrending.setAlpha(1.0f);
        } else if (tabPos == 3) {
            binding.ivSelectDeal.animate().alpha(1.0f);
            binding.ivSelectDeal.setVisibility(View.VISIBLE);
            binding.txtDeal.setAlpha(1.0f);
        } else if (tabPos == 4) {
            binding.ivSelectMore.animate().alpha(1.0f);
            binding.ivSelectMore.setVisibility(View.VISIBLE);
            binding.txtMore.setAlpha(1.0f);
        }
        valueOldPos = tabPos;
    }
}