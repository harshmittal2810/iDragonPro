package com.idragonpro.andmagnus.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentGIdragon implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("PgName")
    @Expose
    private String pgName;
    @SerializedName("isActive")
    @Expose
    private String isActive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgName) {
        this.pgName = pgName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
