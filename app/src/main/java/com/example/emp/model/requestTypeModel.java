package com.example.emp.model;

public class requestTypeModel {
    String requestId,requestName,requestNote,requestBy,date;

    public requestTypeModel() {
    }

    public requestTypeModel(String requestId, String requestName, String requestNote, String requestBy, String date) {
        this.requestId = requestId;
        this.requestName = requestName;
        this.requestNote = requestNote;
        this.requestBy = requestBy;
        this.date = date;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getRequestNote() {
        return requestNote;
    }

    public void setRequestNote(String requestNote) {
        this.requestNote = requestNote;
    }

    public String getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(String requestBy) {
        this.requestBy = requestBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
