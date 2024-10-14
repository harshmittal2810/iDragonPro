package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.models.AdsData;

public class AdsResponseModel {

    @SerializedName("adsDetails")
    @Expose
    private AdsData adsData = null;

    public AdsData getAdsData() {
        return adsData;
    }

    public void setAdsData(AdsData adsData) {
        this.adsData = adsData;
    }
}
