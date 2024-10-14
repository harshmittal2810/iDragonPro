package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPaymentGatewayDetails {
    @SerializedName("id") @Expose private Integer id;
    @SerializedName("PgName") @Expose private String pgName;
    @SerializedName("isActive") @Expose private Integer isActive;
    @SerializedName("created_at") @Expose private String createdAt;
    @SerializedName("updated_at") @Expose private String updatedAt;
    @SerializedName("Api_Key") @Expose private String apiKey;
    @SerializedName("Api_Auth_Token") @Expose private String apiAuthToken;
    @SerializedName("Api_Secret_Key") @Expose private String apiSecretKey;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiSecretKey() {
        return apiSecretKey;
    }

    public void setApiSecretKey(String apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiAuthToken() {
        return apiAuthToken;
    }

    public void setApiAuthToken(String apiAuthToken) {
        this.apiAuthToken = apiAuthToken;
    }
}
