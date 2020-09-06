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
        @SerializedName("rate")
        private int rate;
        @SerializedName("number_reviews")
        private int numberReviews;
        @SerializedName("number_orders")
        private int numberOrders;
        @SerializedName("type")
        private String type;
        @SerializedName("in_favorite")
        private boolean inFavorite;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCouponCode() {
            return couponCode;
        }

        public void setCouponCode(String couponCode) {
            this.couponCode = couponCode;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getNumberReviews() {
            return numberReviews;
        }

        public void setNumberReviews(int numberReviews) {
            this.numberReviews = numberReviews;
        }

        public int getNumberOrders() {
            return numberOrders;
        }

        public void setNumberOrders(int numberOrders) {
            this.numberOrders = numberOrders;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isInFavorite() {
            return inFavorite;
        }

        public void setInFavorite(boolean inFavorite) {
            this.inFavorite = inFavorite;
        }
    }
}
