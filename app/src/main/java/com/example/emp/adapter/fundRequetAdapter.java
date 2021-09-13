package com.example.emp.adapter;





import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.emp.R;
import com.example.emp.common.common;
import com.example.emp.model.EventBus.UpdateRequest;
import com.example.emp.model.fundRequestModel;
import com.example.emp.ui.myRequest.myRequestFragment;
import com.example.emp.ui.myRequest.myRequestViewModel;
import com.google.firebase.database.FirebaseDatabase;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fundRequetAdapter extends RecyclerView.Adapter<fundRequetAdapter.MyViewHolder>{
    private Context context;
    private final List<fundRequestModel> fundRequestModels;
    private myRequestViewModel requestViewModel;
    private UpdateRequest updateAddonModel;

    public fundRequetAdapter(Context context, List<fundRequestModel> fundRequestModels) {
        this.context = context;
        this.fundRequestModels = fundRequestModels;
        updateAddonModel=new UpdateRequest();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View itemView= LayoutInflater.from(context).inflate( R.layout.layout_request,parent,false);
        requestViewModel=new myRequestViewModel();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_amount.setText(new StringBuilder(fundRequestModels.get(position).getTitle()).append("(").append("₦").append(fundRequestModels.get(position).getAmount()).append(")"));
        holder.txt_note.setText(new StringBuilder().append(fundRequestModels.get(position).getDetail()));
        holder.txt_status.setText(new StringBuilder().append((fundRequestModels.get(position).getStatus())));
        holder.txt_regDate.setText(new StringBuilder("Date: ").append(fundRequestModels.get(position).getRegDate()));
        holder.txt_approvedDate.setText(new StringBuilder("Date: ").append(fundRequestModels.get(position).getApprovedDate()));

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

        holder.bnt_accpt.setOnClickListener(v -> {
            updateStatus(position,getRequest("approved",common.STAFF_SIGN_DETAILS.getLevel(),common.STAFF_SIGN_DETAILS.getLevel()));
            fundRequestModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fundRequestModels.size());
            notifyDataSetChanged();
        });



        holder.bnt_rej.setOnClickListener(v -> {
          updateStatus(position,getRequest("disapproval",common.STAFF_SIGN_DETAILS.getLevel(),common.STAFF_SIGN_DETAILS.getLevel()));
            fundRequestModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, fundRequestModels.size());
            notifyDataSetChanged();


        });


    }

    private fundRequestModel getRequest(String status,String posRequest,String approvalLevel){
        fundRequestModel requestModel=new fundRequestModel();
        requestModel.setStatus(status);
        requestModel.setPosRequest(posRequest);
        requestModel.setApprovalLevel(approvalLevel);

        return requestModel;
    }

    private void updateStatus(int pos,fundRequestModel model){
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,Object> oData=new HashMap<>();
        oData.put("status",model.getStatus());
        oData.put("posRequest",model.getPosRequest());
        oData.put("approvalLevel",model.getApprovalLevel());
        oData.put("approvedDate",DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime));
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_REQUEST)
                .child(fundRequestModels.get(pos).getUId())
                .child(fundRequestModels.get(pos).getPushKey())
                .updateChildren(oData)
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "faid to accept request", Toast.LENGTH_SHORT).show();
                }).addOnSuccessListener(unused -> {
            Toast.makeText(context, "request has been approved.", Toast.LENGTH_SHORT).show();
           // requestViewModel.loadFundRequest();
        });
        //this would refresh yhe view and reload the request records


    }

    @Override
    public int getItemCount() {
        return fundRequestModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_amount,txt_note,txt_status,txt_regDate,txt_approvedDate;
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




            // ButterKnife.bind(this, itemView);
        }
    }
}
