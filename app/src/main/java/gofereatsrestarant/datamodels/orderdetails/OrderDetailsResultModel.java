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

/*****************************************************************
 Order Details Result Model
 ****************************************************************/
public class OrderDetailsResultModel {

    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("order_details")
    @Expose
    private OrderDetailsAllModel orderDetailsAllModels;


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

    public OrderDetailsAllModel getOrderDetailsAllModels() {
        return orderDetailsAllModels;
    }

    public void setOrderDetailsAllModels(OrderDetailsAllModel orderDetailsAllModels) {
        this.orderDetailsAllModels = orderDetailsAllModels;
    }
}
