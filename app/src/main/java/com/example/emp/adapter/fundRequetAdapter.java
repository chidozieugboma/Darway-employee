package com.example.emp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.emp.R;
import com.example.emp.common.common;
import com.example.emp.model.EventBus.UpdateRequest;
import com.example.emp.model.ServerUserModel;
import com.example.emp.model.StaffUserModel;
import com.example.emp.model.fundRequestModel;
import com.example.emp.ui.Request.RequestViewModel;
import com.example.emp.ui.myRequest.myRequestFragment;
import com.example.emp.ui.myRequest.myRequestViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class fundRequetAdapter extends RecyclerView.Adapter<fundRequetAdapter.MyViewHolder>{
    private Context context;
    private final List<fundRequestModel> fundRequestModels;
    private RequestViewModel requestViewModel;
    private UpdateRequest updateAddonModel;
    Spinner edt_second_Line;
    private AlertDialog dialog;
    String supervisorId,supervisorKey,selectedSecSupervisor,myStatus;

    public fundRequetAdapter(Context context, List<fundRequestModel> fundRequestModels) {
        this.context = context;
        this.fundRequestModels = fundRequestModels;
        updateAddonModel=new UpdateRequest();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView= LayoutInflater.from(context).inflate( R.layout.layout_request,parent,false);
        requestViewModel=new RequestViewModel();

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        dialog=new SpotsDialog.Builder().setContext(context).setCancelable(false).build();

        holder.txt_amount.setText(new StringBuilder(fundRequestModels.get(position).getTitle()).append("(").append("₦").append(fundRequestModels.get(position).getAmount()).append(")"));
        holder.txt_note.setText(new StringBuilder().append(fundRequestModels.get(position).getDetail()));
        holder.txt_status.setText(new StringBuilder().append((fundRequestModels.get(position).getStatus())));
        holder.txt_regDate.setText(new StringBuilder("Date: ").append(fundRequestModels.get(position).getRegDate()));
        holder.txt_approvedDate.setText(new StringBuilder("Date: ").append(fundRequestModels.get(position).getApprovedDate()));
        holder.txt_trans_id.setText(new StringBuilder(fundRequestModels.get(position).getTransId()));

        if(common.currentServerUser.getLevel().equals("1") || common.currentServerUser.getLevel().equals("11")){
            holder.bnt_rej.setVisibility(View.INVISIBLE);
            holder.bnt_accpt.setVisibility(View.INVISIBLE);
        }else if(!common.currentServerUser.getStaffId().equals(fundRequestModels.get(position).getStaffId()) || common.currentServerUser.getLevel().equals("2") || common.currentServerUser.getLevel().equals("3") || common.currentServerUser.getLevel().equals("4")) {
           if(!common.STAFF_SIGN_DETAILS.getStaffId().contains(fundRequestModels.get(position).getStaffId())){
               holder.bnt_rej.setVisibility(View.VISIBLE);
               holder.bnt_accpt.setVisibility(View.VISIBLE);
           }else {
               holder.bnt_rej.setVisibility(View.INVISIBLE);
               holder.bnt_accpt.setVisibility(View.INVISIBLE);
           }


        }
        if(fundRequestModels.get(position).getStatus().equals("Pending")){
            holder.img_marked.setVisibility(View.INVISIBLE);
            holder.img_paid.setVisibility(View.INVISIBLE);
            holder.img_rejected.setVisibility(View.INVISIBLE);
        }else if(fundRequestModels.get(position).getStatus().equals("approved")){
            holder.img_marked.setVisibility(View.VISIBLE);
            holder.img_paid.setVisibility(View.INVISIBLE);
            holder.img_rejected.setVisibility(View.INVISIBLE);
        }else if(fundRequestModels.get(position).getStatus().equals("disapproval")){
            holder.img_marked.setVisibility(View.INVISIBLE);
            holder.img_paid.setVisibility(View.INVISIBLE);
            holder.img_rejected.setVisibility(View.VISIBLE);
        }else if(fundRequestModels.get(position).getStatus().equals("paid")){
            holder.img_marked.setVisibility(View.VISIBLE);
            holder.img_paid.setVisibility(View.VISIBLE);
            holder.img_rejected.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(view -> {
            // Toast.makeText(context, fixedCustomerModelsList.get(position).getPushKey(), Toast.LENGTH_SHORT).show();
            showRequest();
        });

        holder.bnt_accpt.setOnClickListener(v -> {
            if(selectedSecSupervisor!=null){

            for(ServerUserModel staffs: common.LOAD_ADMIN_STAFFS){
                if(staffs.getName().matches(selectedSecSupervisor)){
                    supervisorId=staffs.getStaffId();
                    supervisorKey=staffs.getUid();
                }
            }
                updateStatus(position,getRequest("approved",common.STAFF_SIGN_DETAILS.getLevel(),common.STAFF_SIGN_DETAILS.getLevel(),selectedSecSupervisor,"None"));
                saveSuperVisors(supervisorKey,fundRequestModels.get(position).getUId());
                //set important values
                myStatus="approved";
                writeToLog(position,fundRequestModels.get(position));

                fundRequestModels.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, fundRequestModels.size());
                notifyDataSetChanged();
            }else {
                Toast.makeText(context, "Kindly assign this request to line manger", Toast.LENGTH_LONG).show();
            }


        });



       holder.bnt_rej.setOnClickListener(v -> {
          updateStatus(position,getRequest("disapproval",common.STAFF_SIGN_DETAILS.getLevel(),common.STAFF_SIGN_DETAILS.getLevel(),"cancel","cancel"));
           myStatus="disapproval";
           writeToLog(position,fundRequestModels.get(position));
           fundRequestModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fundRequestModels.size());
            notifyDataSetChanged();


        });


    }

    private void showRequest(){
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("FUND REQUEST");
      //  builder.setMessage("Review Request");

        //get supervisor
        getSupervisor();

        @SuppressLint("InflateParams") View itemView= LayoutInflater.from(context).inflate(R.layout.layout_approval,null);
        edt_second_Line=itemView.findViewById(R.id.edt_second_Line);


        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
             dialogInterface.dismiss();
        })
                .setPositiveButton("APPLY", (dialogInterface, i) -> {
                    //common
                    selectedSecSupervisor=edt_second_Line.getSelectedItem().toString();
                    dialogInterface.dismiss();
                });


        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog registerDialog= builder.create();
        registerDialog.show();
    }


    private void getSupervisor(){
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
                                    if (useryModel.getLevel().equals("3")){
                                        tempList.add(useryModel.getName());
                                    }

                                }
                             dialog.dismiss();
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, tempList);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                edt_second_Line.setAdapter(dataAdapter);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "fail to save supervisor for requester", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Saved successfully to supervisors for requester", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private fundRequestModel getRequest(String status,String posRequest,String approvalLevel,String curretSignedTo,String secondLeavelApproval){
        fundRequestModel requestModel=new fundRequestModel();
        requestModel.setStatus(status);
        requestModel.setPosRequest(posRequest);
        requestModel.setApprovalLevel(approvalLevel);
        requestModel.setCurretSignedTo(curretSignedTo);
        requestModel.setSecondLevelApprovalBy(secondLeavelApproval);

        return requestModel;
    }

    private void writeToLog(int pos,fundRequestModel model){
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,Object> writeLog=new HashMap<>();
        writeLog.put("title",model.getTitle());
        writeLog.put("amount",model.getAmount());
        writeLog.put("staffId",model.getStaffId());
        writeLog.put("detail",model.getDetail());
        writeLog.put("curretSignedTo",selectedSecSupervisor);
        writeLog.put("firstLevelApprovalBy",common.STAFF_SIGN_DETAILS.getStaffName());
        writeLog.put("SecondLevelApprovalBy",model.getSecondLevelApprovalBy());
        writeLog.put("status",myStatus);
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
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateStatus(int pos, fundRequestModel model){
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,Object> oData=new HashMap<>();
        oData.put("status",model.getStatus());
        oData.put("posRequest",model.getPosRequest());
        oData.put("approvalLevel",model.getApprovalLevel());
        oData.put("approvedDate",DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime));
        oData.put("curretSignedTo",model.getCurretSignedTo());
        oData.put("SecondLevelApprovalBy",model.getSecondLevelApprovalBy());
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUEST)
                .child(fundRequestModels.get(pos).getUId())
                .child(fundRequestModels.get(pos).getPushKey())
                .updateChildren(oData)
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "faid to accept request", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(unused -> {
            Toast.makeText(context, "request has been "+myStatus, Toast.LENGTH_SHORT).show();
            requestViewModel.loadFundRequest();
        });
        //this would refresh yhe view and reload the request records


    }

    @Override
    public int getItemCount() {
        return fundRequestModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_amount,txt_note,txt_status,txt_regDate,txt_approvedDate,txt_trans_id;
        ImageView img_marked,img_rejected,img_paid;
        Button bnt_accpt,bnt_rej;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_amount= itemView.findViewById(R.id.txt_amount);
            txt_note=itemView.findViewById(R.id.txt_note);
            txt_status=itemView.findViewById(R.id.txt_status);
            txt_regDate=itemView.findViewById(R.id.txt_regDate);
            txt_approvedDate=itemView.findViewById(R.id.txt_approvedDate);
            bnt_rej=itemView.findViewById(R.id.bnt_rej);
            bnt_accpt=itemView.findViewById(R.id.bnt_accpt);
            img_marked=itemView.findViewById(R.id.img_marked);
            img_rejected=itemView.findViewById(R.id.img_rejected);
            img_paid=itemView.findViewById(R.id.img_paid);
            txt_trans_id=itemView.findViewById(R.id.txt_trans_id);




            // ButterKnife.bind(this, itemView);
        }
    }
}
