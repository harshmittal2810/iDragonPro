
package com.idragonpro.andmagnus.models.pari_section_details_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Section implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("iTitle")
    @Expose
    private String iTitle;
    @SerializedName("iDescription")
    @Expose
    private String iDescription;
    @SerializedName("iVideoUrl")
    @Expose
    private String iVideoUrl;
    @SerializedName("IsActive")
    @Expose
    private String isActive;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("iImageUrl")
    @Expose
    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getiTitle() {
        return iTitle;
    }

    public void setiTitle(String iTitle) {
        this.iTitle = iTitle;
    }

    public String getiDescription() {
        return iDescription;
    }

    public void setiDescription(String iDescription) {
        this.iDescription = iDescription;
    }

    public String getiVideoUrl() {
        return iVideoUrl;
    }

    public void setiVideoUrl(String iVideoUrl) {
        this.iVideoUrl = iVideoUrl;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
