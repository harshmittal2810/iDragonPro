
package com.idragonpro.andmagnus.models.pari_section_details_response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PariSectionDetailResponse {

    @SerializedName("pariSection")
    @Expose
    private PariSection pariSection;

    public PariSection getPariSection() {
        return pariSection;
    }

    public void setPariSection(PariSection pariSection) {
        this.pariSection = pariSection;
    }

}
