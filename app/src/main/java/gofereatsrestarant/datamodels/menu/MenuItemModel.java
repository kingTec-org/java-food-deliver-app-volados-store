package gofereatsrestarant.datamodels.menu;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.menu
 * @category MenuItemModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import gofereatsrestarant.datamodels.main.MenuImageList;


public class MenuItemModel implements Serializable {

    @SerializedName("name")
    @Expose
    private String menuName;
    @SerializedName("id")
    @Expose
    private Integer menuId;
    @SerializedName("price")
    @Expose
    private String menuPrice;
    @SerializedName("image")
    @Expose
    private MenuImageList menuImage;
    @SerializedName("is_visible")
    @Expose
    private int menuVisible;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("modifier")
    @Expose
    private ArrayList<MenuModifier> modifier = new ArrayList<>();


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

    public MenuImageList getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(MenuImageList menuImage) {
        this.menuImage = menuImage;
    }

    public int getMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(int menuVisible) {
        this.menuVisible = menuVisible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArrayList<MenuModifier> getModifier() {
        return modifier;
    }

    public void setModifier(ArrayList<MenuModifier> modifier) {
        this.modifier = modifier;
    }
}
