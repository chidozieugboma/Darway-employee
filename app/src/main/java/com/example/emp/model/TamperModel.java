package com.example.emp.model;

public class TamperModel {
    String meter_Number,Token1,Token2,Token3,key,status,marker1,marker2,marker3,regDate,uId;

    public TamperModel() {
    }


    public TamperModel(String meter_Number, String token1, String token2, String token3, String key, String status, String marker1, String marker2, String marker3, String regDate, String uId) {
        this.meter_Number = meter_Number;
        Token1 = token1;
        Token2 = token2;
        Token3 = token3;
        this.key = key;
        this.status = status;
        this.marker1 = marker1;
        this.marker2 = marker2;
        this.marker3 = marker3;
        this.regDate = regDate;
        this.uId = uId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMarker1() {
        return marker1;
    }

    public void setMarker1(String marker1) {
        this.marker1 = marker1;
    }

    public String getMarker2() {
        return marker2;
    }

    public void setMarker2(String marker2) {
        this.marker2 = marker2;
    }

    public String getMarker3() {
        return marker3;
    }

    public void setMarker3(String marker3) {
        this.marker3 = marker3;
    }

    public String getToken1() {
        return Token1;
    }

    public void setToken1(String token1) {
        Token1 = token1;
    }

    public String getToken2() {
        return Token2;
    }

    public void setToken2(String token2) {
        Token2 = token2;
    }

    public String getToken3() {
        return Token3;
    }

    public void setToken3(String token3) {
        Token3 = token3;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMeter_Number() {
        return meter_Number;
    }

    public void setMeter_Number(String meter_Number) {
        this.meter_Number = meter_Number;
    }

}
