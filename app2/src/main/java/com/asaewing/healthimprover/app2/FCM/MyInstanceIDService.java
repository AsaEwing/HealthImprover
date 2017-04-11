package com.asaewing.healthimprover.app2.FCM;


import android.util.Log;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;



public class MyInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = "FCM_ID";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "**"+TAG+"**"+"Refreshed token: " + refreshedToken);
        //MainActivity.volleyMethod.vpostSend_FCMId(refreshedToken);
        MainActivity2.mInfoMap.IMput(HiDBHelper.KEY_AC_FCM_id,refreshedToken);
        MainActivity2.flag_FCM = true;

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
