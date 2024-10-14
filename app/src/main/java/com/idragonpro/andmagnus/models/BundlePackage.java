package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.BundleMovies;

import java.io.Serializable;
import java.util.List;

public class BundlePackage implements Serializable {

    @SerializedName("bundle_packages_id")
    @Expose
    private long bundlePackagesId;
    @SerializedName("BundlePackage")
    @Expose
    private String bundlePackage;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("androidPrice")
    @Expose
    private String androidPrice;
    @SerializedName("sCode")
    @Expose
    private String sCode;
    @SerializedName("androidValidityInDays")
    @Expose
    private Integer androidValidityInDays;
    @SerializedName("video_list")
    @Expose
    private List<BundleMovies> videoList;

    public long getBundlePackagesId() {
        return bundlePackagesId;
    }

    public void setBundlePackagesId(long bundlePackagesId) {
        this.bundlePackagesId = bundlePackagesId;
    }

    public String getBundlePackage() {
        return bundlePackage;
    }

    public void setBundlePackage(String bundlePackage) {
        this.bundlePackage = bundlePackage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAndroidPrice() {
        return androidPrice;
    }

    public void setAndroidPrice(String androidPrice) {
        this.androidPrice = androidPrice;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public Integer getAndroidValidityInDays() {
        return androidValidityInDays;
    }

    public void setAndroidValidityInDays(Integer androidValidityInDays) {
        this.androidValidityInDays = androidValidityInDays;
    }

    public List<BundleMovies> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<BundleMovies> videoList) {
        this.videoList = videoList;
    }
}
