package com.mohannad.coupon.view.adapter.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.view.ui.home.HomePageFragment;

import java.util.List;

public class HomePagesAdapter extends FragmentStateAdapter {

    List<StoreResponse.Store> categoriesTabs;

    public HomePagesAdapter(FragmentManager fa, @NonNull Lifecycle lifecycle, List<StoreResponse.Store> categories) {
        super(fa,lifecycle);
        this.categoriesTabs = categories;
    }

    public void addAll(List<StoreResponse.Store> categories) {
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
