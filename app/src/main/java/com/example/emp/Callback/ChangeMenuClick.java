package com.example.emp.Callback;

public class ChangeMenuClick {
    private Boolean accountEnabled;

    public ChangeMenuClick(Boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }

    public Boolean getAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(Boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }
}