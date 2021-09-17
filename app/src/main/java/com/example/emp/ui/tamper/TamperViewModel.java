package com.example.emp.ui.tamper;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emp.Callback.iTamperCallbackListener1;
import com.example.emp.common.common;
import com.example.emp.model.TamperModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TamperViewModel extends ViewModel implements iTamperCallbackListener1 {

    private MutableLiveData<List<TamperModel>> tamperList;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private iTamperCallbackListener1 iTamperCallbackListener;

    public TamperViewModel() {
        iTamperCallbackListener = this;
    }

    MutableLiveData<List<TamperModel>> getTamperList() {
        if (tamperList == null) {
            tamperList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();

            loadTamper();


        }
        return tamperList;
    }

    public void loadTamper() {
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<TamperModel> tempList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(currentuser)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()) {
                                TamperModel tamperModel = itemSnapShot.getValue(TamperModel.class);
                                if (tamperModel != null) {
                                    tamperModel.setKey(itemSnapShot.getKey());
                                }
                                tempList.add(tamperModel);
                            }
                            if (tempList.size() > 0)
                                iTamperCallbackListener.onTamperLoadSuccess(tempList);
                            else
                                iTamperCallbackListener.onTamperLoadFailed("customer database is empty!");

                        } else
                            iTamperCallbackListener.onTamperLoadFailed("customer do not exits!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        iTamperCallbackListener.onTamperLoadFailed(databaseError.getMessage());

                    }
                });
    }


    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onTamperLoadSuccess(List<TamperModel> tamperModels) {
        tamperList.setValue(tamperModels);
    }

    @Override
    public void onTamperLoadFailed(String message) {
        messageError.setValue(message);
    }
}
