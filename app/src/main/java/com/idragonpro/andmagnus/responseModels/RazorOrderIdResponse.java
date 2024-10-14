package com.idragonpro.andmagnus.responseModels;

import com.google.gson.annotations.SerializedName;

public class RazorOrderIdResponse {
    @SerializedName("amount") private int amount;
    @SerializedName("amount_due") private int amountDue;
    @SerializedName("amount_paid") private int amountPaid;
    @SerializedName("attempts") private int attempts;
    @SerializedName("created_at") private int createdAt;
    @SerializedName("currency") private String currency;
    @SerializedName("entity") private String entity;
    @SerializedName("id") private String id;
    @SerializedName("offer_id") private Object offerId;
    @SerializedName("receipt") private String receipt;
    @SerializedName("status") private String status;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(int amountDue) {
        this.amountDue = amountDue;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getOfferId() {
        return offerId;
    }

    public void setOfferId(Object offerId) {
        this.offerId = offerId;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
