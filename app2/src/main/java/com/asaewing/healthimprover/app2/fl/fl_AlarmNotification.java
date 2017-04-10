package com.asaewing.healthimprover.app2.fl;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asaewing.healthimprover.app2.Others.CallAlarmReceiver;
import com.asaewing.healthimprover.app2.R;

import java.util.Calendar;

/**
 * Created by ken on 2016/10/20.
 */
public class fl_AlarmNotification extends RootFragment {

    private MediaPlayer mp = new MediaPlayer();
    private Vibrator vibrator;
    private PowerManager.WakeLock mWakelock;


    public fl_AlarmNotification() {
        // Required empty public constructor
    }

    public static fl_AlarmNotification newInstance() {
        fl_AlarmNotification fragment = new fl_AlarmNotification();


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
        //getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
        //Window win = getActivity().getWindow();
        //WindowManager.LayoutParams winParams = win.getAttributes();
        //winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        //        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
        //        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        startMedia();
        startVibrator();
        createDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fl_test02, container, false);

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
        acquireWakeLock();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseWakeLock();
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

    private void createDialog() {
        new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.clock)
                .setTitle("鬧鐘提醒時間到了!!!")
                .setMessage("設定時間到了哦!!!!!!")
                .setPositiveButton("延後10分鐘", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        tenMRemind();
                        mp.stop();
                        vibrator.cancel();

                        //Intent intent = new Intent();
                        //設定要start的Avtivity，第一個參數是現在的Activity，第二個參數是要開啟的Activity
                        //intent.setClass(getActivity(), MainActivity.class);
                        //開啟另一個Activity
                        //startActivity(intent);

                        //getActivity().finish();
                    }
                })
                .setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mp.stop();
                        vibrator.cancel();

                        //Intent intent = new Intent();
                        //設定要start的Avtivity，第一個參數是現在的Activity，第二個參數是要開啟的Activity
                        //intent.setClass(getActivity(), MainActivity.class);
                        //開啟另一個Activity
                        //startActivity(intent);

                        //getActivity().finish();
                    }
                }).show();
    }
    private void tenMRemind(){
        //设置时间
        Calendar calendar_now =Calendar.getInstance();


        calendar_now.setTimeInMillis(System.currentTimeMillis());
        calendar_now.set(Calendar.HOUR_OF_DAY, calendar_now.get(Calendar.HOUR_OF_DAY));
        calendar_now.set(Calendar.MINUTE, calendar_now.get(Calendar.MINUTE)+10);
        calendar_now.set(Calendar.SECOND, 0);
        calendar_now.set(Calendar.MILLISECOND, 0);

        //时间选择好了
        Intent intent = new Intent(getActivity(), CallAlarmReceiver.class);
        //注册闹钟广播
        PendingIntent sender = PendingIntent.getBroadcast(
                getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am;
        am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar_now.getTimeInMillis(), sender);
    }

    /**
     * 开始播放铃声
     */
    private void startMedia() {
        try {
            mp.setDataSource(getActivity(),
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 震动
     */
    private void startVibrator() {
        /**
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         *
         */
        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 500, 1000, 500, 1000 }; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, 0);
    }

    /**
     * 唤醒屏幕
     */
    private void acquireWakeLock() {
        if (mWakelock == null) {
            PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
            mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass()
                    .getCanonicalName());
            mWakelock.acquire();
        }
    }

    /**
     * 释放锁屏
     */
    private void releaseWakeLock() {
        if (mWakelock != null && mWakelock.isHeld()) {
            mWakelock.release();
            mWakelock = null;
        }
    }

}