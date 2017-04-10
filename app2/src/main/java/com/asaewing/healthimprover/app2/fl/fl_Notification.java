package com.asaewing.healthimprover.app2.fl;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.CallAlarmReceiver;
import com.asaewing.healthimprover.app2.R;

import java.util.Calendar;


/**
 * Created by ken on 2016/9/29.
 */
public class fl_Notification extends RootFragment {

    private final Calendar calendar=Calendar.getInstance();
    private String Data = "Note";

    private TextView mDate;
    private Button mSetting;
    private Button mCancle;


    public fl_Notification() {
        // Required empty public constructor
    }

    public static fl_Notification newInstance() {
        fl_Notification fragment = new fl_Notification();

        return fragment;
    }

    //TODO----Data----
    @Override
    public void mSaveState() {

    }

    //TODO----生命週期----
    @Override
    public void onAttach(Context context) {
        TAG = getClass().getSimpleName();

        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.alarm_activity_main, container, false);
        mDate = (TextView) rootView.findViewById(R.id.date);
        mSetting = (Button) rootView.findViewById(R.id.setting);
        mCancle = (Button) rootView.findViewById(R.id.cancle);

        //取得活动的Preferences对象
        SharedPreferences settings = getActivity().getSharedPreferences(Data,Activity.MODE_PRIVATE);
        String str = settings.getString("TIME1", "");
        mDate.setText(str);

        //设置闹钟
        mSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                calendar.setTimeInMillis(System.currentTimeMillis());
                int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute=calendar.get(Calendar.MINUTE);

                new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener(){
                            public void onTimeSet(TimePicker view,int hourOfDay,
                                                  int minute){
                                // 设置时间
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);

                                //广播跳转
                                Intent intent = new Intent(getActivity(), CallAlarmReceiver.class);
                                //启动一个广播
                                PendingIntent sender=PendingIntent.getBroadcast(
                                        getActivity(),0, intent, 0);
                                //创建闹钟
                                AlarmManager am;
                                am = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                                am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);

                                String tmpS=format(hourOfDay)+"："+format(minute);
                                mDate.setText(tmpS);

                                //SharedPreferences保存数据，并提交
                                SharedPreferences time1Share = getActivity().getSharedPreferences(Data,0);
                                SharedPreferences.Editor editor = time1Share.edit();
                                editor.putString("TIME1", tmpS);
                                editor.apply();

                                Toast.makeText(getActivity(),"設置鬧鐘時間為"+tmpS,Toast.LENGTH_SHORT).show();
                            }
                        },mHour,mMinute,false).show();
            }
        });

        mCancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                //取消
                //TODO 這裡出問題
                Intent intent = new Intent(getActivity(), CallAlarmReceiver.class);//TODO 這裡出問題
                PendingIntent sender=PendingIntent.getBroadcast(
                        getActivity(),0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager am;
                am =(AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
                am.cancel(sender);
            }
        });

        String tmpHi = "來設定提醒時間吧！";
        assert MainActivity2.HiCard_Text != null;
        MainActivity2.HiCard_Text.start(tmpHi);

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    
    private String format(int x){
        String s=""+x;
        if(s.length()==1) s="0"+s;
        return s;
    }
    
}

