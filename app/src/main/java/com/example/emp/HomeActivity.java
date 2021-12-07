package com.example.emp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emp.common.common;
import com.example.emp.model.EventBus.MenuItemBack;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emp.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private DrawerLayout drawer;
    private int menuClickId=-1;
    NavigationView navigationView;
    private NavController navController;
    FusedLocationProviderClient fusedLocationProviderClient;
    String Home1,Home2,Home3;
    private Location location;
    String homeLocation;
    private ImageView img_fingerPrint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
        R.id.nav_home, R.id.nav_sign_in, R.id.nav_request,R.id.nav_myrequest,R.id.nav_tamper,R.id.nav_signout,R.id.nav_Attendance)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        View headerView = navigationView.getHeaderView(0);
        TextView txt_user= headerView.findViewById(R.id.txt_user);
        TextView txtCompany= headerView.findViewById(R.id.txtCompany);
        common.setSpanString("Welcome \n",common.currentServerUser.getStaffName(),txt_user); //Copy this function from client App
        txtCompany.setText(new StringBuilder(common.currentServerUser.getCompanyName()).append(" (") .append(common.currentCompany).append(") "));

        startLocationUpdate();
        getLocations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void signOut() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Signout")
                .setMessage("Do you really want to sign out?")
                .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss()).setPositiveButton("OK", (dialogInterface, i) -> {

                    common.STAFF_SIGN_DETAILS=null;
                    common.currentServerUser=null;
                    common.REQUESTOR=null;
                    common.currentCompany=null;
                    common.LOAD_ADMIN_STAFFS=null;
                    FirebaseAuth.getInstance().signOut();


            Intent intent=new Intent(HomeActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        drawer.closeDrawers();
        switch (item.getItemId()){
            case R.id.nav_home:
                if(item.getItemId() !=menuClickId)
                    navController.navigate(R.id.nav_home);

                break;
            case R.id.nav_sign_in:
                if(item.getItemId() !=menuClickId){
                    showFingerDialog();
                    //navController.navigate(R.id.nav_sign_in);

                }

                break;
            case R.id.nav_request:
                if(item.getItemId() !=menuClickId){
                    //showPasswordResetDialog();
                    Log.i("erro02", String.valueOf(item.getItemId()));
                    navController.navigate(R.id.nav_request);
                }

                break;

            case R.id.nav_myrequest:
                if(item.getItemId() !=menuClickId){
                    //showPasswordResetDialog();
                    Log.i("erro03",String.valueOf(item.getItemId()));
                    navController.navigate(R.id.nav_myrequest);
                }

                break;

            case R.id.nav_tamper:
                if(item.getItemId() !=menuClickId){
                    //showPasswordResetDialog();
                    Log.i("erro04", String.valueOf(item.getItemId()));
                    navController.navigate(R.id.nav_tamper);
                }
                break;
            case R.id.nav_Attendance:
                if(item.getItemId() !=menuClickId){
                    //showPasswordResetDialog();
                    Log.i("erro04", String.valueOf(item.getItemId()));
                    navController.navigate(R.id.nav_Attendance);
                }
                break;

            case R.id.nav_signout:
                signOut();
                break;
            default:
                menuClickId=-1;
                break;

        }
        menuClickId=item.getItemId();
        return true;
    }

    private void showFingerDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(this);

        builder.setTitle("Staff Attendance");
        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(this).inflate(R.layout.fragment_finger_print,null);
        builder.setView(itemView);
        androidx.appcompat.app.AlertDialog dialog=builder.create();

        img_fingerPrint=itemView.findViewById(R.id.img_fingerPrint);

        img_fingerPrint.setOnClickListener(view -> {
          if(isLocationEnabled(this)){
              if(homeLocation==null){
                  startLocationUpdate();
                  getLocations();
              }
              saveFinger(homeLocation,common.currentServerUser.getDept(),common.currentServerUser.getStaffName(),"Sign In",false,common.currentServerUser.getPusshkey(),String.valueOf(new Date()));
              saveFingerHistory(homeLocation,common.currentServerUser.getDept(),common.currentServerUser.getStaffName(),"Sign In",false,common.currentServerUser.getPusshkey(),String.valueOf(new Date()));

              dialog.hide();
          }else {
              Toast.makeText(this, "enable Location", Toast.LENGTH_SHORT).show();
          }

        });


        dialog.show();

    }

    private void startLocationUpdate() {
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

    }


    private void getLocations() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            location = task.getResult();
            if(location!=null){
                Geocoder geocoder=new Geocoder(HomeActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        if (address.getAddressLine(0) != null) {
                            homeLocation = address.getAddressLine(0) + ",";
                        }
                        if (address.getLocality() != null) {
                            Home1 = address.getLocality() + ",";
                        }
                        if (address.getCountryName() != null) {
                            Home2 = address.getCountryName() + ",";
                        }
                        if (address.getCountryCode() != null) {
                            Home3 = address.getCountryCode();
                        }
                        homeLocation = homeLocation + Home1 + Home2 + Home3;
                    }
                    //  common.CURRENT_LOCATION_STAFF=homeLocation;
                    Log.i("dozie02",homeLocation);
                    // Log.i("dozie03",addresses.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveFinger(String currentLocation,String dept,String name,String narrative,Boolean OnLeave,String pusshkey,String signDate){
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String,Object> addData=new HashMap<>();
        addData.put("currentLocation",currentLocation);
        addData.put("dept",dept);
        addData.put("name",name);
        addData.put("narrative:",narrative);
        addData.put("onLeave",OnLeave);
        addData.put("pusshkey",pusshkey);
        addData.put("signDate",signDate);
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_ACCOUNT_ATTENDANCE)
                .child(currentuser)
                .setValue(addData)
                .addOnFailureListener(e -> {

                }).addOnSuccessListener(unused -> Toast.makeText(HomeActivity.this, "Sign was successful", Toast.LENGTH_LONG).show());

    }

    private void saveFingerHistory(String currentLocation,String dept,String name,String narrative,Boolean OnLeave,String pusshkey,String signDate){
        Map<String,Object> addData=new HashMap<>();
        addData.put("currentLocation",currentLocation);
        addData.put("dept",dept);
        addData.put("name",name);
        addData.put("narrative:",narrative);
        addData.put("onLeave",OnLeave);
        addData.put("pusshkey",pusshkey);
        addData.put("signDate",signDate);
        FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                .child(common.currentCompany)
                .child(common.STAFF_ACCOUNT)
                .child(common.STAFF_ACCOUNT_ATTENDANCE_HISTORY)
                .push()
                .setValue(addData)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(HomeActivity.this, "Sign was successful", Toast.LENGTH_LONG).show();
            }
        });

    }

    @SuppressWarnings("deprecation")
    public static Boolean isLocationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is a new method provided in API 28
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This was deprecated in API 28
            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return (mode != Settings.Secure.LOCATION_MODE_OFF);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onMenuItemBack(MenuItemBack event){
        menuClickId=-1;
        //navController.popBackStack(R.id.nav_home,true);
        if(getSupportFragmentManager().getBackStackEntryCount()>0)
            getSupportFragmentManager().popBackStack();
    }
}
