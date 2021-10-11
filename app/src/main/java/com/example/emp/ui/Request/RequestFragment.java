package com.example.emp.ui.Request;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emp.Callback.ChangeMenuClick;
import com.example.emp.R;
import com.example.emp.Remote.IFCMService;
import com.example.emp.Remote.RetrofitFCMClient;
import com.example.emp.adapter.fundRequetAdapter;
import com.example.emp.common.MySwiperHelper;
import com.example.emp.common.common;
import com.example.emp.model.EventBus.ToastEvent;
import com.example.emp.model.EventBus.UpdateCustomerEvent;
import com.example.emp.model.ServerUserModel;
import com.example.emp.model.StaffUserModel;
import com.example.emp.model.TokenModel;
import com.example.emp.model.fundRequestModel;
import com.example.emp.model.requestTypeModel;
import com.example.emp.sendNotificationPack.FCMSendData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.SingleObserver;


public class RequestFragment extends Fragment {


    private RequestViewModel requestViewModel1;
    private AlertDialog dialog;
    private Unbinder unbinder;
    Spinner edt_first_Line,spin_request_type;
    String supervisorId,supervisorKey;
    String m;
    EditText edt_title;
    private IFCMService ifcmService;
   // hrLogin mstaffId;

    private List<fundRequestModel> fundRequestModels;
    fundRequetAdapter adapter;
    private LayoutAnimationController layoutAnimationController;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @BindView(R.id.recycleRequest)
    RecyclerView recycleRequest;

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCustomerEvent.class))
            EventBus.getDefault().removeStickyEvent(UpdateCustomerEvent.class);
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().postSticky(new ChangeMenuClick(true));
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        requestViewModel1 = new ViewModelProvider(this).get(RequestViewModel.class);
        View root = inflater.inflate(R.layout.fragment_request, container, false);

        unbinder= ButterKnife.bind(this,root);
        InitView();

        requestViewModel1.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        requestViewModel1.getFundRequestModelList().observe(getViewLifecycleOwner(),fundRequestModelList -> {
            dialog.dismiss();
            fundRequestModels=fundRequestModelList;
            adapter=new fundRequetAdapter(getContext(),fundRequestModelList);
            recycleRequest.setAdapter(adapter);
            recycleRequest.setLayoutAnimation(layoutAnimationController);
        });


        ifcmService = RetrofitFCMClient.getInstance().create(IFCMService.class);


        return root;
    }

    private void InitView() {
        setHasOptionsMenu(true);

        dialog= new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recycleRequest.setLayoutManager(layoutManager);
        recycleRequest.addItemDecoration(new DividerItemDecoration(requireContext(),layoutManager.getOrientation()));

        new MySwiperHelper(getContext(), recycleRequest, 150) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {

                buf.add(new MyButton(getContext(), "DELETE", 30, 0, Color.parseColor("#4f1123"),
                        pos -> {
                            Toast.makeText(getContext(), fundRequestModels.get(pos).getDetail(), Toast.LENGTH_SHORT).show();

                        }));

            }
        };

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_menu_request,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            showAddRequestDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddRequestDialog() {

        //Add items;
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Fund Request");
        //get staff supervisor
        getSupervisor();
        getRequestTypes();

        @SuppressLint("InflateParams") View itemView =LayoutInflater.from(getContext()).inflate(R.layout.layout_add_request,null);
        edt_title= itemView.findViewById(R.id.edt_title);
        EditText edt_amount= itemView.findViewById(R.id.edt_amount);
        edt_first_Line=itemView.findViewById(R.id.edt_first_Line);
        EditText edt_details=itemView.findViewById(R.id.edt_details);
        spin_request_type=itemView.findViewById(R.id.spin_request_type);
        //Add code



        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        }).setPositiveButton("REQUEST",(dialogInterface, i) -> {
            dialog.setMessage("Uploading.....");
            dialog.show();

            dialog.dismiss();
            if(TextUtils.isEmpty(edt_title.getText()) || TextUtils.isEmpty(edt_amount.getText())){
                Toast.makeText(getContext(), "kindly fill all required fields", Toast.LENGTH_LONG).show();
            }else {

                if(common.currentServerUser.getLevel().equals("1")){
                    for(StaffUserModel staffs: common.LOAD_ALL_REG_STAFFS){
                        if(staffs.getStaffName().matches(edt_first_Line.getSelectedItem().toString())){
                            supervisorId=staffs.getStaffId();
                            supervisorKey=staffs.getUid();
                            common.SUPERVISORID=supervisorId;


                        }
                    }
                }else {
                    for(ServerUserModel staffs: common.LOAD_ADMIN_STAFFS){
                        if(staffs.getName().matches(edt_first_Line.getSelectedItem().toString())){
                            supervisorId=staffs.getStaffId();
                            supervisorKey=staffs.getUid();
                            common.SUPERVISORID=supervisorId;


                        }
                    }
                }



                createRequest(String.valueOf(accountNumber()),edt_title.getText().toString(), edt_amount.getText().toString(),
                        edt_first_Line.getSelectedItem().toString(),supervisorId,edt_details.getText().toString(),common.STAFF_SIGN_DETAILS.getStaffId(),
                        common.STAFF_SIGN_DETAILS.getLevel(),"TR-DR-"+accountNumber(),spin_request_type.getSelectedItem().toString());
                saveSuperVisors(supervisorKey,common.STAFF_SIGN_DETAILS.getUid());
            }

        });
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog=builder.create();
        dialog.show();


    }

    private void getRequestTypes(){
        List<String> tempList=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.REQUEST_TYPES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot fn:snapshot.getChildren()){
                                requestTypeModel model=fn.getValue(requestTypeModel.class);
                                tempList.add(model.getRequestName());
                            }


                            // Creating adapter for spinner
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tempList);

                            // Drop down layout style - list view with radio button
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // attaching data adapter to spinner
                            spin_request_type.setAdapter(dataAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getSupervisor(){
        List<String> tempList = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_LOGIN)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot itemSnapShot : snapshot.getChildren()) {
                                StaffUserModel useryModel = itemSnapShot.getValue(StaffUserModel.class);
                                if (useryModel != null) {

                                }
                                if (common.currentServerUser.getLevel().equals("1") || common.currentServerUser.getLevel().equals("11")) {
                                    if (useryModel.getLevel().equals("2"))
                                        tempList.add(useryModel.getStaffName());
                                } else if ((common.currentServerUser.getLevel().equals("2"))) {
                                   /* if (useryModel.getLevel().equals("3"))
                                        tempList.add(useryModel.getStaffName());*/
                                    getSupervisorAbove2();
                                }
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tempList);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                edt_first_Line.setAdapter(dataAdapter);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }


    private void getSupervisorAbove2(){
        List<String> tempList = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference(common.SERVER_REF)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot itemSnapShot : snapshot.getChildren()) {
                                ServerUserModel useryModel = itemSnapShot.getValue(ServerUserModel.class);
                                if (useryModel != null) {

                                }
                                if ((common.currentServerUser.getLevel().equals("2"))) {
                                    if (useryModel.getLevel().equals("3"))
                                        tempList.add(useryModel.getName());
                                }
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, tempList);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                edt_first_Line.setAdapter(dataAdapter);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

    }



    private int accountNumber(){
        int max=90000000;
        int min=65000000;
        Random random = new Random();
        int randomNumber = random.nextInt(max - min) + min;
        return randomNumber;
    }

    //use to write to Log
    private void writeToLog(int pos,fundRequestModel model){
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,Object> writeLog=new HashMap<>();
        writeLog.put("title",model.getTitle());
        writeLog.put("amount",model.getAmount());
        writeLog.put("staffId",model.getStaffId());
        writeLog.put("detail",model.getDetail());
        writeLog.put("curretSignedTo",model.getSupervisor());
        writeLog.put("firstLevelApprovalBy",model.getFirstLevelApprovalBy());
        writeLog.put("SecondLevelApprovalBy",model.getSecondLevelApprovalBy());
        writeLog.put("status",model.getStatus());
        writeLog.put("regDate", DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime));
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUEST_LOG)
                .child(fundRequestModels.get(pos).getUId())
                .push()
                .setValue(writeLog)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void createRequest(String account,String title,String amount,String supervisor,String supervisorId,String detail,String staffId,String staffLevel,String transId,String requestType){
        String pussh= FirebaseDatabase.getInstance().getReference(common.STAFF_REQUEST).push().getKey();
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,Object> addData=new HashMap<>();
        addData.put("requestType",requestType);
        addData.put("key",account);
        addData.put("title",title);
        addData.put("amount",amount);
        addData.put("supervisor",supervisor);
        addData.put("supervisorId",supervisorId);
        addData.put("staffId",staffId);
        addData.put("detail",detail);
        addData.put("status","Pending");
        addData.put("UId",currentuser);
        addData.put("posRequest","0");
        addData.put("approvalLevel",staffLevel);
        addData.put("pushKey",pussh);
        addData.put("transId",transId);
        addData.put("curretSignedTo",supervisor);
        addData.put("firstLevelApprovalBy",supervisor);
        addData.put("requestBy",common.STAFF_SIGN_DETAILS.getStaffName());
        addData.put("regDate", DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime));
        addData.put("approvedDate", DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime));
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUEST)
                .child(currentuser)
                .child(pussh)
                .setValue(addData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), "tier Creation Failed", Toast.LENGTH_SHORT).show()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                requestViewModel1.loadFundRequest();
                EventBus.getDefault().postSticky(new ToastEvent(true,false));
                sendNotification(supervisorKey);
            }else {
                Toast.makeText(getContext(), "Save not successful", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void sendNotification(String key) {
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_TOKENS)
                .child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            TokenModel tokenModel=snapshot.getValue(TokenModel.class);
                          //  sendNotifications(tokenModel.getToken(), "New Fund Request",edt_title.getText().toString().trim());
/*                            Intent intent = new Intent(getContext(), RequestFragment.class);
                            common.showNotification(getContext(),new Random().nextInt(),"New Fund Request",edt_title.getText().toString().trim(),intent);*/
                           // sendMessage("New Fund Request",edt_title.getText().toString(),tokenModel.getToken());

                            Map<String, String> notiData = new HashMap<>();
                            notiData.put(common.NOTI_TITLE, "New Fund Request");
                            notiData.put(common.NOTI_CONTENT, "You have new Request from " + tokenModel.getToken());

                            FCMSendData sendData = new FCMSendData(common.createTopicOrder(), notiData);
                            compositeDisposable.add(ifcmService.sendNotification(sendData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(fcmResponse -> {
                                        Toast.makeText(getContext(), "Notification sent.", Toast.LENGTH_SHORT).show();
                                        Log.i("myNoti",String.valueOf(fcmResponse.getFailure()));
                                    },throwable -> {
                                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }));


                            Log.i("show01",key);
                            Log.d("show02", "onDataChange: "+tokenModel.getToken());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void saveSuperVisors(String supervisorkey,String requestorKey){
        Map<String,Object> oData=new HashMap<>();
        oData.put("requester",requestorKey);
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUESTORS_SUPERVISORS)
                .child(supervisorkey)
                .push()
                .setValue(oData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), "fail to save supervisor for requester", Toast.LENGTH_SHORT).show()).addOnSuccessListener(unused -> Toast.makeText(getContext(), "Saved successfully to supervisors for requester", Toast.LENGTH_SHORT).show());
    }


    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onUpdateCustomerActive(UpdateCustomerEvent event){
        Map<String,Object> updateData=new HashMap<>();
        updateData.put("active",event.isActive());
        FirebaseDatabase.getInstance()
                .getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUEST)
                .child(event.getCustomersModel().getKey())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Update state to "+event.isActive(), Toast.LENGTH_SHORT).show());
    }
}