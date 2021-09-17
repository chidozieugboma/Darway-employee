package com.example.emp.model;

public class ServerUserModel {
    private String Uid,name,phone,companyId,companyName,acctType,address,village,key,level,staffId;
    private boolean active,accountEnabled,companyStatus;
    private double lat,lng;

    public ServerUserModel() {

    }

    public ServerUserModel(String uid, String name, String phone, String companyId, String companyName, String acctType, String address, String village, String key, String level, String staffId, boolean active, boolean accountEnabled, boolean companyStatus, double lat, double lng) {
        Uid = uid;
        this.name = name;
        this.phone = phone;
        this.companyId = companyId;
        this.companyName = companyName;
        this.acctType = acctType;
        this.address = address;
        this.village = village;
        this.key = key;
        this.level = level;
        this.staffId = staffId;
        this.active = active;
        this.accountEnabled = accountEnabled;
        this.companyStatus = companyStatus;
        this.lat = lat;
        this.lng = lng;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(boolean companyStatus) {
        this.companyStatus = companyStatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public boolean isAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}


