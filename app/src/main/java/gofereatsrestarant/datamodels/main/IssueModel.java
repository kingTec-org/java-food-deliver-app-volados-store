package gofereatsrestarant.datamodels.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 6/22/18.
 */

/*****************************************************************
 IssueModel
 ****************************************************************/

public class IssueModel {
    @SerializedName("id")
    @Expose
    private Integer issueId;

    @SerializedName("name")
    @Expose
    private String issueName;


    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
}
