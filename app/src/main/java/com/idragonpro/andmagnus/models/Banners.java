package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.Movies;

import java.io.Serializable;

public class Banners implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iVideoId")
    @Expose
    private String iVideoId;
    @SerializedName(value = "bannerUrl", alternate = "iBannerUrl")
    @Expose
    private String bannerUrl;
    @SerializedName(value = "bannerOrder", alternate = "iBannerOrder")
    @Expose
    private String bannerOrder;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("videodetails")
    @Expose
    private Movies videodetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getiVideoId() {
        return iVideoId;
    }

    public void setiVideoId(String iVideoId) {
        this.iVideoId = iVideoId;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerOrder() {
        return bannerOrder;
    }

    public void setBannerOrder(String bannerOrder) {
        this.bannerOrder = bannerOrder;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Movies getVideodetails() {
        return videodetails;
    }

    public void setVideodetails(Movies videodetails) {
        this.videodetails = videodetails;
    }
}
