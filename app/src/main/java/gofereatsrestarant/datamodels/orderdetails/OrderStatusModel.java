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
 Order Status and Time Model
 ****************************************************************/
public class OrderStatusModel {

    @SerializedName("accepted")
    @Expose
    private String accepted;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("completed")
    @Expose
    private String completed;

    public String getAccepted() {
        return accepted;
    }

    public void setAccepted(String accepted) {
        this.accepted = accepted;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }
}
