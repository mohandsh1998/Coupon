package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("coupons")
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
}
