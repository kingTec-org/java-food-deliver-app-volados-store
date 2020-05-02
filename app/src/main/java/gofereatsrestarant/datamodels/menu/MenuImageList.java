package gofereatsrestarant.datamodels.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 7/23/18.
 */

public class MenuImageList implements Serializable {

    @SerializedName("small")
    @Expose
    private String smallImage;

    @SerializedName("medium")
    @Expose
    private String largeImage;

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }
}
