package com.mohannad.coupon.data.network;

import com.mohannad.coupon.data.model.AuthResponse;
import com.mohannad.coupon.data.model.CategoriesResponse;
import com.mohannad.coupon.data.model.CompaniesResponse;
import com.mohannad.coupon.data.model.CopyCouponResponse;
import com.mohannad.coupon.data.model.CountryResponse;
import com.mohannad.coupon.data.model.CouponHomeResponse;
import com.mohannad.coupon.data.model.DealResponse;
import com.mohannad.coupon.data.model.FavoriteResponse;
import com.mohannad.coupon.data.model.HelpResponse;
import com.mohannad.coupon.data.model.MessageResponse;
import com.mohannad.coupon.data.model.ProductsResponse;
import com.mohannad.coupon.data.model.SearchResponse;
import com.mohannad.coupon.data.model.UsedCouponResponse;

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
                                                   @Header("Authorization") String token,
                                                   @Path("idCategory") int idCategory,
                                                   @Query("page") int page);

    // all coupons for company in home page
    @GET("/api/company/{idCompany}/coupon/")
    Call<CouponHomeResponse> getAllCouponsCompany(@Header("lang") String lang,
                                                  @Header("country-id") int countryId,
                                                  @Header("Authorization") String token,
                                                  @Path("idCompany") int idCompany,
                                                  @Query("page") int page);

    // copy coupon by user
    @POST("/api/coupon/{idCoupon}/copy")
    Call<CopyCouponResponse> copyCoupon(@Header("lang") String lang,
                                        @Header("Authorization") String token,
                                        @Path("idCoupon") int idCoupon);

    // add or remove coupon to favorite
    @POST("/api/favorite/coupon/{idCoupon}")
    Call<MessageResponse> addOrRemoveCouponFavorite(@Header("lang") String lang,
                                                    @Header("Authorization") String token,
                                                    @Path("idCoupon") int idCoupon);

    // all products for category
    @GET("/api/products/{idTitle}")
    Call<ProductsResponse> getAllProductsCategory(@Header("lang") String lang,
                                                  @Header("Authorization") String token,
                                                  @Path("idTitle") int idTitle,
                                                  @Query("store_id") int idCategory);

    // all products for company
    @GET("/api/products/{idTitle}")
    Call<ProductsResponse> getAllProductsCompany(@Header("lang") String lang,
                                                 @Header("Authorization") String token,
                                                 @Path("idTitle") int idTitle,
                                                 @Query("company_id") int idCompany);


    // add or remove product to favorite
    @POST("/api/favorite/product/{idProduct}")
    Call<MessageResponse> addOrRemoveProductFavorite(@Header("lang") String lang,
                                                     @Header("Authorization") String token,
                                                     @Path("idProduct") int idProduct);

    // all countries
    @GET("/api/countries")
    Call<CountryResponse> getCountries(@Header("lang") String lang);

    // send suggestion coupon
    @FormUrlEncoded
    @POST("/api/suggestion-coupon/create")
    Call<MessageResponse> suggestionCoupon(
            @Header("lang") String lang,
            @Field("email") String email,
            @Field("country_id") int countryId,
            @Field("coupon_code") String couponCode,
            @Field("company_id") int companyId,
            @Field("mobile") String mobile,
            @Field("desc") String desc
    );

    // change password
    @FormUrlEncoded
    @POST("/api/change-password")
    Call<MessageResponse> changePassword(
            @Header("lang") String lang,
            @Header("Authorization") String token,
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword,
            @Field("new_password_confirmation") String newPasswordConfirmation
    );

    // contact us
    @FormUrlEncoded
    @POST("/api/contactus/create")
    Call<MessageResponse> contactUs(
            @Header("lang") String lang,
            @Field("subject") String subject,
            @Field("email") String email,
            @Field("content") String content
    );

    // used coupons by user
    @GET("/api/used/coupon/")
    Call<UsedCouponResponse> getUsedCoupons(@Header("lang") String lang,
                                            @Header("Authorization") String token);

    // search on coupon by user
    @GET("/api/search/coupon")
    Call<SearchResponse> searchCoupon(@Header("lang") String lang,
                                      @Header("Authorization") String token,
                                      @Query("search_data") String word,
                                      @Query("page") int page);

}
