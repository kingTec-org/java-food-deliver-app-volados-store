package gofereatsrestarant.configs;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage configs
 * @category SessionManager
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.content.SharedPreferences;

import javax.inject.Inject;

/*****************************************************************
 Session manager to set and get glopal values
 ***************************************************************/
public class SessionManager {
    public @Inject
    SharedPreferences sharedPreferences;

    public SessionManager() {
        AppController.getAppComponent().inject(this);
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString("token", token).apply();
    }

    public String getRestaurantName() {
        return sharedPreferences.getString("RestaurantName", "");
    }

    public void setRestaurantName(String RestaurantName) {
        sharedPreferences.edit().putString("RestaurantName", RestaurantName).apply();
    }

    public String getType() {
        return sharedPreferences.getString("type", "");
    }

    public void setType(String type) {
        sharedPreferences.edit().putString("type", type).apply();
    }

    public String getProfileImg() {
        return sharedPreferences.getString("imageUrl", "");
    }

    public void setProfileImg(String imageUrl) {
        sharedPreferences.edit().putString("imageUrl", imageUrl).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString("phoneNumber", "");
    }

    public void setPhoneNumber(String phoneNumber) {
        sharedPreferences.edit().putString("phoneNumber", phoneNumber).apply();
    }

    public boolean getIsSawLike() {
        return sharedPreferences.getBoolean("isSawLike", true);
    }

    public void setIsSawLike(Boolean isSaw) {
        sharedPreferences.edit().putBoolean("isSawLike", isSaw).apply();
    }

    public boolean getIsSawUnLike() {
        return sharedPreferences.getBoolean("isSawUnLike", true);
    }

    public void setIsSawUnLike(Boolean isSaw) {
        sharedPreferences.edit().putBoolean("isSawUnLike", isSaw).apply();
    }

    public boolean getIsSawSuperLike() {
        return sharedPreferences.getBoolean("isSawSuperLike", true);
    }

    public void setIsSawSuperLike(Boolean isSaw) {
        sharedPreferences.edit().putBoolean("isSawSuperLike", isSaw).apply();
    }

    public boolean getIsSwipeLike() {
        return sharedPreferences.getBoolean("isSwipeLike", true);
    }

    public void setIsSwipeLike(Boolean isSaw) {
        sharedPreferences.edit().putBoolean("isSwipeLike", isSaw).apply();
    }

    public boolean getIsSwipeUnLike() {
        return sharedPreferences.getBoolean("isSwipeUnLike", true);
    }

    public void setIsSwipeUnLike(Boolean isSwipe) {
        sharedPreferences.edit().putBoolean("isSwipeUnLike", isSwipe).apply();
    }

    public boolean getIsSwipeSuperLike() {
        return sharedPreferences.getBoolean("isSwipeSuperLike", true);
    }

    public void setIsSwipeSuperLike(Boolean isSwipe) {
        sharedPreferences.edit().putBoolean("isSwipeSuperLike", isSwipe).apply();
    }

    public String getCountryCode() {
        return sharedPreferences.getString("countryCode", "");
    }

    public void setCountryCode(String countryCode) {
        sharedPreferences.edit().putString("countryCode", countryCode).apply();
    }

    public String getCurrencyCode() {
        return sharedPreferences.getString("currencyCode", "");
    }

    public void setCurrencyCode(String currencyCode) {
        sharedPreferences.edit().putString("currencyCode", currencyCode).apply();
    }

    public String getCurrencySymbol() {
        return sharedPreferences.getString("currencysymbol", "");
    }

    public void setCurrencySymbol(String currencySymbol) {
        sharedPreferences.edit().putString("currencysymbol", currencySymbol).apply();
    }

    public int getDeviceWidth() {
        return sharedPreferences.getInt("deviceWidth", 0);
    }

    public void setDeviceWidth(int width) {
        sharedPreferences.edit().putInt("deviceWidth", width).apply();
    }

