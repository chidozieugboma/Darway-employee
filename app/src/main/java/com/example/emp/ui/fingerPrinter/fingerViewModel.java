package com.example.emp.ui.fingerPrinter;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emp.Callback.iFundRequestCallbackListener;
import com.example.emp.Callback.iStaffFingerPrint;
import com.example.emp.common.common;
import com.example.emp.model.Reguestor_Supervisor;
import com.example.emp.model.fundRequestModel;
import com.example.emp.model.staff_FingerPrint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fingerViewModel extends ViewModel implements iStaffFingerPrint {
    private MutableLiveData<List<staff_FingerPrint>> staffFingerModelList;
    private MutableLiveData<String> messageError=new MutableLiveData<>();
    private iStaffFingerPrint iStaffFingerPrint;
    List<staff_FingerPrint> tempList;

    public fingerViewModel() {
        iStaffFingerPrint=this;
    }


    MutableLiveData<List<staff_FingerPrint>> getstaffFingerModelList() {
        if(staffFingerModelList==null){
            staffFingerModelList=new MutableLiveData<>();
            messageError=new MutableLiveData<>();

            tempList=new ArrayList<>();

            if(common.currentServerUser.getLevel().equals("12"))
            loadStaffSiginRequest();
        }
        return staffFingerModelList;
    }





    public void loadStaffSiginRequest() {
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_ACCOUNT_ATTENDANCE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {
                                staff_FingerPrint useryModel = itemSnapShot.getValue(staff_FingerPrint.class);
                                if (useryModel != null) {
                                    // useryModel.setKey(itemSnapShot.getKey());
                                }
                                tempList.add(useryModel);
                            }
                            if(tempList.size()>0)
                                iStaffFingerPrint.onStaffFingerPrintLoadSuccess(tempList);
                            else
                                iStaffFingerPrint.onStaffFingerPrintFailed("request database is empty!");

                        }else
                            iStaffFingerPrint.onStaffFingerPrintFailed("request do not exits!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        iStaffFingerPrint.onStaffFingerPrintFailed(databaseError.getMessage());

                    }
                });
    }



    public MutableLiveData<String> getMessageError() {
        return messageError;
    }


    @Override
    public void onStaffFingerPrintLoadSuccess(List<staff_FingerPrint> StaffFingerPrintModels) {
        staffFingerModelList.setValue(StaffFingerPrintModels);
    }

    @Override
    public void onStaffFingerPrintFailed(String message) {
        messageError.setValue(message);
    }

}