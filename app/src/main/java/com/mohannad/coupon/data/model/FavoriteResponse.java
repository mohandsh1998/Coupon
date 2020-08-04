package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("favorites")
    private List<Favorite> favorites;

    public boolean isStatus() {
        return status;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public static class Favorite {
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
    }
}
