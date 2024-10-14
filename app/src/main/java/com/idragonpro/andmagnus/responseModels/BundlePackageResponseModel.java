package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.models.BundlePackage;

import java.io.Serializable;
import java.util.List;

public class BundlePackageResponseModel implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("bundle_package_list")
    @Expose
    private List<BundlePackage> bundlePackageList;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BundlePackage> getBundlePackageList() {
        return bundlePackageList;
    }

    public void setBundlePackageList(List<BundlePackage> bundlePackageList) {
        this.bundlePackageList = bundlePackageList;
    }

}
