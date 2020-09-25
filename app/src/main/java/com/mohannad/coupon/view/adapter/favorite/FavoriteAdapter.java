package com.mohannad.coupon.view.adapter.favorite;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.databinding.ItemCouponRvBinding;
import com.mohannad.coupon.databinding.ItemProductRvBinding;
import com.mohannad.coupon.utils.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private final int VIEW_TYPE_COUPONS = 1;
    private final int VIEW_TYPE_PRODUCTS = 2;
    Context mContext;
    private List<FavoriteResponse.Favorite> favoriteItems;
    private Animation shake;
    private int copyItem = -1;
    // tag to start animation when copy coupon only
    private boolean startAnimation = false;

    private FavoriteClickListener favoriteClickListener;

    public FavoriteAdapter(Context context, ArrayList<FavoriteResponse.Favorite> favoriteItems, FavoriteClickListener favoriteClickListener) {
        this.mContext = context;
        this.favoriteItems = favoriteItems;
        this.favoriteClickListener = favoriteClickListener;
        // animation when copy coupon
        shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        copyItem = -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (favoriteItems.get(position).getType().equals("coupon")) {
            return VIEW_TYPE_COUPONS;
        } else if (favoriteItems.get(position).getType().equals("product")) {
            return VIEW_TYPE_PRODUCTS;
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_COUPONS:
                ItemCouponRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_coupon_rv, parent, false);
                return new CouponFavoriteViewHolder(binding);
            case VIEW_TYPE_PRODUCTS:
                ItemProductRvBinding productRvBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product_rv, parent, false);
                return new ProductFavoriteViewHolder(productRvBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    public void addAll(List<FavoriteResponse.Favorite> favoriteItems) {
        this.favoriteItems.addAll(favoriteItems);
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        if (position == copyItem) {
            // if position item that want to delete it equal copyItem -> change copyItem to default value  = -1
            copyItem = -1;
        } else if (position < copyItem)
            // if position item that want to delete it before copyItem -> decrease the copyItem "1"
            --copyItem;

        favoriteItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favoriteItems.size());
    }

    public void setCopyItem(int position) {
        copyItem = position;
        notifyDataSetChanged();
    }

    public void clear() {
        copyItem = -1;
        this.favoriteItems.clear();
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    class CouponFavoriteViewHolder extends BaseViewHolder {
        private ItemCouponRvBinding itemView;

        public CouponFavoriteViewHolder(ItemCouponRvBinding itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.imgFavoriteCouponItemCouponRv.setVisibility(View.GONE);
            this.itemView.imgDeleteCouponItemCouponRv.setVisibility(View.VISIBLE);
        }

        public void onBind(int position) {
            super.onBind(position);
            FavoriteResponse.Favorite favorite = favoriteItems.get(position);
            // desc
            this.itemView.tvDescItemCouponRv.setText(favorite.getDesc());
            // check if allow to display count coupon used or not
            if (favorite.isAllowToOfferCountUsed()) {
                // display count coupon used
                this.itemView.tvTextNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemView.tvNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemView.tvNumTimesUsedItemCouponRv.setText(favorite.getCountUsed() + " " + mContext.getString(R.string.times));
            } else {
                // hide count coupon used
                this.itemView.tvTextNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                this.itemView.tvNumTimesUsedItemCouponRv.setVisibility(View.GONE);
            }
            // check if allow to display last used or not
            if (favorite.isAllowToOfferLastUseDate()) {
                // display last used
                this.itemView.tvTextLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemView.tvLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemView.tvLastDateUsedItemCouponRv.setText(favorite.getLastUseDate());
            } else {
                // hide last used
                this.itemView.tvTextLastDateUsedItemCouponRv.setVisibility(View.GONE);
                this.itemView.tvLastDateUsedItemCouponRv.setVisibility(View.GONE);
            }
            // load image company
            Glide.with(mContext)
                    .load(favorite.getCompanyImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .into(this.itemView.imgCompanyItemCouponRv);

            // when the user click on shop now
            this.itemView.tvShopNowItemCouponRv.setOnClickListener(v -> {
                favoriteClickListener.onClickItem(favorite);
            });
            // when the user click to share coupon
            this.itemView.imgShareItemCouponRv.setOnClickListener(v -> {
                favoriteClickListener.shareCoupon(position, favorite);
            });
            // when the user click to delete coupon
            this.itemView.imgDeleteCouponItemCouponRv.setOnClickListener(v -> {
                removeAt(position);
                favoriteClickListener.deleteCouponFromFavorite(position, favorite);
            });
            // when the user click to copy coupon
            this.itemView.tvCopyCouponItemCouponRv.setOnClickListener(v -> {
                setCopyItem(position);
                startAnimation = true;
                favoriteClickListener.copyCoupon(position, favorite);
            });

            this.itemView.shimmerCopyCoupon.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                //write your code here to be executed after 1 second
                this.itemView.shimmerCopyCoupon.setVisibility(View.GONE);
            }, 800);

            // check if position == coupon has been copied -> will show code coupon
            // if not -> hide code coupon and show copy coupon text
            if (position == copyItem) {
                // change text to code coupon
                this.itemView.tvCopyCouponItemCouponRv.setText(favorite.getCouponCode());
                this.itemView.tvCopyCouponItemCouponRv.setTextColor(mContext.getResources().getColor(R.color.pink));
                // change background
                this.itemView.tvCopyCouponItemCouponRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_pink_raduis_9dp));
                if (startAnimation) {
                    // start animation
                    this.itemView.tvCopyCouponItemCouponRv.startAnimation(shake);
                    this.itemView.getRoot().startAnimation(shake);
                    startAnimation = false;
                }
            } else {
                // change code coupon to text copy coupon
                this.itemView.tvCopyCouponItemCouponRv.setText(mContext.getString(R.string.get_code));
                this.itemView.tvCopyCouponItemCouponRv.setTextColor(mContext.getResources().getColor(R.color.white));
                // change background
                this.itemView.tvCopyCouponItemCouponRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_gradient_pink_9dp));
            }
        }
    }

    class ProductFavoriteViewHolder extends BaseViewHolder {
        ItemProductRvBinding itemProductRvBinding;

        public ProductFavoriteViewHolder(ItemProductRvBinding itemProductRvBinding) {
            super(itemProductRvBinding);
            this.itemProductRvBinding = itemProductRvBinding;
            this.itemProductRvBinding.imgFavoriteProductItemProductRv.setVisibility(View.GONE);
            this.itemProductRvBinding.imgDeleteProductItemProductRv.setVisibility(View.VISIBLE);
        }

        public void onBind(int position) {
            super.onBind(position);
            FavoriteResponse.Favorite favorite = favoriteItems.get(position);
            this.itemProductRvBinding.tvProductNameItemProductRv.setText(favorite.getNameProduct());
            this.itemProductRvBinding.tvDescItemProductRv.setText(favorite.getDesc());
            // check if file path "" then this product not have video
            if (TextUtils.isEmpty(favorite.getFilePath())) {
                // hide icon video
                this.itemProductRvBinding.imgVideoItemProductRv.setVisibility(View.GONE);
                this.itemProductRvBinding.imgCompanyItemProductRv.setOnClickListener(v -> {
                    favoriteClickListener.openImage(favorite);
                });
            } else {
                // show icon video
                this.itemProductRvBinding.imgVideoItemProductRv.setVisibility(View.VISIBLE);
                this.itemProductRvBinding.imgCompanyItemProductRv.setOnClickListener(v -> {
                    favoriteClickListener.openVideo(favorite);
                });
            }
            // loading image company
            Glide.with(mContext)
                    .load(favorite.getImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(this.itemProductRvBinding.imgCompanyItemProductRv);
            // when the user click on item
            this.itemView.getRootView().setOnClickListener(v -> {
                favoriteClickListener.onClickItem(favorite);
            });
            // when the user click to delete coupon
            this.itemProductRvBinding.imgDeleteProductItemProductRv.setOnClickListener(v -> {
                removeAt(position);
                favoriteClickListener.deleteProductFromFavorite(position, favorite);
            });
            // when the user click to copy coupon
            this.itemProductRvBinding.tvCopyCouponItemProductRv.setOnClickListener(v -> {
                setCopyItem(position);
                startAnimation = true;
                favoriteClickListener.copyCoupon(position, favorite);
            });

            // when the user click to share product
            this.itemProductRvBinding.imgShareItemProductRv.setOnClickListener(v -> {
                favoriteClickListener.shareProduct(position, favorite);
            });


            // check if position == coupon has been copied -> will show code coupon
            // if not -> hide code coupon and show copy coupon text
            if (position == copyItem) {
                // change text to code coupon
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setText(favorite.getCouponCode());
                // change background
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setBackground(mContext.getDrawable(R.drawable.shape_stroke_pink_raduis_9dp));
                if (startAnimation) {
                    // start animation
                    this.itemProductRvBinding.tvCopyCouponItemProductRv.startAnimation(shake);
                    this.itemProductRvBinding.getRoot().startAnimation(shake);
                    startAnimation = false;
                }
            } else {
                // change code coupon to text copy coupon
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setText(mContext.getString(R.string.get_code));
                // change background
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setBackground(mContext.getDrawable(R.drawable.shape_gray1_radius_9dp));
            }
        }
    }

    public interface FavoriteClickListener {
        void shareProduct(int position, FavoriteResponse.Favorite favorite);

        void shareCoupon(int position, FavoriteResponse.Favorite favorite);

        void deleteCouponFromFavorite(int position, FavoriteResponse.Favorite favorite);

        void deleteProductFromFavorite(int position, FavoriteResponse.Favorite favorite);

        void copyCoupon(int position, FavoriteResponse.Favorite favorite);

        void openImage(FavoriteResponse.Favorite favorite);

        void openVideo(FavoriteResponse.Favorite favorite);

        void onClickItem(FavoriteResponse.Favorite favorite);
    }
}
