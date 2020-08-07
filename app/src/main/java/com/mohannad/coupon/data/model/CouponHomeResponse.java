package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponHomeResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("items")
    private Item items;

    public boolean isStatus() {
        return status;
    }

    public Item getItems() {
        return items;
    }

    public static class Item {
        @SerializedName("current_page")
        private int currentPage;
        @SerializedName("data")
        private List<Coupon> coupons;
        @SerializedName("first_page_url")
        private String firstPageUrl;
        @SerializedName("from")
        private int from;
        @SerializedName("last_page")
        private int lastPage;
        @SerializedName("last_page_url")
        private String lastPageUrl;
        @SerializedName("next_page_url")
        private String nextPageUrl;
        @SerializedName("per_page")
        private int perPage;

        public int getCurrentPage() {
            return currentPage;
        }

        public List<Coupon> getCoupons() {
            return coupons;
        }

        public String getFirstPageUrl() {
            return firstPageUrl;
        }

        public int getFrom() {
            return from;
        }

        public int getLastPage() {
            return lastPage;
        }

        public String getLastPageUrl() {
            return lastPageUrl;
        }

        public String getNextPageUrl() {
            return nextPageUrl;
        }

        public int getPerPage() {
            return perPage;
        }
    }

    public static class Coupon {
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

        public void setInFavorite(boolean inFavorite) {
            this.inFavorite = inFavorite;
        }
    }
}
