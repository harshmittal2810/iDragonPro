package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SplashResponseModel {

    @SerializedName("promoflash") @Expose private Promoflash promoflash;

    @SerializedName("promoflash_analytics_setting") @Expose
    private PromoflashAnalyticsSetting promoflashAnalyticsSetting;

    public Promoflash getPromoflash() {
        return promoflash;
    }

    public void setPromoflash(Promoflash promoflash) {
        this.promoflash = promoflash;
    }

    public PromoflashAnalyticsSetting getPromoflashAnalyticsSetting() {
        return promoflashAnalyticsSetting;
    }

    public void setPromoflashAnalyticsSetting(PromoflashAnalyticsSetting promoflashAnalyticsSetting) {
        this.promoflashAnalyticsSetting = promoflashAnalyticsSetting;
    }

    public class Promoflash {

        @SerializedName("FlashVideoURL") @Expose private String flashVideoURL;
        @SerializedName("FlashVideoURLPC") @Expose private String flashVideoURLPC;
        @SerializedName("FlashTime") @Expose private int flashTime;
        @SerializedName("PaymentAudioUrl") @Expose private String PaymentAudioUrl;
        @SerializedName("EnablePaymentFlashVideo") @Expose private String EnablePaymentFlashVideo;
        @SerializedName("IsShowGoogleAd") @Expose private String isShowGoogleAd;
        @SerializedName("AdCta") @Expose private String adCta;

        public String getFlashVideoURLPC() {
            return flashVideoURLPC;
        }

        public void setFlashVideoURLPC(String flashVideoURLPC) {
            this.flashVideoURLPC = flashVideoURLPC;
        }

        public String getFlashVideoURL() {
            return flashVideoURL;
        }

        public void setFlashVideoURL(String flashVideoURL) {
            this.flashVideoURL = flashVideoURL;
        }

        public int getFlashTime() {
            return flashTime;
        }

        public void setFlashTime(int flashTime) {
            this.flashTime = flashTime;
        }

        public String getPaymentAudioUrl() {
            return PaymentAudioUrl;
        }

        public void setPaymentAudioUrl(String paymentAudioUrl) {
            PaymentAudioUrl = paymentAudioUrl;
        }

        public String getEnablePaymentFlashVideo() {
            return EnablePaymentFlashVideo;
        }

        public void setEnablePaymentFlashVideo(String enablePaymentFlashVideo) {
            EnablePaymentFlashVideo = enablePaymentFlashVideo;
        }

        public String getIsShowGoogleAd() {
            return isShowGoogleAd;
        }

        public void setIsShowGoogleAd(String isShowGoogleAd) {
            this.isShowGoogleAd = isShowGoogleAd;
        }

        public String getAdCta() {
            return adCta;
        }

        public void setAdCta(String adCta) {
            this.adCta = adCta;
        }

        public String getSubscriptionAudioUrl() {
            return "https://videofiles100cdn.b-cdn.net/PARI/2023/Aug2023/MovieSubscription.wav";
        }

        public void setSubscriptionAudioUrl(String paymentAudioUrl) {
            //PaymentAudioUrl = paymentAudioUrl;
        }

        public String getEnableSubscritionFlashVideo() {
            return "0";
        }

        public void setEnableSubscritionFlashVideo(String enablePaymentFlashVideo) {
            // EnablePaymentFlashVideo = enablePaymentFlashVideo;
        }
    }

    public class PromoflashAnalyticsSetting {

        @SerializedName("id") @Expose private Integer id;
        @SerializedName("range_1_from") @Expose private Integer range1From;
        @SerializedName("range_1_to") @Expose private Integer range1To;
        @SerializedName("is_active_range_1") @Expose private String isActiveRange1;
        @SerializedName("range_2_from") @Expose private Integer range2From;
        @SerializedName("range_2_to") @Expose private Integer range2To;
        @SerializedName("is_active_range_2") @Expose private String isActiveRange2;
        @SerializedName("range_3_from") @Expose private Integer range3From;
        @SerializedName("range_3_to") @Expose private Integer range3To;
        @SerializedName("is_active_range_3") @Expose private String isActiveRange3;
        @SerializedName("updated_at") @Expose private String updatedAt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getRange1From() {
            return range1From;
        }

        public void setRange1From(Integer range1From) {
            this.range1From = range1From;
        }

        public Integer getRange1To() {
            return range1To;
        }

        public void setRange1To(Integer range1To) {
            this.range1To = range1To;
        }

        public String getIsActiveRange1() {
            return isActiveRange1;
        }

        public void setIsActiveRange1(String isActiveRange1) {
            this.isActiveRange1 = isActiveRange1;
        }

        public Integer getRange2From() {
            return range2From;
        }

        public void setRange2From(Integer range2From) {
            this.range2From = range2From;
        }

        public Integer getRange2To() {
            return range2To;
        }

        public void setRange2To(Integer range2To) {
            this.range2To = range2To;
        }

        public String getIsActiveRange2() {
            return isActiveRange2;
        }

        public void setIsActiveRange2(String isActiveRange2) {
            this.isActiveRange2 = isActiveRange2;
        }

        public Integer getRange3From() {
            return range3From;
        }

        public void setRange3From(Integer range3From) {
            this.range3From = range3From;
        }

        public Integer getRange3To() {
            return range3To;
        }

        public void setRange3To(Integer range3To) {
            this.range3To = range3To;
        }

        public String getIsActiveRange3() {
            return isActiveRange3;
        }

        public void setIsActiveRange3(String isActiveRange3) {
            this.isActiveRange3 = isActiveRange3;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
