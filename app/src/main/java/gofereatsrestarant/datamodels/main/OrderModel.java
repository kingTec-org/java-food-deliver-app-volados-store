package gofereatsrestarant.datamodels.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.main
 * @category OrderModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 OrderModel
 ****************************************************************/
public class OrderModel {

    @SerializedName("id")
    @Expose
    private Integer orderId;
    @SerializedName("order_item_count")
    @Expose
    private Integer orderItemCount;

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("order_item_name")
    @Expose
    private String orderItem;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("order_price")
    @Expose
    private String orderPrice;
    @SerializedName("remaining_seconds")
    @Expose
    private Integer remainingSeconds;
    @SerializedName("total_seconds")
    @Expose
    private Integer totalSeconds;
    @SerializedName("status_text")
    @Expose
    private String orderStatus;
    @SerializedName("isSelected")
    @Expose
    private boolean isSeleted;
    @SerializedName("store_to_driver_thumbs")
    @Expose
    private String restaurantDriverThumbs;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("order_type")
    @Expose
    private int orderType;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("order_subtotal")
    @Expose
    private String orderSubtotal;
    @SerializedName("driver_image")
    @Expose
    private String driverImage;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(Integer orderItemCount) {
        this.orderItemCount = orderItemCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Integer getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(Integer remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public Integer getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(Integer totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getRestaurantDriverThumbs() {
        return restaurantDriverThumbs;
    }

    public void setRestaurantDriverThumbs(String restaurantDriverThumbs) {
        this.restaurantDriverThumbs = restaurantDriverThumbs;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderSubtotal() {
        return orderSubtotal;
    }

    public void setOrderSubtotal(String orderSubtotal) {
        this.orderSubtotal = orderSubtotal;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }
}
