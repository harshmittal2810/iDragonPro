package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.models.Banners;
import com.idragonpro.andmagnus.models.Sections;

import java.util.List;

public class HomeResponseModel {
    @SerializedName("categories")
    @Expose
    private List<Sections> homeContent;
    @SerializedName("banners")
    @Expose
    private List<Banners> banner;

    public List<Sections> getHomeContent() {
        return homeContent;
    }

    public void setHomeContent(List<Sections> homeContent) {
        this.homeContent = homeContent;
    }

    public List<Banners> getBanner() {
        return banner;
    }

    public void setBanner(List<Banners> banner) {
        this.banner = banner;
    }
}
