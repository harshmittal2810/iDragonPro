package com.idragonpro.andmagnus.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.models.EpisodeNew;

import java.io.Serializable;
import java.util.List;

public class BundleMovies implements Serializable {
    @SerializedName(value = "id", alternate = "iVideoId")
    @Expose
    private String sVedioId;
    @SerializedName("VideoType")
    @Expose
    private String sType;
    @SerializedName("Season")
    @Expose
    private String sSeason;
    @SerializedName("Episode")
    @Expose
    private String sEpisode;
    @SerializedName("Name")
    @Expose
    private String sName;
    @SerializedName("Synopsis")
    @Expose
    private String sSummary;
    @SerializedName("Language")
    @Expose
    private String sLanguage;
    @SerializedName("sDirector")
    @Expose
    private String sDirector;
    @SerializedName("nBigBannerUrl")
    @Expose
    private String sBigBanner;
    @SerializedName("nSmallBannerUrl")
    @Expose
    private String sSmallBanner;
    @SerializedName("InfoBannerUrl")
    @Expose
    private String sInfoBanner;
    @SerializedName("TrailerUrl")
    @Expose
    private String sTrailer;
    @SerializedName("VideoUrl")
    @Expose
    private String VideoUrl;
    @SerializedName("Time")
    @Expose
    private String sTime;
    @SerializedName("Year")
    @Expose
    private String sYear;
    @SerializedName("IsFree")
    @Expose
    private String iSfree;
    @SerializedName("Category")
    @Expose
    private String language_category;
    @SerializedName("ComingSoon")
    @Expose
    private String sComingSoon;
    @SerializedName("daysdiff")
    @Expose
    private long daysdiff ;
    @SerializedName("timediff")
    @Expose
    private long timediff ;
    @SerializedName("episodes")
    @Expose
    private List<EpisodeNew> allepisodes;
    @SerializedName("subscriptions")
    @Expose
    private SubscriptionModel subscriptions;
    @SerializedName("packages")
    @Expose
    private List<PackageModel> packageModels = null;
    @SerializedName("movieprices")
    @Expose
    private SubscriptionModel movieprices;
    @SerializedName("sAllowedInPackage")
    @Expose
    private String sAllowedInPackage;
    @SerializedName("tagPosition")
    @Expose
    private String tagPosition;
    @SerializedName("tagUrl")
    @Expose
    private String tagUrl;
    @SerializedName("nSubscriptionBannerUrl")
    @Expose
    private String nSubscriptionBannerUrl;
    private boolean unmute;
    @SerializedName("payment_gateways")
    @Expose
    private List<PaymentGIdragon> payment_gateways = null;
    @SerializedName("ads_for_paid_movie")
    @Expose
    private String ads_for_paid_movie;
    @SerializedName("ads_paid_movie_count")
    @Expose
    private String ads_paid_movie_count;

    public String getsVedioId() {
        return sVedioId;
    }

    public void setsVedioId(String sVedioId) {
        this.sVedioId = sVedioId;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getsSeason() {
        return sSeason;
    }

    public void setsSeason(String sSeason) {
        this.sSeason = sSeason;
    }

    public String getsEpisode() {
        return sEpisode;
    }

    public void setsEpisode(String sEpisode) {
        this.sEpisode = sEpisode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsSummary() {
        return sSummary;
    }

    public void setsSummary(String sSummary) {
        this.sSummary = sSummary;
    }

    public String getsLanguage() {
        return sLanguage;
    }

    public void setsLanguage(String sLanguage) {
        this.sLanguage = sLanguage;
    }

    //By Bushra: to add coming soon feature
    public String getsComingSoon() {
        return sComingSoon;
    }

    public void setsComingSoon(String sComingSoon) {
        this.sComingSoon = sComingSoon;
    }

    public String getsDirector() {
        return sDirector;
    }

    public void setsDirector(String sDirector) {
        this.sDirector = sDirector;
    }

    public String getsBigBanner() {
        return sBigBanner;
    }

    public void setsBigBanner(String sBigBanner) {
        this.sBigBanner = sBigBanner;
    }

    public String getsSmallBanner() {
        return sSmallBanner;
    }

    public void setsSmallBanner(String sSmallBanner) {
        this.sSmallBanner = sSmallBanner;
    }

    public String getsInfoBanner() {
        return sInfoBanner;
    }

    public void setsInfoBanner(String sInfoBanner) {
        this.sInfoBanner = sInfoBanner;
    }

    public String getsTrailer() {
        return sTrailer;
    }

    public void setsTrailer(String sTrailer) {
        this.sTrailer = sTrailer;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsYear() {
        return sYear;
    }

    public void setsYear(String sYear) {
        this.sYear = sYear;
    }

    public String getiSfree() {
        return iSfree;
    }

    public void setiSfree(String iSfree) {
        this.iSfree = iSfree;
    }

    public String getLanguage_category() {
        return language_category;
    }

    public void setLanguage_category(String language_category) {
        this.language_category = language_category;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public List<EpisodeNew> getAllepisodes() {
        return allepisodes;
    }

    public void setAllepisodes(List<EpisodeNew> allepisodes) {
        this.allepisodes = allepisodes;
    }

    public SubscriptionModel getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(SubscriptionModel subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<PackageModel> getPackageModels() {
        return packageModels;
    }

    public void setPackageModels(List<PackageModel> packageModels) {
        this.packageModels = packageModels;
    }

    public long getDaysdiff() {
        return daysdiff;
    }

    public void setDaysdiff(long daysdiff) {
        this.daysdiff = daysdiff;
    }

    public SubscriptionModel getMovieprices() {
        return movieprices;
    }

    public void setMovieprices(SubscriptionModel movieprices) {
        this.movieprices = movieprices;
    }

    public String getsAllowedInPackage() {
        return sAllowedInPackage;
    }

    public void setsAllowedInPackage(String sAllowedInPackage) {
        this.sAllowedInPackage = sAllowedInPackage;
    }

    public long getTimediff() {
        return timediff;
    }

    public void setTimediff(long timediff) {
        this.timediff = timediff;
    }

    public String getTagPosition() {
        return tagPosition;
    }

    public void setTagPosition(String tagPosition) {
        this.tagPosition = tagPosition;
    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }

    public String getnSubscriptionBannerUrl() {
        return nSubscriptionBannerUrl;
    }

    public void setUnmute(boolean unmute) {
        this.unmute = unmute;
    }

    public boolean getUnmute() {
        return unmute;
    }

    public List<PaymentGIdragon> getPayment_gateways() {
        return payment_gateways;
    }

    public void setPayment_gateways(List<PaymentGIdragon> payment_gateways) {
        this.payment_gateways = payment_gateways;
    }

    public String getAds_for_paid_movie() {
        return ads_for_paid_movie;
    }

    public void setAds_for_paid_movie(String ads_for_paid_movie) {
        this.ads_for_paid_movie = ads_for_paid_movie;
    }

    public String getAds_paid_movie_count() {
        return ads_paid_movie_count;
    }

    public void setAds_paid_movie_count(String ads_paid_movie_count) {
        this.ads_paid_movie_count = ads_paid_movie_count;
    }
}
