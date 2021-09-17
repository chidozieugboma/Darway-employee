package com.example.emp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.emp.common.common;
import com.example.emp.model.EventBus.MenuItemBack;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emp.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private DrawerLayout drawer;
    private int menuClickId=-1;
    NavigationView navigationView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

  /*      binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    /*    drawer = binding.drawerLayout;
        navigationView = binding.navView;*/
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
        R.id.nav_home, R.id.nav_sign_in, R.id.nav_request,R.id.nav_myrequest,R.id.nav_tamper,R.id.nav_signout)
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
                    //showBottomSheetDialog();
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
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onMenuItemBack(MenuItemBack event){
        menuClickId=-1;
        //navController.popBackStack(R.id.nav_home,true);
        if(getSupportFragmentManager().getBackStackEntryCount()>0)
            getSupportFragmentManager().popBackStack();
    }
}