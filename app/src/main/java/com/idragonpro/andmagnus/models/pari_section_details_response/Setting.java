
package com.idragonpro.andmagnus.models.pari_section_details_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("IsButtonShow")
    @Expose
    private String isButtonShow;
    @SerializedName("iButtonImageUrl")
    @Expose
    private String iButtonImageUrl;
    @SerializedName("iAudioUrl")
    @Expose
    private String iAudioUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsButtonShow() {
        return isButtonShow;
    }

    public void setIsButtonShow(String isButtonShow) {
        this.isButtonShow = isButtonShow;
    }

    public String getiButtonImageUrl() {
        return iButtonImageUrl;
    }

    public void setiButtonImageUrl(String iButtonImageUrl) {
        this.iButtonImageUrl = iButtonImageUrl;
    }

    public String getiAudioUrl() {
        return iAudioUrl;
    }

    public void setiAudioUrl(String iAudioUrl) {
        this.iAudioUrl = iAudioUrl;
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

}
