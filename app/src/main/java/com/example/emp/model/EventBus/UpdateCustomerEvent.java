package com.example.emp.model.EventBus;

import com.example.emp.model.fundRequestModel;

public class UpdateCustomerEvent {
    private com.example.emp.model.fundRequestModel fundRequestModel;
    private boolean active;

    public UpdateCustomerEvent(fundRequestModel fundRequestModel, boolean active) {
        this.fundRequestModel = fundRequestModel;
        this.active = active;
    }

    public fundRequestModel getCustomersModel() {
        return fundRequestModel;
    }

    public void setCustomersModel(fundRequestModel fundRequestModel) {
        this.fundRequestModel = fundRequestModel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive1(boolean active) {
        this.active = active;
    }
}