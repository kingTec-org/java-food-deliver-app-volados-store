package gofereatsrestarant.datamodels.orderdetails;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.main
 * @category OrderDetailsModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 Order Details Model
 ****************************************************************/
public class OrderDetailsModel {

    @SerializedName("name")
    @Expose
    private String orderItem;
    @SerializedName("price")
    @Expose
    private String orderItemPrice;
    @SerializedName("quantity")
    @Expose
    private Integer orderQuantity;
    @SerializedName("addons")
    @Expose
    private String addOns;
    @SerializedName("notes")
    @Expose
    private String userNeed;


    public String getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }

    public String getOrderItemPrice() {
        return orderItemPrice;
    }

    public void setOrderItemPrice(String orderItemPrice) {
        this.orderItemPrice = orderItemPrice;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderId) {
        this.orderQuantity = orderQuantity;
    }

    public String getAddOns() {
        return addOns;
    }

    public void setAddOns(String addOns) {
        this.addOns = addOns;
    }

    public String getUserNeed() {
        return userNeed;
    }

    public void setUserNeed(String userNeed) {
        this.userNeed = userNeed;
    }

}
