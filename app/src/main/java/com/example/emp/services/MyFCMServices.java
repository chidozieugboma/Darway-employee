package com.example.emp.services;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.emp.common.common;
import com.example.emp.ui.myRequest.myRequestFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFCMServices extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String, String> dataRecv = remoteMessage.getData();

        Log.i("myNi",dataRecv.toString());

            Intent intent = new Intent(this, myRequestFragment.class);
            common.showNotification(this, new Random().nextInt(),
                    dataRecv.get(common.NOTI_TITLE),
                    dataRecv.get(common.NOTI_CONTENT),
                    intent);
        Log.d("DozieNoti", "onMessageReceived: "+common.NOTI_CONTENT);

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        common.updateToken(this, s,false,false);
        // updateTokenToServer(s);
    }

}
