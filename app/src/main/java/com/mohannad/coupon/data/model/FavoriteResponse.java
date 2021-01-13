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
        @SerializedName("name")
        private String nameProduct;
        @SerializedName("image")
        private String image;
        @SerializedName("file_path")
        private String filePath;
        @SerializedName("rate")
        private float rate;
        @SerializedName("number_reviews")
        private int numberReviews;
        @SerializedName("number_orders")
        private int numberOrders;
        @SerializedName("type")
        private String type; // coupon or product
        @SerializedName("best_selling")
        private String bestSelling;
        @SerializedName("best_selling_title")
        private String bestSellingTitle;

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

        public String getNameProduct() {
            return nameProduct;
        }

        public String getImage() {
            return image;
        }

        public String getFilePath() {
            return filePath;
        }

        public float getRate() {
            return rate;
        }

        public int getNumberReviews() {
            return numberReviews;
        }

        public int getNumberOrders() {
            return numberOrders;
        }

        public String getType() {
            return type;
        }

        public String getBestSellingTitle() {
            return bestSellingTitle;
        }

        public String getBestSelling() {
            return bestSelling;
        }
    }
}
