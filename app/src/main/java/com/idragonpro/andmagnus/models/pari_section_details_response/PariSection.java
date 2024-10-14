
package com.idragonpro.andmagnus.models.pari_section_details_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PariSection {

    @SerializedName("setting")
    @Expose
    private List<Setting> setting = null;
    @SerializedName("section")
    @Expose
    private List<Section> section = null;

    public List<Setting> getSetting() {
        return setting;
    }

    public void setSetting(List<Setting> setting) {
        this.setting = setting;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }

}
