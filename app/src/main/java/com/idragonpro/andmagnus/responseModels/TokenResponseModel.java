package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponseModel {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("token")
    @Expose
    private String token;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
