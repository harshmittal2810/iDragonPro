package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.SubscriptionModel;

import java.util.List;

public class GetAllSingleMovieSubResponseModel {
    @SerializedName("subscriptions")
    @Expose
    private List<SubscriptionModel> subscriptions;

    public List<SubscriptionModel> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionModel> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
