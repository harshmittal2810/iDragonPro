package com.idragonpro.andmagnus.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionModel implements Serializable {

    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("iUserId")
    @Expose
    private String iUserId;
    @SerializedName(value = "sAmount", alternate = "iPrice")
    @Expose
    private String sAmount;
    @SerializedName("iPriceWithPackage")
    @Expose
    private String iPriceWithPackage;
    @SerializedName("sType")
    @Expose
    private String sType;
    @SerializedName("iVideoId")
    @Expose
    private String iVideoId;
    @SerializedName("sTransId")
    @Expose
    private String sTransId;
    @SerializedName("sRazorPayId")
    @Expose
    private String sRazorPayId;
    @SerializedName("sStatus")
    @Expose
    private String sStatus;
    @SerializedName("videodetails")
    @Expose
    private Movies videodetails;
    @SerializedName("daysdiff")
    @Expose
    private long daysdiff;
    @SerializedName("timediff")
    @Expose
    private long timediff;
    @SerializedName("Package")
    @Expose
    private String Package;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIUserId() {
        return iUserId;
    }

    public void setIUserId(String iUserId) {
        this.iUserId = iUserId;
    }

    public String getSAmount() {
        return sAmount;
    }

    public void setSAmount(String sAmount) {
        this.sAmount = sAmount;
    }

    public String getSType() {
        return sType;
    }

    public void setSType(String sType) {
        this.sType = sType;
    }

    public String getIVideoId() {
        return iVideoId;
    }

    public void setIVideoId(String iVideoId) {
        this.iVideoId = iVideoId;
    }

    public String getSTransId() {
        return sTransId;
    }

    public void setSTransId(String sTransId) {
        this.sTransId = sTransId;
    }

    public String getSRazorPayId() {
        return sRazorPayId;
    }

    public void setSRazorPayId(String sRazorPayId) {
        this.sRazorPayId = sRazorPayId;
    }

    public String getSStatus() {
        return sStatus;
    }

    public void setSStatus(String sStatus) {
        this.sStatus = sStatus;
    }

    public Movies getVideodetails() {
        return videodetails;
    }

    public void setVideodetails(Movies videodetails) {
        this.videodetails = videodetails;
    }

    public long getDaysdiff() {
        return daysdiff;
    }

    public void setDaysdiff(long daysdiff) {
        this.daysdiff = daysdiff;
    }

    public String getiPriceWithPackage() {
        return iPriceWithPackage;
    }

    public void setiPriceWithPackage(String iPriceWithPackage) {
        this.iPriceWithPackage = iPriceWithPackage;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public long getTimediff() {
        return timediff;
    }

    public void setTimediff(long timediff) {
        this.timediff = timediff;
    }
}
