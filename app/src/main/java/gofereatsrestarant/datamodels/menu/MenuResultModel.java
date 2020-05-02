package gofereatsrestarant.datamodels.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import gofereatsrestarant.datamodels.main.MenuModel;


/*****************************************************************
 Menu Result Model
 ****************************************************************/

public class MenuResultModel {


    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("store_menu")
    @Expose
    private ArrayList<MenuModel> restaurantMenu = new ArrayList<>();

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<MenuModel> getRestaurantMenu() {
        return restaurantMenu;
    }

    public void setRestaurantMenu(ArrayList<MenuModel> restaurantMenu) {
        this.restaurantMenu = restaurantMenu;
    }
}
