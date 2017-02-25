package com.asaewing.healthimprover.FCM;


import android.util.Log;

import com.asaewing.healthimprover.MainActivity;
import com.asaewing.healthimprover.Others.HiDBHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by asa on 2016/11/22.
 */

public class MyInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = "FCM_ID";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "**"+TAG+"**"+"Refreshed token: " + refreshedToken);
        //MainActivity.volleyMethod.vpostSend_FCMId(refreshedToken);
        MainActivity.mInfoMap.IMput(HiDBHelper.KEY_AC_FCM_id,refreshedToken);
        MainActivity.flag_FCM = true;

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }
}
