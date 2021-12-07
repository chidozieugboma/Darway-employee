package com.example.emp.model;

public class staff_FingerPrint {
    String currentLocation,dept,name,narrative,pusshkey,signDate;
    Boolean onLeave;

    public staff_FingerPrint() {
    }

    public staff_FingerPrint(String currentLocation, String dept, String name, String narrative, String pusshkey, String signDate, Boolean onLeave) {
        this.currentLocation = currentLocation;
        this.dept = dept;
        this.name = name;
        this.narrative = narrative;
        this.pusshkey = pusshkey;
        this.signDate = signDate;
        this.onLeave = onLeave;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getdept() {
        return dept;
    }

    public void setdept(String dept) {
        this.dept = dept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNarrative() {
        return narrative;
    }

    public void setNarrative(String narrative) {
        this.narrative = narrative;
    }

    public String getPusshkey() {
        return pusshkey;
    }

    public void setPusshkey(String pusshkey) {
        this.pusshkey = pusshkey;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public Boolean getOnLeave() {
        return onLeave;
    }

    public void setOnLeave(Boolean onLeave) {
        this.onLeave = onLeave;
    }
}
