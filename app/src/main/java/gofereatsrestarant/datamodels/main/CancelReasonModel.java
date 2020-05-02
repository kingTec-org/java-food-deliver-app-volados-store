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
 cancel reason model
 ****************************************************************/
public class CancelReasonModel {

    @SerializedName("id")
    @Expose
    private Integer cancelId;
    @SerializedName("reason")
    @Expose
    private String reason;

    public Integer getCancelId() {
        return cancelId;
    }

    public void setCancelId(Integer cancelId) {
        this.cancelId = cancelId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
