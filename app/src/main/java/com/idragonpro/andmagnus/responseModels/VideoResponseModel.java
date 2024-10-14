package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.Movies;

public class VideoResponseModel {

    @SerializedName("video")
    @Expose
    private Movies video;

    public Movies getVideo() {
        return video;
    }

    public void setVideo(Movies video) {
        this.video = video;
    }
}
