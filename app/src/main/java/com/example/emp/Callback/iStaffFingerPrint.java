package com.example.emp.Callback;

import com.example.emp.model.staff_FingerPrint;

import java.util.List;

public interface iStaffFingerPrint {
    void onStaffFingerPrintLoadSuccess(List<staff_FingerPrint> StaffFingerPrintModels);
    void onStaffFingerPrintFailed(String message);
}
