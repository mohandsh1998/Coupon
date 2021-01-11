package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("titles")
    private List<Coupon> titles;

    public boolean isStatus() {
        return status;
    }

    public List<Coupon> getTitles() {
        return titles;
    }
}
