package com.mohannad.coupon.callback;

public interface ResponseServer<T> {
    void onSuccess(boolean status, int code, T response);
    void onFailure(String message);
}
