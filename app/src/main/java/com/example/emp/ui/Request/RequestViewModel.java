package com.example.emp.ui.Request;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emp.Callback.iFundRequestCallbackListener;
import com.example.emp.common.common;
import com.example.emp.model.fundRequestModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestViewModel extends ViewModel implements iFundRequestCallbackListener {
    private MutableLiveData<List<fundRequestModel>> fundRequestModelList;
    private MutableLiveData<String> messageError=new MutableLiveData<>();
    private iFundRequestCallbackListener iFundRequestCallbackListener;

    public RequestViewModel() {
        iFundRequestCallbackListener=this;
    }


    MutableLiveData<List<fundRequestModel>> getFundRequestModelList() {
        if(fundRequestModelList==null){
            fundRequestModelList=new MutableLiveData<>();
            messageError=new MutableLiveData<>();

            loadFundRequest();
        }
        return fundRequestModelList;
    }


    public void loadFundRequest() {
        List<fundRequestModel> tempList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUEST)
                .child(common.STAFF_SIGN_DETAILS.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {
                                fundRequestModel useryModel = itemSnapShot.getValue(fundRequestModel.class);
                                if (useryModel != null) {
                                    // useryModel.setKey(itemSnapShot.getKey());
                                }
                                if(!useryModel.getPosRequest().equals(common.STAFF_SIGN_DETAILS.getLevel()) && useryModel.getStatus().equals("Pending"))
                                tempList.add(useryModel);
                            }
                            if(tempList.size()>0)
                                iFundRequestCallbackListener.onFundRequestLoadSuccess(tempList);
                            else
                                iFundRequestCallbackListener.onFundRequestLoadFailed("customer database is empty!");

                        }else
                            iFundRequestCallbackListener.onFundRequestLoadFailed("customer do not exits!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        iFundRequestCallbackListener.onFundRequestLoadFailed(databaseError.getMessage());

                    }
                });
    }


    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onFundRequestLoadSuccess(List<fundRequestModel> fundRequestModels) {
        if(fundRequestModels!=null){
            fundRequestModelList.setValue(fundRequestModels);
        }

    }

    @Override
    public void onFundRequestLoadFailed(String message) {
        messageError.setValue(message);
    }
}