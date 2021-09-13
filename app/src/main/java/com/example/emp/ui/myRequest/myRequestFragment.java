package com.example.emp.ui.myRequest;

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
import com.example.emp.adapter.fundRequetAdapter;
import com.example.emp.common.MySwiperHelper;
import com.example.emp.common.common;
import com.example.emp.model.EventBus.ToastEvent;
import com.example.emp.model.EventBus.UpdateCustomerEvent;
import com.example.emp.model.StaffUserModel;
import com.example.emp.model.fundRequestModel;
import com.example.emp.ui.Request.RequestViewModel;
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

public class myRequestFragment extends Fragment {


    private myRequestViewModel requestViewModel1;
    private AlertDialog dialog;
    private Unbinder unbinder;


    private List<fundRequestModel> fundRequestModels;
    fundRequetAdapter adapter;
    private LayoutAnimationController layoutAnimationController;


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
        requestViewModel1 = new ViewModelProvider(this).get(myRequestViewModel.class);
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