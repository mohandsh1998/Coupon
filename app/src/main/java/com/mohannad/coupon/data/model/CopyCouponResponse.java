package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

public class CopyCouponResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("coupon")
    private Coupon coupon;

    public boolean isStatus() {
        return status;
    }

    public Coupon getCoupon() {
        return coupon;
    }
}
