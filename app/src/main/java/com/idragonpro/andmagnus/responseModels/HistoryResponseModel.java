package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.Movies;

import java.util.List;

public class HistoryResponseModel {
    @SerializedName("watchlist")
    @Expose
    private List<Movies> video_data;

    public List<Movies> getVideo_data() {
        return video_data;
    }

    public void setVideo_data(List<Movies> video_data) {
        this.video_data = video_data;
    }
}
