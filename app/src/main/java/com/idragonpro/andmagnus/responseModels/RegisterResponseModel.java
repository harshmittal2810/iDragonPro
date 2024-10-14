package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponseModel {

    @SerializedName("id")
    @Expose
    private String userId;
    @SerializedName("name")
    @Expose
    private String sFirstName;
    @SerializedName("lastname")
    @Expose
    private String sLastName;
    @SerializedName("email")
    @Expose
    private String sEmail;
    @SerializedName("sPhone")
    @Expose
    private String sPhone;
    @SerializedName("isOldUser")
    @Expose
    private String isOldUser;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSFirstName() {
        return sFirstName;
    }

    public void setSFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getSLastName() {
        return sLastName;
    }

    public void setSLastName(String sLastName) {
        this.sLastName = sLastName;
    }

    public String getSEmail() {
        return sEmail;
    }

    public void setSEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getSPhone() {
        return sPhone;
    }

    public void setSPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getIsOldUser() {
        return isOldUser;
    }

    public void setIsOldUser(String isOldUser) {
        this.isOldUser = isOldUser;
    }

}
