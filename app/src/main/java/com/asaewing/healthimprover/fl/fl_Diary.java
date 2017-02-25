package com.asaewing.healthimprover.fl;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asaewing.healthimprover.AnalyticsTrackers;
import com.asaewing.healthimprover.FoodActivity;
import com.asaewing.healthimprover.MainActivity;
import com.asaewing.healthimprover.Others.CT48;
import com.asaewing.healthimprover.Others.HiDBHelper;
import com.asaewing.healthimprover.Others.ListAdapter;
import com.asaewing.healthimprover.Others.MyAutoView;
import com.asaewing.healthimprover.Others.ScrollDisabledListView;
import com.asaewing.healthimprover.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_NULL;
import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.KeyEvent.getMaxKeyCode;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 *
 */
public class fl_Diary extends RootFragment implements View.OnClickListener,View.OnKeyListener{

    private int HH,HL,WH,WL,tmpH,tmpL;
    private float Height,Weight;
    private String Dialog_Flag;

    //private static ImageButton fab_Weight,fab_Food;
    private static FloatingActionButton fab_Height,fab_Weight,fab_Food;
    private static TextView fab_Height_T,fab_Weight_T,fab_Food_T;
    private static RelativeLayout fab_Height_RL,fab_Weight_RL,fab_Food_RL,Diary_Main_RL;

    private ListView WeightList,CalList;
    private boolean canSeeList = false;

    public static boolean fabFlag = true;
    private static View rootView2;

    public fl_Diary() {
        // Required empty public constructor
    }

    public static fl_Diary newInstance() {

        return new fl_Diary();
    }

