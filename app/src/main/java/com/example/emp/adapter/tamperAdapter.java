package com.example.emp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emp.R;
import com.example.emp.model.TamperModel;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public class tamperAdapter extends RecyclerView.Adapter<tamperAdapter.MyViewHolder> {
    private final Context context;
    private final List<TamperModel> tamperModels;

    public tamperAdapter(Context context, List<TamperModel> tamperModelList) {
        this.context = context;
        this.tamperModels = tamperModelList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate( R.layout.layout_tamper,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_meterNumber.setText(new StringBuilder(tamperModels.get(position).getMeter_Number()));
        holder.txt_status.setText(new StringBuilder().append(tamperModels.get(position).getStatus()));
        holder.txt_regDate.setText(new StringBuilder("Date: ").append(tamperModels.get(position).getRegDate()));


        if(tamperModels.get(position).getMarker1().equals("1") || tamperModels.get(position).getMarker1().equals("11")){
            holder.img_marked1.setVisibility(View.VISIBLE);
        }
        if(tamperModels.get(position).getMarker2().equals("1") || tamperModels.get(position).getMarker2().equals("11")){
            holder.img_marked2.setVisibility(View.VISIBLE);
        }
        if(tamperModels.get(position).getMarker3().equals("1") || tamperModels.get(position).getMarker3().equals("11")) {
            holder.img_marked3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return tamperModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_meterNumber, txt_status, txt_regDate;
        ImageView img_marked1,img_marked2,img_marked3;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_meterNumber = itemView.findViewById(R.id.txt_meterNumber);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_regDate = itemView.findViewById(R.id.txt_regDate);

            //for marks
            img_marked1=itemView.findViewById(R.id.img_marked1);
            img_marked2=itemView.findViewById(R.id.img_marked2);
            img_marked3=itemView.findViewById(R.id.img_marked3);

            img_marked1.setVisibility(View.INVISIBLE);
            img_marked2.setVisibility(View.INVISIBLE);
            img_marked3.setVisibility(View.INVISIBLE);
        }
    }
}
