package com.asaewing.healthimprover.app2.FCM;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by asa on 2016/11/22.
 */

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private String TAG = "FCM_Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "**"+TAG+"**"+"onMessageReceived:"+remoteMessage.getFrom());
        //Log.d(TAG, "**"+TAG+"**"+"onMessageReceived:"+remoteMessage.getNotification().getTitle());
        //Log.d(TAG, "**"+TAG+"**"+"onMessageReceived:"+remoteMessage.getNotification().getBody());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "**"+TAG+"**"+"onMessageReceived:"+"Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "**"+TAG+"**"+"onMessageReceived:"+"Message Notification Body: " + remoteMessage.getNotification().getBody());
            Toast.makeText(getApplication(),remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();
        }
    }
}
