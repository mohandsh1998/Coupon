package com.mohannad.coupon.view.adapter.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.data.model.StoreResponse.Store;
import com.mohannad.coupon.databinding.ItemStoreHorizontalRvBinding;
import com.mohannad.coupon.databinding.ItemStoreRvBinding;
import com.mohannad.coupon.utils.Constants;

import java.util.List;

public class StoreHorizontalAdapter extends RecyclerView.Adapter<StoreHorizontalAdapter.StoreViewHolder> {
    private List<Store> categories;
    private Context mContext;
    private StoreHorizontalClickListener storeClickListener;
    private int storeIdSelected;
    private int theme;

    public StoreHorizontalAdapter(Context context, List<Store> categories, int storeIdSelected,
                                  int theme, StoreHorizontalClickListener storeClickListener) {
        this.mContext = context;
        this.categories = categories;
        this.storeIdSelected = storeIdSelected;
        this.theme = theme;
        this.storeClickListener = storeClickListener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoreHorizontalRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_store_horizontal_rv, parent, false);
        return new StoreViewHolder(binding);
    }

    public void setStoreIdSelected(int storeId) {
        this.storeIdSelected = storeId;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    public void addAll(List<Store> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {
        private ItemStoreHorizontalRvBinding itemView;

        public StoreViewHolder(ItemStoreHorizontalRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(Store store) {
            if (theme == Constants.MODERN_THEME) {
                StoreResponse.Theme modernTheme = store.getThemes().get(0).getThemeId() == 2 ? store.getThemes().get(0) : store.getThemes().get(1);
                loadTheme(modernTheme);
                itemView.tvStoreName.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                // light and dark
                StoreResponse.Theme lightTheme = store.getThemes().get(0).getThemeId() == 1 ? store.getThemes().get(0) : store.getThemes().get(1);
                loadTheme(lightTheme);
                itemView.tvStoreName.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            itemView.tvStoreName.setText(store.getName().trim());

            itemView.getRoot().setOnClickListener(v -> {
                storeClickListener.showStoreCoupons(store.getId());
            });

            if (storeIdSelected == store.getId()) {
                if (theme == Constants.MODERN_THEME)
                    itemView.container.setBackgroundResource(R.drawable.shape_stroke_turquoise_raduis_15dp);
                else
                    itemView.container.setBackgroundResource(R.drawable.shape_stroke_white_raduis_15dp);
            } else itemView.container.setBackgroundResource(0);
        }

        void loadTheme(StoreResponse.Theme theme) {
            // loading background store
            Glide.with(mContext).load(theme.getImage()).into(new CustomTarget<Drawable>() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                    itemView.lyContainerStore.setBackground(resource);
                }

                @Override
                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {

                }
            });

            Glide.with(mContext).load(theme.getLogo()).into(itemView.imgLogoStore);
        }
    }

    public interface StoreHorizontalClickListener {
        void showStoreCoupons(int storeId);
    }
}
