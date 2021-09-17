package com.example.emp.Callback;


import com.example.emp.model.TamperModel;

import java.util.List;

public interface iTamperCallbackListener1 {
    void onTamperLoadSuccess(List<TamperModel> tamperModels);
    void onTamperLoadFailed(String message);
}
