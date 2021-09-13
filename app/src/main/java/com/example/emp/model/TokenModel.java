package com.example.emp.model;

public class TokenModel {
    private String phone,token;
    private Boolean serverToken,vendorToken;

    public TokenModel() {
    }

    public TokenModel(String phone, String token, Boolean serverToken, Boolean vendorToken) {
        this.phone = phone;
        this.token = token;
        this.serverToken = serverToken;
        this.vendorToken = vendorToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getServerToken() {
        return serverToken;
    }

    public void setServerToken(Boolean serverToken) {
        this.serverToken = serverToken;
    }

    public Boolean getVendorToken() {
        return vendorToken;
    }

    public void setVendorToken(Boolean vendorToken) {
        this.vendorToken = vendorToken;
    }
}
