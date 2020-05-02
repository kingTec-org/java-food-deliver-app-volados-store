package gofereatsrestarant.datamodels.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 7/25/18.
 */

public class MenuImageList implements Serializable {
    @SerializedName("small")
    @Expose
    private String smallImage;

    @SerializedName("medium")
    @Expose
    private String mediumImage;

    @SerializedName("original")
    @Expose
    private String original;

    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
    }

    public String getMediumImage() {
        return mediumImage;
    }

    public void setMediumImage(String mediumImage) {
        this.mediumImage = mediumImage;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }
}
