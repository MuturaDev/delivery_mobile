package com.bg.deliveryapp.api.models.responses;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

public class AuthenticationResponse extends SugarRecord<AuthenticationResponse> {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("base64EncodedAuthenticationKey")
    @Expose
    private String base64EncodedAuthenticationKey;
    @SerializedName("permissions")
    @Expose
    private List<String> permissions = null;


    public AuthenticationResponse() {
    }

    private String username;
    private String password;

    public AuthenticationResponse(String name, String base64EncodedAuthenticationKey, List<String> permissions) {
        this.name = name;
        this.base64EncodedAuthenticationKey = base64EncodedAuthenticationKey;
        this.permissions = permissions;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getBase64EncodedAuthenticationKey() {
        return base64EncodedAuthenticationKey;
    }

    public List<String> getPermissions() {
        return permissions;
    }


    public boolean isUserLogededIn(){

        if(TextUtils.isEmpty(getBase64EncodedAuthenticationKey()) || TextUtils.isEmpty(getUsername()) || TextUtils.isEmpty(getPassword()) || TextUtils.isEmpty(getName()))
            return false;
        else
            return true;

    }
}
