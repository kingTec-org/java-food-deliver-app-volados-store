package gofereatsrestarant.datamodels.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.main
 * @category MenuModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import gofereatsrestarant.datamodels.menu.MenuCategoryModel;

/*****************************************************************
 MenuModel
 ****************************************************************/
public class MenuModel implements Serializable {


    @SerializedName("menu_category")
    @Expose
    private ArrayList<MenuCategoryModel> menuCategory = new ArrayList<>();
    @SerializedName("name")
    @Expose
    private String menuName;
    @SerializedName("id")
    @Expose
    private int menuId;
    @SerializedName("price")
    @Expose
    private String menuPrice;
    /*@SerializedName("image")
    @Expose*/
    private String menuImage;
    @SerializedName("is_visible")
    @Expose
    private String menuVisible;
    @SerializedName("menu_item")
    @Expose
    private String menuItem;
    @SerializedName("image")
    @Expose
    private String menuImageList;

    public ArrayList<MenuCategoryModel> getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(ArrayList<MenuCategoryModel> menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(String menuVisible) {
        this.menuVisible = menuVisible;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public String getMenuImageList() {
        return menuImageList;
    }

    public void setMenuImageList(String menuImageList) {
        this.menuImageList = menuImageList;
    }
}
