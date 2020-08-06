package com.mohannad.coupon.data.network;

import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CopyCouponResponse;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/api/helps")
    Call<HelpResponse> getHelpContents(@Header("lang") String lang);

    @GET("/api/deals")
    Call<DealResponse> getDeals(@Header("lang") String lang,
                                @Header("country-id") int countryId,
                                @Query("page") int page);

    @POST("/api/favorite")
    Call<FavoriteResponse> getFavorites(
            @Header("lang") String lang,
            @Header("Authorization") String token);

    /**** HOME PAGE ****/

    // categories tabs that will show in home page
    @GET("/api/stores")
    Call<CategoriesResponse> getCategories(@Header("lang") String lang);

    // companies that will show inside each category in home page
    @GET("/api/all_companies/{idCategory}")
    Call<CompaniesResponse> getCompanies(@Header("lang") String lang,
                                         @Header("country-id") int countryId,
                                         @Path("idCategory") int idCategory);

    // all coupons for category in home page
    @GET("/api/store/{idCategory}/coupon/")
    Call<CouponHomeResponse> getAllCouponsCategory(@Header("lang") String lang,
                                                   @Header("country-id") int countryId,
                                                   @Path("idCategory") int idCategory,
                                                   @Query("page") int page);

    // all coupons for company in home page
    @GET("/api/company/{idCompany}/coupon/")
    Call<CouponHomeResponse> getAllCouponsCompany(@Header("lang") String lang,
                                                  @Header("country-id") int countryId,
                                                  @Path("idCompany") int idCompany,
                                                  @Query("page") int page);

    // copy coupon by user
    @POST("/api/coupon/{idCoupon}/copy")
    Call<CopyCouponResponse> copyCoupon(@Header("lang") String lang,
                                        @Path("idCoupon") int idCoupon);
    // add or remove coupon to favorite
    @POST("/api/favorite/coupon/{idCoupon}")
    Call<MessageResponse> addOrRemoveCouponFavorite(@Header("lang") String lang,
                                                    @Header("Authorization") String token,
                                                    @Path("idCoupon") int idCoupon);

}
