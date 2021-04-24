package com.bg.deliveryapp.api.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EditCustomerRequest {
    @SerializedName("clientId")
    @Expose
    private Integer clientId;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("contactPerson")
    @Expose
    private String contactPerson;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("phoneNumber2")
    @Expose
    private String phoneNumber2;
    @SerializedName("phoneNumber3")
    @Expose
    private String phoneNumber3;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("areaCode")
    @Expose
    private Integer arareaCodeea;

    public EditCustomerRequest(Integer clientId, String firstname, String lastname, String contactPerson, String type, String email, String phoneNumber, String phoneNumber2, String phoneNumber3, String location, Integer arareaCodeea) {
        this.clientId = clientId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactPerson = contactPerson;
        this.type = type;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.phoneNumber2 = phoneNumber2;
        this.phoneNumber3 = phoneNumber3;
        this.location = location;
        this.arareaCodeea = arareaCodeea;
    }


    public Integer getClientId() {
        return clientId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public String getPhoneNumber3() {
        return phoneNumber3;
    }

    public String getLocation() {
        return location;
    }

    public Integer getArareaCodeea() {
        return arareaCodeea;
    }
}