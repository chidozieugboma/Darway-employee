package com.example.emp.model;

public class StaffUserModel {
    private String Uid,staffName,phone,companyId,companyName,acctType,dept,designation,staffId,currentLocation,signDate,narrative,pusshkey,level;
    private boolean onLeave;

    public StaffUserModel() {
    }

    public StaffUserModel(String uid, String staffName, String phone, String companyId, String companyName, String acctType, String dept, String designation, String staffId, String currentLocation, String signDate, String narrative, String pusshkey, String level, boolean onLeave) {
        Uid = uid;
        this.staffName = staffName;
        this.phone = phone;
        this.companyId = companyId;
        this.companyName = companyName;
        this.acctType = acctType;
        this.dept = dept;
        this.designation = designation;
        this.staffId = staffId;
        this.currentLocation = currentLocation;
        this.signDate = signDate;
        this.narrative = narrative;
        this.pusshkey = pusshkey;
        this.level = level;
        this.onLeave = onLeave;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
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

    public String getPusshkey() {
        return pusshkey;
    }

    public void setPusshkey(String pusshkey) {
        this.pusshkey = pusshkey;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isOnLeave() {
        return onLeave;
    }

    public void setOnLeave(boolean onLeave) {
        this.onLeave = onLeave;
    }



}
