package gofereatsrestarant.datamodels.menu;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage datamodels.menu
 * @category MenuModifier
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
 Menu Modifier Model
 ****************************************************************/

public class MenuModifier implements Serializable {

    @SerializedName("name")
    @Expose
    private String menuName;
    @SerializedName("id")
    @Expose
    private Integer menuId;
    @SerializedName("modifier_item")
    @Expose
    private ArrayList<MenuModifierModel> modifierItem = new ArrayList<>();

    public ArrayList<MenuModifierModel> getModifierItem() {
        return modifierItem;
    }

    public void setModifierItem(ArrayList<MenuModifierModel> modifierItem) {
        this.modifierItem = modifierItem;
    }

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
}
