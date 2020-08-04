package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HelpResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("helps")
    private List<Help> helps;

    public boolean isStatus() {
        return status;
    }

    public List<Help> getHelps() {
        return helps;
    }

    public static class Help {
        @SerializedName("id")
        private int id;
        @SerializedName("content")
        private String content;

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }
    }
}
