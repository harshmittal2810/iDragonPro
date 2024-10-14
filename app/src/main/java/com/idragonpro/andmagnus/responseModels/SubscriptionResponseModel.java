package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.SubscriptionModel;

public class SubscriptionResponseModel {
    @SerializedName("subscription")
    @Expose
    private SubscriptionModel subscription;


    public SubscriptionModel getSubscription() {
        return subscription;
    }

    public void setSubscription(SubscriptionModel subscription) {
        this.subscription = subscription;
    }
}
