package com.mohannad.coupon.view.adapter.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.view.ui.deal.SlideAdsFragment;
import com.mohannad.coupon.view.ui.home.HomePageFragment;

import java.util.List;

public class HomePagesAdapter extends FragmentStateAdapter {

    List<CategoriesResponse.Category> categoriesTabs;

    public HomePagesAdapter(FragmentManager fa, @NonNull Lifecycle lifecycle, List<CategoriesResponse.Category> categories) {
        super(fa,lifecycle);
        this.categoriesTabs = categories;
    }

    public void addAll(List<CategoriesResponse.Category> categories) {
        this.categoriesTabs.clear();
        this.categoriesTabs.addAll(categories);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // sent to HomePageFragment id category
        return HomePageFragment.newInstance(categoriesTabs.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return categoriesTabs.size();
    }

}