    public void clearToken() {
        sharedPreferences.edit().putString("token", "").apply();
    }

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
        setType("1");
    }

    public int getUserId() {
        return sharedPreferences.getInt("userId", 0);
    }

    public void setUserId(int userId) {
        sharedPreferences.edit().putInt("userId", userId).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString("userName", "");
    }

    public void setUserName(String userName) {
        sharedPreferences.edit().putString("userName", userName).apply();
    }

    public String getSwipeType() {
        return sharedPreferences.getString("swipeType", "");
    }

    public void setSwipeType(String swipeType) {
        sharedPreferences.edit().putString("swipeType", swipeType).apply();
    }

    public boolean getSettingUpdate() {
        return sharedPreferences.getBoolean("settingUpdate", false);
    }

    public void setSettingUpdate(boolean settingUpdate) {
        sharedPreferences.edit().putBoolean("settingUpdate", settingUpdate).apply();
    }

    public int getTouchX() {
        return sharedPreferences.getInt("touchX", 0);
    }

    public void setTouchX(int touchX) {
        sharedPreferences.edit().putInt("touchX", touchX).apply();
    }

    public int getTouchY() {
        return sharedPreferences.getInt("touchY", 0);
    }

    public void setTouchY(int touchY) {
        sharedPreferences.edit().putInt("touchY", touchY).apply();
    }

    public String getDeviceId() {
        return sharedPreferences.getString("deviceId", "");
    }

    public void setDeviceId(String deviceId) {
        sharedPreferences.edit().putString("deviceId", deviceId).apply();
    }

    public int getImageid() {
        return sharedPreferences.getInt("image_id", 0);
    }

    public void setImageid(int imageid) {
        sharedPreferences.edit().putInt("image_id", imageid).apply();
    }

    public Integer getUnMatchReasonId() {
        return sharedPreferences.getInt("unMatchReasonId", 0);
    }

    public void setUnMatchReasonId(Integer unMatchReasonId) {
        sharedPreferences.edit().putInt("unMatchReasonId", unMatchReasonId).apply();
    }

    public boolean getIsUnmatch() {
        return sharedPreferences.getBoolean("isUnmatch", false);
    }

    public void setIsUnmatch(Boolean isUnmatch) {
        sharedPreferences.edit().putBoolean("isUnmatch", isUnmatch).apply();
    }

    public String getPushNotification() {
        return sharedPreferences.getString("pushNotification", "");
    }

    public void setPushNotification(String pushNotification) {
        sharedPreferences.edit().putString("pushNotification", pushNotification).apply();
    }

    public boolean getIsOrder() {
        return sharedPreferences.getBoolean("isOrder", false);
    }

    public void setIsOrder(Boolean isOrder) {
        sharedPreferences.edit().putBoolean("isOrder", isOrder).apply();
    }


    public String getDriverUpdatedLat() {
        return sharedPreferences.getString("driverUpdatedLat", "");
    }

    public void setDriverUpdatedLat(String driverUpdatedLat) {
        sharedPreferences.edit().putString("driverUpdatedLat", driverUpdatedLat).apply();
    }

    public String getDriverUpdatedLng() {
        return sharedPreferences.getString("driverUpdatedLng", "");
    }

    public void setDriverUpdatedLng(String driverUpdatedLng) {
        sharedPreferences.edit().putString("driverUpdatedLng", driverUpdatedLng).apply();
    }

    public int getClickedMenu() {
        return sharedPreferences.getInt("clickedMenu", 0);
    }

    public void setClickedMenu(int clickedMenu) {
        sharedPreferences.edit().putInt("clickedMenu", clickedMenu).apply();
    }

    public String getLanguage() {
        return sharedPreferences.getString("language", "English");
    }

    public void setLanguage(String language) {
        sharedPreferences.edit().putString("language", language).apply();
    }

    public String getLanguageCode() {
        return sharedPreferences.getString("languagecode", "en");
    }

    public void setLanguageCode(String languagecode) {
        sharedPreferences.edit().putString("languagecode", languagecode).apply();
    }
}
