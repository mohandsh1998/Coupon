package com.mohannad.coupon.data.network;

import com.mohannad.coupon.data.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("/api/register")
    Call<AuthResponse> registerUser(
            @Header("lang") String lang,
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("fcm_token") String fcmToken,
            @Field("device_type") String deviceType,
            @Field("allow_notification") int allowNotification
    );

    @FormUrlEncoded
    @POST("/api/login")
    Call<AuthResponse> loginUser(
            @Header("lang") String lang,
            @Field("email") String email,
            @Field("password") String password,
            @Field("fcm_token") String fcmToken,
            @Field("device_type") String deviceType,
            @Field("allow_notification") int allowNotification
    );
}
