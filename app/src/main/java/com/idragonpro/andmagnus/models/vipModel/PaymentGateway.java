package com.idragonpro.andmagnus.models.vipModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentGateway {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("PgName")
    @Expose
    private String pgName;
    @SerializedName("Merchant_Id")
    @Expose
    private Object merchantId;
    @SerializedName("Client_Id")
    @Expose
    private Object clientId;
    @SerializedName("Client_Secret_Key")
    @Expose
    private Object clientSecretKey;
    @SerializedName("Api_Key")
    @Expose
    private Object apiKey;
    @SerializedName("Api_Auth_Token")
    @Expose
    private Object apiAuthToken;
    @SerializedName("Api_Secret_Key")
    @Expose
    private Object apiSecretKey;
    @SerializedName("Optional_1")
    @Expose
    private Object optional1;
    @SerializedName("Optional_2")
    @Expose
    private Object optional2;
    @SerializedName("Optional_3")
    @Expose
    private Object optional3;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public Object getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Object merchantId) {
        this.merchantId = merchantId;
    }

    public Object getClientId() {
        return clientId;
    }

    public void setClientId(Object clientId) {
        this.clientId = clientId;
    }

    public Object getClientSecretKey() {
        return clientSecretKey;
    }

    public void setClientSecretKey(Object clientSecretKey) {
        this.clientSecretKey = clientSecretKey;
    }

    public Object getApiKey() {
        return apiKey;
    }

    public void setApiKey(Object apiKey) {
        this.apiKey = apiKey;
    }

    public Object getApiAuthToken() {
        return apiAuthToken;
    }

    public void setApiAuthToken(Object apiAuthToken) {
        this.apiAuthToken = apiAuthToken;
    }

    public Object getApiSecretKey() {
        return apiSecretKey;
    }

    public void setApiSecretKey(Object apiSecretKey) {
        this.apiSecretKey = apiSecretKey;
    }

    public Object getOptional1() {
        return optional1;
    }

    public void setOptional1(Object optional1) {
        this.optional1 = optional1;
    }

    public Object getOptional2() {
        return optional2;
    }

    public void setOptional2(Object optional2) {
        this.optional2 = optional2;
    }

    public Object getOptional3() {
        return optional3;
    }

    public void setOptional3(Object optional3) {
        this.optional3 = optional3;
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
}
