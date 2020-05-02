package gofereatsrestarant.datamodels.orderdetails;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.main
 * @category OrderResultModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Order Details Result Model
 ****************************************************************/
public class OrderDetailsAllModel {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("status")
    @Expose
    private String orderStatus;
    @SerializedName("subtotal")
    @Expose
    private String subTotal;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("support_phone")
    @Expose
    private String supportPhone;
    @SerializedName("driver_name")
    @Expose
    private String driverName;
    @SerializedName("driver_image")
    @Expose
    private String driverImage;
    @SerializedName("pickup_location")
    @Expose
    private String pickupLocation;
    @SerializedName("drop_location")
    @Expose
    private String dropLocation;
    @SerializedName("pickup_latitude")
    @Expose
    private String pickupLatitude;
    @SerializedName("pickup_longitude")
    @Expose
    private String pickupLongitude;
    @SerializedName("drop_longitude")
    @Expose
    private String dropLongitude;
    @SerializedName("drop_latitude")
    @Expose
    private String dropLatitude;
    @SerializedName("driver_latitude")
    @Expose
    private String driverLatitude;
    @SerializedName("driver_longitude")
    @Expose
    private String driverLongitude;
    @SerializedName("order_delivery_status")
    @Expose
    private Integer orderDeliveryStatus;
    @SerializedName("completed_date_time")
    @Expose
    private String completedDateTime;
    @SerializedName("status_times")
    @Expose
    private OrderStatusModel statusTimes;
    @SerializedName("item_details")
    @Expose
    private ArrayList<OrderDetailsModel> itemDetails;
    @SerializedName("store_to_driver_thumbs")
    @Expose
    private String restaurantDriverThumbs;

    @SerializedName("is_request")
    @Expose
    private int isRequest;

    @SerializedName("order_notes")
    @Expose
    private String orderNotes;

    @SerializedName("restaurant_fee")
    @Expose
    private String accessFee;

    @SerializedName("order_time")
    @Expose
    private String orderTime;

    @SerializedName("order_date")
    @Expose
    private String orderDate;

    @SerializedName("res_penality")
    @Expose
    private String restaurantPenality;

    @SerializedName("applied_penality")
    @Expose
    private String appliedPenality;

    @SerializedName("driver_number")
    @Expose
    private String driverContactNumber;

    @SerializedName("vechile_name")
    @Expose
    private String driverVehicleType;

    @SerializedName("vechile_number")
    @Expose
    private String driverVehicleNumber;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSupportPhone() {
        return supportPhone;
    }

    public void setSupportPhone(String supportPhone) {
        this.supportPhone = supportPhone;
    }

    public OrderStatusModel getStatusTimes() {
        return statusTimes;
    }

    public void setStatusTimes(OrderStatusModel statusTimes) {
        this.statusTimes = statusTimes;
    }

    public ArrayList<OrderDetailsModel> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(ArrayList<OrderDetailsModel> itemDetails) {
        this.itemDetails = itemDetails;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(String pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public String getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(String pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public String getDropLongitude() {
        return dropLongitude;
    }

    public void setDropLongitude(String dropLongitude) {
        this.dropLongitude = dropLongitude;
    }

    public String getDropLatitude() {
        return dropLatitude;
    }

    public void setDropLatitude(String dropLatitude) {
        this.dropLatitude = dropLatitude;
    }

    public String getDriverLatitude() {
        return driverLatitude;
    }

    public void setDriverLatitude(String driverLatitude) {
        this.driverLatitude = driverLatitude;
    }

    public String getDriverLongitude() {
        return driverLongitude;
    }

    public void setDriverLongitude(String driverLongitude) {
        this.driverLongitude = driverLongitude;
    }

    public String getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(String completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public Integer getOrderDeliveryStatus() {
        return orderDeliveryStatus;
    }

    public void setOrderDeliveryStatus(Integer orderDeliveryStatus) {
        this.orderDeliveryStatus = orderDeliveryStatus;
    }

    public String getRestaurantDriverThumbs() {
        return restaurantDriverThumbs;
    }

    public void setRestaurantDriverThumbs(String restaurantDriverThumbs) {
        this.restaurantDriverThumbs = restaurantDriverThumbs;
    }

    public int getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(int isRequest) {
        this.isRequest = isRequest;
    }

    public String getOrderNotes() {
        return orderNotes;
    }

    public void setOrderNotes(String orderNotes) {
        this.orderNotes = orderNotes;
    }

    public String getAccessFee() {
        return accessFee;
    }

    public void setAccessFee(String accessFee) {
        this.accessFee = accessFee;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getRestaurantPenality() {
        return restaurantPenality;
    }

    public void setRestaurantPenality(String restaurantPenality) {
        this.restaurantPenality = restaurantPenality;
    }

    public String getAppliedPenality() {
        return appliedPenality;
    }

    public void setAppliedPenality(String appliedPenality) {
        this.appliedPenality = appliedPenality;
    }

    public String getDriverContactNumber() {
        return driverContactNumber;
    }

    public void setDriverContactNumber(String driverContactNumber) {
        this.driverContactNumber = driverContactNumber;
    }

    public String getDriverVehicleType() {
        return driverVehicleType;
    }

    public void setDriverVehicleType(String driverVehicleType) {
        this.driverVehicleType = driverVehicleType;
    }

    public String getDriverVehicleNumber() {
        return driverVehicleNumber;
    }

    public void setDriverVehicleNumber(String driverVehicleNumber) {
        this.driverVehicleNumber = driverVehicleNumber;
    }
}
