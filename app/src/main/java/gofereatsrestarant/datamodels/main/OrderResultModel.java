package gofereatsrestarant.datamodels.main;
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
 Order Result Model
 ****************************************************************/
public class OrderResultModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("default_currency_symbol")
    @Expose
    private String defaultCurrencysymbol;
    @SerializedName("store_name")
    @Expose
    private String restaurantName;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("pending_orders")
    @Expose
    private ArrayList<OrderModel> pendingOrderDetails;
    @SerializedName("current_orders")
    @Expose
    private ArrayList<OrderModel> currentOrderDetails;
    @SerializedName("delivery_orders")
    @Expose
    private ArrayList<OrderModel> deliveryOrderDetails;
    @SerializedName("completed_orders")
    @Expose
    private ArrayList<OrderModel> completedOrderDetails;
    @SerializedName("schedule_order")
    @Expose
    private ArrayList<OrderModel> scheduledOrderDetails;

    public String getDefaultCurrencysymbol() {
        return defaultCurrencysymbol;
    }

    public void setDefaultCurrencysymbol(String defaultCurrencysymbol) {
        this.defaultCurrencysymbol = defaultCurrencysymbol;
    }

    @SerializedName("accepted_orders")
    @Expose
    private ArrayList<OrderModel> acceptedOrderDetails;

    @SerializedName("store_delivery")
    @Expose
    private ArrayList<IssueModel> driverIssueModels;

    @SerializedName("cancelled_orders")
    @Expose
    private ArrayList<OrderModel> cancelledOrderDetails;
    @SerializedName("order_history")
    @Expose
    private ArrayList<OrderModel> orderHistory;


    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<OrderModel> getPendingOrderDetails() {
        return pendingOrderDetails;
    }

    public void setPendingOrderDetails(ArrayList<OrderModel> pendingOrderDetails) {
        this.pendingOrderDetails = pendingOrderDetails;
    }

    public ArrayList<OrderModel> getCurrentOrderDetails() {
        return currentOrderDetails;
    }

    public void setCurrentOrderDetails(ArrayList<OrderModel> currentOrderDetails) {
        this.currentOrderDetails = currentOrderDetails;
    }

    public ArrayList<OrderModel> getDeliveryOrderDetails() {
        return deliveryOrderDetails;
    }

    public void setDeliveryOrderDetails(ArrayList<OrderModel> deliveryOrderDetails) {
        this.deliveryOrderDetails = deliveryOrderDetails;
    }

    public ArrayList<OrderModel> getCompletedOrderDetails() {
        return completedOrderDetails;
    }

    public void setCompletedOrderDetails(ArrayList<OrderModel> completedOrderDetails) {
        this.completedOrderDetails = completedOrderDetails;
    }

    public ArrayList<OrderModel> getScheduledOrderDetails() {
        return scheduledOrderDetails;
    }

    public void setScheduledOrderDetails(ArrayList<OrderModel> scheduledOrderDetails) {
        this.scheduledOrderDetails = scheduledOrderDetails;
    }

    public ArrayList<OrderModel> getAcceptedOrderDetails() {
        return acceptedOrderDetails;
    }

    public void setAcceptedOrderDetails(ArrayList<OrderModel> acceptedOrderDetails) {
        this.acceptedOrderDetails = acceptedOrderDetails;
    }


    public ArrayList<IssueModel> getDriverIssueModels() {
        return driverIssueModels;
    }

    public void setDriverIssueModels(ArrayList<IssueModel> driverIssueModels) {
        this.driverIssueModels = driverIssueModels;
    }

    public ArrayList<OrderModel> getCancelledOrderDetails() {
        return cancelledOrderDetails;
    }

    public void setCancelledOrderDetails(ArrayList<OrderModel> cancelledOrderDetails) {
        this.cancelledOrderDetails = cancelledOrderDetails;
    }

    public ArrayList<OrderModel> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(ArrayList<OrderModel> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
