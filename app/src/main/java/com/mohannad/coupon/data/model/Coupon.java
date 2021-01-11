package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

public class Coupon {
    @SerializedName("id")
    private int id;
    @SerializedName("desc")
    private String desc;
    @SerializedName("last_use_date")
    private String lastUseDate;
    @SerializedName("count_used")
    private int countUsed;
    @SerializedName("coupon_code")
    private String couponCode;
    @SerializedName("link")
    private String link;
    @SerializedName("allow_to_offer_count_used")
    private boolean allowToOfferCountUsed;
    @SerializedName("allow_to_offer_last_use_date")
    private boolean allowToOfferLastUseDate;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("company_image")
    private String companyImage;
    @SerializedName("type")
    private String type;    //coupon, title, ads
    @SerializedName("title")
    private String title;
    //image ads
    @SerializedName("image")
    private String image;
    @SerializedName("ads_type")
    private String adsType;
    @SerializedName("in_favorite")
    private boolean inFavorite;
    @SerializedName("best_selling")
    private String bestSelling;
    @SerializedName("best_selling_title")
    private String bestSellingTitle;
    // trend
    @SerializedName("store_id")
    private int categoryId;
    @SerializedName("company_id")
    private int companyId;

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getLastUseDate() {
        return lastUseDate;
    }

    public int getCountUsed() {
        return countUsed;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getLink() {
        return link;
    }

    public boolean isAllowToOfferCountUsed() {
        return allowToOfferCountUsed;
    }

    public boolean isAllowToOfferLastUseDate() {
        return allowToOfferLastUseDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyImage() {
        return companyImage;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getAdsType() {
        return adsType;
    }

    public boolean isInFavorite() {
        return inFavorite;
    }

    public String getBestSelling() {
        return bestSelling;
    }

    public String getBestSellingTitle() {
        return bestSellingTitle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setInFavorite(boolean inFavorite) {
        this.inFavorite = inFavorite;
    }
}