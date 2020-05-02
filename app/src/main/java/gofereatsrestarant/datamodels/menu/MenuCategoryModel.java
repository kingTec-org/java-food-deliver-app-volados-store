package gofereatsrestarant.datamodels.menu;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.menu
 * @category MenuCategoryModel
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
 Menu Category Model
 ****************************************************************/


public class MenuCategoryModel implements Serializable {


    @SerializedName("name")
    @Expose
    private String menuName;
    @SerializedName("id")
    @Expose
    private Integer menuId;
    @SerializedName("menu_item")
    @Expose
    private ArrayList<MenuItemModel> menuItem;


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public ArrayList<MenuItemModel> getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(ArrayList<MenuItemModel> menuItem) {
        this.menuItem = menuItem;
    }
}
