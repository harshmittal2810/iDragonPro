package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Content implements Serializable {

    @SerializedName("iVideoId")
    @Expose
    private Integer iVideoId;
    @SerializedName("sType")
    @Expose
    private String sType;
    @SerializedName("sSeason")
    @Expose
    private String sSeason;
    @SerializedName("sEpisode")
    @Expose
    private String sEpisode;
    @SerializedName("sName")
    @Expose
    private String sName;
    @SerializedName("sSummary")
    @Expose
    private String sSummary;
    @SerializedName("sLanguage")
    @Expose
    private String sLanguage;
    @SerializedName("sGenre")
    @Expose
    private String sGenre;
    @SerializedName("sRegion")
    @Expose
    private String sRegion;
    @SerializedName("sTags")
    @Expose
    private String sTags;
    @SerializedName("sCast")
    @Expose
    private String sCast;
    @SerializedName("sDirector")
    @Expose
    private String sDirector;
    @SerializedName("sBigBanner")
    @Expose
    private String sBigBanner;
    @SerializedName("sSmallBanner")
    @Expose
    private String sSmallBanner;
    @SerializedName("sInfoBanner")
    @Expose
    private String sInfoBanner;
    @SerializedName("sTrailer")
    @Expose
    private String sTrailer;
    @SerializedName("sVideo")
   // @Expose
    private String sVideo;
    @SerializedName("sTime")
    @Expose
    private String sTime;
    @SerializedName("sYear")
    @Expose
    private String sYear;
    @SerializedName("s1080JobId")
 //   @Expose
    private String s1080JobId;
    @SerializedName("s1080VideoUrl")
    @Expose
    private String s1080VideoUrl;
    @SerializedName("s720JobId")
//    @Expose
    private String s720JobId;
    @SerializedName("s720VideoUrl")
    @Expose
    private String s720VideoUrl;
    @SerializedName("s480JobId")
   // @Expose
    private String s480JobId;
    @SerializedName("s480VideoUrl")
    @Expose
    private String s480VideoUrl;
    @SerializedName("i1080Completed")
    @Expose
    private Integer i1080Completed;
    @SerializedName("i720Completed")
    @Expose
    private Integer i720Completed;
    @SerializedName("i480Completed")
    @Expose
    private Integer i480Completed;
    @SerializedName("sCreatedTimestamp")
    @Expose
    private String sCreatedTimestamp;
    @SerializedName("sVideoJobId")
    @Expose
    private String sVideoJobId;
    @SerializedName("sShowOnHome")
    @Expose
    private String sShowOnHome;
    @SerializedName("sShowOnMovies")
    @Expose
    private String sShowOnMovies;
    @SerializedName("sShowOnSeries")
    @Expose
    private String sShowOnSeries;
    @SerializedName("sShowOnKids")
    @Expose
    private String sShowOnKids;
    @SerializedName("s1080TJobId")
    @Expose
    private String s1080TJobId;
    @SerializedName("s1080TrailorUrl")
    @Expose
    private String s1080TrailorUrl;
    @SerializedName("s720TJobId")
    @Expose
    private String s720TJobId;
    @SerializedName("s720TrailorUrl")
    @Expose
    private String s720TrailorUrl;
    @SerializedName("s480TJobId")
    @Expose
    private String s480TJobId;
    @SerializedName("s480TrailorUrl")
    @Expose
    private String s480TrailorUrl;
    @SerializedName("i1080TCompleted")
    @Expose
    private Integer i1080TCompleted;
    @SerializedName("i720TCompleted")
    @Expose
    private Integer i720TCompleted;
    @SerializedName("i480TCompleted")
    @Expose
    private Integer i480TCompleted;
    @SerializedName("sTrailorJobId")
    @Expose
    private String sTrailorJobId;
    @SerializedName("sComingSoon")
    @Expose
    private String sComingSoon;
    @SerializedName("iOrder")
    @Expose
    private Object iOrder;
    @SerializedName("remtime")
    @Expose
    private Integer remtime;

    public Integer getIVideoId() {
        return iVideoId;
    }

    public void setIVideoId(Integer iVideoId) {
        this.iVideoId = iVideoId;
    }

    public String getSType() {
        return sType;
    }

    public void setSType(String sType) {
        this.sType = sType;
    }

    public String getSSeason() {
        return sSeason;
    }

    public void setSSeason(String sSeason) {
        this.sSeason = sSeason;
    }

    public String getSEpisode() {
        return sEpisode;
    }

    public void setSEpisode(String sEpisode) {
        this.sEpisode = sEpisode;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getSSummary() {
        return sSummary;
    }

    public void setSSummary(String sSummary) {
        this.sSummary = sSummary;
    }

    public String getSLanguage() {
        return sLanguage;
    }

    public void setSLanguage(String sLanguage) {
        this.sLanguage = sLanguage;
    }

    public String getSGenre() {
        return sGenre;
    }

    public void setSGenre(String sGenre) {
        this.sGenre = sGenre;
    }

    public String getSRegion() {
        return sRegion;
    }

    public void setSRegion(String sRegion) {
        this.sRegion = sRegion;
    }

    public String getSTags() {
        return sTags;
    }

    public void setSTags(String sTags) {
        this.sTags = sTags;
    }

    public String getSCast() {
        return sCast;
    }

    public void setSCast(String sCast) {
        this.sCast = sCast;
    }

    public String getSDirector() {
        return sDirector;
    }

    public void setSDirector(String sDirector) {
        this.sDirector = sDirector;
    }

    public String getSBigBanner() {
        return sBigBanner;
    }

    public void setSBigBanner(String sBigBanner) {
        this.sBigBanner = sBigBanner;
    }

    public String getSSmallBanner() {
        return sSmallBanner;
    }

    public void setSSmallBanner(String sSmallBanner) {
        this.sSmallBanner = sSmallBanner;
    }

    public String getSInfoBanner() {
        return sInfoBanner;
    }

    public void setSInfoBanner(String sInfoBanner) {
        this.sInfoBanner = sInfoBanner;
    }

    public String getSTrailer() {
        return sTrailer;
    }

    public void setSTrailer(String sTrailer) {
        this.sTrailer = sTrailer;
    }

    public String getSVideo() {
        return sVideo;
    }

    public void setSVideo(String sVideo) {
        this.sVideo = sVideo;
    }

    public String getSTime() {
        return sTime;
    }

    public void setSTime(String sTime) {
        this.sTime = sTime;
    }

    public String getSYear() {
        return sYear;
    }

    public void setSYear(String sYear) {
        this.sYear = sYear;
    }

    public String getS1080JobId() {
        return s1080JobId;
    }

    public void setS1080JobId(String s1080JobId) {
        this.s1080JobId = s1080JobId;
    }

    public String getS1080VideoUrl() {
        return s1080VideoUrl;
    }

    public void setS1080VideoUrl(String s1080VideoUrl) {
        this.s1080VideoUrl = s1080VideoUrl;
    }

    public String getS720JobId() {
        return s720JobId;
    }

    public void setS720JobId(String s720JobId) {
        this.s720JobId = s720JobId;
    }

    public String getS720VideoUrl() {
        return s720VideoUrl;
    }

    public void setS720VideoUrl(String s720VideoUrl) {
        this.s720VideoUrl = s720VideoUrl;
    }

    public String getS480JobId() {
        return s480JobId;
    }

    public void setS480JobId(String s480JobId) {
        this.s480JobId = s480JobId;
    }

    public String getS480VideoUrl() {
        return s480VideoUrl;
    }

    public void setS480VideoUrl(String s480VideoUrl) {
        this.s480VideoUrl = s480VideoUrl;
    }

    public Integer getI1080Completed() {
        return i1080Completed;
    }

    public void setI1080Completed(Integer i1080Completed) {
        this.i1080Completed = i1080Completed;
    }

    public Integer getI720Completed() {
        return i720Completed;
    }

    public void setI720Completed(Integer i720Completed) {
        this.i720Completed = i720Completed;
    }

    public Integer getI480Completed() {
        return i480Completed;
    }

    public void setI480Completed(Integer i480Completed) {
        this.i480Completed = i480Completed;
    }

    public String getSCreatedTimestamp() {
        return sCreatedTimestamp;
    }

    public void setSCreatedTimestamp(String sCreatedTimestamp) {
        this.sCreatedTimestamp = sCreatedTimestamp;
    }

    public String getSVideoJobId() {
        return sVideoJobId;
    }

    public void setSVideoJobId(String sVideoJobId) {
        this.sVideoJobId = sVideoJobId;
    }

    public String getSShowOnHome() {
        return sShowOnHome;
    }

    public void setSShowOnHome(String sShowOnHome) {
        this.sShowOnHome = sShowOnHome;
    }

    public String getSShowOnMovies() {
        return sShowOnMovies;
    }

    public void setSShowOnMovies(String sShowOnMovies) {
        this.sShowOnMovies = sShowOnMovies;
    }

    public String getSShowOnSeries() {
        return sShowOnSeries;
    }

    public void setSShowOnSeries(String sShowOnSeries) {
        this.sShowOnSeries = sShowOnSeries;
    }

    public String getSShowOnKids() {
        return sShowOnKids;
    }

    public void setSShowOnKids(String sShowOnKids) {
        this.sShowOnKids = sShowOnKids;
    }

    public String getS1080TJobId() {
        return s1080TJobId;
    }

    public void setS1080TJobId(String s1080TJobId) {
        this.s1080TJobId = s1080TJobId;
    }

    public String getS1080TrailorUrl() {
        return s1080TrailorUrl;
    }

    public void setS1080TrailorUrl(String s1080TrailorUrl) {
        this.s1080TrailorUrl = s1080TrailorUrl;
    }

    public String getS720TJobId() {
        return s720TJobId;
    }

    public void setS720TJobId(String s720TJobId) {
        this.s720TJobId = s720TJobId;
    }

    public String getS720TrailorUrl() {
        return s720TrailorUrl;
    }

    public void setS720TrailorUrl(String s720TrailorUrl) {
        this.s720TrailorUrl = s720TrailorUrl;
    }

    public String getS480TJobId() {
        return s480TJobId;
    }

    public void setS480TJobId(String s480TJobId) {
        this.s480TJobId = s480TJobId;
    }

    public String getS480TrailorUrl() {
        return s480TrailorUrl;
    }

    public void setS480TrailorUrl(String s480TrailorUrl) {
        this.s480TrailorUrl = s480TrailorUrl;
    }

    public Integer getI1080TCompleted() {
        return i1080TCompleted;
    }

    public void setI1080TCompleted(Integer i1080TCompleted) {
        this.i1080TCompleted = i1080TCompleted;
    }

    public Integer getI720TCompleted() {
        return i720TCompleted;
    }

    public void setI720TCompleted(Integer i720TCompleted) {
        this.i720TCompleted = i720TCompleted;
    }

    public Integer getI480TCompleted() {
        return i480TCompleted;
    }

    public void setI480TCompleted(Integer i480TCompleted) {
        this.i480TCompleted = i480TCompleted;
    }

    public String getSTrailorJobId() {
        return sTrailorJobId;
    }

    public void setSTrailorJobId(String sTrailorJobId) {
        this.sTrailorJobId = sTrailorJobId;
    }

    public String getSComingSoon() {
        return sComingSoon;
    }

    public void setSComingSoon(String sComingSoon) {
        this.sComingSoon = sComingSoon;
    }

    public Object getIOrder() {
        return iOrder;
    }

    public void setIOrder(Object iOrder) {
        this.iOrder = iOrder;
    }

    public Integer getRemtime() {
        return remtime;
    }

    public void setRemtime(Integer remtime) {
        this.remtime = remtime;
    }

}
