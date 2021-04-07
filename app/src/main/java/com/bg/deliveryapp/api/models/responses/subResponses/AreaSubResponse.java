package com.bg.deliveryapp.api.models.responses.subResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaSubResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public AreaSubResponse(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
