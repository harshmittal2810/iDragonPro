package com.idragonpro.andmagnus.models;

import com.google.gson.annotations.SerializedName;

public class GetRazorOrderIDRequest {

    @SerializedName("amount") private String amount;

    @SerializedName("currency") private String currency;

    @SerializedName("receipt") private String receipt;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
