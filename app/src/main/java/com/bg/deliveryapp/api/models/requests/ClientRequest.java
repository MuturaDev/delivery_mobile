package com.bg.deliveryapp.api.models.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientRequest {

    private Object clientId;
    private String firstname;
    private String lastname;
    private String contactPerson;
    private String type;
    private String email;
    private String phoneNumber;
    private String phoneNumber2;
    private String phoneNumber3;
    private String location;
    private Integer areaCode;

    public ClientRequest(Object clientId, String firstname, String lastname, String contactPerson, String type, String email, String phoneNumber, String phoneNumber2, String phoneNumber3, String location, Integer areaCode) {
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
        this.areaCode = areaCode;
    }

    public Object getClientId() {
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

    public Integer getAreaCode() {
        return areaCode;
    }
}
