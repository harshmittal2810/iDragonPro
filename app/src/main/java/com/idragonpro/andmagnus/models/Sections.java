package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sections {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iCategoryId")
    @Expose
    private String iCategoryId;
    @SerializedName("iCategoryOrder")
    @Expose
    private String iCategoryOrder;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("iType")
    @Expose
    private String iType;
    @SerializedName("banners")
    @Expose
    private List<Banners> banners;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getiCategoryId() {
        return iCategoryId;
    }

    public void setiCategoryId(String iCategoryId) {
        this.iCategoryId = iCategoryId;
    }

    public String getiCategoryOrder() {
        return iCategoryOrder;
    }

    public void setiCategoryOrder(String iCategoryOrder) {
        this.iCategoryOrder = iCategoryOrder;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getiType() {
        return iType;
    }

    public void setiType(String iType) {
        this.iType = iType;
    }

    public List<Banners> getBanners() {
        return banners;
    }

    public void setBanners(List<Banners> banners) {
        this.banners = banners;
    }
}
