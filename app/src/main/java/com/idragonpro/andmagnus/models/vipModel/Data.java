package com.idragonpro.andmagnus.models.vipModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.idragonpro.andmagnus.beans.PackageModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Data implements Serializable {

    @SerializedName("packages")
    @Expose
    private ArrayList<PackageModel> packages;
    @SerializedName("payment_gateways")
    @Expose
    private List<PaymentGateway> paymentGateways = null;

    public ArrayList<PackageModel> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<PackageModel> packages) {
        this.packages = packages;
    }

    public List<PaymentGateway> getPaymentGateways() {
        return paymentGateways;
    }

    public void setPaymentGateways(List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
    }

}
