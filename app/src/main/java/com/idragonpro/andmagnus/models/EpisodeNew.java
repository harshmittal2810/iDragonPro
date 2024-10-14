package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EpisodeNew implements Serializable {
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("episodes_no")
    @Expose
    private String episodes_no;
    @SerializedName("episodes_id")
    @Expose
    private int episodes_id;
    @SerializedName("iVideoId")
    @Expose
    private int iVideoId;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getEpisodes_no() {
        return episodes_no;
    }

    public void setEpisodes_no(String episodes_no) {
        this.episodes_no = episodes_no;
    }

    public int getEpisodes_id() {
        return episodes_id;
    }

    public void setEpisodes_id(int episodes_id) {
        this.episodes_id = episodes_id;
    }

    public int getiVideoId() {
        return iVideoId;
    }

    public void setiVideoId(int iVideoId) {
        this.iVideoId = iVideoId;
    }
}
