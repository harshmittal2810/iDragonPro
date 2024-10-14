package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("user_id")
    @Expose
    private int iUserId;
    @SerializedName("movie_id")
    @Expose
    private int iVideoId;
    @SerializedName("comment")
    @Expose
    private String sReview;
    @SerializedName("iReplyTo")
    @Expose
    private int iReplyTo;
    @SerializedName("created_at")
    @Expose
    private String sCreatedTimeStamp;
    @SerializedName("name")
    @Expose
    private String sFirstName;
    @SerializedName("lastname")
    @Expose
    private String sLastName;

    public int getIUserId() {
        return iUserId;
    }

    public void setIUserId(int iUserId) {
        this.iUserId = iUserId;
    }

    public int getIVideoId() {
        return iVideoId;
    }

    public void setIVideoId(int iVideoId) {
        this.iVideoId = iVideoId;
    }

    public String getSReview() {
        return sReview;
    }

    public void setSReview(String sReview) {
        this.sReview = sReview;
    }

    public String getSCreatedTimeStamp() {
        return sCreatedTimeStamp;
    }

    public void setSCreatedTimeStamp(String sCreatedTimeStamp) {
        this.sCreatedTimeStamp = sCreatedTimeStamp;
    }

    public String getSFirstName() {
        return sFirstName;
    }

    public void setSFirstName(String sFirstName) {
        this.sFirstName = sFirstName;
    }

    public String getSLastName() {
        return sLastName;
    }

    public void setSLastName(String sLastName) {
        this.sLastName = sLastName;
    }

}
