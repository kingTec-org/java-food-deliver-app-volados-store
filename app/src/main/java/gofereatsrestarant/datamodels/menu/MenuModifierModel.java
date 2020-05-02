package gofereatsrestarant.datamodels.menu;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.menu
 * @category MenuModifierModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/*****************************************************************
 Menu Modifier Model
 ****************************************************************/


public class MenuModifierModel implements Serializable {


    @SerializedName("menutitle")
    @Expose
    private String menutitle;
    @SerializedName("name")
    @Expose
    private String menuName;
    @SerializedName("id")
    @Expose
    private Integer menuId;
    @SerializedName("price")
    @Expose
    private String menuPrice;
    @SerializedName("is_visible")
    @Expose
    private int menuVisible;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public int getMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(int menuVisible) {
        this.menuVisible = menuVisible;
    }

    public String getMenutitle() {
        return menutitle;
    }

    public void setMenutitle(String menutitle) {
        this.menutitle = menutitle;
    }
}
