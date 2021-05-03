package com.bg.deliveryapp.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateOrderProductsResponse implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("desc")
    @Expose
    private String desc;


    private String totalPrice = "0";
    private String quantity = "1";
    private boolean selected = false;

    public CreateOrderProductsResponse(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

}
