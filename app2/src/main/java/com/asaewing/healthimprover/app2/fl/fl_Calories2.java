package com.asaewing.healthimprover.app2.fl;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.CT48;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 */
public class fl_Calories2 extends RootFragment
        implements View.OnClickListener {
    //implements View.OnClickListener, GestureDetector.OnGestureListener

    //private CircularProgressBar CPBar;
    private ProgressBar mProgressBar_In,mProgressBar_Out;
    private TextView mPBtext_In,mPBtext_Out;
    private TextView mPBtext_In_end,mPBtext_Out_end;
    private String mPBstr_In_A = "今日飲食熱量狀況：\n已吃進約 %d 卡，還差 %d 卡";
    private String mPBstr_In_B = "今日飲食熱量狀況：\n已吃進約 %d 卡，超過 %d 卡";
    private String mPBstr_Out_A = "今日運動熱量消耗狀況：\n已消耗約 %d 卡，還差 %d 卡";
    private String mPBstr_Out_B = "今日運動熱量消耗狀況：\n已消耗約 %d 卡，超過 %d 卡";
    private int colorBar_Step = Color.rgb(0x4d, 0xa0, 0xf2)
            ,colorText_Step = Color.rgb(0xff, 0x8c, 0x00)
            ,colorBar_Calories = Color.rgb(0x16, 0x6c, 0xe8)
            ,colorText_Calories = Color.rgb(0xf0, 0x98, 0x00)
            ,colorBackground = Color.rgb(0xec, 0xf8, 0xff);
    private int mCPBar_Flag=1;
    private int mStep=0,mStepMax=1000;
    private int mCalOut=0,mCalOutMax=800;
    private int mCalIn=0,mCalInMax=800;
    private double bee;

    private double Height = 0,Weight = 0;

    private SensorManager mSManager;
    private Sensor mSensor;
    private Vibrator mVibrator;
    private sensorListener mShakeListener;

    //GestureDetector mGestureDetector;
    //MainActivity2.MyOnTouchListener myOnTouchListener;

    private static FloatingActionButton fabCal_Target,fabCal_Sport,fabCal_Recommend;
    private static RelativeLayout fab_Sport_RL,fab_Target_RL,fab_Recommend_RL;
    private static boolean fabFlag = true,fabAnimFlag=false;
    private TextView Target_text;

    //AnimationSet AS_fab = new AnimationSet(false);
    private Animation alphaAnimationShow,alphaAnimationHide;

    //private float fabMoveR,fabMoveR45;

    public fl_Calories2() {
        // Required empty public constructor
    }

    public static fl_Calories2 newInstance() {
        fl_Calories2 fragment = new fl_Calories2();

        return fragment;
    }

    //TODO----Data----
    public double BEE(double Height,double Weight,double Age,boolean isMale){
        //kg,cm
        double bee = 0;
        if (isMale){
            bee = 66.5+13.7*Weight+5*Height-6.8*Age;
        } else {
            bee = 655+9.6*Weight+1.8*Height-4.7*Age;
        }
        return bee;
    }

    @SuppressLint("DefaultLocale")
    private void initValueGet(boolean isFirst){
        int HH = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_H);
        int HL = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_L);
        int WH = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_H);
        int WL = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_L);
        if ((HH + HL*0.01)<10) {
            HH = 165;
            HL = 0;
        }
        if ((WH + WL*0.01)<2) {
            WH = 50;
            HL = 0;
        }
        Height = (float) (HH + HL*0.01);
        Weight = (float) (WH + WL*0.01);


        String strAge = MainActivity2.mInfoMap.IMgetString(HiDBHelper.KEY_AC_Birthday);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendarBirthday = Calendar.getInstance();
        Calendar calendarNow = Calendar.getInstance();
        String today = sdf.format(calendarNow.getTime());
        calendarNow.add(Calendar.DAY_OF_YEAR,-1);
        String yesterday = sdf.format(calendarNow.getTime());
        calendarNow.add(Calendar.DAY_OF_YEAR,1);

        try {
            calendarBirthday.setTime(sdf.parse(strAge));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int intAge = calendarNow.get(Calendar.YEAR)-calendarBirthday.get(Calendar.YEAR);
        boolean isMale = false;
        if (MainActivity2.mInfoMap.IMgetString(HiDBHelper.KEY_AC_Gender).equals("male")){
            isMale = true;
        }

        bee = BEE(Height,Weight,intAge,isMale);
        mCalInMax = (int)((bee+1)*1.2);
        mCalOutMax = (int)((bee+1)/5);

        Calendar c = Calendar.getInstance();
        String time = String.format("%02d:%02d:00"
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE));
        double timeNow48 = new CT48(time).getCt48();

        double tmpWake48 = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_AC_WakeTime48);

        double tmpCalInToday = 0;
        Cursor cursorCalIn = MainActivity2.helper.CalInSelect(today);
        if (cursorCalIn.getCount()>0){
            cursorCalIn.moveToFirst();

            for (int ii=0;ii<cursorCalIn.getCount();ii++){
                String tmpDay = cursorCalIn.getString(cursorCalIn.getColumnIndex(HiDBHelper.KEY_CalIn_Date));
                double tmpTime48 = Double.parseDouble(cursorCalIn.getString(cursorCalIn.getColumnIndex(HiDBHelper.KEY_CalIn_Time48)));

                if (timeNow48<tmpWake48){
                    if (tmpDay.equals(today)
                            && tmpTime48<tmpWake48){
                        double tmpCal = Double.parseDouble(cursorCalIn.getString(cursorCalIn.getColumnIndex(HiDBHelper.KEY_CalIn_oneCal)));
                        tmpCalInToday += tmpCal;
                    }
                    if (tmpDay.equals(yesterday)
                            && tmpTime48>tmpWake48){
                        double tmpCal = Double.parseDouble(cursorCalIn.getString(cursorCalIn.getColumnIndex(HiDBHelper.KEY_CalIn_oneCal)));
                        tmpCalInToday += tmpCal;
                    }
                } else {
                    if (tmpDay.equals(today)
                            && tmpTime48>tmpWake48){
                        double tmpCal = Double.parseDouble(cursorCalIn.getString(cursorCalIn.getColumnIndex(HiDBHelper.KEY_CalIn_oneCal)));
                        tmpCalInToday += tmpCal;
                    }
                }
            }
        }

        if (tmpCalInToday>0) mCalIn = (int)(Math.floor(tmpCalInToday)+1);
        else mCalIn = 0;
        cursorCalIn.close();

        double tmpCalOutToday = 0;
        Cursor cursorCalOut = MainActivity2.helper.CalOutSelect(today);
        if (cursorCalOut.getCount()>0){
            cursorCalOut.moveToFirst();

            for (int ii=0;ii<cursorCalOut.getCount();ii++){
                String tmpDay = cursorCalOut.getString(cursorCalOut.getColumnIndex(HiDBHelper.KEY_CalOut_Date));
                double tmpTime48 = Double.parseDouble(cursorCalOut.getString(cursorCalOut.getColumnIndex(HiDBHelper.KEY_CalOut_Time48)));

                if (timeNow48<tmpWake48){
                    if (tmpDay.equals(today)
                            && tmpTime48<tmpWake48){
                        double tmpCal = Double.parseDouble(cursorCalOut.getString(cursorCalOut.getColumnIndex(HiDBHelper.KEY_CalOut_Cal)));
                        tmpCalOutToday += tmpCal;
                    }
                    if (tmpDay.equals(yesterday)
                            && tmpTime48>tmpWake48){
                        double tmpCal = Double.parseDouble(cursorCalOut.getString(cursorCalOut.getColumnIndex(HiDBHelper.KEY_CalOut_Cal)));
                        tmpCalOutToday += tmpCal;
                    }
                } else {
                    if (tmpDay.equals(today)
                            && tmpTime48>tmpWake48){
                        double tmpCal = Double.parseDouble(cursorCalOut.getString(cursorCalOut.getColumnIndex(HiDBHelper.KEY_CalOut_Cal)));
                        tmpCalOutToday += tmpCal;
                    }
                }
            }
        }

        if (tmpCalOutToday>0) mCalOut = (int)(Math.floor(tmpCalOutToday)+1);
        else mCalOut = 0;
        cursorCalOut.close();

        if (!isFirst){
            mProgressBar_Out.setProgress(mCalOut);
            mProgressBar_In.setProgress(mCalIn);

            int tmpIn = mCalInMax-mCalIn;
            mPBtext_In.setText(String.format(tmpIn > 0 ? mPBstr_In_A:mPBstr_In_B,mCalIn,Math.abs(tmpIn)));
            String tmpStrIn = String.valueOf(mCalIn)+"/"+String.valueOf(mCalInMax)+" Cal";
            mPBtext_In_end.setText(tmpStrIn);

            int tmpOut = mCalOutMax-mCalOut;
            mPBtext_Out.setText(String.format(tmpOut > 0 ? mPBstr_Out_A:mPBstr_Out_B,mCalOut,Math.abs(tmpOut)));
            String tmpStrOut = String.valueOf(mCalOut)+"/"+String.valueOf(mCalOutMax)+" Cal";
            mPBtext_Out_end.setText(tmpStrOut);


        }

        /*if (mStep == 0) mStep = 300;
        if (mCalIn == 0) mCalIn = 600;
        if (mCalOut == 0) mCalOut = 450;*/
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
        //mStep = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Step_before);
        //mCalIn = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_CalIn_before);

        //initValueGet();

        alphaAnimationShow = new AlphaAnimation(0.0F, 1.0F);
        alphaAnimationHide = new AlphaAnimation(1.0F, 0.0F);

        alphaAnimationShow.setDuration(360);
        alphaAnimationHide.setDuration(360);

        //fabMoveR = -MainActivity2.mInfoMap.IMgetFloat("WindowWidth")/5;
        //fabMoveR45 = (float) (Math.cos(Math.PI/8)*fabMoveR);
        //Log.d(TAG, "**" + TAG + "**fabMoveR**" + fabMoveR + "**" + fabMoveR45);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        rootView = inflater.inflate(R.layout.fl_calories2, container, false);

        initValueGet(true);

        //PB in
        mProgressBar_In = (ProgressBar) rootView.findViewById(R.id.Cal_PBIn);
        mProgressBar_In.setIndeterminate(false);
        //mProgressBar_In.getLayoutParams().height = 500;
        mProgressBar_In.setMax(mCalInMax);
        //mProgressBar_In.setProgress(mCalIn);

        mPBtext_In = (TextView) rootView.findViewById(R.id.Cal_textIn);
        int tmpIn = mCalInMax-mCalIn;
        mPBtext_In.setText(String.format(tmpIn > 0 ? mPBstr_In_A:mPBstr_In_B,mCalIn,Math.abs(tmpIn)));

        mPBtext_In_end = (TextView) rootView.findViewById(R.id.Cal_textIn_end);
        String tmpStrIn = String.valueOf(mCalIn)+"/"+String.valueOf(mCalInMax)+" Cal";
        mPBtext_In_end.setText(tmpStrIn);

        //PB out
        mProgressBar_Out = (ProgressBar) rootView.findViewById(R.id.Cal_PBOut);
        mProgressBar_Out.setIndeterminate(false);
        //mProgressBar_Out.getLayoutParams().height = 500;
        mProgressBar_Out.setMax(mCalOutMax);
        //mProgressBar_Out.setProgress(mCalOut);

        mPBtext_Out = (TextView) rootView.findViewById(R.id.Cal_textOut);
        int tmpOut = mCalOutMax-mCalOut;
        mPBtext_Out.setText(String.format(tmpOut > 0 ? mPBstr_Out_A:mPBstr_Out_B,mCalOut,Math.abs(tmpOut)));

        mPBtext_Out_end = (TextView) rootView.findViewById(R.id.Cal_textOut_end);
        String tmpStrOut = String.valueOf(mCalOut)+"/"+String.valueOf(mCalOutMax)+" Cal";
        mPBtext_Out_end.setText(tmpStrOut);

        //Target
        Target_text = (TextView)rootView.findViewById(R.id.Target_text);

        View view = inflater.inflate(R.layout.fab_calories2,null);
        FrameLayout aView = (FrameLayout) getActivity().findViewById(R.id.fl_c_Main_fab);
        aView.addView(view);

        fabCal_Target = (FloatingActionButton) view.findViewById(R.id.cfab_Target);
        fabCal_Target.setOnClickListener(this);
        fabCal_Sport = (FloatingActionButton) view.findViewById(R.id.cfab_Sport);
        fabCal_Sport.setOnClickListener(this);
        fabCal_Recommend = (FloatingActionButton) view.findViewById(R.id.cfab_Recommend);
        fabCal_Recommend.setOnClickListener(this);

        fab_Sport_RL = (RelativeLayout) view.findViewById(R.id.cfab_Sport_RL);
        fab_Target_RL = (RelativeLayout) view.findViewById(R.id.cfab_Target_RL);
        fab_Recommend_RL = (RelativeLayout) view.findViewById(R.id.cfab_Recommend_RL);

        TargetUpdate();

        return rootView;
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

        initValueGet(false);

        mSManager = (SensorManager) getActivity().getSystemService(Service.SENSOR_SERVICE);
        // 震动
        mVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        //监听器
        mShakeListener=new sensorListener();
        // 加速度传感器
        mSManager.registerListener(mShakeListener,
                mSManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                // 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                // 根据不同应用，需要的反应速率不同，具体根据实际情况设定
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //MainActivity2.mInfoMap.IMput(HiDBHelper.KEY_BI_Step_before,mStep);
        //MainActivity2.mInfoMap.IMput(HiDBHelper.KEY_BI_CalIn_before,mCalIn);
    }

    @Override
    public void onStop() {
        super.onStop();

        //MainActivity2.mInfoMap.IMput(HiDBHelper.KEY_BI_Step_before,mStep);
        //MainActivity2.mInfoMap.IMput(HiDBHelper.KEY_BI_CalIn_before,mCalIn);

        mSManager.unregisterListener(mShakeListener);
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
        mSManager.unregisterListener(mShakeListener);
        /*((MainActivity) getActivity())
                .unregisterMyOnTouchListener(myOnTouchListener);*/
    }

    public void fabMainClick() {
        Log.d(TAG, "**" + TAG + "**buttonClick**fabMain******");

        if (!fabFlag) {
            fabClose(true);
        } else {
            fabOpen(true);

            MainActivity2.HiCardPlay("","","今天要做什麼運動呢？");
        }
    }

    public void fabClose(boolean fabMain) {
        long tmpTime2;
        if (!fabFlag) {
            if (fabMain) {
                tmpTime2 = 360;
            } else {
                tmpTime2 = 280;
            }
            fabAnimFlag=true;
            fabCal_Sport.setClickable(false);
            fabCal_Target.setClickable(false);
            fabCal_Recommend.setClickable(false);

            MainActivity2.fabMain.animate().rotationBy(180f)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            MainActivity2.fabMain.setClickable(false);
                            fabCal_Target.setClickable(false);
                            fabCal_Sport.setClickable(false);
                            fabCal_Recommend.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MainActivity2.fabMain.setImageResource(R.drawable.ic_menu_calories);

                            MainActivity2.fabMain.animate().rotationBy(180f)
                                    .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            MainActivity2.fabMain.setClickable(true);
                                            fabCal_Target.setClickable(true);
                                            fabCal_Sport.setClickable(true);
                                            fabCal_Recommend.setClickable(true);
                                            fabAnimFlag=false;
                                            fabFlag = true;
                                            MainActivity2.fabMain.setRotation(0);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

            closeRL(fab_Sport_RL, tmpTime2);
            closeRL(fab_Target_RL, tmpTime2);
            closeRL(fab_Recommend_RL, tmpTime2);

            //fabFlag = true;
        }
    }

    public void fabOpen(boolean fabMain) {
        if (fabFlag) {
            fabAnimFlag = true;
            fabCal_Target.setVisibility(View.VISIBLE);
            fabCal_Sport.setVisibility(View.VISIBLE);
            fabCal_Recommend.setVisibility(View.VISIBLE);
            MainActivity2.fabMain.setClickable(false);
            fabCal_Target.setClickable(false);
            fabCal_Sport.setClickable(false);
            fabCal_Recommend.setClickable(false);

            if (fabMain) {
                MainActivity2.fabMain.animate().rotationBy(180f)
                        .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                //MainActivity2.fabMain.setClickable(false);
                            }

                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                MainActivity2.fabMain.setImageResource(R.drawable.ic_fab_cancel_bk);

                                MainActivity2.fabMain.animate().rotationBy(180f)
                                        .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                //MainActivity2.fabMain.setClickable(false);
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                MainActivity2.fabMain.setClickable(true);
                                                fabCal_Target.setClickable(true);
                                                fabCal_Sport.setClickable(true);
                                                fabCal_Recommend.setClickable(true);

                                                fabFlag = false;
                                                fabAnimFlag=false;
                                                MainActivity2.fabMain.setRotation(0);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
            }

            //fabFlag = false;
            double tmpWidth = MainActivity2.fabMain.getWidth()*1.15;
            openMoveRL(fab_Sport_RL,tmpWidth*1);
            openMoveRL(fab_Target_RL,tmpWidth*2);
            openMoveRL(fab_Recommend_RL,tmpWidth*3);
        }
    }

    @SuppressLint("DefaultLocale")
    public void TargetUpdate(){
        Calendar cNow = Calendar.getInstance();
        Calendar cTar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String targetStr = "";
        int countTarget = 0;
        Cursor cursor = MainActivity2.helper.TargetSelect(HiDBHelper.KEY_Target_Weight);
        if (cursor.getCount()>0){
            String tmpW = "";
            cursor.moveToLast();
            try {
                cTar.setTime(df.parse(cursor.getString(cursor.getColumnIndex(HiDBHelper.KEY_Target_endDate))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int rangeYear = cTar.get(Calendar.YEAR)-cNow.get(Calendar.YEAR);
            int day1 = cNow.get(Calendar.DAY_OF_YEAR);
            int day2 = cTar.get(Calendar.DAY_OF_YEAR);
            int dayDiffW = day2-day1;
            for (int ii=cNow.get(Calendar.YEAR);ii<cNow.get(Calendar.YEAR)+rangeYear;ii++){
                if ((ii%4)==0)dayDiffW += 366;
                else dayDiffW += 365;
            }

            double tmpWeight = Double.parseDouble(cursor.getString(cursor.getColumnIndex(HiDBHelper.KEY_Target_TargetValue)));
            double tmp = tmpWeight;
            tmp -= MainActivity2.mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Weight_before_H);
            tmp -= MainActivity2.mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Weight_before_L)/100;
            tmp = Double.parseDouble(String.format("%.2f",tmp));
            if (tmp>0.2){
                tmpW = String.format("目標體重：%s公斤\n" +
                        "還需增加：%s公斤\n" +
                        "剩下天數：%s"
                        ,String.valueOf(tmpWeight),String.valueOf(tmp),String.valueOf(dayDiffW));
            } else if (tmp<-0.2){
                tmpW = String.format("目標體重：%s公斤\n" +
                        "還需減少：%s公斤\n" +
                        "剩下天數：%s"
                        ,String.valueOf(tmpWeight),String.valueOf(tmp),String.valueOf(dayDiffW));
            } else {
                tmpW = "恭喜達成體重目標！";
            }

            if (countTarget>0){
                targetStr += "\n"+tmpW;
            } else {
                targetStr = tmpW;
            }

            countTarget++;
        }

        cursor = MainActivity2.helper.TargetSelect(HiDBHelper.KEY_Target_Height);
        if (cursor.getCount()>0){
            cursor.moveToLast();
            try {
                cTar.setTime(df.parse(cursor.getString(cursor.getColumnIndex(HiDBHelper.KEY_Target_endDate))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int rangeYear = cTar.get(Calendar.YEAR)-cNow.get(Calendar.YEAR);
            int day1 = cNow.get(Calendar.DAY_OF_YEAR);
            int day2 = cTar.get(Calendar.DAY_OF_YEAR);
            int dayDiffW = day2-day1;
            for (int ii=cNow.get(Calendar.YEAR);ii<cNow.get(Calendar.YEAR)+rangeYear;ii++){
                if ((ii%4)==0)dayDiffW += 366;
                else dayDiffW += 365;
            }

            String tmpH = "";
            double tmpＨeight = Double.parseDouble(cursor.getString(cursor.getColumnIndex(HiDBHelper.KEY_Target_TargetValue)));
            double tmp = tmpＨeight;
            tmp -= MainActivity2.mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Height_before_H);
            tmp -= MainActivity2.mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Height_before_L)/100;
            tmp = Double.parseDouble(String.format("%.2f",tmp));
            if (tmp>0.2){
                tmpH = String.format("目標身高：%s公分\n" +
                                "還需增加：%s公分\n" +
                                "剩下天數：%s"
                        ,String.valueOf(tmpＨeight),String.valueOf(tmp),String.valueOf(dayDiffW));
            } else if (tmp<-0.2){
                tmpH = String.format("目標身高：%s公分\n" +
                                "還需減少：%s公分\n" +
                                "剩下天數：%s"
                        ,String.valueOf(tmpＨeight),String.valueOf(tmp),String.valueOf(dayDiffW));
            } else {
                tmpH = "恭喜達成身高目標！";
            }

            if (countTarget>0){
                targetStr += "\n"+tmpH;
            } else {
                targetStr = tmpH;
            }

            countTarget++;
        }

        if (countTarget==0){
            targetStr = "目前尚無訂立目標";
        }

        Target_text.setText(targetStr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(final View v) {

        switch (v.getId()){

            case R.id.cfab_Sport:
                DialogSport();

                String tmpHi = "記下你剛剛做了什麼運動吧！";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
                break;

            case R.id.cfab_Target:
                DialogTarget();

                tmpHi = "想新增什麼目標嗎？";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
                break;

            case R.id.cfab_Recommend:
                DialogRecommend();

                tmpHi = "你的意見，會使我變得更好！";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    public void DialogTarget(){
        final String[] strFlag = {"w"};
        final int[] tmpH = {0};
        final int[] tmpL = {0};
        final NumberPicker mNP_HW_I,mNP_HW_D;

        AlertDialog.Builder builderTarget = new AlertDialog.Builder(getActivity());
        builderTarget.setTitle("新增目標");
        builderTarget.setView(R.layout.target_dialog);

        builderTarget.setPositiveButton(R.string.Dialog_button_OK2
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double tmpDouble = (double) tmpH[0]+(double) tmpL[0]*0.01;
                        String tmpTarget = "";
                        switch (strFlag[0]) {
                            case "w": {
                                tmpTarget = HiDBHelper.KEY_Target_Weight;
                                /*ContentValues values = new ContentValues();
                                values.put(HiDBHelper.KEY_Target_Target, HiDBHelper.KEY_Target_Weight);
                                values.put(HiDBHelper.KEY_Target_TargetValue, String.valueOf(tmpDouble));
                                MainActivity2.helper.TargetInsert(values);*/
                                break;
                            }
                            case "h": {
                                tmpTarget = HiDBHelper.KEY_Target_Height;
                                /*ContentValues values = new ContentValues();
                                values.put(HiDBHelper.KEY_Target_Target, HiDBHelper.KEY_Target_Height);
                                values.put(HiDBHelper.KEY_Target_TargetValue, String.valueOf(tmpDouble));
                                MainActivity2.helper.TargetInsert(values);*/

                                break;
                            }
                            case "bmi":

                                break;
                        }
                        DialogTarget2(tmpTarget,tmpDouble);
                        //TargetUpdate();

                        //MainActivity2.fabMainClose();
                    }
                });
        builderTarget.setNegativeButton(R.string.Dialog_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity2.fabMainClose();
                    }
                });

        AlertDialog dialogTarget;
        dialogTarget = builderTarget.create();
        dialogTarget.show();

        dialogTarget.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MainActivity2.fabMainClose();
            }
        });

        mNP_HW_I = (NumberPicker) dialogTarget.findViewById(R.id.Target_NP_Int);
        assert mNP_HW_I != null;
        mNP_HW_I.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                String tmpStr = String.valueOf(value);
                if (value < 10) {
                    tmpStr = "0" + tmpStr;
                }
                return tmpStr;
            }
        });
        mNP_HW_I.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tmpH[0] = newVal;
            }
        });
        mNP_HW_I.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });
        mNP_HW_I.setMaxValue(250);
        mNP_HW_I.setMinValue(0);
        mNP_HW_I.setValue(MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_H));

        mNP_HW_D = (NumberPicker) dialogTarget.findViewById(R.id.Target_NP_Dec);
        assert mNP_HW_D != null;
        mNP_HW_D.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                String tmpStr = String.valueOf(value);
                if (value < 10) {
                    tmpStr = "0" + tmpStr;
                }
                return tmpStr;
            }
        });
        mNP_HW_D.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tmpL[0] = newVal;
            }
        });
        mNP_HW_D.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });
        mNP_HW_D.setMaxValue(99);
        mNP_HW_D.setMinValue(0);
        //mNP_HW_D.setValue(MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_L));
        mNP_HW_D.setValue(0);

        RadioGroup rGroup = (RadioGroup)dialogTarget.findViewById(R.id.Target_Group1);

        assert rGroup != null;
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    switch (checkedId) {
                        case R.id.Target_Height_RB:
                            mNP_HW_I.setValue(MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_H));
                            mNP_HW_D.setValue(MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_L));
                            strFlag[0] = "h";
                            break;
                        case R.id.Target_Weight_RB:
                            mNP_HW_I.setValue(MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_H));
                            mNP_HW_D.setValue(MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_L));
                            strFlag[0] = "w";
                            break;
                        case R.id.Target_BMI_RB:
                            double height = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_H);
                            height += MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_L)*0.01;
                            double weight = MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_H);
                            weight += MainActivity2.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_L)*0.01;
                            float tmp = (float) (weight/Math.pow(height*0.01,2));
                            tmp = new BigDecimal(tmp).setScale(2, RoundingMode.HALF_UP).floatValue();
                            //String tmpDF = new DecimalFormat("#.00").format(tmp);
                            mNP_HW_I.setValue((int)Math.floor(tmp));
                            mNP_HW_D.setValue((int)((tmp-Math.floor(tmp))*100));
                            strFlag[0] = "bmi";
                            break;
                    }
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void DialogTarget2(final String target, final double targetValue){
        final int[] rangeDayEdit = {30};

        final Calendar c = Calendar.getInstance();
        final String startDay = String.format("%04d-%02d-%02d",c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,c.get(Calendar.DAY_OF_MONTH));
        final String startTime = String.format("%02d:%02d:00"
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE));
        final String[] endDay = {""};

        c.add(Calendar.DAY_OF_YEAR,rangeDayEdit[0]);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        String strDate = String.format("%04d/%02d/%02d",c.get(Calendar.YEAR), month,c.get(Calendar.DAY_OF_MONTH));

        endDay[0] = String.format("%04d-%02d-%02d",c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,c.get(Calendar.DAY_OF_MONTH));

        AlertDialog.Builder builderTarget = new AlertDialog.Builder(getActivity());
        builderTarget.setTitle("選擇目標時間");

        builderTarget.setView(R.layout.target_dialog2);

        builderTarget.setPositiveButton(R.string.Dialog_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ContentValues values = new ContentValues();
                        values.put(HiDBHelper.KEY_Target_initDate, startDay);
                        values.put(HiDBHelper.KEY_Target_initTime, startTime);
                        values.put(HiDBHelper.KEY_Target_endDate, endDay[0]);
                        values.put(HiDBHelper.KEY_Target_endTime, "00:00:00");
                        values.put(HiDBHelper.KEY_Target_conDay, String.valueOf(rangeDayEdit[0]));
                        values.put(HiDBHelper.KEY_Target_conHour, String.valueOf(0));

                        values.put(HiDBHelper.KEY_Target_Target, target);
                        values.put(HiDBHelper.KEY_Target_TargetValue, String.valueOf(targetValue));
                        MainActivity2.helper.TargetInsert(values);

                        MainActivity2.volleyMethod.vpostSend_TargetJson(startDay,startTime
                                ,String.valueOf(rangeDayEdit[0]),String.valueOf(0)
                                ,endDay[0],"00:00:00"
                                ,target, String.valueOf(targetValue));

                        TargetUpdate();

                        MainActivity2.fabMainClose();
                    }
                });
        builderTarget.setNegativeButton(R.string.Dialog_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity2.fabMainClose();
                    }
                });

        AlertDialog dialogTarget;
        dialogTarget = builderTarget.create();
        dialogTarget.show();

        dialogTarget.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MainActivity2.fabMainClose();
            }
        });


        final EditText Target_textDay,Target_textTime;
        Target_textDay = (EditText)dialogTarget.findViewById(R.id.Target_textDay);
        Target_textTime = (EditText)dialogTarget.findViewById(R.id.Target_textTime);
        assert Target_textDay != null;
        Target_textDay.setText(strDate);
        assert Target_textTime != null;
        Target_textTime.setText(String.valueOf(rangeDayEdit[0]));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.Target_textDay:
                        final Calendar cFuture = Calendar.getInstance();
                        cFuture.setTime(c.getTime());
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String strDate1 = String.format("%04d/%02d/%02d",year,monthOfYear+1,dayOfMonth);
                                //date[0] = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                                //assert AutoDate != null;
                                Target_textDay.setText(strDate1);

                                strDate1 = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                                @SuppressLint("SimpleDateFormat")
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar cNow = Calendar.getInstance();
                                String today = sdf.format(cNow.getTime());

                                try {
                                    cFuture.setTime(sdf.parse(strDate1));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                int rangeDay = 0;
                                try {
                                    int rangeYear = cFuture.get(Calendar.YEAR)-cNow.get(Calendar.YEAR);
                                    int day1 = cNow.get(Calendar.DAY_OF_YEAR);
                                    int day2 = cFuture.get(Calendar.DAY_OF_YEAR);
                                    rangeDay = day2-day1;
                                    for (int ii=cNow.get(Calendar.YEAR);ii<cNow.get(Calendar.YEAR)+rangeYear;ii++){
                                        if ((ii%4)==0)rangeDay += 366;
                                        else rangeDay += 365;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                rangeDayEdit[0] = rangeDay;
                                Target_textTime.setText(String.valueOf(rangeDayEdit[0]));

                                c.setTime(cFuture.getTime());
                                endDay[0] = strDate1;

                            }
                        }, cFuture.get(Calendar.YEAR), cFuture.get(Calendar.MONTH), cFuture.get(Calendar.DAY_OF_MONTH));

                        datePickerDialog.show();
                        break;
                }
            }
        };

        Target_textDay.setOnClickListener(listener);
        //Target_textTime.setOnClickListener(listener);

        Target_textTime.addTextChangedListener(new TextWatcher() {

            Calendar cNow = Calendar.getInstance();
            Calendar tmpC = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG,"**TextChange1**"+charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG,"**TextChange2**"+charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length()==0){
                    rangeDayEdit[0] = 0;
                } else {
                    rangeDayEdit[0] = Integer.parseInt(editable.toString());
                }

                tmpC.setTime(cNow.getTime());
                tmpC.add(Calendar.DAY_OF_YEAR,rangeDayEdit[0]);
                Target_textDay.setText(sdf.format(tmpC.getTime()));
                endDay[0] = sdf2.format(tmpC.getTime());

                Log.d(TAG,"**TextChange3**"+editable.toString());
            }
        });

    }

    @SuppressLint("DefaultLocale")
    public void DialogSport(){

        final int[] rg_id = {R.id.Sport_RB11};
        double calOut=0;
        final double[] hour = {0,0};
        final RadioGroup radioGroup1,radioGroup2,radioGroup3;
        EditText editTextHour = null;
        EditText editTextMin = null;

        final int RB_Count = 9;
        final int[] RB_List_id = new int[]{R.id.Sport_RB11, R.id.Sport_RB12, R.id.Sport_RB13
                                    , R.id.Sport_RB21, R.id.Sport_RB22, R.id.Sport_RB23
                                    , R.id.Sport_RB31, R.id.Sport_RB32, R.id.Sport_RB33};

        final String[] RB_List_name = new String[]{"慢跑","籃球","排球"
                                            ,"網球","羽球","游泳"
                                            ,"腳踏車","乒乓球","跳舞"};

        final String[] RB_List_KEY = new String[]{HiDBHelper.KEY_CalOut_Sport_Run
                ,HiDBHelper.KEY_CalOut_Sport_Basketball
                ,HiDBHelper.KEY_CalOut_Sport_Volleyball
                ,HiDBHelper.KEY_CalOut_Sport_Tennis
                ,HiDBHelper.KEY_CalOut_Sport_Badminton
                ,HiDBHelper.KEY_CalOut_Sport_Swim
                ,HiDBHelper.KEY_CalOut_Sport_Bike
                ,HiDBHelper.KEY_CalOut_Sport_Pingpong
                ,HiDBHelper.KEY_CalOut_Sport_Dance};

        final double[] RB_List_rate = new double[]{10,8,5.75,9,7.6,9,4,6,6.5};

        final Button AutoDate,AutoTime;
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        final int[] month = {c.get(Calendar.MONTH) + 1};
        String apm = "a.m.";
        if (hourOfDay>12){
            hourOfDay -=12;
            apm = "p.m.";
        }
        String strDate = String.format("%04d/%02d/%02d",c.get(Calendar.YEAR), month[0],c.get(Calendar.DAY_OF_MONTH));
        String strTime = String.format("%02d:%02d %s",hourOfDay,c.get(Calendar.MINUTE),apm);

        final String[] date = {String.format("%04d-%02d-%02d"
                , c.get(Calendar.YEAR)
                , c.get(Calendar.MONTH) + 1
                , c.get(Calendar.DAY_OF_MONTH))};
        final String[] time = {String.format("%02d:%02d:00"
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE))};

        AlertDialog.Builder builderSport = new AlertDialog.Builder(getActivity());
        builderSport.setTitle("紀錄運動");
        builderSport.setView(R.layout.sport_dialog);

        builderSport.setPositiveButton(R.string.Dialog_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String tmp = "";
                        for (int ii=0;ii<RB_Count;ii++){
                            if (rg_id[0]==RB_List_id[ii]){
                                double cal = Weight;
                                double conTime = hour[0]+hour[1];
                                cal = cal*(conTime);
                                cal = cal*RB_List_rate[ii];

                                cal = Double.parseDouble(String.format("%.2f",cal));

                                tmp += RB_List_name[ii];
                                tmp += "**";
                                tmp += conTime;
                                tmp += "**";
                                tmp += cal;

                                Toast.makeText(getActivity(),tmp,Toast.LENGTH_SHORT).show();
                                ContentValues values = new ContentValues();
                                values.put(HiDBHelper.KEY_CalOut_Date,date[0]);
                                values.put(HiDBHelper.KEY_CalOut_Time,time[0]);
                                values.put(HiDBHelper.KEY_CalOut_Time48,String.valueOf(new CT48(time[0]).getCt48()));
                                values.put(HiDBHelper.KEY_CalOut_Sport,RB_List_KEY[ii]);
                                values.put(HiDBHelper.KEY_CalOut_Cal,String.valueOf(cal));
                                values.put(HiDBHelper.KEY_CalOut_Continue,String.valueOf(conTime));
                                MainActivity2.helper.CalOutInsert(values);

                                MainActivity2.volleyMethod.vpostSend_SportJson(date[0],time[0]
                                ,RB_List_KEY[ii],String.valueOf(cal),"","",String.valueOf(conTime),"");

                                initValueGet(false);
                                break;
                            }
                        }

                        MainActivity2.fabMainClose();
                    }
                });
        builderSport.setNegativeButton(R.string.Dialog_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity2.fabMainClose();
                    }
                });

        final AlertDialog dialogSport;
        dialogSport = builderSport.create();
        dialogSport.show();

        dialogSport.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MainActivity2.fabMainClose();
            }
        });

        AutoDate = (Button) dialogSport.findViewById(R.id.Sport_text_Date2);
        AutoTime = (Button) dialogSport.findViewById(R.id.Sport_text_Time2);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Sport_text_Date2:
                        Calendar c1 = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String strDate1 = String.format("%04d/%02d/%02d",year,monthOfYear+1,dayOfMonth);
                                date[0] = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                                assert AutoDate != null;
                                AutoDate.setText(strDate1);
                            }
                        }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));

                        datePickerDialog.show();
                        break;
                    case R.id.Sport_text_Time2:
                        Calendar c2 = Calendar.getInstance();
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                time[0] = String.format("%02d:%02d:00",hourOfDay,minute);
                                String apm = "a.m.";
                                if (hourOfDay>12){
                                    hourOfDay -=12;
                                    apm = "p.m.";
                                }
                                String strTime2 = String.format("%02d:%02d %s",hourOfDay,minute,apm);
                                assert AutoTime != null;
                                AutoTime.setText(strTime2);
                            }
                        }, c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE),false);

                        timePickerDialog.show();
                        break;
                }
            }
        };

        if (AutoDate != null) {
            AutoDate.getBackground().setAlpha(20);
            AutoDate.setClickable(true);
            AutoDate.setOnClickListener(clickListener);
            AutoDate.setText(strDate);
        }
        if (AutoTime != null) {
            AutoTime.getBackground().setAlpha(20);
            AutoTime.setClickable(true);
            AutoTime.setOnClickListener(clickListener);
            AutoTime.setText(strTime);
        }

        //boolean changeRG = false;

        radioGroup1 = (RadioGroup)dialogSport.findViewById(R.id.Sport_Group1);
        radioGroup2 = (RadioGroup)dialogSport.findViewById(R.id.Sport_Group2);
        radioGroup3 = (RadioGroup)dialogSport.findViewById(R.id.Sport_Group3);


        if (radioGroup1 != null && radioGroup2 != null && radioGroup3 != null) {

            RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                    RadioButton radioButton = (RadioButton)dialogSport.findViewById(rg_id[0]);
                    if (radioButton != null) {
                        //radioButton.setChecked(false);
                    }
                    Log.d(TAG,"**RG*********"+checkedId);

                    if (checkedId!=-1){
                        //rg_id[0] = radioGroup.getId();
                        if (radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB11
                                ||radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB12
                                ||radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB13){
                            Log.d(TAG,"**RG**1**");

                            if (rg_id[0]==R.id.Sport_RB21
                                    ||rg_id[0]==R.id.Sport_RB22
                                    ||rg_id[0]==R.id.Sport_RB23){
                                Log.d(TAG,"**RG**12**");
                                radioGroup2.clearCheck();
                            } else if (rg_id[0]==R.id.Sport_RB31
                                    ||rg_id[0]==R.id.Sport_RB32
                                    ||rg_id[0]==R.id.Sport_RB33){
                                Log.d(TAG,"**RG**13**");
                                radioGroup3.clearCheck();
                            }

                        } else if (radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB21
                                ||radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB22
                                ||radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB23){
                            Log.d(TAG,"**RG**2**");

                            if (rg_id[0]==R.id.Sport_RB11
                                    ||rg_id[0]==R.id.Sport_RB12
                                    ||rg_id[0]==R.id.Sport_RB13){
                                Log.d(TAG,"**RG**21**");
                                radioGroup1.clearCheck();
                            } else if (rg_id[0]==R.id.Sport_RB31
                                    ||rg_id[0]==R.id.Sport_RB32
                                    ||rg_id[0]==R.id.Sport_RB33){
                                Log.d(TAG,"**RG**23**");
                                radioGroup3.clearCheck();
                            }

                        } else if (radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB31
                                ||radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB32
                                ||radioGroup.getCheckedRadioButtonId()==R.id.Sport_RB33){
                            Log.d(TAG,"**RG**3**");

                            if (rg_id[0]==R.id.Sport_RB11
                                    ||rg_id[0]==R.id.Sport_RB12
                                    ||rg_id[0]==R.id.Sport_RB13){
                                Log.d(TAG,"**RG**31**");
                                radioGroup1.clearCheck();
                            } else if (rg_id[0]==R.id.Sport_RB21
                                    ||rg_id[0]==R.id.Sport_RB22
                                    ||rg_id[0]==R.id.Sport_RB23){
                                Log.d(TAG,"**RG**32**");
                                radioGroup2.clearCheck();
                            }

                        }

                        rg_id[0] = radioGroup.getCheckedRadioButtonId();

                    } else Log.d(TAG,"**RG**4");

                    Log.d(TAG,"**RG****final*****"+rg_id[0]);

                }
            };

            radioGroup1.setOnCheckedChangeListener(changeListener);
            radioGroup2.setOnCheckedChangeListener(changeListener);
            radioGroup3.setOnCheckedChangeListener(changeListener);
        }

        for (int ii=0;ii<RB_Count;ii++){
            RadioButton radioButton
                    = (RadioButton)dialogSport.findViewById(RB_List_id[ii]);
            if (radioButton != null) {
                radioButton.setText(RB_List_name[ii]);
            }
        }

        editTextHour = (EditText)dialogSport.findViewById(R.id.Sport_edit_hour);
        editTextMin = (EditText)dialogSport.findViewById(R.id.Sport_edit_min);

        if (editTextHour != null) {
            editTextHour.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length()>0){
                        hour[0] = Double.parseDouble(editable.toString());
                    } else hour[0] = 0;
                }
            });
        }

        if (editTextMin != null) {
            editTextMin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.length()>0){
                        hour[1] = Double.parseDouble(editable.toString())/60;
                        hour[1] = Double.parseDouble(String.format("%.2f",hour[1]));
                    } else hour[1] = 0;
                }
            });
        }

    }

    public void DialogRecommend(){
        final String[] tmpStr = {""};
        EditText editText = null;

        AlertDialog.Builder builderRecommend = new AlertDialog.Builder(getActivity());
        builderRecommend.setTitle("意見回饋");
        builderRecommend.setView(R.layout.recommend_dialog);

        builderRecommend.setPositiveButton(R.string.Dialog_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity2.volleyMethod.vpostSend_Recommend(tmpStr[0]);

                        MainActivity2.fabMainClose();
                    }
                });
        builderRecommend.setNegativeButton(R.string.Dialog_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity2.fabMainClose();
                    }
                });

        AlertDialog dialogRecommend;
        dialogRecommend = builderRecommend.create();
        dialogRecommend.show();
        editText = (EditText)dialogRecommend.findViewById(R.id.editTextRecommend);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tmpStr[0] = editable.toString();

            }
        });
    }

    /*@Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG,"**onFling**"+(e2.getY()-e1.getY()));

        if (Math.abs(e2.getY()-e1.getY())>120
                && Math.abs(e2.getX()-e1.getX())<20
                && !fabAnimFlag){

            //CPBar = (CircularProgressBar) rootView.findViewById(R.id.circleProgressbar);

            if (mCPBar_Flag == 1) {
                mCPBar_Flag = 0;
                CPBar.clearAnimation();
                CPBar.setInitProgress(colorBar_Step, colorBackground, colorText_Step
                        , mStepMax, getString(R.string.CBar_Step));
                CPBar.setProgressWithAnimation(mStep);

                String tmpHi = "今天走了" + mStep + "步";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
            } else {
                mCPBar_Flag = 1;
                CPBar.clearAnimation();
                CPBar.setInitProgress(colorBar_Calories, colorBackground, colorText_Calories
                        , mCalOutMax, getString(R.string.CBar_Calories));
                CPBar.setProgressWithAnimation(mCalOut);

                String tmpHi = "今天消耗了" + mCalOut + "卡";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
            }
        }

        return false;
    }*/

    private void closeRL(final RelativeLayout relativeLayout, long Time){
        relativeLayout.animate().alpha(0f)
                .translationY(0f).setDuration(Time)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        relativeLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void openMoveRL(RelativeLayout relativeLayout,double moveY){
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.animate().alpha(1f)
                .translationY(-(float)(moveY))
                .setDuration(360)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }
                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private class sensorListener implements SensorEventListener {
        double tmpDD,tmpDD_before;
        int tmpA=0,tmpB=0,tmpC=0;
        boolean first=false;

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();
            // values[0]:X轴，values[1]：Y轴，values[2]：Z轴
            float[] values = event.values;
            if (sensorType == Sensor.TYPE_ACCELEROMETER) {
                tmpDD_before = tmpDD;
                tmpDD = Math.pow(Math.abs(values[0]),2)
                        +Math.pow(Math.abs(values[1]),2)
                        +Math.pow(Math.abs(values[2]),2);
                tmpDD = Math.sqrt(tmpDD);

                if (tmpDD>tmpDD_before) {
                    tmpA++;
                    first = false;
                    if (tmpB>5) {
                        tmpC++;
                        first = true;
                    }
                    tmpB = 0;

                } else if (tmpDD<tmpDD_before) {
                    tmpB++;
                    first = false;
                    if (tmpA>5) {
                        tmpC++;
                        first = true;
                    }
                    tmpA = 0;
                } else {
                    tmpA = 0;
                    tmpB = 0;
                    first = false;
                }

                if (tmpC>99) {
                    tmpC = 0;
                    first = false;
                }

                if (first && tmpC%2 == 0) {
                    mStep++;
                    //TODO
                    //if (mCPBar_Flag == 0) CPBar.setProgress(mStep);
                    first = false;
                }


                /*if ((Math.abs(values[0]) > 5 || Math.abs(values[1]) > 5 || Math.abs(values[2]) > 5)){
                    //tmpDD >29 ) {
                    //dosomething
                    //Toast.makeText(getActivity(), "(*_*)", Toast.LENGTH_SHORT).show();
                    // 摇动手机后，再伴随震动提示~~
                    mVibrator.vibrate(200);

                    mStep++;
                    if (mCPBar_Flag == 0) CPBar.setProgress(mStep);
                }*/
            }
        }
    }
}
