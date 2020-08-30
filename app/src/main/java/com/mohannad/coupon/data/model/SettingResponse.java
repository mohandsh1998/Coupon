package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("settings")
    private Setting setting;

    public boolean isStatus() {
        return status;
    }

    public Setting getSetting() {
        return setting;
    }

    public static class Setting {
        @SerializedName("instagram")
        private String instagram;
        @SerializedName("telegram")
        private String telegram;
        @SerializedName("snapchat")
        private String snapchat;
        @SerializedName("title_ads")
        private String titleAds;

        public String getInstagram() {
            return instagram;
        }

        public String getTelegram() {
            return telegram;
        }

        public String getSnapchat() {
            return snapchat;
        }

        public String getTitleAds() {
            return titleAds;
        }
    }
}
