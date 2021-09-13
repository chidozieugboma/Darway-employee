package com.example.emp.model.EventBus;

import com.example.emp.ui.Request.RequestFragment;

import java.util.List;

public class UpdateRequest {
    private List<RequestFragment> requestFragments;

    public UpdateRequest() {
    }

    public List<RequestFragment> getRequestFragments() {
        return requestFragments;
    }

    public void setRequestFragments(List<RequestFragment> requestFragments) {
        this.requestFragments = requestFragments;
    }
}
