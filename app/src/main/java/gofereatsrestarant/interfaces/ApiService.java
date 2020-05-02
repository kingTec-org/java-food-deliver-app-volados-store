package gofereatsrestarant.interfaces;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage interfaces
 * @category ApiService
 * @author Trioangle Product Team
 * @version 1.0
 **/

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/*****************************************************************
 ApiService
 ****************************************************************/

public interface ApiService {

    // Login
    @GET("login")
    Call<ResponseBody> login(@Query("type") String type, @QueryMap HashMap<String, String> hashMap);

    // Update Device ID for Push notification
    @GET("update_device_id")
    Call<ResponseBody> updateDeviceId(@Query("type") Integer type, @Query("token") String token, @Query("device_type") String device_type, @Query("device_id") String device_id);

    // get order list for home page
    @GET("orders")
    Call<ResponseBody> getOrders(@Query("token") String token);

    // accept order in home page
    @GET("accept_order")
    Call<ResponseBody> acceptOrders(@Query("order_id") Integer orderId, @Query("token") String token);

    // get order details for selected order
    @GET("store_order_details")
    Call<ResponseBody> orderDetails(@Query("order_id") Integer orderId, @Query("token") String token);

    // cancel selected order
    @GET("get_cancel_reason")
    Call<ResponseBody> cancelOrdersReason(@Query("type") Integer userType, @Query("token") String token);

    // cancel selected order
    @GET("store_cancel_order")
    Call<ResponseBody> cancelOrders(@Query("cancel_reason") Integer cancelReason, @Query("cancel_message") String cancelMessage, @Query("order_id") Integer orderId, @Query("token") String token);

    // delay selected order
    @GET("delay_order")
    Call<ResponseBody> delayOrders(@Query("order_id") Integer orderId, @Query("delay_min") Integer delayMin, @Query("delay_message") String delayMessage, @Query("token") String token);

    // Store not accept the request
    @GET("refund")
    Call<ResponseBody> refund(@Query("order_id") Integer orderId, @Query("token") String token);

    // done selected order
    @GET("food_ready")
    Call<ResponseBody> doneOrders(@Query("order_id") Integer orderId, @Query("token") String token);

    @GET("store_menu")
    Call<ResponseBody> getstoreMenu(@Query("token") String token);

    @GET("toggle_visible")
    Call<ResponseBody> togglevisible(@Query("token") String token, @Query("type") String type, @Query("id") int id);

    // Review to Driver
    @GET("review_store_to_driver")
    Call<ResponseBody> reviewToDriver(@Query("token") String token, @Query("order_id") int orderId, @Query("is_thumbs") int isThumbs, @Query("issues") String issues);

    @GET("logout")
    Call<ResponseBody> logout(@Query("token") String token);

    @GET("order_history")
    Call<ResponseBody> getOrderHistory(@Query("token") String token);

    @GET("store_availabilty")
    Call<ResponseBody> storeavailtoggle(@Query("token") String token, @Query("status") int status);

    @GET("language")
    Call<ResponseBody> updateLanguage(@Query("type") String type,@Query("token") String token, @Query("language") String language);

}
