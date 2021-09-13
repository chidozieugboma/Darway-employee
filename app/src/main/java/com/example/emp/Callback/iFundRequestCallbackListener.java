package com.example.emp.Callback;

import com.example.emp.model.fundRequestModel;

import java.util.List;

public interface iFundRequestCallbackListener {
    void onFundRequestLoadSuccess(List<fundRequestModel> fundRequestModels);
    void onFundRequestLoadFailed(String message);
}
