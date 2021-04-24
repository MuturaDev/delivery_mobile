package com.bg.deliveryapp.api.models.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateOrderDeliveryResponse implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shtDesc")
    @Expose
    private String shtDesc;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("selected")
    @Expose
    private String selected;

    public CreateOrderDeliveryResponse(Integer id, String shtDesc, String desc, String selected) {
        this.id = id;
        this.shtDesc = shtDesc;
        this.desc = desc;
        this.selected = selected;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getShtDesc() {
        return shtDesc;
    }

    public String getDesc() {
        return desc;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
