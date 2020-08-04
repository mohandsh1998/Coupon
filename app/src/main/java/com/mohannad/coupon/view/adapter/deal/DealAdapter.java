package com.mohannad.coupon.view.adapter.deal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.databinding.ItemDealRvBinding;
import com.mohannad.coupon.databinding.ItemHelpRvBinding;

import java.util.ArrayList;
import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {
    private List<DealResponse.DealItem> dealItems;
    Context mContext;

    public DealAdapter(Context context, ArrayList<DealResponse.DealItem> dealItems) {
        this.dealItems = dealItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDealRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_deal_rv, parent, false);
        return new DealViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        holder.bind(dealItems.get(position));
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
        return dealItems.size();
    }

    class DealViewHolder extends RecyclerView.ViewHolder {
        private ItemDealRvBinding itemView;

        public DealViewHolder(ItemDealRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(DealResponse.DealItem dealItem) {
            this.itemView.tvPercentageDiscountItemDealRv.setText(dealItem.getPercentage() + " %");
            this.itemView.tvDealDateItemDealRv.setText(dealItem.getContent());
            Glide.with(mContext)
                    .load(dealItem.getImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(this.itemView.imgDealCompany);
        }
    }
}
