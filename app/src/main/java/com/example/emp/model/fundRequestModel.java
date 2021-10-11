package com.example.emp.model;

public class fundRequestModel {
    String key,title,amount,detail,supervisor,supervisorId,staffId,status,regDate,approvedDate,UId,posRequest,pushKey,approvalLevel,transId,curretSignedTo,firstLevelApprovalBy,SecondLevelApprovalBy,
    requestBy,requestType;

    public fundRequestModel() {
    }


    public fundRequestModel(String key, String title, String amount, String detail, String supervisor, String supervisorId, String staffId, String status, String regDate, String approvedDate, String UId, String posRequest, String pushKey, String approvalLevel, String transId, String curretSignedTo, String firstLevelApprovalBy, String secondLevelApprovalBy, String requestBy, String requestType) {
        this.key = key;
        this.title = title;
        this.amount = amount;
        this.detail = detail;
        this.supervisor = supervisor;
        this.supervisorId = supervisorId;
        this.staffId = staffId;
        this.status = status;
        this.regDate = regDate;
        this.approvedDate = approvedDate;
        this.UId = UId;
        this.posRequest = posRequest;
        this.pushKey = pushKey;
        this.approvalLevel = approvalLevel;
        this.transId = transId;
        this.curretSignedTo = curretSignedTo;
        this.firstLevelApprovalBy = firstLevelApprovalBy;
        SecondLevelApprovalBy = secondLevelApprovalBy;
        this.requestBy = requestBy;
        this.requestType = requestType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getCurretSignedTo() {
        return curretSignedTo;
    }

    public void setCurretSignedTo(String curretSignedTo) {
        this.curretSignedTo = curretSignedTo;
    }

    public String getFirstLevelApprovalBy() {
        return firstLevelApprovalBy;
    }

    public void setFirstLevelApprovalBy(String firstLevelApprovalBy) {
        this.firstLevelApprovalBy = firstLevelApprovalBy;
    }

    public String getSecondLevelApprovalBy() {
        return SecondLevelApprovalBy;
    }

    public void setSecondLevelApprovalBy(String secondLevelApprovalBy) {
        SecondLevelApprovalBy = secondLevelApprovalBy;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getApprovalLevel() {
        return approvalLevel;
    }

    public void setApprovalLevel(String approvalLevel) {
        this.approvalLevel = approvalLevel;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getPosRequest() {
        return posRequest;
    }

    public void setPosRequest(String posRequest) {
        this.posRequest = posRequest;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }
}
