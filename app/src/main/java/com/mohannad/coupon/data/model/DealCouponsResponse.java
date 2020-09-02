package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DealCouponsResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("coupons")
    private List<Coupon> coupons;

    public boolean isStatus() {
        return status;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }
}
