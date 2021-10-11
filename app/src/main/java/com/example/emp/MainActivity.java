package com.example.emp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.emp.Remote.ICloudFunctions;
import com.example.emp.Remote.RetrofitICloudClient;
import com.example.emp.common.common;
import com.example.emp.model.CompanyModel;
import com.example.emp.model.StaffUserModel;
import com.example.emp.model.departmentModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.installations.FirebaseInstallations;
//import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import dmax.dialog.SpotsDialog;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {
    private static int APP_REQUEST_CODE=1717;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private DatabaseReference serverRef;
    private List<AuthUI.IdpConfig> providers;
    private ICloudFunctions cloudFunctions;
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    Spinner edt_dept;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if(listener !=null)
            firebaseAuth.removeAuthStateListener(listener);
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        providers= Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
        firebaseAuth=FirebaseAuth.getInstance();
        dialog=new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        cloudFunctions= RetrofitICloudClient.getInstance().create(ICloudFunctions.class);
        listener=firebaseAuthLocal ->{
            FirebaseUser user=firebaseAuthLocal.getCurrentUser();
            if(user !=null){
                checkUserFromFirebase(user);
            }else{
                PhoneLogin();
            }

        };
    }

    private void checkUserFromFirebase(FirebaseUser user) {
        dialog.show();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_LOGIN)
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //  Toast.makeText(MainActivity.this, "You are already resisted", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().getCurrentUser()
                                    .getIdToken(true)
                                    .addOnFailureListener(e -> Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show())
                                    .addOnCompleteListener(tokenResultTask -> {
                                        common.authorizeKey= tokenResultTask.getResult().getToken();

                                        dialog.dismiss();

                                        StaffUserModel userModel=dataSnapshot.getValue(StaffUserModel.class);
                                        common.currentCompany= Objects.requireNonNull(userModel).getCompanyId();
                                        common.STAFF_SIGN_DETAILS=userModel;
                                        common.currentServerUser=userModel;
                                        goToHomeActivity(userModel);

                                    });

                        }else {
                            showRegistrationDialog(user);
                            dialog.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i("DozieError",databaseError.getDetails());

                    }
                });
    }

    private void showRegistrationDialog(FirebaseUser user) {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Staff Registration");
        builder.setMessage("Please fill information");

        @SuppressLint("InflateParams") View itemView= LayoutInflater.from(this).inflate(R.layout.activity_staff_register,null);
        EditText edt_staff_Name = itemView.findViewById(R.id.edt_staff_Name);
        EditText edt_staff_number =itemView.findViewById(R.id.edt_staff_number);
        EditText edt_designation=itemView.findViewById(R.id.edt_designation);
        EditText edt_vendor_Phone= itemView.findViewById(R.id.edt_vendor_Phone);
        edt_dept= itemView.findViewById(R.id.edt_dept);

        //load dept
        loadDept();

        //set Data
        edt_vendor_Phone.setText(user.getPhoneNumber());
        builder.setNegativeButton("CANCEL", (DialogInterface dialogInterface, int i) -> {
            dialogInterface.dismiss();
        }).setPositiveButton("REGISTER", (dialogInterface, i) -> {
            if(TextUtils.isEmpty(edt_staff_Name.getText().toString()) || TextUtils.isEmpty(edt_staff_number.getText().toString())){
                Toast.makeText(MainActivity.this, "Please enter your name and staff id ", Toast.LENGTH_LONG).show();
                return;
            }
            StaffUserModel staffUserModel=new StaffUserModel();
            staffUserModel.setUid(user.getUid());
            staffUserModel.setStaffName(edt_staff_Name.getText().toString());
            staffUserModel.setPhone(edt_vendor_Phone.getText().toString());
            staffUserModel.setAcctType("staff account");
            staffUserModel.setCompanyId(common.currentCompany); //d833505
            staffUserModel.setCompanyName("Unknown");
            staffUserModel.setLevel("1"); // this id is for ordinary staffs
            staffUserModel.setStaffId(edt_staff_number.getText().toString());
            staffUserModel.setDesignation(edt_designation.getText().toString());
            staffUserModel.setDept(edt_dept.getSelectedItem().toString());
            staffUserModel.setOnLeave(false);

            registerUser(staffUserModel);

            dialog.show();

            FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                    .child(common.currentCompany)
                    .child(common.STAFF_ACCOUNT)
                    .child(common.STAFF_LOGIN)
                    .child(staffUserModel.getUid())
                    .setValue(staffUserModel)
                    .addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnCompleteListener(task -> {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, "Congratulations !! Registration was successful", Toast.LENGTH_SHORT).show();
                //gotoHomeActivity(serverUserModel);

            });

        });

        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog registerDialog= builder.create();
        registerDialog.show();

    }

    private void registerUser(StaffUserModel userModel ){
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_LOGIN)
                .child(userModel.getUid())
                .setValue(userModel)
                .addOnCompleteListener(task -> {

                }).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Failed to create users", Toast.LENGTH_SHORT).show());

    }

    private void loadDept(){
        List<String> tempList = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.DEPARTMENT)
                .child("Department")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            for(DataSnapshot itemsapshot: snapshot.getChildren()){
                                departmentModel depatModel = itemsapshot.getValue(departmentModel.class);
                                if (depatModel != null) {
                                    depatModel.setKey(itemsapshot.getKey());
                                }
                                if (depatModel != null) {
                                    tempList.add(depatModel.getName());
                                }
                                // Creating adapter for spinner
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity. this, android.R.layout.simple_spinner_item, tempList);

                                // Drop down layout style - list view with radio button
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // attaching data adapter to spinner
                                edt_dept.setAdapter(dataAdapter);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void goToHomeActivity(StaffUserModel userModel) {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    common.currentServerUser=userModel; // Important ! always needs to assign a value before use
                    //start activity soon
                    if(common.currentCompany.equals("Unknown")){
                        Toast.makeText(this, "Kindly Contact Darway coast to activate your account", Toast.LENGTH_SHORT).show();
                    }else {
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        finish();
                    }

                }).addOnCompleteListener(task -> {
            common.currentServerUser=userModel; // Important ! always needs to assign a value before use
            //start activity soon
            common.updateToken(MainActivity.this, (task.getResult()).getToken(),false,false);
            if(common.currentCompany.equals("Unknown")) {
                Toast.makeText(this, "Kindly Contact Darway coast to activate your account", Toast.LENGTH_SHORT).show();
                showCodeDialog();
            }else {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                finish();
            }


        });





    }

 /*   private void goToHomeActivity(StaffUserModel userModel) {
        FirebaseInstanceId.getInstance()
                .getToken(true)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    common.currentServerUser=userModel; // Important ! always needs to assign a value before use
                    //start activity soon
                    if(common.currentCompany.equals("Unknown")) {
                        Toast.makeText(this, "Kindly Contact Darway coast to activate your account", Toast.LENGTH_SHORT).show();
                        showCodeDialog();
                    }else {
                        Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }


                }).addOnCompleteListener(task -> {
            common.currentServerUser=userModel; // Important ! always needs to assign a value before use
            //start activity soon
            common.updateToken(MainActivity.this, (task.getResult()).getToken(),false,false);
            if(common.currentCompany.equals("Unknown")) {
                Toast.makeText(this, "kindly contact administrator to activate your account", Toast.LENGTH_SHORT).show();
                showCodeDialog();
            }else {
                    *//* SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("company",common.currentServerUser.getCompanyName());
                        editor.putString("currentUserId",userModel.getUid());
                        editor.putString("currentUsername",userModel.getName());
                        editor.commit();*//*
                Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

            }

        });

    }*/

    private void showCodeDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Company Code");
        builder.setMessage("Please supply company code");

        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(this).inflate(R.layout.layout_company_code, null);
        EditText edt_companyCode = itemView.findViewById(R.id.edt_companyCode);
        builder.setNegativeButton("CANCEL", (DialogInterface dialogInterface, int i) -> {
            dialogInterface.dismiss();
        }).setPositiveButton("REGISTER", (dialogInterface, i) -> {
            if (TextUtils.isEmpty(edt_companyCode.getText().toString())) {
                Toast.makeText(MainActivity.this, "Please enter your company code", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //  findCompany(edt_companyCode.getText().toString());

                DatabaseReference companyRef=FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                        .child(edt_companyCode.getText().toString());
                companyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            CompanyModel companyModel = snapshot.getValue(CompanyModel.class);
                            if (companyModel != null) {
                                companyModel.setKey(snapshot.getKey());
                            }
                            Map<String,Object> updateData=new HashMap<>();
                            updateData.put("companyId",companyModel.getKey());
                            updateData.put("companyName",companyModel.getCompanyName());
                            updateData.put("accountEnabled",true);
                            updateData.put("companyStatus",true);

                            FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                                    .child(common.currentCompany)
                                    .child(common.STAFF_ACCOUNT)
                                    .child(common.STAFF_LOGIN)
                                    .child(common.currentServerUser.getUid())
                                    .updateChildren(updateData)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "unable to assign to company", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //Do nothing
                                            //Create default account for vendor
                                            //common.STAFF_SIGN_DETAILS=userModel;

                                        }
                                    });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }


        });
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog showDialog= builder.create();
        showDialog.show();
    }

    private void PhoneLogin() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),APP_REQUEST_CODE);
    }

}