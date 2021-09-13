package com.example.emp.model;

public class hrLogin {
    String acctType,companyId,companyName,dept,designation,level,staffName,phone,uid;

    public hrLogin() {
    }

    public hrLogin(String acctType, String companyId, String companyName, String dept, String designation, String level, String staffName, String phone, String uid) {
        this.acctType = acctType;
        this.companyId = companyId;
        this.companyName = companyName;
        this.dept = dept;
        this.designation = designation;
        this.level = level;
        this.staffName = staffName;
        this.phone = phone;
        this.uid = uid;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
