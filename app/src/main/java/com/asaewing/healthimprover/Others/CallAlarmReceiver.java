package com.asaewing.healthimprover.Others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.asaewing.healthimprover.MainActivity;
import com.asaewing.healthimprover.background.AlarmSettingAgain;
import com.asaewing.healthimprover.fl.fl_AlarmNotification;

public class CallAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent alaramIntent = new Intent(context, AlarmSettingAgain.class);
        //Intent alaramIntent = new Intent(context, MainActivity.class);
        //Intent alaramIntent = new Intent(context, fl_AlarmNotification.class);
        alaramIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alaramIntent);
    }

}
