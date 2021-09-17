package com.example.emp.common;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emp.model.Reguestor_Supervisor;
import com.example.emp.model.ServerUserModel;
import com.example.emp.model.StaffUserModel;
import com.example.emp.model.TokenModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class common {
    public static final String STAFF_ACCOUNT = "HR";
    public static final String STAFF_LOGIN ="Login_details";
    public static final String SERVER_REF ="Server" ;
    public static final String ATTENTANCE = "Attendance";
    public static final String STAFF_ACCOUNT_ATTENDANCE ="HR_Attendance_Register" ;
    public static final String DEPARTMENT = "HR";
    public static final String TAMPER = "tampers";
    public static final String NOTI_TITLE ="title";
    public static final String NOTI_CONTENT = "content";
    public static final String STAFF_REQUESTORS_SUPERVISORS ="requester_supervisors";
    public static final String STAFF_REQUEST_LOG ="request_log";
    public static List<ServerUserModel> LOAD_ADMIN_STAFFS;
    public static String MY_REQUESTOR;
    public static List<Reguestor_Supervisor> REQUESTOR;
    public static List<StaffUserModel> LOAD_ALL_REG_STAFFS;
    public static StaffUserModel STAFF_SIGN_DETAILS ;
    private static final String STAFF_TOKENS = "HRTokens";
    public static String authorizeKey;
    public static String currentCompany="d833505";
    public static final String COMPANY_REF ="company";
    public static final String STAFF_REQUEST="request";
    public static StaffUserModel currentServerUser;

    public static void updateToken(Context context, String newToken, boolean isServer, boolean isVendor) {
        if(common.currentServerUser !=null){
            FirebaseDatabase.getInstance().getReference(common.COMPANY_REF)
                    .child(common.currentCompany)
                    .child(common.STAFF_ACCOUNT)
                    .child(common.STAFF_TOKENS)
                    .child(common.currentServerUser.getUid())
                    .setValue(new TokenModel(common.currentServerUser.getPhone(), newToken,isServer,isVendor))
                    .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
        }


    }

    public static void setSpanString(String welcome, String name, TextView textView) {
        SpannableStringBuilder builder=new SpannableStringBuilder();
        builder.append(welcome);
        SpannableString spannableString=new SpannableString(name);
        StyleSpan boldSpan=new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(boldSpan,0,name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        textView.setText(builder,TextView.BufferType.SPANNABLE);

    }

}
