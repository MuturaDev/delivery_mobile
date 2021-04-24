package com.bg.deliveryapp.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderPaymentsListResponse {
    @SerializedName("deliveryDate")
    @Expose
    private Object deliveryDate;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("amountPaid")
    @Expose
    private Float amountPaid;
    @SerializedName("orderId")
    @Expose
    private Integer orderId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("delDate")
    @Expose
    private String delDate;


    public OrderPaymentsListResponse(Object deliveryDate, String paymentMode, String reference, Float amountPaid, Integer orderId, Integer id, String delDate) {
        this.deliveryDate = deliveryDate;
        this.paymentMode = paymentMode;
        this.reference = reference;
        this.amountPaid = amountPaid;
        this.orderId = orderId;
        this.id = id;
        this.delDate = delDate;
    }


    public Object getDeliveryDate() {
        return deliveryDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getReference() {
        return reference;
    }

    public Float getAmountPaid() {
        return amountPaid;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getId() {
        return id;
    }

    public String getDelDate() {
        return delDate;
    }
}