    //TODO----Data----
    @Override
    public void mSaveState(){
        MainActivity.mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_H,HH);
        MainActivity.mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_L,HL);
        MainActivity.mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_H,WH);
        MainActivity.mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_L,WL);
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
        super.onCreateView(inflater,container,savedInstanceState);

        rootView=inflater.inflate(R.layout.fl_diary, container, false);

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fab_diary,null);
        FrameLayout aView = (FrameLayout) getActivity().findViewById(R.id.fl_c_Main_fab);
        aView.addView(view);

        fab_Height = (FloatingActionButton) view.findViewById(R.id.FABM_Height);
        fab_Weight = (FloatingActionButton) view.findViewById(R.id.FABM_Weight);
        fab_Food = (FloatingActionButton) view.findViewById(R.id.FABM_Food);

        fab_Height_T = (TextView) view.findViewById(R.id.FABM_Height_Text);
        fab_Weight_T = (TextView) view.findViewById(R.id.FABM_Weight_Text);
        fab_Food_T = (TextView) view.findViewById(R.id.FABM_Food_Text);

        fab_Height_RL = (RelativeLayout) view.findViewById(R.id.FABM_Height_RL);
        fab_Weight_RL = (RelativeLayout) view.findViewById(R.id.FABM_Weight_RL);
        fab_Food_RL = (RelativeLayout) view.findViewById(R.id.FABM_Food_RL);

        fab_Height.setImageResource(R.drawable.ic_fab_paint_wt);
        fab_Weight.setImageResource(R.drawable.ic_fab_paint_wt);
        fab_Food.setImageResource(R.drawable.ic_fab_paint_wt);
        fab_Height.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0x3f, 0xc0, 0x06)));
        fab_Weight.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0x3f, 0xc0, 0x06)));
        fab_Food.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0x3f, 0xc0, 0x06)));

        fab_Height_T.setText(R.string.FABM_Height);
        fab_Weight_T.setText(R.string.FABM_Weight);
        fab_Food_T.setText(R.string.FABM_Food);
        fab_Height.setOnClickListener(this);
        fab_Weight.setOnClickListener(this);
        fab_Food.setOnClickListener(this);

        Diary_Main_RL = (RelativeLayout) rootView.findViewById(R.id.DiaryMainLayout);
        Diary_Main_RL.setOnClickListener(this);

        WeightList = (ListView) rootView.findViewById(R.id.listView_diary_W);
        CalList = (ListView) rootView.findViewById(R.id.listView_diary_C);

        final EditText editDay = (EditText)rootView.findViewById(R.id.Pre_edit_day);
        final EditText editWeight = (EditText)rootView.findViewById(R.id.Pre_edit_weight);
        final TextView preText = (TextView)rootView.findViewById(R.id.Pre_text);

        Button preButton = (Button)rootView.findViewById(R.id.Pre_button);
        preButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                @SuppressLint("DefaultLocale")
                String nowTime = String.format("%02d:%02d:00",calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE));
                if (MainActivity.mInfoMap.IMgetFloat(HiDBHelper.KEY_AC_WakeTime48)<new CT48(nowTime).getCt48()){
                    calendar.add(Calendar.DAY_OF_YEAR,-1);
                } else {
                    calendar.add(Calendar.DAY_OF_YEAR,-2);
                }
                Log.d(TAG,"**nowTime**"+nowTime);
                preText.setText("");
                if (editWeight.getText().toString().equals("") && editDay.getText().toString().equals("")){
                    String tmp0 = "Null Ask";
                    tmp0 = String.format("%d-%d-%d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(getActivity(),tmp0,Toast.LENGTH_SHORT).show();

                } else if (editWeight.getText().toString().equals("") && !editDay.getText().toString().equals("")){
                    String tmp3 = String.format("mode=3 Day:%s"
                            ,editDay.getText().toString());
                    //Toast.makeText(getActivity(),tmp3,Toast.LENGTH_SHORT).show();
                    MainActivity.volleyMethod.vpostGet_Pre(3,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)
                            ,Integer.parseInt(editDay.getText().toString())+1,0);

                } else if (!editWeight.getText().toString().equals("") && !editDay.getText().toString().equals("")){
                    String tmp2 = String.format("mode=2 Day:%s W:%s"
                            ,editDay.getText().toString()
                            ,editWeight.getText().toString());
                    //Toast.makeText(getActivity(),tmp2,Toast.LENGTH_SHORT).show();
                    MainActivity.volleyMethod.vpostGet_Pre(2,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH)
                            ,Integer.parseInt(editDay.getText().toString())+1
                            ,Double.parseDouble(editWeight.getText().toString()));
                }

            }
        });
        rootView2 = rootView;
        return rootView;
    }

    public static void PreUpdate(int mode,String response){
        TextView preText = (TextView)rootView2.findViewById(R.id.Pre_text);
        if (mode==2){
            //addCal=3900&date=2016-11-08&weight=54.07914
            int andIndex = response.indexOf("&");

            String tmpCal = response.substring(response.indexOf("addCal=")+7,andIndex);
            //Log.d(TAG, "**vpostGet_Pre2**1:addCal=" + tmp);

            andIndex = response.indexOf("&",andIndex+1);
            String tmpDate = response.substring(response.indexOf("date=")+5,andIndex);
            //Log.d(TAG, "**vpostGet_Pre2**2:date=" + tmp);

            String tmpWeight = response.substring(response.indexOf("weight=")+7);
            //Log.d(TAG, "**vpostGet_Pre2**3:weight=" + tmp);

            String tmp = String.format("每天至少增加%s卡的熱量",tmpCal);
            preText.setText(tmp);

        } else if (mode==3){
            //date=2016-11-08&weight=52.33512
            int andIndex = response.indexOf("&");

            String tmpDate = response.substring(response.indexOf("date=")+5,andIndex);
            //Log.d(TAG, "**vpostGet_Pre3**1:date=" + tmp);

            String tmpWeight = response.substring(response.indexOf("weight=")+7);
            if (tmpWeight.length()>5){
                tmpWeight = tmpWeight.substring(0,5);
            }
            //Log.d(TAG, "**vpostGet_Pre3**2:weight=" + tmp);

            String tmp = String.format("預計於%s時，體重為%s",tmpDate,tmpWeight);

            Calendar calendar = Calendar.getInstance();
            String nowYear = String.valueOf(calendar.get(Calendar.YEAR));
            Log.d(TAG,"**Year**"+nowYear);

            if (tmpDate.substring(0,4).equals(nowYear)){
                tmp = String.format("預計於%s時，體重為%s",tmpDate.substring(5),tmpWeight);
            }

            tmp = tmp.replace("-","/");

            preText.setText(tmp);

        }
    }

    public void ListUpdate(){
        if (canSeeList){
            Cursor cursorW = MainActivity.helper.WeightSelect();
            String[] stringListW = new String[]{"無任何體重資料"};
            int countW = cursorW.getCount();
            if (countW>0){
                stringListW = new String[16];
                stringListW[0] = "共有"+countW+"筆體重資料，最近15筆：";
                for (int ii=cursorW.getCount()-1;ii>cursorW.getCount()-16;ii--){
                    cursorW.moveToPosition(ii);
                    String time = cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Time));
                    CT48 ct48 = new CT48(time);

                    String tmpStr= "Time:"+cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Date)).substring(5)+
                            " "+time.substring(0,5)+
                            "\n   "+cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Weight))
                            +" kg";
                    stringListW[cursorW.getCount()-ii] = tmpStr;

                }
            }
            cursorW.close();
            ArrayAdapter mAdapterW = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    stringListW);
            WeightList.setAdapter(mAdapterW);

            Cursor cursorC = MainActivity.helper.CalInSelect();
            String[] stringListC = new String[]{"無任何飲食資料"};
            int countC = cursorC.getCount();
            if (countC>0){
                stringListC = new String[16];
                stringListC[0] = "共有"+countC+"筆飲食資料，最近15筆：";
                for (int ii=cursorC.getCount()-1;ii>cursorC.getCount()-16;ii--){
                    cursorC.moveToPosition(ii);
                    String time = cursorC.getString(cursorC.getColumnIndex(HiDBHelper.KEY_CalIn_Time));
                    CT48 ct48 = new CT48(time);

                    String tmpStr= "Time:"+cursorC.getString(cursorC.getColumnIndex(HiDBHelper.KEY_CalIn_Date)).substring(5)+
                            " "+time.substring(0,5)+
                            "\n   "+cursorC.getString(cursorC.getColumnIndex(HiDBHelper.KEY_CalIn_oneCal))
                            +" Cal";
                    stringListC[cursorC.getCount()-ii] = tmpStr;

                }
            }
            cursorC.close();
            ArrayAdapter mAdapterC = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    stringListC);
            CalList.setAdapter(mAdapterC);
        } else {
            WeightList.setVisibility(View.GONE);
            CalList.setVisibility(View.GONE);
        }

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
        ListUpdate();
        initValueGet();
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

    @SuppressLint("SetTextI18n")
    private void setBMI() {
        if (HH == 0 && HL == 0) {
            Height = 1f;
        } else {
            Height = (float) (HH + HL*0.01);
        }
        if (WH == 0 && WL == 0) {
            Weight = 0f;
        } else {
            Weight = (float) (WH + WL*0.01);
        }

        float tmp = (float) (Weight/Math.pow(Height*0.01,2));
        tmp = new BigDecimal(tmp).setScale(2, RoundingMode.HALF_UP).floatValue();
        String tmpDF = new DecimalFormat("#.00").format(tmp);
        TextView Diary_BMI_text = (TextView)rootView.findViewById(R.id.Diary_BMI);

        /*String tmpText = "身高 ： "+String.valueOf(Height);
        tmpText += "\n";
        tmpText += "體重 ： "+String.valueOf(Weight);
        tmpText += "\n";
        tmpText += "BMI : " + tmpDF;*/

        String tmpText = String.valueOf(Height);
        tmpText += "\n";
        tmpText += String.valueOf(Weight);
        tmpText += "\n";
        tmpText += tmpDF;
        Diary_BMI_text.setText(tmpText);

        mSaveState();
    }

    private void initValueGet(){
        HH = MainActivity.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_H);
        HL = MainActivity.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_L);
        WH = MainActivity.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_H);
        WL = MainActivity.mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_L);
        if ((HH + HL*0.01)<10) {
            Height = 165f;
            HH = 165;
            HL = 0;
        } else {
            Height = (float) (HH + HL*0.01);
        }
        if ((WH + WL*0.01)<2) {
            Weight = 50f;
            WH = 50;
            HL = 0;
        } else {
            Weight = (float) (WH + WL*0.01);
        }

        /*if (mNP_HW_text.getValue() == 0) {
            mNP_HW_I.setValue(HH);
            mNP_HW_D.setValue(HL);
        } else {
            mNP_HW_I.setValue(WH);
            mNP_HW_D.setValue(WL);
        }*/

        setBMI();
    }

    public void fabMainClick() {
        Log.d(TAG,"**"+TAG+"**buttonClick**fabMain******");

        if (!fabFlag) {
            fabClose(true);
        } else {
            fabOpen(true);

            MainActivity.HiCardPlay("","","你要紀錄什麼呢？");
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

            MainActivity.fabMain.setClickable(false);
            fab_Height.setClickable(false);
            fab_Weight.setClickable(false);
            fab_Food.setClickable(false);

            MainActivity.fabMain.animate().rotationBy(180f)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            /*MainActivity.fabMain.setClickable(false);
                            fab_Height.setClickable(false);
                            fab_Weight.setClickable(false);*/
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MainActivity.fabMain.setImageResource(R.drawable.ic_fab_paint_bk);

                            MainActivity.fabMain.animate().rotationBy(180f)
                                    .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            MainActivity.fabMain.setClickable(true);
                                            fabFlag = true;
                                            MainActivity.fabMain.setRotation(0);
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

            closeRL(fab_Height_RL, tmpTime2);
            closeRL(fab_Weight_RL, tmpTime2);
            closeRL(fab_Food_RL, tmpTime2);

            //fabFlag = true;
        }
    }

    public void fabOpen(boolean fabMain) {
        if (fabFlag) {
            fab_Height_RL.setVisibility(View.VISIBLE);
            fab_Weight_RL.setVisibility(View.VISIBLE);
            fab_Food_RL.setVisibility(View.VISIBLE);
            fab_Height_T.setText(R.string.FABM_Height);
            fab_Weight_T.setText(R.string.FABM_Weight);
            fab_Food_T.setText(R.string.FABM_Food);
            MainActivity.fabMain.setClickable(false);
            fab_Height.setClickable(false);
            fab_Weight.setClickable(false);
            fab_Food.setClickable(false);

            if (fabMain) {
                MainActivity.fabMain.animate().rotationBy(180f)
                        .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                /*MainActivity.fabMain.setClickable(false);
                                fab_Height.setClickable(false);
                                fab_Weight.setClickable(false);*/
                            }

                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                MainActivity.fabMain.setImageResource(R.drawable.ic_fab_cancel_bk);

                                MainActivity.fabMain.animate().rotationBy(180f)
                                        .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                MainActivity.fabMain.setClickable(true);
                                                fab_Height.setClickable(true);
                                                fab_Weight.setClickable(true);
                                                fab_Food.setClickable(true);
                                                fabFlag = false;
                                                MainActivity.fabMain.setRotation(0);
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

            double tmpWidth = MainActivity.fabMain.getWidth()*1.15;
            openMoveRL(fab_Height_RL,tmpWidth*1);
            openMoveRL(fab_Weight_RL,tmpWidth*2);
            openMoveRL(fab_Food_RL,tmpWidth*3);

            //fabFlag = false;
        }
    }

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

    @SuppressLint("DefaultLocale")
    private void NumberPickerDialogCreate(String s) {
        final Button AutoDate,AutoTime;
        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        final int[] month = {c.get(Calendar.MONTH) + 1};
        String apm = "a.m.";
        if (hourOfDay>12){
            hourOfDay -=12;
            apm = "p.m.";
        }
        final String[] strDate = {String.format("%04d/%02d/%02d", c.get(Calendar.YEAR), month[0], c.get(Calendar.DAY_OF_MONTH))};
        final String[] strTime = {String.format("%02d:%02d %s", hourOfDay, c.get(Calendar.MINUTE), apm)};

        final String[] date = {String.format("%04d-%02d-%02d"
                , c.get(Calendar.YEAR)
                , c.get(Calendar.MONTH) + 1
                , c.get(Calendar.DAY_OF_MONTH))};
        final String[] time = {String.format("%02d:%02d:00"
                , c.get(Calendar.HOUR_OF_DAY)
                , c.get(Calendar.MINUTE))};

        int sTitle = 0,NP_H = 0,NP_L = 0;
        if (s.equals("Height")) {
            sTitle = R.string.Dialog_Title_Height;
            Dialog_Flag = "Height";
            NP_H = HH;
            NP_L = HL;
            tmpH = HH;
            tmpL = HL;

        } else if (s.equals("Weight")) {
            sTitle = R.string.Dialog_Title_Weight;
            Dialog_Flag = "Weight";
            NP_H = WH;
            NP_L = WL;
            tmpH = WH;
            tmpL = WL;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        NumberPicker mNP_HW_I,mNP_HW_D;
        builder.setTitle(sTitle);
        builder.setView(R.layout.number_picker);

        builder.setPositiveButton(R.string.Dialog_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Dialog_Flag.equals("Height")) {
                            HH = tmpH;
                            HL = tmpL;
                            String tmp = strDate[0];
                            String strDate = String.format("%s-%s-%s"
                                    ,tmp.substring(0,4)
                                    ,tmp.substring(5,7)
                                    ,tmp.substring(8));
                            tmp = strTime[0];
                            String strTime = "";
                            if (tmp.contains("A")||tmp.contains("a")){
                                strTime = tmp.substring(0,5)+":00";
                            } else if (tmp.contains("P")||tmp.contains("p")){
                                int hour = Integer.parseInt(tmp.substring(0,2))+12;
                                strTime = String.format("%02d:%s:00",hour,tmp.substring(3,5));
                            }
                            CT48 ct48 = new CT48(strTime);

                            ContentValues values = new ContentValues();
                            values.put(HiDBHelper.KEY_Height_Date,strDate);
                            values.put(HiDBHelper.KEY_Height_Time,strTime);
                            values.put(HiDBHelper.KEY_Height_Time48,ct48.getCt48());
                            values.put(HiDBHelper.KEY_Height_Height,String.valueOf(HH+HL*0.01));
                            MainActivity.helper.HeightInsert(values);

                        } else if (Dialog_Flag.equals("Weight")) {
                            WH = tmpH;
                            WL = tmpL;
                            String tmp = strDate[0];
                            String strDate = String.format("%s-%s-%s"
                                    ,tmp.substring(0,4)
                                    ,tmp.substring(5,7)
                                    ,tmp.substring(8));
                            tmp = strTime[0];
                            String strTime = "";
                            if (tmp.contains("A")||tmp.contains("a")){
                                strTime = tmp.substring(0,5)+":00";
                            } else if (tmp.contains("P")||tmp.contains("p")){
                                int hour = Integer.parseInt(tmp.substring(0,2))+12;
                                strTime = String.format("%02d:%s:00",hour,tmp.substring(3,5));
                            }

                            CT48 ct48 = new CT48(strTime);
                            double wAll = Double.parseDouble(String.format("%.2f",WH+WL*0.01));

                            ContentValues values = new ContentValues();
                            values.put(HiDBHelper.KEY_Weight_Date,strDate);
                            values.put(HiDBHelper.KEY_Weight_Time,strTime);
                            values.put(HiDBHelper.KEY_Weight_Time48,ct48.getCt48());
                            values.put(HiDBHelper.KEY_Weight_Weight,String.valueOf(wAll));
                            MainActivity.helper.WeightInsert(values);

                            MainActivity.volleyMethod.vpostSend_WeightJson(new String[]{strDate},new String[]{strTime},new double[]{wAll});
                        }
                        setBMI();
                        ListUpdate();

                        MainActivity.fabMainClose();
                    }
                });
        builder.setNegativeButton(R.string.Dialog_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setBMI();
                        MainActivity.fabMainClose();
                    }
                });

        AlertDialog dialog;
        dialog = builder.create();
        dialog.show();

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                MainActivity.fabMainClose();
            }
        });

        mNP_HW_I = (NumberPicker) dialog.findViewById(R.id.Diary_HW_Int);
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
                tmpH = newVal;
            }
        });
        mNP_HW_I.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });
        mNP_HW_I.setMaxValue(250);
        mNP_HW_I.setMinValue(0);
        mNP_HW_I.setValue(NP_H);

        mNP_HW_D = (NumberPicker) dialog.findViewById(R.id.Diary_HW_Dec);
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
                tmpL = newVal;
            }
        });
        mNP_HW_D.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });
        mNP_HW_D.setMaxValue(99);
        mNP_HW_D.setMinValue(0);
        mNP_HW_D.setValue(NP_L);

        AutoDate = (Button) dialog.findViewById(R.id.NP_text_Date2);
        AutoTime = (Button) dialog.findViewById(R.id.NP_text_Time2);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.NP_text_Date2:
                        Calendar c1 = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String strDate1 = String.format("%04d/%02d/%02d",year,monthOfYear+1,dayOfMonth);
                                date[0] = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                                assert AutoDate != null;
                                AutoDate.setText(strDate1);
                                strDate[0] = strDate1;
                            }
                        }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));

                        datePickerDialog.show();
                        break;
                    case R.id.NP_text_Time2:
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
                                strTime[0] = strTime2;
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
            AutoDate.setText(strDate[0]);
        }
        if (AutoTime != null) {
            AutoTime.getBackground().setAlpha(20);
            AutoTime.setClickable(true);
            AutoTime.setOnClickListener(clickListener);
            AutoTime.setText(strTime[0]);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.DiaryMainLayout:
                fabClose(false);
                break;

            case R.id.FABM_Height:
                NumberPickerDialogCreate("Height");

                String tmpHi = "喔～是身高！";
                assert MainActivity.HiCard_Text != null;
                MainActivity.HiCard_Text.start(tmpHi);
                break;

            case R.id.FABM_Weight:
                NumberPickerDialogCreate("Weight");

                tmpHi = "喔～是體重";
                assert MainActivity.HiCard_Text != null;
                MainActivity.HiCard_Text.start(tmpHi);
                break;

            case R.id.FABM_Food:
                //NumberPickerDialogCreate("Weight");
                //FoodDialogCreate();
                Intent intent = new Intent(getActivity().getApplicationContext(), FoodActivity.class);
                startActivity(intent);

                tmpHi = "喔～是食物";
                assert MainActivity.HiCard_Text != null;
                MainActivity.HiCard_Text.start(tmpHi);
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.d(TAG,"**Root**key**");
        return false;
    }
}