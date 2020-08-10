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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mohannad.coupon.R;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.databinding.CompaniesLayoutBinding;
import com.mohannad.coupon.databinding.ItemAdsRvBinding;
import com.mohannad.coupon.databinding.ItemCouponRvBinding;
import com.mohannad.coupon.databinding.ItemTitleRvBinding;

import java.util.List;

public class CouponsAdapter extends RecyclerView.Adapter<CouponsAdapter.BaseViewHolder> {
    private final int VIEW_TYPE_COMPANIES = 0;
    private final int VIEW_TYPE_COUPONS = 1;
    private final int VIEW_TYPE_ADS = 2;
    private final int VIEW_TYPE_TITLE = 3;
    private boolean selectedAll = true;
    private Context mContext;
    private Animation shake;
    private CompaniesAdapter companiesAdapter;
    private List<CouponHomeResponse.Coupon> couponList;
    private CouponClickListener couponClickListener;
    private int shopItem;

    public CouponsAdapter(Context mContext, List<CouponHomeResponse.Coupon> couponList,
                          CompaniesAdapter companiesAdapter, CouponClickListener couponClickListener) {
        this.mContext = mContext;
        this.couponList = couponList;
        this.companiesAdapter = companiesAdapter;
        // animation when copy coupon
        shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        this.couponClickListener = couponClickListener;
        shopItem = -1;
    }

    public void setShopItem(int position) {
        shopItem = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_COMPANIES;
        } else {
            if (couponList.get(position - 1).getType().equals("coupon")) {
                return VIEW_TYPE_COUPONS;
            } else if (couponList.get(position - 1).getType().equals("ads")) {
                return VIEW_TYPE_ADS;
            } else {
                return VIEW_TYPE_TITLE;
            }
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_COMPANIES:
                return new CompaniesViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.companies_layout, parent, false));
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
        holder.onBind(position - 1);
    }

    @Override
    public int getItemCount() {
        return couponList == null ? 0 : couponList.size() + 1;
    }

    public void selectAllView(boolean selected) {
        selectedAll = selected;
        notifyDataSetChanged();
    }

    public void addAll(List<CouponHomeResponse.Coupon> coupons) {
        this.couponList.addAll(coupons);
        notifyDataSetChanged();
    }

    public void clear() {
        shopItem = -1;
        this.couponList.clear();
    }

    public void changeStatusFavoriteCoupon(CouponHomeResponse.Coupon coupon) {
        // check if the coupon in favorite or not to change img favorite
        if (coupon.isInFavorite()) {
            coupon.setInFavorite(false);
        } else coupon.setInFavorite(true);
        notifyDataSetChanged();
    }

    // view holder for companies
    class CompaniesViewHolder extends BaseViewHolder {
        CompaniesLayoutBinding companiesLayoutBinding;

        public CompaniesViewHolder(CompaniesLayoutBinding companiesLayoutBinding) {
            super(companiesLayoutBinding);
            this.companiesLayoutBinding = companiesLayoutBinding;
            this.companiesLayoutBinding.rvCompanies.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            this.companiesLayoutBinding.rvCompanies.setHasFixedSize(true);
            this.companiesLayoutBinding.rvCompanies.setAdapter(companiesAdapter);
        }

        @Override
        public void onBind(int position) {
            super.onBind(position);
            if (selectedAll) {
                // add border on all coupons
                this.companiesLayoutBinding.imgAllCompanies.setBackground(mContext.getDrawable(R.drawable.shape_white_with_border_pink_radius_9dp));
                // add shadow
                this.companiesLayoutBinding.imgAllCompanies.setElevation(24);
            } else {
                // remove border
                this.companiesLayoutBinding.imgAllCompanies.setBackground(mContext.getDrawable(R.drawable.shape_white_radius_9dp));
                // remove shadow
                this.companiesLayoutBinding.imgAllCompanies.setElevation(0);
            }
            this.companiesLayoutBinding.imgAllCompanies.setOnClickListener(v -> {
                selectedAll = true;
                couponClickListener.onClickAllCoupons();
            });
        }

        @Override
        public int getCurrentPosition() {
            return super.getCurrentPosition();
        }
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
            CouponHomeResponse.Coupon coupon = couponList.get(position);
            // name company
            itemCouponRvBinding.tvCompanyNameItemCouponRv.setText(coupon.getCompanyName());
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
            // check if the coupon in favorite or not
            if (coupon.isInFavorite()) {
                itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else {
                itemCouponRvBinding.imgFavoriteCouponItemCouponRv.setImageResource(R.drawable.ic_favorite);
            }

            // when the user click to copy coupon
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
                visibleOrHideContentCouponViews(false);
            } else {
                visibleOrHideQuestionViews(false);
                visibleOrHideContentCouponViews(true);
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
        private void visibleOrHideContentCouponViews(boolean visible) {
            if (visible) {
                itemCouponRvBinding.tvDescItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvTextNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvTextLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvNumTimesUsedItemCouponRv.setVisibility(View.VISIBLE);
                itemCouponRvBinding.tvLastDateUsedItemCouponRv.setVisibility(View.VISIBLE);
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
            // when the user click to shop now
            this.itemAdsRvBinding.tvShopNowItemAdsRv.setOnClickListener(v -> {
                couponClickListener.shopNowAds(position, coupon);
            });
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
        void shareCoupon(int position, CouponHomeResponse.Coupon coupon);

        void addToFavoriteCoupon(int position, CouponHomeResponse.Coupon coupon);

        void copyCoupon(int position, CouponHomeResponse.Coupon coupon);

        void shopNowCoupon(int position, CouponHomeResponse.Coupon coupon);

        void shopNowAds(int position, CouponHomeResponse.Coupon coupon);

        void answerQuestion(int position, CouponHomeResponse.Coupon coupon, boolean answer);

        void onClickAllCoupons();
    }

}
