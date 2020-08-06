package com.mohannad.coupon.data.model;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("msg")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
