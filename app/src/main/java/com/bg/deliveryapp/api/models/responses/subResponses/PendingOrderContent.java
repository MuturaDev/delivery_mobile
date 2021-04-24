package com.bg.deliveryapp.api.models.responses.subResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PendingOrderContent implements Serializable {
    @SerializedName("orderNo")
    @Expose
    private Integer orderNo;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("phoneNo")
    @Expose
    private String phoneNo;
    @SerializedName("deliveryMethod")
    @Expose
    private String deliveryMethod;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("lpoNo")
    @Expose
    private String lpoNo;
    @SerializedName("price")
    @Expose
    private Object price;
    @SerializedName("quantity")
    @Expose
    private Object quantity;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("product")
    @Expose
    private Object product;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("customerId")
    @Expose
    private Object customerId;
    @SerializedName("productId")
    @Expose
    private Object productId;
    @SerializedName("statusDate")
    @Expose
    private Object statusDate;
    @SerializedName("orderStatus")
    @Expose
    private String orderStatus;


    public PendingOrderContent(Integer orderNo, String customerName, String phoneNo, String deliveryMethod, String category, String location, String orderDate, String lpoNo, Object price, Object quantity, String totalAmount, Object product, String status, Object customerId, Object productId, Object statusDate, String orderStatus) {
        this.orderNo = orderNo;
        this.customerName = customerName;
        this.phoneNo = phoneNo;
        this.deliveryMethod = deliveryMethod;
        this.category = category;
        this.location = location;
        this.orderDate = orderDate;
        this.lpoNo = lpoNo;
        this.price = price;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.product = product;
        this.status = status;
        this.customerId = customerId;
        this.productId = productId;
        this.statusDate = statusDate;
        this.orderStatus = orderStatus;
    }


    public Integer getOrderNo() {
        return orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getLpoNo() {
        return lpoNo;
    }

    public Object getPrice() {
        return price;
    }

    public Object getQuantity() {
        return quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public Object getProduct() {
        return product;
    }

    public String getStatus() {
        return status;
    }

    public Object getCustomerId() {
        return customerId;
    }

    public Object getProductId() {
        return productId;
    }

    public Object getStatusDate() {
        return statusDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
