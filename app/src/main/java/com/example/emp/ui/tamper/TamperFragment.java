package com.example.emp.ui.tamper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.emp.Callback.ChangeMenuClick;
import com.example.emp.R;
import com.example.emp.adapter.tamperAdapter;
import com.example.emp.common.MySwiperHelper;
import com.example.emp.common.common;
import com.example.emp.model.EventBus.UpdateCustomerEvent;
import com.example.emp.model.TamperModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;


public class TamperFragment extends Fragment {

    private TamperViewModel tamperViewModel;
    private AlertDialog dialog;
    private LayoutAnimationController layoutAnimationController;
    private List<TamperModel> tamperModels;
    tamperAdapter adapter;
    private Unbinder unbinder;
    EditText edt_token1,edt_token2,edt_token3;

    @BindView(R.id.recycleTemper)
    RecyclerView recycleTemper;

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
        tamperViewModel = new ViewModelProvider(this).get(TamperViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tamper, container, false);


        unbinder= ButterKnife.bind(this,root);
        InitView();

        tamperViewModel.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        tamperViewModel.getTamperList().observe(getViewLifecycleOwner(),tamperModelList -> {
            dialog.dismiss();
            tamperModels=tamperModelList;
            adapter=new tamperAdapter(getContext(),tamperModelList);
            recycleTemper.setAdapter(adapter);
            recycleTemper.setLayoutAnimation(layoutAnimationController);
        });

        return root;
    }

    private void InitView() {
        setHasOptionsMenu(true);

        dialog= new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recycleTemper.setLayoutManager(layoutManager);
        recycleTemper.addItemDecoration(new DividerItemDecoration(requireContext(),layoutManager.getOrientation()));

        new MySwiperHelper(getContext(), recycleTemper, 150) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {

                buf.add(new MyButton(getContext(), "TOKEN", 30, 0, Color.parseColor("#4f1123"),
                        pos -> {
                            //   Toast.makeText(getContext(), tamperModels.get(pos).getMeter_Number(), Toast.LENGTH_SHORT).show();
                            //show Token
                            showTokenDialog(tamperModels.get(pos).getMeter_Number(),tamperModels.get(pos).getKey());
                        }));

            }
        };

    }

    private void showTokenDialog(String meter_number,String key) {
        //show token dialog
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Meter Token");

        @SuppressLint("InflateParams") View itemView =LayoutInflater.from(getContext()).inflate(R.layout.layout_token,null);
        edt_token1= itemView.findViewById(R.id.edt_token1);
        edt_token2= itemView.findViewById(R.id.edt_token2);
        edt_token3= itemView.findViewById(R.id.edt_token3);
        //Add code
        builder.setNegativeButton("OK", (dialogInterface, i) -> {

        });

        loadTokens(meter_number,key);

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog=builder.create();
        dialog.show();
    }
    private void loadTokens(String meter,String key){
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(currentuser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot ds:snapshot.getChildren()){
                                TamperModel tamperModel=ds.getValue(TamperModel.class);
                                if(tamperModel.getMeter_Number().equals(meter)){
                                    edt_token1.setText(tamperModel.getToken1());
                                    edt_token2.setText(tamperModel.getToken2());
                                    edt_token3.setText(tamperModel.getToken3());
                                }

                            }
                        }

                        if(!edt_token1.getText().toString().equals("0") ) {
                            updateToken1View(key);
                        }
                        if(!edt_token2.getText().toString().equals("0")) {
                            updateToken2View(key);
                        }
                        if(!edt_token3.getText().toString().equals("0")) {
                            updateToken3View(key);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void updateToken1View(String key){
        Map<String,Object> updateAdd=new HashMap<>();
        updateAdd.put("marker1","11");
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(currentuser)
                .child(key)
                .updateChildren(updateAdd)
                .addOnFailureListener(e -> {

                }).addOnSuccessListener(unused -> Toast.makeText(getContext(), "status updeted", Toast.LENGTH_SHORT).show());


    }

    private void updateToken2View(String key){
        Map<String,Object> updateAdd=new HashMap<>();
        updateAdd.put("marker2","11");
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(currentuser)
                .child(key)
                .updateChildren(updateAdd)
                .addOnFailureListener(e -> {

                }).addOnSuccessListener(unused -> Toast.makeText(getContext(), "status updeted", Toast.LENGTH_SHORT).show());



    }

    private void updateToken3View(String key){
        Map<String,Object> updateAdd=new HashMap<>();
        updateAdd.put("marker3","11");
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(currentuser)
                .child(key)
                .updateChildren(updateAdd)
                .addOnFailureListener(e -> {

                }).addOnSuccessListener(unused -> Toast.makeText(getContext(), "status updeted", Toast.LENGTH_SHORT).show());



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_tamper,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_tamper) {
            showAddTamperDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddTamperDialog() {

        //Add items;
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Meter Tamper");

        @SuppressLint("InflateParams") View itemView =LayoutInflater.from(getContext()).inflate(R.layout.layout_add_tamper,null);
        EditText edt_meter_no= itemView.findViewById(R.id.edt_meter_no);
        //Add code
        builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {

        }).setPositiveButton("SEND",(dialogInterface, i) -> {
            dialog.setMessage("Uploading.....");
            dialog.show();

            dialog.dismiss();
            if(TextUtils.isEmpty(edt_meter_no.getText().toString())){
                Toast.makeText(getContext(), "kindly fill all required fields", Toast.LENGTH_LONG).show();
            }else {
                saveTamper(edt_meter_no.getText().toString(),String.valueOf(accountNumber()),itemView);
            }

        });
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog=builder.create();
        dialog.show();

    }
    private int accountNumber(){
        int max=900000;
        int min=650000;
        Random random = new Random();
        int randomNumber = random.nextInt(max - min) + min;
        return randomNumber;
    }


    private void saveTamper(String meter,String key,View items) {
        String currentuser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        // String pussh= FirebaseDatabase.getInstance().getReference(common.TAMPER).push().getKey();
        Date currentTime = Calendar.getInstance().getTime();
        Map<String,String> addItem=new HashMap<>();
        addItem.put("meter_Number",meter);
        addItem.put("key",key);
        addItem.put("uId",currentuser);
        addItem.put("status","pending");
        addItem.put("marker1","0");
        addItem.put("marker2","0");
        addItem.put("marker3","0");
        addItem.put("Token1","0");
        addItem.put("Token2","0");
        addItem.put("Token3","0");
        addItem.put("regDate", DateFormat.getDateInstance(DateFormat.MEDIUM).format(currentTime));

        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(currentuser)
                .push()
                .setValue(addItem)
                .addOnFailureListener(e -> {
                    /*Toast.makeText(getContext(), "Meter number was not sent successfully. try again!!!",
                            Toast.LENGTH_LONG).show();*/
                    Snackbar.make(getContext(),items, "Meter number was not sent Failed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }).addOnSuccessListener(unused -> {
            Snackbar.make(getContext(),items, "Record sent successfully.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            tamperViewModel.loadTamper();
        });
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onUpdateCustomerActive(UpdateCustomerEvent event){
        Map<String,Object> updateData=new HashMap<>();
        updateData.put("active",event.isActive());
        FirebaseDatabase.getInstance()
                .getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.TAMPER)
                .child(event.getCustomersModel().getKey())
                .updateChildren(updateData)
                .addOnFailureListener(e -> Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show())
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Update state to "+event.isActive(), Toast.LENGTH_SHORT).show());
    }

}