package com.mohannad.coupon.view.adapter.deal;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.view.ui.deal.SlideAdsFragment;

import java.util.List;

public class SlideAdsAdapter extends FragmentStateAdapter {
    List<DealResponse.DealsAds> dealsAds;

    public SlideAdsAdapter(FragmentActivity fa, List<DealResponse.DealsAds> dealsAds) {
        super(fa);
        this.dealsAds = dealsAds;
    }
    public void addAll(List<DealResponse.DealsAds> dealsAds) {
        this.dealsAds.clear();
        this.dealsAds.addAll(dealsAds);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return SlideAdsFragment.newInstance(dealsAds.get(position).getImage(), dealsAds.get(position).getLink());
    }

    @Override
    public int getItemCount() {
        return dealsAds.size();
    }

}
