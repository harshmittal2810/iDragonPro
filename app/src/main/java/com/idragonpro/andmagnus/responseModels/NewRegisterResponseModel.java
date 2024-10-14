package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewRegisterResponseModel {

    @SerializedName("user")
    @Expose
    private RegisterResponseModel registerResponseModel;

    public RegisterResponseModel getRegisterResponseModel() {
        return registerResponseModel;
    }

    public void setRegisterResponseModel(RegisterResponseModel registerResponseModel) {
        this.registerResponseModel = registerResponseModel;
    }
}
