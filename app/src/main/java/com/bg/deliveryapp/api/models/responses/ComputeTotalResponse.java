package com.bg.deliveryapp.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComputeTotalResponse {
    @SerializedName("price")
    @Expose
    private Integer price;

    public ComputeTotalResponse(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }
}
