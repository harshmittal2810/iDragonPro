package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApkVersionResponseModel {

    @SerializedName("appmaintenance")
    @Expose
    private Appmaintenance appmaintenance;
    @SerializedName("status")
    @Expose
    private String status;

    public Appmaintenance getAppmaintenance() {
        return appmaintenance;
    }

    public void setAppmaintenance(Appmaintenance appmaintenance) {
        this.appmaintenance = appmaintenance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Appmaintenance {
        @SerializedName("appversion")
        @Expose
        private String appversion;

        public String getAppversion() {
            return appversion;
        }

        public void setAppversion(String appversion) {
            this.appversion = appversion;
        }
    }
}
