package com.bg.deliveryapp.api.models.responses;

import com.bg.deliveryapp.api.models.responses.subResponses.PendingOrderContent;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingOrdersResponse {
    @SerializedName("content")
    @Expose
    private List<PendingOrderContent> content = null;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("last")
    @Expose
    private Boolean last;
    @SerializedName("first")
    @Expose
    private Boolean first;
    @SerializedName("sort")
    @Expose
    private Object sort;
    @SerializedName("numberOfElements")
    @Expose
    private Integer numberOfElements;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("number")
    @Expose
    private Integer number;

    public PendingOrdersResponse(List<PendingOrderContent> content, Integer totalPages, Integer totalElements, Boolean last, Boolean first, Object sort, Integer numberOfElements, Integer size, Integer number) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;
        this.first = first;
        this.sort = sort;
        this.numberOfElements = numberOfElements;
        this.size = size;
        this.number = number;
    }


    public List<PendingOrderContent> getContent() {
        return content;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public Boolean getLast() {
        return last;
    }

    public Boolean getFirst() {
        return first;
    }

    public Object getSort() {
        return sort;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getNumber() {
        return number;
    }
}
