package com.example.emp.common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.emp.R;
import com.example.emp.model.Reguestor_Supervisor;
import com.example.emp.model.ServerUserModel;
import com.example.emp.model.StaffUserModel;
import com.example.emp.model.TokenModel;
import com.google.firebase.database.FirebaseDatabase;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    public static final String REQUEST_TYPES ="request_types";
    public static String SUPERVISORID ;
    public static List<ServerUserModel> LOAD_ADMIN_STAFFS;
    public static String MY_REQUESTOR;
    public static List<Reguestor_Supervisor> REQUESTOR;
    public static List<StaffUserModel> LOAD_ALL_REG_STAFFS;
    public static StaffUserModel STAFF_SIGN_DETAILS ;
    public static final String STAFF_TOKENS = "HRTokens";
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

    public static void showNotification(Context context, int id, String title, String content, Intent intent) {
        PendingIntent pendingIntent=null;
        if(intent !=null){
            pendingIntent=PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            String NOTIFICATION_CHANNEL_ID="darway_e360";
            NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
                NotificationChannel notificationChannel=new NotificationChannel(NOTIFICATION_CHANNEL_ID,"darway_e360",NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("darway_e360");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
                notificationChannel.enableVibration(true);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }else {
                    Toast.makeText(context, "Message is null", Toast.LENGTH_SHORT).show();
                    Log.i("Message01","Null");
                }
            }
            NotificationCompat.Builder builder=new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_baseline_circle_notifications_24)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_baseline_circle_notifications_24));
            if(pendingIntent !=null)
                builder.setContentIntent(pendingIntent);
            Notification notification=builder.build();
            if (notificationManager != null) {
                notificationManager.notify(id,notification);
            }else {
                Toast.makeText(context, "Notification is null", Toast.LENGTH_SHORT).show();
                Log.i("Notification01","Null");
            }
        }
    }

    public static String createTopicOrder() {
        return "/topics/"+
                common.SUPERVISORID+
                "_"+
                "New_Fund_Request";
    }

/*    public static String createTopicOrder() {
        return new String("/topics/New_Fund_Request").toString();
    }*/
   public static String formatAmount(double price) {
    if(price !=0){
        DecimalFormat df=new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.UP);
        String finalPrice= df.format(price);
        return finalPrice.replace(" ",".");
    }else
        return "0.00";
}

}
