package com.bg.deliveryapp.api.models.requests;

import com.bg.deliveryapp.api.models.requests.subRequest.OrderProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateOrderRequest {
    @SerializedName("orderNo")
    @Expose
    private Object orderNo;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("deliveryMethod")
    @Expose
    private Integer deliveryMethod;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("ordDate")
    @Expose
    private String ordDate;
    @SerializedName("lpoNo")
    @Expose
    private String lpoNo;
    @SerializedName("category")
    @Expose
    private Integer category;
    @SerializedName("orderProducts")
    @Expose
    private List<OrderProduct> orderProducts = null;


    public CreateOrderRequest(Object orderNo, Integer customerId, Integer deliveryMethod, String location, String ordDate, String lpoNo, Integer category, List<OrderProduct> orderProducts) {
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.deliveryMethod = deliveryMethod;
        this.location = location;
        this.ordDate = ordDate;
        this.lpoNo = lpoNo;
        this.category = category;
        this.orderProducts = orderProducts;
    }


    public Object getOrderNo() {
        return orderNo;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public Integer getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getLocation() {
        return location;
    }

    public String getOrdDate() {
        return ordDate;
    }

    public String getLpoNo() {
        return lpoNo;
    }

    public Integer getCategory() {
        return category;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }


}
