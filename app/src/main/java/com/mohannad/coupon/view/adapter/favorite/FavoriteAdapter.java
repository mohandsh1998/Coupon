package com.mohannad.coupon.view.adapter.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.databinding.ItemDealRvBinding;
import com.mohannad.coupon.databinding.ItemFavoriteRvBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<FavoriteResponse.Favorite> favoriteItems;
    Context mContext;

    public FavoriteAdapter(Context context, ArrayList<FavoriteResponse.Favorite> favoriteItems) {
        this.favoriteItems = favoriteItems;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_favorite_rv, parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.bind(favoriteItems.get(position));
    }

    public void addAll(List<FavoriteResponse.Favorite> favoriteItems) {
        this.favoriteItems.addAll(favoriteItems);
        notifyDataSetChanged();
    }

    public void clear() {
        this.favoriteItems.clear();
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ItemFavoriteRvBinding itemView;

        public FavoriteViewHolder(ItemFavoriteRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(FavoriteResponse.Favorite favorite) {
            // company name
            this.itemView.tvCompanyNameItemFavoriteRv.setText(favorite.getCompanyName());
            // desc
            this.itemView.tvDescItemFavoriteRv.setText(favorite.getDesc());
            // check if allow to display count coupon used or not
            if (favorite.isAllowToOfferCountUsed()) {
                // display count coupon used
                this.itemView.tvTextNumTimesUsedItemDealRv.setVisibility(View.VISIBLE);
                this.itemView.tvNumTimesUsedItemDealRv.setVisibility(View.VISIBLE);
                this.itemView.tvNumTimesUsedItemDealRv.setText(favorite.getCountUsed() + " " + mContext.getString(R.string.times));
            } else {
                // hide count coupon used
                this.itemView.tvTextNumTimesUsedItemDealRv.setVisibility(View.GONE);
                this.itemView.tvNumTimesUsedItemDealRv.setVisibility(View.GONE);
            }
            // check if allow to display last used or not
            if (favorite.isAllowToOfferLastUseDate()) {
                // display last used
                this.itemView.tvTextLastDateUsedItemDealRv.setVisibility(View.VISIBLE);
                this.itemView.tvLastDateUsedItemDealRv.setVisibility(View.VISIBLE);
                this.itemView.tvLastDateUsedItemDealRv.setText(favorite.getLastUseDate());
            } else {
                // hide last used
                this.itemView.tvTextLastDateUsedItemDealRv.setVisibility(View.GONE);
                this.itemView.tvLastDateUsedItemDealRv.setVisibility(View.GONE);
            }
            // load image company
            Glide.with(mContext)
                    .load(favorite.getCompanyImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(this.itemView.imgCompanyItemFavoriteRv);
        }
    }
}
