package com.idragonpro.andmagnus.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PackageModel implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("Package")
    @Expose
    private String _package;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("sCode")
    @Expose
    private String sCode;
    @SerializedName("validityInDays")
    @Expose
    private long validityInDays;
    @SerializedName("VideoType")
    @Expose
    private String videoType;
    @SerializedName("isPackage")
    @Expose
    private String isPackage;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("iPriceWithPackage")
    @Expose
    private String iPriceWithPackage;
    private String ogPrice;
    private List<BundleMovies> videoList;

    @SerializedName("IsShowWithBundle")
    @Expose
    private String IsShowWithBundle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSCode() {
        return sCode;
    }

    public void setSCode(String sCode) {
        this.sCode = sCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getValidityInDays() {
        return validityInDays;
    }

    public void setValidityInDays(long validityInDays) {
        this.validityInDays = validityInDays;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getiPriceWithPackage() {
        return iPriceWithPackage;
    }

    public void setiPriceWithPackage(String iPriceWithPackage) {
        this.iPriceWithPackage = iPriceWithPackage;
    }

    public void setOgPrice(String ogPrice) {
        this.ogPrice = ogPrice;
    }

    public String getOgPrice() {
        return ogPrice;
    }

    public void setVideoList(List<BundleMovies> videoList) {

        this.videoList = videoList;
    }

    public List<BundleMovies> getVideoList() {
        return videoList;
    }

    public String getIsShowWithBundle() {
        return IsShowWithBundle;
    }

    public void setIsShowWithBundle(String isShowWithBundle) {
        IsShowWithBundle = isShowWithBundle;
    }
}
