package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

public class CopyCouponResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("coupon")
    private Coupon coupon;

    public boolean isStatus() {
        return status;
    }

    public Coupon getCoupon() {
        return coupon;
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
        private String type;

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
    }
}
