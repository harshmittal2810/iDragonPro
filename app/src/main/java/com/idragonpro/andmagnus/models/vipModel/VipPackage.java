package com.idragonpro.andmagnus.models.vipModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VipPackage implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Package")
    @Expose
    private String _package;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("IosPrice")
    @Expose
    private String iosPrice;
    @SerializedName("sCode")
    @Expose
    private String sCode;
    @SerializedName("validityInDays")
    @Expose
    private Integer validityInDays;
    @SerializedName("IosvalidityInDays")
    @Expose
    private Integer iosvalidityInDays;
    @SerializedName("VideoType")
    @Expose
    private String videoType;
    @SerializedName("isPackage")
    @Expose
    private String isPackage;
    @SerializedName("isMain")
    @Expose
    private Integer isMain;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("IsShowWithBundle")
    @Expose
    private String isShowWithBundle;
    @SerializedName("hindiPackage")
    @Expose
    private Object hindiPackage;
    @SerializedName("hindiPackageDescription")
    @Expose
    private Object hindiPackageDescription;
    @SerializedName("marathiPackage")
    @Expose
    private Object marathiPackage;
    @SerializedName("marathiPackageDescription")
    @Expose
    private Object marathiPackageDescription;
    @SerializedName("tamilPackage")
    @Expose
    private Object tamilPackage;
    @SerializedName("tamilPackageDescription")
    @Expose
    private Object tamilPackageDescription;
    @SerializedName("telguPackage")
    @Expose
    private Object telguPackage;
    @SerializedName("telguPackageDescription")
    @Expose
    private Object telguPackageDescription;
    @SerializedName("kannadaPackage")
    @Expose
    private Object kannadaPackage;
    @SerializedName("kannadaPackageDescription")
    @Expose
    private Object kannadaPackageDescription;
    @SerializedName("punjabiPackage")
    @Expose
    private Object punjabiPackage;
    @SerializedName("punjabiPackageDescription")
    @Expose
    private Object punjabiPackageDescription;
    @SerializedName("gujaratiPackage")
    @Expose
    private Object gujaratiPackage;
    @SerializedName("gujaratiPackageDescription")
    @Expose
    private Object gujaratiPackageDescription;
    @SerializedName("bengaliPackage")
    @Expose
    private Object bengaliPackage;
    @SerializedName("bengaliPackageDescription")
    @Expose
    private Object bengaliPackageDescription;
    @SerializedName("urduPackage")
    @Expose
    private Object urduPackage;
    @SerializedName("urduPackageDescription")
    @Expose
    private Object urduPackageDescription;
    @SerializedName("StatusId")
    @Expose
    private Object statusId;
    @SerializedName("StatusName")
    @Expose
    private Object statusName;
    @SerializedName("CreatedById")
    @Expose
    private Object createdById;
    @SerializedName("ModifiedById")
    @Expose
    private Object modifiedById;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIosPrice() {
        return iosPrice;
    }

    public void setIosPrice(String iosPrice) {
        this.iosPrice = iosPrice;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public Integer getValidityInDays() {
        return validityInDays;
    }

    public void setValidityInDays(Integer validityInDays) {
        this.validityInDays = validityInDays;
    }

    public Integer getIosvalidityInDays() {
        return iosvalidityInDays;
    }

    public void setIosvalidityInDays(Integer iosvalidityInDays) {
        this.iosvalidityInDays = iosvalidityInDays;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getIsPackage() {
        return isPackage;
    }

    public void setIsPackage(String isPackage) {
        this.isPackage = isPackage;
    }

    public Integer getIsMain() {
        return isMain;
    }

    public void setIsMain(Integer isMain) {
        this.isMain = isMain;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsShowWithBundle() {
        return isShowWithBundle;
    }

    public void setIsShowWithBundle(String isShowWithBundle) {
        this.isShowWithBundle = isShowWithBundle;
    }

    public Object getHindiPackage() {
        return hindiPackage;
    }

    public void setHindiPackage(Object hindiPackage) {
        this.hindiPackage = hindiPackage;
    }

    public Object getHindiPackageDescription() {
        return hindiPackageDescription;
    }

    public void setHindiPackageDescription(Object hindiPackageDescription) {
        this.hindiPackageDescription = hindiPackageDescription;
    }

    public Object getMarathiPackage() {
        return marathiPackage;
    }

    public void setMarathiPackage(Object marathiPackage) {
        this.marathiPackage = marathiPackage;
    }

    public Object getMarathiPackageDescription() {
        return marathiPackageDescription;
    }

    public void setMarathiPackageDescription(Object marathiPackageDescription) {
        this.marathiPackageDescription = marathiPackageDescription;
    }

    public Object getTamilPackage() {
        return tamilPackage;
    }

    public void setTamilPackage(Object tamilPackage) {
        this.tamilPackage = tamilPackage;
    }

    public Object getTamilPackageDescription() {
        return tamilPackageDescription;
    }

    public void setTamilPackageDescription(Object tamilPackageDescription) {
        this.tamilPackageDescription = tamilPackageDescription;
    }

    public Object getTelguPackage() {
        return telguPackage;
    }

    public void setTelguPackage(Object telguPackage) {
        this.telguPackage = telguPackage;
    }

    public Object getTelguPackageDescription() {
        return telguPackageDescription;
    }

    public void setTelguPackageDescription(Object telguPackageDescription) {
        this.telguPackageDescription = telguPackageDescription;
    }

    public Object getKannadaPackage() {
        return kannadaPackage;
    }

    public void setKannadaPackage(Object kannadaPackage) {
        this.kannadaPackage = kannadaPackage;
    }

    public Object getKannadaPackageDescription() {
        return kannadaPackageDescription;
    }

    public void setKannadaPackageDescription(Object kannadaPackageDescription) {
        this.kannadaPackageDescription = kannadaPackageDescription;
    }

    public Object getPunjabiPackage() {
        return punjabiPackage;
    }

    public void setPunjabiPackage(Object punjabiPackage) {
        this.punjabiPackage = punjabiPackage;
    }

    public Object getPunjabiPackageDescription() {
        return punjabiPackageDescription;
    }

    public void setPunjabiPackageDescription(Object punjabiPackageDescription) {
        this.punjabiPackageDescription = punjabiPackageDescription;
    }

    public Object getGujaratiPackage() {
        return gujaratiPackage;
    }

    public void setGujaratiPackage(Object gujaratiPackage) {
        this.gujaratiPackage = gujaratiPackage;
    }

    public Object getGujaratiPackageDescription() {
        return gujaratiPackageDescription;
    }

    public void setGujaratiPackageDescription(Object gujaratiPackageDescription) {
        this.gujaratiPackageDescription = gujaratiPackageDescription;
    }

    public Object getBengaliPackage() {
        return bengaliPackage;
    }

    public void setBengaliPackage(Object bengaliPackage) {
        this.bengaliPackage = bengaliPackage;
    }

    public Object getBengaliPackageDescription() {
        return bengaliPackageDescription;
    }

    public void setBengaliPackageDescription(Object bengaliPackageDescription) {
        this.bengaliPackageDescription = bengaliPackageDescription;
    }

    public Object getUrduPackage() {
        return urduPackage;
    }

    public void setUrduPackage(Object urduPackage) {
        this.urduPackage = urduPackage;
    }

    public Object getUrduPackageDescription() {
        return urduPackageDescription;
    }

    public void setUrduPackageDescription(Object urduPackageDescription) {
        this.urduPackageDescription = urduPackageDescription;
    }

    public Object getStatusId() {
        return statusId;
    }

    public void setStatusId(Object statusId) {
        this.statusId = statusId;
    }

    public Object getStatusName() {
        return statusName;
    }

    public void setStatusName(Object statusName) {
        this.statusName = statusName;
    }

    public Object getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Object createdById) {
        this.createdById = createdById;
    }

    public Object getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Object modifiedById) {
        this.modifiedById = modifiedById;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }
}
