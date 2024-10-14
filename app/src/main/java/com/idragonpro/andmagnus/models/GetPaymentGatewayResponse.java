package com.idragonpro.andmagnus.models;

import java.util.List;

public class GetPaymentGatewayResponse {
    private List<GetPaymentGatewayDetails> paymentGateways = null;

    public List<GetPaymentGatewayDetails> getPaymentGateways() {
        return paymentGateways;
    }

    public void setPaymentGateways(List<GetPaymentGatewayDetails> paymentGateways) {
        this.paymentGateways = paymentGateways;
    }

}
