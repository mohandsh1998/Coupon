package com.mohannad.coupon.view.adapter.store;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
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
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.StoreResponse;
import com.mohannad.coupon.data.model.StoreResponse.Store;
import com.mohannad.coupon.data.model.StoreResponse.Theme;
import com.mohannad.coupon.databinding.ItemStoreRvBinding;
import com.mohannad.coupon.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private List<Store> categories;
    private Context mContext;
    private StoreClickListener storeClickListener;
    private int theme;

    public StoreAdapter(Context context, List<Store> categories, int theme,
                        StoreClickListener storeClickListener) {
        this.categories = categories;
        this.mContext = context;
        this.theme = theme;
        this.storeClickListener = storeClickListener;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoreRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_store_rv, parent, false);
        return new StoreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {
        holder.bind(position);
    }

    public void addAll(List<Store> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        notifyDataSetChanged();
    }

    public Store getItem(int position) {
        return categories.get(position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class StoreViewHolder extends RecyclerView.ViewHolder {
        private ItemStoreRvBinding itemView;

        public StoreViewHolder(ItemStoreRvBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        void bind(int position) {
            Store store = getItem(position);

            if (theme == Constants.MODERN_THEME) {
                Theme modernTheme = store.getThemes().get(0).getThemeId() == 2 ? store.getThemes().get(0) : store.getThemes().get(1);
                loadTheme(modernTheme);
                itemView.tvStoreName.setText(position % 2 != 0 ? store.getName() : "\n" + store.getName());
                itemView.tvStoreName.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                // light and dark
                Theme lightTheme = store.getThemes().get(0).getThemeId() == 1 ? store.getThemes().get(0) : store.getThemes().get(1);
                loadTheme(lightTheme);
                itemView.tvStoreName.setText(store.getName());
                itemView.tvStoreName.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            itemView.getRoot().setOnClickListener(v -> {
                storeClickListener.openHomePage(position, store.getId());
            });
        }

        void loadTheme(Theme theme) {
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

    public interface StoreClickListener {
        void openHomePage(int position, int storeId);
    }
}
