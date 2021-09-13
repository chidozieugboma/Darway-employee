package com.example.emp.model;

public class CompanyModel {
    private String key,uId,name,phone,companyName,companyId,address,paymentUrl,Date_Reg;
    private Boolean active;

    public CompanyModel() {
    }

    public CompanyModel(String key, String uId, String name, String phone, String companyName, String companyId, String address, String paymentUrl, String date_Reg, Boolean active) {
        this.key = key;
        this.uId = uId;
        this.name = name;
        this.phone = phone;
        this.companyName = companyName;
        this.companyId = companyId;
        this.address = address;
        this.paymentUrl = paymentUrl;
        Date_Reg = date_Reg;
        this.active = active;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDate_Reg() {
        return Date_Reg;
    }

    public void setDate_Reg(String date_Reg) {
        Date_Reg = date_Reg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
