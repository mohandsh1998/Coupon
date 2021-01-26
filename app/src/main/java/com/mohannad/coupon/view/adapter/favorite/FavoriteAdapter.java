package com.mohannad.coupon.view.adapter.favorite;

import android.content.Context;
import android.graphics.Paint;
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
import com.mohannad.coupon.databinding.ItemCouponsRvBinding;
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
    private Animation bottomTop;
    private Animation centerTop;
    private int shopItem;
    private int copyItem = -1;
    private int thanksAnimItem;
    // tag to start animation when copy coupon only
    private boolean startAnimation = false;

    private FavoriteClickListener favoriteClickListener;

    public FavoriteAdapter(Context context, ArrayList<FavoriteResponse.Favorite> favoriteItems, FavoriteClickListener favoriteClickListener) {
        this.mContext = context;
        this.favoriteItems = favoriteItems;
        this.favoriteClickListener = favoriteClickListener;
        // animation when copy coupon
        shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        bottomTop = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_70);
        centerTop = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_0);
        shopItem = -1;
        copyItem = -1;
        thanksAnimItem = -1;
    }

    public void setShopItem(int position) {
        shopItem = position;
        notifyDataSetChanged();
    }

    public void setThanksAnimItem(int position) {
        thanksAnimItem = position;
        notifyDataSetChanged();
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
                ItemCouponsRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_coupons_rv, parent, false);
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
        private ItemCouponsRvBinding itemCouponRvBinding;

        public CouponFavoriteViewHolder(ItemCouponsRvBinding itemView) {
            super(itemView);
            this.itemCouponRvBinding = itemView;
            this.itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setVisibility(View.INVISIBLE);
            this.itemCouponRvBinding.imgDeleteCouponItemCouponRv.setVisibility(View.VISIBLE);
        }

        public void onBind(int position) {
            super.onBind(position);
            FavoriteResponse.Favorite favorite = favoriteItems.get(position);
            // company name
            this.itemCouponRvBinding.tvCompanyNameItemCouponRv.setText(favorite.getCompanyName());
            // desc
            this.itemCouponRvBinding.tvDescItemCouponRv.setText(favorite.getDesc());
            // load image company
            Glide.with(mContext)
                    .load(favorite.getCompanyImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .into(this.itemCouponRvBinding.imgCompanyItemCouponRv);

            // check if allow to display num of used to coupon or not
            if (favorite.isAllowToOfferCountUsed() || favorite.isAllowToOfferLastUseDate()) {
                itemCouponRvBinding.lyData.setVisibility(View.VISIBLE);
            } else {
                itemCouponRvBinding.lyData.setVisibility(View.GONE);
            }
            // check if allow to display count coupon used or not
            if (favorite.isAllowToOfferCountUsed()) {
                // display count coupon used
                this.itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setText(favorite.getCountUsed() + " " + mContext.getString(R.string.times));
            } else {
                // hide count coupon used
                this.itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                this.itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.GONE);
            }
            // check if allow to display last used or not
            if (favorite.isAllowToOfferLastUseDate()) {
                // display last used
                this.itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                this.itemCouponRvBinding.tvLastDateUsedItemCouponRv.setText(favorite.getLastUseDate());
            } else {
                // hide last used
                this.itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.GONE);
                this.itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.GONE);
            }

            // when the user click to copy coupon
            this.itemCouponRvBinding.tvCopyCouponItemCouponRv.setOnClickListener(v -> {
                setCopyItem(position);
                startAnimation = true;
                favoriteClickListener.copyCoupon(position, favorite);
            });

            // when the user click to share coupon
            this.itemCouponRvBinding.imgShareItemCouponRv.setOnClickListener(v -> {
                favoriteClickListener.shareCoupon(position, favorite);
            });

            // when the user click on shop now
            this.itemCouponRvBinding.tvShopNowItemCouponRv.setOnClickListener(v -> {
                // show question views and hide coupon content views when the user click on shop now
                setShopItem(position);
                favoriteClickListener.onClickItem(favorite);
            });
            // when the user click to answer yes on question
            itemCouponRvBinding.btnYesItemCouponRv.setOnClickListener(v -> {
                // hide question views and show coupon content views when the user answer on question
                setShopItem(-1);
                setThanksAnimItem(position);
                favoriteClickListener.answerQuestion(position, favorite, true);
            });
            // when the user click to answer no on question
            itemCouponRvBinding.btnNoItemCouponRv.setOnClickListener(v -> {
                // hide question views and show coupon content views when the user answer on question
                setShopItem(-1);
                favoriteClickListener.answerQuestion(position, favorite, false);
            });
            // check if position == coupon has been shopped -> will show question views and hide coupon content views
            // if not -> hide question views and show coupon content views
            if (position == shopItem) {
                visibleOrHideQuestionViews(true);
                visibleOrHideContentCouponViews(false, favorite.isAllowToOfferCountUsed(), favorite.isAllowToOfferLastUseDate());
            } else {
                visibleOrHideQuestionViews(false);
                visibleOrHideContentCouponViews(true, favorite.isAllowToOfferCountUsed(), favorite.isAllowToOfferLastUseDate());
            }
            // when the user click to delete coupon
            this.itemCouponRvBinding.imgDeleteCouponItemCouponRv.setOnClickListener(v -> {
                removeAt(position);
                favoriteClickListener.deleteCouponFromFavorite(position, favorite);
            });

            this.itemCouponRvBinding.shimmerCopyCoupon.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                //write your code here to be executed after 1 second
                this.itemCouponRvBinding.shimmerCopyCoupon.setVisibility(View.GONE);
            }, 800);
            itemCouponRvBinding.lyThanks.setVisibility(View.GONE);
            // check if position == coupon has been answer yes -> will show animation thank u
            if (thanksAnimItem == position) {
                // show with animation thanks layout from bottom to top
                itemCouponRvBinding.lyThanks.startAnimation(bottomTop);
                itemCouponRvBinding.lyThanks.setVisibility(View.VISIBLE);
                bottomTop.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        thanksAnimItem = -1;
                        // timer 1 sec for anim
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            // when the first animation end -> start the second animation from center to top to hide thanks layout
                            itemCouponRvBinding.lyThanks.startAnimation(centerTop);
                        }, 1000);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                // listener when finished the second animation hide the thanks layout
                centerTop.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        itemCouponRvBinding.lyThanks.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
            // check if position == coupon has been copied -> will show code coupon
            // if not -> hide code coupon and show copy coupon text
            if (position == copyItem) {
                // change text to code coupon
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setText(favorite.getCouponCode());
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_copy, 0, 0, 0);
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setTextColor(mContext.getResources().getColor(R.color.pink));
                // change background
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_gray_raduis_9dp));
                if (startAnimation) {
                    // start animation
                    itemCouponRvBinding.tvCopyCouponItemCouponRv.startAnimation(shake);
                    itemCouponRvBinding.getRoot().startAnimation(shake);
                    startAnimation = false;
                }
            } else {
                // change code coupon to text copy coupon
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setText(mContext.getString(R.string.get_code));
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setTextColor(mContext.getResources().getColor(R.color.pink));
                // change background
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_pink_light_raduis_9dp));
            }
            if (!TextUtils.isEmpty(favorite.getBestSellingTitle())) {
                itemCouponRvBinding.tvBestSellingItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvBestSellingItemCouponRv.setText(favorite.getBestSellingTitle());
                itemCouponRvBinding.tvBestSellingItemCouponRv.setOnClickListener(v -> {
                    favoriteClickListener.bestSelling(position, favorite);
                });
            } else {
                itemCouponRvBinding.tvBestSellingItemCouponRv.setVisibility(View.GONE);
            }
        }

        // show or hide question views
        private void visibleOrHideQuestionViews(boolean visible) {
            if (visible) {
                itemCouponRvBinding.lyQuestion.setVisibility(View.VISIBLE);
            } else {
                itemCouponRvBinding.lyQuestion.setVisibility(View.GONE);
            }
        }

        // show or hide content coupon views
        private void visibleOrHideContentCouponViews(boolean visible, boolean timesOfUsed, boolean lastUsed) {
            if (visible) {
                itemCouponRvBinding.tvDescItemCouponRv.setVisibility(View.VISIBLE);
                if (timesOfUsed || lastUsed) {
                    itemCouponRvBinding.lyData.setVisibility(View.VISIBLE);
                }
                if (timesOfUsed) {
                    itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                    itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                }
                if (lastUsed) {
                    itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                    itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                }
            } else {
                itemCouponRvBinding.lyData.setVisibility(View.GONE);
                itemCouponRvBinding.tvDescItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.GONE);
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
            this.itemProductRvBinding.tvNumReviewsItemProductRv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            this.itemProductRvBinding.tvNumOrderItemProductRv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        }

        public void onBind(int position) {
            super.onBind(position);
            FavoriteResponse.Favorite favorite = favoriteItems.get(position);

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
            // name product
            this.itemProductRvBinding.tvProductNameItemProductRv.setText(favorite.getNameProduct());
            // desc
            this.itemProductRvBinding.tvDescItemProductRv.setText(favorite.getDesc());
            if (favorite.getRate() != 0) {
                this.itemProductRvBinding.appCompatRatingBar.setVisibility(View.VISIBLE);
                this.itemProductRvBinding.tvReviewsItemProductRv.setVisibility(View.VISIBLE);
                this.itemProductRvBinding.appCompatRatingBar.setRating(favorite.getRate());
                this.itemProductRvBinding.tvReviewsItemProductRv.setText(Float.toString(favorite.getRate()));
            } else {
                this.itemProductRvBinding.appCompatRatingBar.setVisibility(View.GONE);
                this.itemProductRvBinding.tvReviewsItemProductRv.setVisibility(View.GONE);
            }
            if (favorite.getNumberReviews() != 0) {
                this.itemProductRvBinding.tvNumReviewsItemProductRv.setVisibility(View.VISIBLE);
                this.itemProductRvBinding.tvNumReviewsItemProductRv.setText(favorite.getNumberReviews() + " " + mContext.getString(R.string.reviews));
            } else {
                this.itemProductRvBinding.tvNumReviewsItemProductRv.setVisibility(View.GONE);
            }

            if (favorite.getNumberOrders() != 0) {
                this.itemProductRvBinding.tvNumOrderItemProductRv.setVisibility(View.VISIBLE);
                this.itemProductRvBinding.tvNumOrderItemProductRv.setText(favorite.getNumberOrders() + " " + mContext.getString(R.string.orders));
            } else {
                this.itemProductRvBinding.tvNumOrderItemProductRv.setVisibility(View.GONE);
            }

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

            // check if product coupon code empty hide "copy code" else Show "copy code"
            if (TextUtils.isEmpty(favorite.getCouponCode())) {
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setVisibility(View.GONE);
            } else {
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setVisibility(View.VISIBLE);
            }

            // check if position == coupon has been copied -> will show code coupon
            // if not -> hide code coupon and show copy coupon text
            if (position == copyItem) {
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setTextColor(ContextCompat.getColor(mContext, R.color.pink));
                // change text to code coupon
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setText(favorite.getCouponCode());
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_copy, 0, 0, 0);
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setTextColor(mContext.getResources().getColor(R.color.pink));
                // change background
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_stroke_gray_raduis_9dp));
                if (startAnimation) {
                    // start animation
                    this.itemProductRvBinding.tvCopyCouponItemProductRv.startAnimation(shake);
                    this.itemProductRvBinding.getRoot().startAnimation(shake);
                    startAnimation = false;
                }
            } else {
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                // change code coupon to text copy coupon
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setText(mContext.getString(R.string.get_code));
                // change background
                this.itemProductRvBinding.tvCopyCouponItemProductRv.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_solid_pink_radius_9dp));
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

        void answerQuestion(int position, FavoriteResponse.Favorite favorite, boolean b);

        void bestSelling(int position, FavoriteResponse.Favorite favorite);
    }
}
