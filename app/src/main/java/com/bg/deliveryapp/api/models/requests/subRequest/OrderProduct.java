package com.bg.deliveryapp.api.models.requests.subRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProduct {
    @SerializedName("prodId")
    @Expose
    private String prodId;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public OrderProduct() {
    }

    public OrderProduct(String prodId, String quantity) {
        this.prodId = prodId;
        this.quantity = quantity;
    }

    public String getProdId() {
        return prodId;
    }

    public String getQuantity() {
        return quantity;
    }
}
