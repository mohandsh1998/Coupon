package com.mohannad.coupon.view.adapter.deal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.databinding.ItemAdsDealRvBinding;
import com.mohannad.coupon.databinding.ItemDealRvBinding;
import com.mohannad.coupon.utils.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final int VIEW_TYPE_ADS = 1;
    private final int VIEW_TYPE_DEAL = 2;
    private SlideAdsAdapter slideAdsAdapter;
    private List<DealResponse.DealItem> dealItems;
    private Context mContext;
    private DealClickListener dealClickListener;

    public DealAdapter(Context context, SlideAdsAdapter slideAdsAdapter, ArrayList<DealResponse.DealItem> dealItems, DealClickListener dealClickListener) {
        this.dealItems = dealItems;
        this.mContext = context;
        this.slideAdsAdapter = slideAdsAdapter;
        this.dealClickListener = dealClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_ADS;
        } else {
            return VIEW_TYPE_DEAL;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ADS) {
            return new AdsDealViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_ads_deal_rv, parent, false));
        } else {
            return new DealViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_deal_rv, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position - 1);
    }

    public void addAll(List<DealResponse.DealItem> dealItems) {
        this.dealItems.addAll(dealItems);
        notifyDataSetChanged();
    }

    public void clear() {
        this.dealItems.clear();
    }

    @Override
    public int getItemCount() {
        return dealItems == null ? 0 : dealItems.size() + 1;
    }

    class DealViewHolder extends BaseViewHolder {
        private ItemDealRvBinding itemView;

        public DealViewHolder(ItemDealRvBinding itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            DealResponse.DealItem dealItem = dealItems.get(position);
            this.itemView.tvDescPercentage.setText(dealItem.getDescPercentage());
            this.itemView.tvDealDateItemDealRv.setText(dealItem.getContent());
            Glide.with(mContext)
                    .load(dealItem.getImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(this.itemView.imgDealCompany);

            if (dealItem.isHasCoupon()) {
                this.itemView.tvShowCouponItemDealRv.setVisibility(View.VISIBLE);
                this.itemView.tvWebsiteItemDealRv.setVisibility(View.GONE);
                this.itemView.view.setVisibility(View.GONE);
            } else {
                this.itemView.tvShowCouponItemDealRv.setVisibility(View.GONE);
                this.itemView.tvWebsiteItemDealRv.setVisibility(View.VISIBLE);
                this.itemView.view.setVisibility(View.VISIBLE);
            }

            // open webview
            this.itemView.getRoot().setOnClickListener(v -> {
                dealClickListener.openDeal(dealItem);
            });
            this.itemView.tvWebsiteItemDealRv.setOnClickListener(v -> {
                dealClickListener.openDeal(dealItem);
            });
            // open coupons for deals
            this.itemView.tvShowCouponItemDealRv.setOnClickListener(v -> {
                dealClickListener.openCoupon(dealItem);
            });
        }
    }

    class AdsDealViewHolder extends BaseViewHolder {
        private ItemAdsDealRvBinding itemAdsDealRvBinding;

        public AdsDealViewHolder(ItemAdsDealRvBinding itemView) {
            super(itemView);
            this.itemAdsDealRvBinding = itemView;
            itemAdsDealRvBinding.viewPagerAdsFragmentDeal.setAdapter(slideAdsAdapter);
            new TabLayoutMediator(itemAdsDealRvBinding.tabDots, itemAdsDealRvBinding.viewPagerAdsFragmentDeal,
                    (tab, position) -> tab.setText("OBJECT " + (position + 1))
            ).attach();
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
        }
    }

    public interface DealClickListener {
        void openCoupon(DealResponse.DealItem dealItem);

        void openDeal(DealResponse.DealItem dealItem);
    }
}
