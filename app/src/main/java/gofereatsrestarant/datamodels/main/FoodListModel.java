package gofereatsrestarant.datamodels.main;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.main
 * @category FoodListModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 Food List Model
 ****************************************************************/


public class FoodListModel {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private Integer menuId;
    @SerializedName("menu_category_id")
    @Expose
    private Integer menuCategoryId;
    @SerializedName("menu_category_name")
    @Expose
    private String menuCategoryName;
    @SerializedName("name")
    @Expose
    private String foodName;
    @SerializedName("price")
    @Expose
    private String foodPrice;
    @SerializedName("type")
    @Expose
    private Integer isVeg;
    @SerializedName("status")
    @Expose
    private Integer isAvailable;
    @SerializedName("menu_item_image")
    @Expose
    private String footImage;
    @SerializedName("description")
    @Expose
    private String foodDescription;
    @SerializedName("tax_percentage")
    @Expose
    private String taxPercentage;
    @SerializedName("is_visible")
    @Expose
    private int menuVisible;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(Integer menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public Integer getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Integer isVeg) {
        this.isVeg = isVeg;
    }

    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getFootImage() {
        return footImage;
    }

    public void setFootImage(String footImage) {
        this.footImage = footImage;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(String taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public int getMenuVisible() {
        return menuVisible;
    }

    public void setMenuVisible(int menuVisible) {
        this.menuVisible = menuVisible;
    }
}
