package com.bg.deliveryapp.api.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakePaymentRequest {
    @SerializedName("deliveryDate")
    @Expose
    private String deliveryDate;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("amountPaid")
    @Expose
    private Integer amountPaid;
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("id")
    @Expose
    private Object id;

    public MakePaymentRequest(String deliveryDate, String paymentMode, String reference, Integer amountPaid, Integer orderId, Object id) {
        this.deliveryDate = deliveryDate;
        this.paymentMode = paymentMode;
        this.reference = reference;
        this.amountPaid = amountPaid;
        this.orderId = orderId;
        this.id = id;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getReference() {
        return reference;
    }

    public Integer getAmountPaid() {
        return amountPaid;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Object getId() {
        return id;
    }
}
