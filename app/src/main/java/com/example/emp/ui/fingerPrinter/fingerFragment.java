package com.example.emp.ui.fingerPrinter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
import com.example.emp.adapter.staffFingerAdapter;
import com.example.emp.common.MySwiperHelper;
import com.example.emp.common.common;
import com.example.emp.model.EventBus.UpdateCustomerEvent;
import com.example.emp.model.fundRequestModel;
import com.example.emp.model.staff_FingerPrint;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class fingerFragment extends Fragment {


    private fingerViewModel requestViewModel1;
    private AlertDialog dialog;
    private Unbinder unbinder;


    private List<staff_FingerPrint> staffFingerPrintModels;
    staffFingerAdapter adapter;
    private LayoutAnimationController layoutAnimationController;


    @BindView(R.id.recycleStaffSignin)
    RecyclerView recycleStaffSignin;

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
        requestViewModel1 = new ViewModelProvider(this).get(fingerViewModel.class);
        View root = inflater.inflate(R.layout.layout_staff_sign_in, container, false);

        unbinder= ButterKnife.bind(this,root);
        InitView();

        requestViewModel1.getMessageError().observe(getViewLifecycleOwner(), s -> {
            Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        requestViewModel1.getstaffFingerModelList().observe(getViewLifecycleOwner(),staffFingerPrintModels -> {
            dialog.dismiss();
            staffFingerPrintModels=staffFingerPrintModels;
            adapter=new staffFingerAdapter(getContext(),staffFingerPrintModels);
            recycleStaffSignin.setAdapter(adapter);
            recycleStaffSignin.setLayoutAnimation(layoutAnimationController);
        });


        return root;
    }

    private void InitView() {
        dialog= new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        layoutAnimationController= AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_item_from_left);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recycleStaffSignin.setLayoutManager(layoutManager);
        recycleStaffSignin.addItemDecoration(new DividerItemDecoration(requireContext(),layoutManager.getOrientation()));

        new MySwiperHelper(getContext(), recycleStaffSignin, 150) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buf) {

                buf.add(new MyButton(getContext(), "DETAILS", 30, 0, Color.parseColor("#4f1123"),
                        pos -> {
                            Toast.makeText(getContext(), staffFingerPrintModels.get(pos).getName(), Toast.LENGTH_SHORT).show();

                        }));

            }
        };

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