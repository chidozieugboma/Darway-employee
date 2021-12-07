package com.example.emp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.emp.R;
import com.example.emp.model.TamperModel;
import com.example.emp.model.staff_FingerPrint;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class staffFingerAdapter extends RecyclerView.Adapter<staffFingerAdapter.MyViewHolder>{
    private final Context context;
    private final List<staff_FingerPrint> tamperModels;

    public staffFingerAdapter(Context context, List<staff_FingerPrint> tamperModelList) {
        this.context = context;
        this.tamperModels = tamperModelList;
    }

    @NonNull
    @Override
    public staffFingerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate( R.layout.fragment_staff_sign_in,parent,false);
        return new staffFingerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull staffFingerAdapter.MyViewHolder holder, int position) {
        holder.txt_dept.setText(tamperModels.get(position).getdept());
        if(tamperModels.get(position).getCurrentLocation() != null)
        holder.txt_Location.setText(tamperModels.get(position).getCurrentLocation());
        holder.txt_regDate.setText(new StringBuilder(" Sign Time  ").append(String.valueOf(convertDateTime(tamperModels.get(position).getSignDate()))));
        holder.txt_Onleave.setText(String.valueOf(tamperModels.get(position).getOnLeave()));
        holder.txt_staffName.setText(tamperModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return tamperModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_staffName,txt_Location,txt_dept,txt_Onleave,txt_regDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_dept=itemView.findViewById(R.id.txt_dept);
            txt_staffName=itemView.findViewById(R.id.txt_staffName);
            txt_Onleave=itemView.findViewById(R.id.txt_Onleave);
            txt_regDate=itemView.findViewById(R.id.txt_regDate);
            txt_Location=itemView.findViewById(R.id.txt_Location);


        }
    }

    private Date convertDateTime(String date1){
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");

        String dateInString = "Wed Mar 14 15:30:00 EET 2018";

        SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy");


        try {

          date = formatter.parse(date1);
           // System.out.println(date);
           // System.out.println(formatterOut.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
