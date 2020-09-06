package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("deals")
    private Deal deal;
    @SerializedName("ads")
    private List<DealsAds> dealsAds;

    public boolean isStatus() {
        return status;
    }

    public Deal getDeal() {
        return deal;
    }

    public List<DealsAds> getDealsAds() {
        return dealsAds;
    }

    public static class Deal {
        @SerializedName("current_page")
        private int currentPage;
        @SerializedName("data")
        private List<DealItem> dealItems;
        @SerializedName("last_page")
        private int lastPage;
        @SerializedName("last_page_url")
        private String lastPageUrl;
        @SerializedName("next_page_url")
        private String nextPageUrl;

        public int getCurrentPage() {
            return currentPage;
        }

        public List<DealItem> getDealItems() {
            return dealItems;
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
    }

    public static class DealItem {
        @SerializedName("id")
        private int id;
        @SerializedName("desc_percentage")
        private String descPercentage;
        @SerializedName("percentage")
        private String percentage;
        @SerializedName("link")
        private String link;
        @SerializedName("content")
        private String content;
        @SerializedName("image")
        private String image;

        public int getId() {
            return id;
        }

        public String getDescPercentage() {
            return descPercentage;
        }

        public String getPercentage() {
            return percentage;
        }

        public String getLink() {
            return link;
        }

        public String getContent() {
            return content;
        }

        public String getImage() {
            return image;
        }
    }

    public static class DealsAds {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("desc")
        private String desc;
        @SerializedName("link")
        private String link;
        @SerializedName("image")
        private String image;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDesc() {
            return desc;
        }

        public String getLink() {
            return link;
        }

        public String getImage() {
            return image;
        }
    }
}
