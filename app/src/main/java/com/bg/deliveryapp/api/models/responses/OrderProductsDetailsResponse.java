package com.bg.deliveryapp.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProductsDetailsResponse {

    @SerializedName("opId")
    @Expose
    private Integer opId;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("totalPrice")
    @Expose
    private Integer totalPrice;
    @SerializedName("prodId")
    @Expose
    private Integer prodId;
    @SerializedName("prodDesc")
    @Expose
    private String prodDesc;


    public OrderProductsDetailsResponse(Integer opId, Integer price, Integer quantity, Integer totalPrice, Integer prodId, String prodDesc) {
        this.opId = opId;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.prodId = prodId;
        this.prodDesc = prodDesc;
    }


    public Integer getOpId() {
        return opId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getProdId() {
        return prodId;
    }

    public String getProdDesc() {
        return prodDesc;
    }
}
