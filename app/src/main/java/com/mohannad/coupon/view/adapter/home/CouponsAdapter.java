package com.mohannad.coupon.view.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.databinding.ItemAdsRvBinding;
import com.mohannad.coupon.databinding.ItemCouponRvBinding;
import com.mohannad.coupon.databinding.ItemTitleRvBinding;

import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.BaseViewHolder> {
    private final int VIEW_TYPE_COUPONS = 1;
    private final int VIEW_TYPE_ADS = 2;
    private final int VIEW_TYPE_TITLE = 3;
    private Context mContext;
    private List<CouponHomeResponse.Coupon> couponList;
    private Animation shake;

    private CouponClickListener couponClickListener;

    public CouponsAdapter(Context mContext, List<CouponHomeResponse.Coupon> couponList, CouponClickListener couponClickListener) {
        this.mContext = mContext;
        this.couponList = couponList;
        // animation when copy coupon
        shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        this.couponClickListener = couponClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (couponList.get(position).getType().equals("coupon")) {
            return VIEW_TYPE_COUPONS;
        } else if (couponList.get(position).getType().equals("ads")) {
            return VIEW_TYPE_ADS;
        } else {
            return VIEW_TYPE_TITLE;
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_COUPONS:
                return new CouponViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_coupon_rv, parent, false));
            case VIEW_TYPE_ADS:
                return new AdsViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_ads_rv, parent, false));
            case VIEW_TYPE_TITLE:
                return new TitleViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_title_rv, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public void addAll(List<CouponHomeResponse.Coupon> coupons) {
        this.couponList.addAll(coupons);
        notifyDataSetChanged();
    }

    public void clear() {
        this.couponList.clear();
    }

    // view holder for coupons
    class CouponViewHolder extends BaseViewHolder {
        private ItemCouponRvBinding itemCouponRvBinding;

        public CouponViewHolder(@NonNull ItemCouponRvBinding itemCouponRvBinding) {
            super(itemCouponRvBinding);
            this.itemCouponRvBinding = itemCouponRvBinding;
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            CouponHomeResponse.Coupon coupon = couponList.get(position);
            // name company
            itemCouponRvBinding.tvCompanyNameItemCouponRv.setText(coupon.getCompanyName());
            // when user click to copy coupon
            itemCouponRvBinding.tvCopyCouponItemCouponRv.setOnClickListener(v -> {
                // change code text
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setText(coupon.getCouponCode());
                // change background
                itemCouponRvBinding.tvCopyCouponItemCouponRv.setBackground(mContext.getDrawable(R.drawable.shape_stroke_pink_raduis_9dp));
                // start animation
                itemCouponRvBinding.tvCopyCouponItemCouponRv.startAnimation(shake);
                itemCouponRvBinding.getRoot().startAnimation(shake);
                couponClickListener.copyCoupon(position, coupon);
            });

            // description coupon
            itemCouponRvBinding.tvDescItemCouponRv.setText(coupon.getDesc());
            // load img company
            Glide.with(mContext)
                    .load(coupon.getCompanyImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
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
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }
    }

    // view holder for ads
    class AdsViewHolder extends BaseViewHolder {
        private ItemAdsRvBinding itemAdsRvBinding;

        public AdsViewHolder(ItemAdsRvBinding itemAdsRvBinding) {
            super(itemAdsRvBinding);
            this.itemAdsRvBinding = itemAdsRvBinding;
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            CouponHomeResponse.Coupon coupon = couponList.get(position);
            // title ads
            this.itemAdsRvBinding.tvTitleItemAdsRv.setText(coupon.getTitle());
            // desc ads
            this.itemAdsRvBinding.tvDescItemAdsRv.setText(coupon.getDesc());
            // load img ads
            Glide.with(mContext)
                    .load(coupon.getImage())
                    //  .placeholder(R.drawable.loading_spinner)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                    .into(this.itemAdsRvBinding.imgAdsItemAdsRv);
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }
    }

    // view holder for title top products
    class TitleViewHolder extends BaseViewHolder {
        ItemTitleRvBinding itemTitleRvBinding;

        public TitleViewHolder(ItemTitleRvBinding itemTitleRvBinding) {
            super(itemTitleRvBinding);
            this.itemTitleRvBinding = itemTitleRvBinding;
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            CouponHomeResponse.Coupon coupon = couponList.get(position);
            itemTitleRvBinding.tvTitleTopProduct.setText(coupon.getTitle());
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        private int mCurrentPosition;

        public BaseViewHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
        }

        public void onBind(int position) {
            mCurrentPosition = position;
        }

        public int getCurrentPosition() {
            return mCurrentPosition;
        }
    }

    public interface CouponClickListener {
        void copyCoupon(int position, CouponHomeResponse.Coupon coupon);
    }

}
