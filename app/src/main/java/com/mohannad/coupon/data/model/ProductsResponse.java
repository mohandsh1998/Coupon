package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("products")
    private List<Product> products;

    public boolean isStatus() {
        return status;
    }

    public List<Product> getProducts() {
        return products;
    }

    public static class Product {
        @SerializedName("id")
        private int id;
        @SerializedName("desc")
        private String desc;
        @SerializedName("name")
        private String name;
        @SerializedName("coupon_code")
        private String couponCode;
        @SerializedName("link")
        private String link;
        @SerializedName("image")
        private String image;
        @SerializedName("file_path")
        private String filePath;
        @SerializedName("company_id")
        private int companyId;
        @SerializedName("store_id")
        private int storeId;
        @SerializedName("type")
        private String type;
        @SerializedName("in_favorite")
        private boolean inFavorite;

        public int getId() {
            return id;
        }

        public String getDesc() {
            return desc;
        }

        public String getName() {
            return name;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public String getLink() {
            return link;
        }

        public String getImage() {
            return image;
        }

        public String getFilePath() {
            return filePath;
        }

        public int getCompanyId() {
            return companyId;
        }

        public int getStoreId() {
            return storeId;
        }

        public String getType() {
            return type;
        }

        public boolean isInFavorite() {
            return inFavorite;
        }

        public void setInFavorite(boolean inFavorite) {
            this.inFavorite = inFavorite;
        }
    }
}
