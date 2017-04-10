package com.asaewing.healthimprover.app2.fl;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 */
public class fl_Calories extends RootFragment
        implements View.OnClickListener {
    //implements View.OnClickListener, GestureDetector.OnGestureListener

    //private CircularProgressBar CPBar;
    private ProgressBar mProgressBar1,mProgressBar2;
    private TextView mPBtext1,mPBtext2;
    private String mPBstrA1 = "已吃進約 %d 卡，還差 %d 卡";
    private String mPBstrB1 = "已吃進約 %d 卡，超過 %d 卡";
    private String mPBstrA2 = "已消耗約 %d 卡，還差 %d 卡";
    private String mPBstrB2 = "已消耗約 %d 卡，超過 %d 卡";
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

    private SensorManager mSManager;
    private Sensor mSensor;
    private Vibrator mVibrator;
    private sensorListener mShakeListener;

    //GestureDetector mGestureDetector;
    //MainActivity2.MyOnTouchListener myOnTouchListener;

    private static FloatingActionButton fabCal_Run,fabCal_Bike,fabCal_Swim;
    private static boolean fabFlag = true,fabAnimFlag=false;

    //AnimationSet AS_fab = new AnimationSet(false);
    private Animation alphaAnimationShow,alphaAnimationHide;

    private float fabMoveR,fabMoveR45;

    public fl_Calories() {
        // Required empty public constructor
    }

    public static fl_Calories newInstance() {
        fl_Calories fragment = new fl_Calories();

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
        double Height = 0,Weight = 0;
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
        Calendar calendaNow = Calendar.getInstance();
        try {
            calendarBirthday.setTime(sdf.parse(strAge));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int intAge = calendaNow.get(Calendar.YEAR)-calendarBirthday.get(Calendar.YEAR);
        boolean isMale = false;
        if (MainActivity2.mInfoMap.IMgetString(HiDBHelper.KEY_AC_Gender).equals("male")){
            isMale = true;
        }

        bee = BEE(Height,Weight,intAge,isMale);
        mCalOutMax = (int)(bee+1);
        mCalInMax = (int)(bee+1);


        if (mStep == 0) mStep = 300;
        if (mCalIn == 0) mCalIn = 600;
        if (mCalOut == 0) mCalOut = 450;

        alphaAnimationShow = new AlphaAnimation(0.0F, 1.0F);
        alphaAnimationHide = new AlphaAnimation(1.0F, 0.0F);

        alphaAnimationShow.setDuration(360);
        alphaAnimationHide.setDuration(360);

        fabMoveR = -MainActivity2.mInfoMap.IMgetFloat("WindowWidth")/5;
        fabMoveR45 = (float) (Math.cos(Math.PI/8)*fabMoveR);
        Log.d(TAG, "**" + TAG + "**fabMoveR**" + fabMoveR + "**" + fabMoveR45);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        rootView = inflater.inflate(R.layout.fl_calories2, container, false);

        //PB1
        mProgressBar1 = (ProgressBar) rootView.findViewById(R.id.Cal_PBIn);
        mProgressBar1.setIndeterminate(false);
        //mProgressBar1.getLayoutParams().height = 500;
        mProgressBar1.setMax(mCalInMax);
        mProgressBar1.setProgress(mCalIn);
        mPBtext1 = (TextView) rootView.findViewById(R.id.Cal_textIn);
        int tmpIn = mCalInMax-mCalIn;
        mPBtext1.setText(String.format(tmpIn > 0 ? mPBstrA1:mPBstrB1,mCalIn,Math.abs(tmpIn)));

        //PB2
        mProgressBar2 = (ProgressBar) rootView.findViewById(R.id.Cal_PBOut);
        mProgressBar2.setIndeterminate(false);
        //mProgressBar2.getLayoutParams().height = 500;
        mProgressBar2.setMax(mCalOutMax);
        mProgressBar2.setProgress(mCalOut);
        mPBtext2 = (TextView) rootView.findViewById(R.id.Cal_textOut);
        int tmpOut = mCalOutMax-mCalOut;
        mPBtext2.setText(String.format(tmpOut > 0 ? mPBstrA2:mPBstrB2,mCalOut,Math.abs(tmpOut)));

        /*CPBar = (CircularProgressBar) rootView.findViewById(R.id.circleProgressbar);
        if (mCPBar_Flag == 0) {
            CPBar.setInitProgress(colorBar_Step,colorBackground,colorText_Step
                    ,mStepMax,getString(R.string.CBar_Step));
            CPBar.setProgressWithAnimation(mStep);

        } else {
            CPBar.setInitProgress(colorBar_Calories,colorBackground,colorText_Calories
                    ,mCalOutMax,getString(R.string.CBar_Calories));
            CPBar.setProgressWithAnimation(mCalOut);
        }*/
        //CPBar.setOnClickListener(this);

        /*mSManager = (SensorManager) getActivity().getSystemService(Service.SENSOR_SERVICE);
        // 震动
        mVibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        //监听器
        mShakeListener=new sensorListener();
        // 加速度传感器
        mSManager.registerListener(mShakeListener,
                mSManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                // 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
                // 根据不同应用，需要的反应速率不同，具体根据实际情况设定
                SensorManager.SENSOR_DELAY_NORMAL);*/

        View view = inflater.inflate(R.layout.fab_calories,null);
        FrameLayout aView = (FrameLayout) getActivity().findViewById(R.id.fl_c_Main_fab);
        aView.addView(view);

        fabCal_Run = (FloatingActionButton) view.findViewById(R.id.fabCal_Run);
        fabCal_Run.setOnClickListener(this);
        fabCal_Bike = (FloatingActionButton) view.findViewById(R.id.fabCal_Bike);
        fabCal_Bike.setOnClickListener(this);
        fabCal_Swim = (FloatingActionButton) view.findViewById(R.id.fabCal_Swim);
        fabCal_Swim.setOnClickListener(this);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabCal_Run.setElevation(10f);
            fabCal_Bike.setElevation(10f);
            fabCal_Swim.setElevation(10f);
        }*/

        /*rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP && !fabAnimFlag){

                    CPBar=(CircularProgressBar) rootView.findViewById(R.id.circleProgressbar);

                    if (mCPBar_Flag == 1) {
                        mCPBar_Flag = 0;
                        CPBar.clearAnimation();
                        CPBar.setInitProgress(colorBar_Step,colorBackground,colorText_Step
                                ,mStepMax,getString(R.string.CBar_Step));
                        CPBar.setProgressWithAnimation(mStep);

                        String tmpHi = "今天走了"+mStep+"步";
                        assert MainActivity2.HiCard_Text != null;
                        MainActivity2.HiCard_Text.start(tmpHi);
                    } else {
                        mCPBar_Flag = 1;
                        CPBar.clearAnimation();
                        CPBar.setInitProgress(colorBar_Calories,colorBackground,colorText_Calories
                                ,mCalOutMax,getString(R.string.CBar_Calories));
                        CPBar.setProgressWithAnimation(mCalOut);

                        String tmpHi = "今天消耗了"+mCalOut+"卡";
                        assert MainActivity2.HiCard_Text != null;
                        MainActivity2.HiCard_Text.start(tmpHi);
                    }
                }
                return true;
            }
        });*/


        /*mGestureDetector = new GestureDetector(
                getActivity(), this);
        myOnTouchListener = new MainActivity2.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };

        ((MainActivity) getActivity())
                .registerMyOnTouchListener(myOnTouchListener);*/

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

            MainActivity2.fabMain.animate().rotationBy(180f)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            MainActivity2.fabMain.setClickable(false);
                            fabCal_Run.setClickable(false);
                            fabCal_Swim.setClickable(false);
                            fabCal_Bike.setClickable(false);
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
                                            fabCal_Run.setClickable(true);
                                            fabCal_Swim.setClickable(true);
                                            fabCal_Bike.setClickable(true);
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

            fabCal_Run.animate().alpha(0f).scaleX(0f).scaleY(0f)
                    .translationX(0f).setDuration(tmpTime2)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            fabCal_Run.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabCal_Run.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }
                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            fabCal_Bike.animate().alpha(0f).scaleX(0f).scaleY(0f)
                    .translationY(0f).setDuration(tmpTime2)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            fabCal_Bike.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabCal_Bike.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }
                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
            fabCal_Swim.animate().alpha(0f).scaleX(0f).scaleY(0f)
                    .translationX(0f).translationY(0f).setDuration(tmpTime2)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            fabCal_Swim.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabCal_Swim.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }
                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

            //fabFlag = true;
        }
    }

    public void fabOpen(boolean fabMain) {
        double tmpWidth = MainActivity2.fabMain.getWidth()*1.15;
        if (fabFlag) {
            fabAnimFlag = true;
            fabCal_Run.setVisibility(View.VISIBLE);
            fabCal_Bike.setVisibility(View.VISIBLE);
            fabCal_Swim.setVisibility(View.VISIBLE);
            MainActivity2.fabMain.setClickable(false);
            fabCal_Run.setClickable(false);
            fabCal_Swim.setClickable(false);
            fabCal_Bike.setClickable(false);

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
                                                fabCal_Run.setClickable(true);
                                                fabCal_Swim.setClickable(true);
                                                fabCal_Bike.setClickable(true);

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

            fabCal_Run.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .translationY(-(float)(tmpWidth))
                    //.translationX(-(float)(MainActivity2.fabMain.getWidth()*1.1)).setDuration(360)
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
            fabCal_Bike.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .translationY(-(float)(tmpWidth*2))
                    //.translationY(-(float)(MainActivity2.fabMain.getWidth()*1.1)).setDuration(360)
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
            fabCal_Swim.animate().alpha(1f).scaleX(1f).scaleY(1f)
                    .translationY(-(float)(tmpWidth*3))
                    //.translationX(-(float)(MainActivity2.fabMain.getWidth()*1.1*0.707))
                    //.translationY(-(float)(MainActivity2.fabMain.getWidth()*1.1*0.707))
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

            //fabFlag = false;
        }
    }

    @SuppressLint("DefaultLocale")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.fabCal_Run:
                /*int tmp;
                if (mCPBar_Flag == 0) {
                    //mStep=mStep+10;
                    //tmp=mStep;
                    mCalOut=mCalOut+10;
                    tmp=mCalOut;
                    int tmpOut = mCalOutMax-mCalOut;
                    mPBtext2.setText(String.format(tmpOut > 0 ? mPBstrA2:mPBstrB2,mCalOut,Math.abs(tmpOut)));
                    mProgressBar2.setProgress(mCalOut);
                } else {
                    mCalIn=mCalIn+10;
                    tmp=mCalIn;

                    int tmpIn = mCalInMax-mCalIn;
                    mPBtext1.setText(String.format(tmpIn > 0 ? mPBstrA1:mPBstrB1,mCalIn,Math.abs(tmpIn)));
                    mProgressBar1.setProgress(mCalIn);

                }*/
                //CPBar.setProgress(tmp);
                //MainActivity2.volleyMethod.vpost_GetCalInRecord("","2016-7-15");

                String tmpHi = "慢跑～～";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
                break;

            case R.id.fabCal_Bike:
                /*int tmp2;
                if (mCPBar_Flag == 0) {
                    //mStep=mStep-10;
                    //tmp2=mStep;
                    mCalOut=mCalOut-10;
                    tmp=mCalOut;

                    int tmpOut = mCalOutMax-mCalOut;
                    mPBtext2.setText(String.format(tmpOut > 0 ? mPBstrA2:mPBstrB2,mCalOut,Math.abs(tmpOut)));
                    mProgressBar2.setProgress(mCalOut);
                } else {
                    mCalIn=mCalIn-10;
                    tmp2=mCalIn;

                    int tmpIn = mCalInMax-mCalIn;
                    mPBtext1.setText(String.format(tmpIn > 0 ? mPBstrA1:mPBstrB1,mCalIn,Math.abs(tmpIn)));
                    mProgressBar1.setProgress(mCalIn);
                }*/
                //CPBar.setProgress(tmp2);

                tmpHi = "騎鐵馬～～";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
                break;

            case R.id.fabCal_Swim:

                tmpHi = "游泳～～";
                assert MainActivity2.HiCard_Text != null;
                MainActivity2.HiCard_Text.start(tmpHi);
                //mCPBar_Flag = mCPBar_Flag == 1 ? 0 : 1;
                break;
        }
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
