package com.example.emp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.emp.R;
import com.example.emp.common.common;
import com.example.emp.databinding.FragmentHomeBinding;
import com.example.emp.model.Reguestor_Supervisor;
import com.example.emp.model.ServerUserModel;
import com.example.emp.model.StaffUserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //load all staffs in memory
            loadAllStaff();
            getAdminStaffs();



        requestor_supervisor(common.STAFF_SIGN_DETAILS.getUid());
        //requestor_supervisor("uSNAyq2mB4bPDtZnDgDmoel6yp32");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<StaffUserModel> loadAllStaff(){
        List<StaffUserModel> tempList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_LOGIN)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot itemSnapShot : snapshot.getChildren()) {
                                StaffUserModel useryModel = itemSnapShot.getValue(StaffUserModel.class);
                                if(useryModel!=null){
                                    // useryModel.setUid(snapshot.getKey());
                                }
                                tempList.add(useryModel);
                            }
                            common.LOAD_ALL_REG_STAFFS=tempList;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return tempList;
    }


    private void getAdminStaffs(){
        List<ServerUserModel> tempList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(common.SERVER_REF)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for (DataSnapshot itemSnapShot : snapshot.getChildren()) {
                                ServerUserModel useryModel = itemSnapShot.getValue(ServerUserModel.class);
                                if(useryModel!=null){
                                    // useryModel.setUid(snapshot.getKey());
                                }
                                tempList.add(useryModel);
                            }
                            common.LOAD_ADMIN_STAFFS=tempList;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void requestor_supervisor(String supervisor){
        List<Reguestor_Supervisor> requestor=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUESTORS_SUPERVISORS)
                .child(supervisor)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                Reguestor_Supervisor reguestor_supervisor=dataSnapshot.getValue(Reguestor_Supervisor.class);
                                if(reguestor_supervisor !=null){

                                }
                                requestor.add(reguestor_supervisor);
                                common.REQUESTOR=requestor;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}