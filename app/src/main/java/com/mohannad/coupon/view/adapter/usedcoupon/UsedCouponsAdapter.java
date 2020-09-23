package com.mohannad.coupon.view.adapter.usedcoupon;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.Coupon;
import com.mohannad.coupon.databinding.ItemCouponRvBinding;
import com.mohannad.coupon.utils.BaseViewHolder;

import java.util.List;

public class UsedCouponsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context mContext;
    private Animation shake;
    private List<Coupon> couponList;
    private CouponClickListener couponClickListener;
    private int shopItem;
    private int copyItem;
    private int thanksAnimItem;
    // tag to start animation when copy coupon only
    private boolean startAnimation = false;
    private Animation bottomTop;
    private Animation centerTop;

    public UsedCouponsAdapter(Context mContext, List<Coupon> couponList, CouponClickListener couponClickListener) {
        this.mContext = mContext;
        this.couponList = couponList;
        // animation when copy coupon
        shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        bottomTop = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_70);
        centerTop = AnimationUtils.loadAnimation(mContext, R.anim.from_bottom_0);
        this.couponClickListener = couponClickListener;
        shopItem = -1;
        copyItem = -1;
        thanksAnimItem = -1;
    }

    public void setShopItem(int position) {
        shopItem = position;
        notifyDataSetChanged();
    }

    public void setCopyItem(int position) {
        copyItem = position;
        notifyDataSetChanged();
    }

    public void setThanksAnimItem(int position) {
        thanksAnimItem = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CouponViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_coupon_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return couponList == null ? 0 : couponList.size();
    }

    public void addAll(List<Coupon> coupons) {
        this.couponList.addAll(coupons);
        notifyDataSetChanged();
    }

    public void clear() {
        shopItem = -1;
        copyItem = -1;
        this.couponList.clear();
    }

    public void changeStatusFavoriteCoupon(Coupon coupon) {
        // check if the coupon in favorite or not to change img favorite
        if (coupon.isInFavorite()) {
            coupon.setInFavorite(false);
        } else coupon.setInFavorite(true);
        notifyDataSetChanged();
    }


    // view holder for coupons
    class CouponViewHolder extends BaseViewHolder {
        private ItemCouponRvBinding itemCouponRvBinding;

        public CouponViewHolder(@NonNull ItemCouponRvBinding itemCouponRvBinding) {
            super(itemCouponRvBinding);
            this.itemCouponRvBinding = itemCouponRvBinding;
            this.itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setVisibility(View.VISIBLE);
            this.itemCouponRvBinding.imgDeleteCouponItemCouponRv.setVisibility(View.GONE);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            Coupon coupon = couponList.get(position);
            // description coupon
            itemCouponRvBinding.tvDescItemCouponRv.setText(coupon.getDesc());
            // load img company
            Glide.with(mContext)
                    .load(coupon.getCompanyImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .into(this.itemCouponRvBinding.imgCompanyItemCouponRv);
            // check if allow to display num of used to coupon or not
            if (coupon.isAllowToOfferCountUsed()) {
                itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setText(coupon.getCountUsed() + " " + mContext.getString(R.string.times));
            } else {
                itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.GONE);
            }
            // check if allow to display last of used to coupon or not
            if (coupon.isAllowToOfferLastUseDate()) {
                itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvLastDateUsedItemCouponRv.setText(coupon.getLastUseDate());

            } else {
                itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.GONE);
            }
            // check if the coupon in favorite or not
            if (coupon.isInFavorite()) {
                itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else {
                itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setImageResource(R.drawable.ic_favorite);
            }

            // when the user click to copy coupon
            itemCouponRvBinding.tvCopyCouponItemCouponRv.setOnClickListener(v -> {
                setCopyItem(position);
                startAnimation = true;
                couponClickListener.copyCoupon(position, coupon);
            });
            // when the user click to add coupon or remove to favorite
            itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setOnClickListener(v -> {
                // this to change img favorite
                changeStatusFavoriteCoupon(coupon);
                couponClickListener.addToFavoriteCoupon(position, coupon);
            });
            // when the user click to share coupon
            itemCouponRvBinding.imgShareItemCouponRv.setOnClickListener(v -> {
                couponClickListener.shareCoupon(position, coupon);
            });
            // when the user click to shop now
            itemCouponRvBinding.tvShopNowItemCouponRv.setOnClickListener(v -> {
                // show question views and hide coupon content views when the user click on shop now
                setShopItem(position);
                couponClickListener.shopNowCoupon(position, coupon);
            });
            // when the user click to answer yes on question
            itemCouponRvBinding.btnYesItemCouponRv.setOnClickListener(v -> {
                // hide question views and show coupon content views when the user answer on question
                setShopItem(-1);
                setThanksAnimItem(position);
                couponClickListener.answerQuestion(position, coupon, true);
            });
            // when the user click to answer no on question
            itemCouponRvBinding.btnNoItemCouponRv.setOnClickListener(v -> {
                // hide question views and show coupon content views when the user answer on question
                setShopItem(-1);
                couponClickListener.answerQuestion(position, coupon, false);
            });
            // check if position == coupon has been shopped -> will show question views and hide coupon content views
            // if not -> hide question views and show coupon content views
            if (position == shopItem) {
                visibleOrHideQuestionViews(true);
                visibleOrHideContentCouponViews(false, coupon.isAllowToOfferCountUsed(), coupon.isAllowToOfferCountUsed());
            } else {
                visibleOrHideQuestionViews(false);
                visibleOrHideContentCouponViews(true, coupon.isAllowToOfferCountUsed(), coupon.isAllowToOfferCountUsed());
            }

            itemCouponRvBinding.shimmerCopyCoupon.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                //write your code here to be executed after 1 second
                itemCouponRvBinding.shimmerCopyCoupon.setVisibility(View.GONE);
            }, 800);
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
                        // when the first animation end -> start the second animation from center to top to hide thanks layout
                        itemCouponRvBinding.lyThanks.startAnimation(centerTop);
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
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setText(coupon.getCouponCode());
                // change background
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setBackground(mContext.getDrawable(R.drawable.shape_stroke_pink_raduis_9dp));
                if (startAnimation) {
                    // start animation
                    itemCouponRvBinding.tvCopyCouponItemCouponRv.startAnimation(shake);
                    itemCouponRvBinding.getRoot().startAnimation(shake);
                    startAnimation = false;
                }
            } else {
                // change code coupon to text copy coupon
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setText(mContext.getString(R.string.code_copied));
                // change background
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setBackground(mContext.getDrawable(R.drawable.shape_gray1_radius_9dp));
            }
        }

        // show or hide question views
        private void visibleOrHideQuestionViews(boolean visible) {
            if (visible) {
                itemCouponRvBinding.tvExperienceItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvTellUsItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.btnYesItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.btnNoItemCouponRv.setVisibility(View.VISIBLE);
            } else {
                itemCouponRvBinding.tvExperienceItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvTellUsItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.btnYesItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.btnNoItemCouponRv.setVisibility(View.GONE);
            }
        }

        // show or hide content coupon views
        private void visibleOrHideContentCouponViews(boolean visible, boolean timesOfUsed, boolean lastUsed) {
            if (visible) {
                itemCouponRvBinding.tvDescItemCouponRv.setVisibility(View.VISIBLE);
                if (timesOfUsed) {
                    itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                    itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                }
                if (lastUsed) {
                    itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                    itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                }
            } else {
                itemCouponRvBinding.tvDescItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.GONE);
                itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.GONE);
            }
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }
    }


    public interface CouponClickListener {
        void shareCoupon(int position, Coupon coupon);

        void addToFavoriteCoupon(int position, Coupon coupon);

        void copyCoupon(int position, Coupon coupon);

        void shopNowCoupon(int position, Coupon coupon);

        void answerQuestion(int position, Coupon coupon, boolean answer);
    }

}
