package com.asaewing.healthimprover.app2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.asaewing.healthimprover.app2.Manager.VolleyManager;
import com.asaewing.healthimprover.app2.Others.CT48;
import com.asaewing.healthimprover.app2.ViewOthers.CircleImageView;
import com.asaewing.healthimprover.app2.Others.DownloadImageTask;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


/**
 * 開發版本 2.0
 *
 * Data ==================================
 * * UseFirst_flag          =第一次使用（原始為true）
 * * AC_Image               =帳戶圖像
 * * AC_ID                  =帳戶ID
 * * AC_Name                =帳戶名稱
 * * AC_Mail_google         =Google帳戶
 * * AC_Mail_facebook       =Facebook帳戶
 * * BI_Height_before       =身高
 * * BI_Weight_before       =體重
 * * BI_Step_before         =步
 * * BI_Calories_before     =卡
 *
 * Map ===================================
 * * UseFirst_flag          =第一次使用（原始為true）
 * * AC_Image               =帳戶圖像
 * * AC_ID                  =帳戶ID
 * * AC_Name                =帳戶名稱
 * * AC_Mail_google         =Google帳戶
 * * AC_Mail_facebook       =Facebook帳戶
 * * BI_Height_H       =身高
 * * BI_Height_L       =身高
 * * BI_Weight_H       =體重
 * * BI_Weight_L       =體重
 * * BI_Step_before         =步
 * * BI_Calories_before     =卡
 * * deviceId               =裝置Id
 * * VPhomeBefore           =紀錄跳離Home時，所在之頁面
 * * WindowWidth            =裝置顯示器寬度
 * * WindowHeight           =裝置顯示器高度
 * *
 * *
 *
 **/

public class MainActivity2 extends RootActivity2
        implements GoogleApiClient.OnConnectionFailedListener,
        NavigationView.OnNavigationItemSelectedListener ,FragmentManager.OnBackStackChangedListener {

    @SuppressLint("StaticFieldLeak")
    //public static VolleyMethod volleyMethod;

    //TODO----Object----
    //Account
    private static final int RC_SIGN_IN = 9001;
    public static GoogleApiClient mGoogleApiClient;
    public static GoogleSignInOptions gso;
    //private ProgressDialog mProgressDialog;
    public static boolean flag_google = false, flag_facebook = false;
    public static boolean flag_FCM = false;

    public static String whoDialog = "";

    private String personName;
    private String personEmail;
    private String personId;
    private Uri personPhoto;

    public static HiDBHelper helper;

    //private FirebaseAnalytics mFirebaseAnalytics;
    //private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    //Flag
    private int id_nav_now = 0, id_nav_before = 0, id_nav_temp = 0;
    public static boolean need_flChange = false;
    public static String need_flChange_nav = "";
    public boolean firstOpen = true;

    private NavigationView mNavView;
    private LocationManager locationMgr;
    private String provider;

    //ProgressDialog
    protected ProgressDialog mProgressDialog;   //進度提示
    protected ProgressDialog mPreProgressDialog;   //進度提示


    public boolean updateData(){
        Log.d(TAG, "**" + TAG + "**upDialog");

        final int[] CA_Count = {0};
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                double tmpDouble = 0;

                Cursor cursorAll = helper.HiSelect();
                CA_Count[0] = cursorAll.getCount();
                Log.d(TAG,"**InfoMap**"+cursorAll.getCount());
                if (CA_Count[0] >0) {
                    cursorAll.moveToFirst();
                    //Int
                    mInfoMap.IMput(HiDBHelper.KEY_Hi_Index
                            , cursorAll.getInt(cursorAll.getColumnIndex(HiDBHelper.KEY_Hi_Index)));
                    //Boolean
                    if (cursorAll.getInt(cursorAll.getColumnIndex(HiDBHelper.KEY_Flag_UseFirst))==0)
                        mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst,false);
                    else mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst,true);
                    if (cursorAll.getInt(cursorAll.getColumnIndex(HiDBHelper.KEY_Flag_SAID))==0)
                        mInfoMap.IMput(HiDBHelper.KEY_Flag_SAID,false);
                    else mInfoMap.IMput(HiDBHelper.KEY_Flag_SAID,true);
                    if (cursorAll.getInt(cursorAll.getColumnIndex(HiDBHelper.KEY_Flag_FAID))==0)
                        mInfoMap.IMput(HiDBHelper.KEY_Flag_FAID,false);
                    else mInfoMap.IMput(HiDBHelper.KEY_Flag_FAID,true);
                    //AC
                    mInfoMap.IMput(HiDBHelper.KEY_AC_uid
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_uid)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_initDate
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_initDate)));
                    if (!flag_FCM){
                        mInfoMap.IMput(HiDBHelper.KEY_AC_FCM_id
                                , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_FCM_id)));
                    }
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Name
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Name)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Locale
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Locale)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_LoginType
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_LoginType)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Birthday
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Birthday)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Gender
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Gender)));
                    //AC WC Time
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Height)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Height, tmpDouble);
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Weight)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Weight, tmpDouble);
                    mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_WakeTime)));
                    //AC
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Image
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Image)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Image_url
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Image_url)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Account
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_Account)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_GoogleMail
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_GoogleMail)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_FacebookMail
                            , cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_FacebookMail)));
                    //BI
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_BI_Height_before)));
                    mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before, tmpDouble);
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_BI_Weight_before)));
                    mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before, tmpDouble);
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_BI_Step_before)));
                    mInfoMap.IMput(HiDBHelper.KEY_BI_Step_before, tmpDouble);
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_BI_CalIn_before)));
                    mInfoMap.IMput(HiDBHelper.KEY_BI_CalIn_before, tmpDouble);
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_BI_CalOut_before)));
                    mInfoMap.IMput(HiDBHelper.KEY_BI_CalOut_before, tmpDouble);
                    tmpDouble = Double.parseDouble(
                            cursorAll.getString(cursorAll.getColumnIndex(HiDBHelper.KEY_AC_WakeTime48)));
                    mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime48, tmpDouble);
                } else {
                    mInfoMap.IMput(HiDBHelper.KEY_Hi_Index, 1);

                    mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst, true);
                    mInfoMap.IMput(HiDBHelper.KEY_Flag_SAID, true);
                    mInfoMap.IMput(HiDBHelper.KEY_Flag_FAID, true);

                    mInfoMap.IMput(HiDBHelper.KEY_AC_uid, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_initDate, "");
                    if (!flag_FCM){
                        mInfoMap.IMput(HiDBHelper.KEY_AC_FCM_id, "");
                    }
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Name, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Locale, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_LoginType, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Birthday, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Gender, "");

                    mInfoMap.IMput(HiDBHelper.KEY_AC_Height, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Weight, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime, "");

                    mInfoMap.IMput(HiDBHelper.KEY_AC_Image, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Image_url, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_Account, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_GoogleMail, "");
                    mInfoMap.IMput(HiDBHelper.KEY_AC_FacebookMail, "");

                    mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_BI_Step_before, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_BI_CalIn_before, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_BI_CalOut_before, 0);
                    mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime48, 0);

                    ContentValues values = new ContentValues();
                    values.put(HiDBHelper.KEY_Hi_Index, 1);
                    values.put(HiDBHelper.KEY_Flag_UseFirst, true);
                    values.put(HiDBHelper.KEY_Flag_SAID, true);
                    values.put(HiDBHelper.KEY_Flag_FAID, true);

                    values.put(HiDBHelper.KEY_AC_uid, "");
                    values.put(HiDBHelper.KEY_AC_initDate, "");
                    if (flag_FCM){
                        values.put(HiDBHelper.KEY_AC_FCM_id, mInfoMap.IMgetString(HiDBHelper.KEY_AC_FCM_id));
                    } else {
                        values.put(HiDBHelper.KEY_AC_FCM_id, "");
                    }
                    values.put(HiDBHelper.KEY_AC_LoginType, "");
                    values.put(HiDBHelper.KEY_AC_Locale, "");
                    values.put(HiDBHelper.KEY_AC_Name, "");
                    values.put(HiDBHelper.KEY_AC_Birthday, "");
                    values.put(HiDBHelper.KEY_AC_Gender, "");

                    values.put(HiDBHelper.KEY_AC_Height, String.valueOf(0));
                    values.put(HiDBHelper.KEY_AC_Weight, String.valueOf(0));
                    values.put(HiDBHelper.KEY_AC_WakeTime, "");

                    values.put(HiDBHelper.KEY_AC_Image, "");
                    values.put(HiDBHelper.KEY_AC_Image_url, "");
                    values.put(HiDBHelper.KEY_AC_Account, "");
                    values.put(HiDBHelper.KEY_AC_GoogleMail, "");
                    values.put(HiDBHelper.KEY_AC_FacebookMail, "");

                    values.put(HiDBHelper.KEY_BI_Height_before, String.valueOf(0));
                    values.put(HiDBHelper.KEY_BI_Weight_before, String.valueOf(0));
                    values.put(HiDBHelper.KEY_BI_Step_before, String.valueOf(0));
                    values.put(HiDBHelper.KEY_BI_CalIn_before, String.valueOf(0));
                    values.put(HiDBHelper.KEY_BI_CalOut_before, String.valueOf(0));
                    values.put(HiDBHelper.KEY_AC_WakeTime48, String.valueOf(0));

                    helper.HiInsert(values);
                }
                cursorAll.close();

                //Height
                float tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Height_before);
                int tmpH, tmpL;
                if (tmp != 0) {
                    tmpH = (int) Math.floor(tmp);
                    tmpL = (int) ((tmp - tmpH) * 100);
                } else {
                    Cursor cursorH = helper.HeightSelect();
                    if (cursorH.getCount()==0){
                        tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_AC_Height);
                    } else {
                        cursorH.moveToLast();
                        tmp = cursorH.getFloat(cursorH.getColumnIndex(HiDBHelper.KEY_Height_Height));
                    }
                    cursorH.close();
                    mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before, tmp);
                    tmpH = (int) Math.floor(tmp);
                    tmpL = (int) ((tmp - tmpH) * 100);
                }
                Log.d(TAG,"**III**"+"Height:"+tmp);
                mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_H, tmpH);
                mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_L, tmpL);

                //Weight
                tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Weight_before);
                if (tmp != 0) {
                    tmpH = (int) Math.floor(tmp);
                    tmpL = (int) ((tmp - tmpH) * 100);
                } else {
                    Cursor cursorW = helper.WeightSelect();
                    if (cursorW.getCount()==0){
                        tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_AC_Weight);
                    } else {
                        cursorW.moveToLast();
                        tmp = cursorW.getFloat(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Weight));
                    }
                    cursorW.close();

                    mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before, tmp);
                    tmpH = (int) Math.floor(tmp);
                    tmpL = (int) ((tmp - tmpH) * 100);
                }
                Log.d(TAG,"**III**"+"Weight:"+tmp);
                mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_H, tmpH);
                mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_L, tmpL);

                Cursor cursor1 = helper.SAIDSelect();
                Cursor cursor2 = helper.FAIDSelect();
                if (cursor1.getCount()==0) helper.SAID_jsonUpdate();
                if (cursor2.getCount()==0) helper.FAID_jsonUpdate();
                cursor1.close();
                cursor2.close();
                /*if (UseFirst[0]){
                    volleyMethod.VVvpost_GetWeightRecord("","");
                    //volleyMethod.vpost_GetWeightRecord("","");
                }*/

                //mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst, false);
                //mInfoMap.IMput(HiDBHelper.KEY_Flag_SAID, false);
                //mInfoMap.IMput(HiDBHelper.KEY_Flag_FAID, false);

            }
        });

        thread.start();

        try {
            thread.join();
            Log.d(TAG,"**Thread**join**");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (thread.isAlive()){
            Log.d(TAG,"**Alive**");
        }

        //initView();

        Cursor cursor = helper.HiSelect();
        cursor.moveToFirst();
        int ii=0,jj=0;
        Log.d(TAG,"**Thread**cursor.getCount()**"+cursor.getCount()+"**"+cursor.getColumnCount());
        if (cursor.getCount()>0){
            for (ii=0;ii<cursor.getColumnCount();ii++) {
                Log.d(TAG,"**AliveNo1**"+ii+"**"+cursor.getColumnName(ii)+"***"+cursor.getString(ii));
                String tmpStr = cursor.getString(ii);
                String tmpStr2 = cursor.getColumnName(ii);
                if(tmpStr.equals("null") || tmpStr.equals("")){
                    if(tmpStr2.equals(HiDBHelper.KEY_AC_uid) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_initDate) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_LoginType) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Locale) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Name) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Birthday) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Gender) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Height) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Weight) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_WakeTime) ||
                            tmpStr2.equals(HiDBHelper.KEY_AC_Account)){
                        jj++;
                    }
                }
            }
        }

        cursor.close();

        //mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst,false);
        //mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst);

        //CA_Count[0]>0 && !UseFirst[0]
        //hideProgressDialog();
        //!(jj>0) && !UseFirst[0]
        if (jj==0 || !mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst)) {
            Log.d(TAG,"**Thread**false**"+jj);
            //initView();
            return false;

        } else {
            Log.d(TAG,"**Thread**true**"+jj);
            return true;
        }
    }

    public void saveDataSP() {
        final float[] Height = new float[1];
        final float[] Weight = new float[1];
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                int HH = mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_H);
                int HL = mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Height_before_L);
                int WH = mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_H);
                int WL = mInfoMap.IMgetInt(HiDBHelper.KEY_BI_Weight_before_L);
                if (HH == 0 && HL == 0) {
                    Height[0] = 1f;
                } else {
                    Height[0] = (float) (HH + HL * 0.01);
                }
                if (WH == 0 && WL == 0) {
                    Weight[0] = 0f;
                } else {
                    Weight[0] = (float) (WH + WL * 0.01);
                }

                //String s = String.valueOf(Height[0]);
                //Height[0] = Float.parseFloat(s);

                Log.d(TAG,"**InfoMap2**"+ Height[0] +"**"+ Weight[0]);

                ContentValues values = new ContentValues();
                values.put(HiDBHelper.KEY_Flag_UseFirst,mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst));
                values.put(HiDBHelper.KEY_Flag_SAID,mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_SAID));
                values.put(HiDBHelper.KEY_Flag_FAID,mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_FAID));

                values.put(HiDBHelper.KEY_AC_uid,mInfoMap.IMgetString(HiDBHelper.KEY_AC_uid));
                values.put(HiDBHelper.KEY_AC_initDate,mInfoMap.IMgetString(HiDBHelper.KEY_AC_initDate));
                values.put(HiDBHelper.KEY_AC_Name,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Name));
                values.put(HiDBHelper.KEY_AC_Locale,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Locale));
                values.put(HiDBHelper.KEY_AC_LoginType,mInfoMap.IMgetString(HiDBHelper.KEY_AC_LoginType));
                values.put(HiDBHelper.KEY_AC_Birthday,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Birthday));
                values.put(HiDBHelper.KEY_AC_Gender,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Gender));

                values.put(HiDBHelper.KEY_AC_Height,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Height));
                values.put(HiDBHelper.KEY_AC_Weight,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Weight));
                values.put(HiDBHelper.KEY_AC_WakeTime,mInfoMap.IMgetString(HiDBHelper.KEY_AC_WakeTime));

                values.put(HiDBHelper.KEY_AC_Image,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Image));
                values.put(HiDBHelper.KEY_AC_Image_url,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Image_url));
                values.put(HiDBHelper.KEY_AC_Account,mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account));
                values.put(HiDBHelper.KEY_AC_GoogleMail,mInfoMap.IMgetString(HiDBHelper.KEY_AC_GoogleMail));
                values.put(HiDBHelper.KEY_AC_FacebookMail,mInfoMap.IMgetString(HiDBHelper.KEY_AC_FacebookMail));

                values.put(HiDBHelper.KEY_BI_Height_before,String.valueOf(Height[0]));
                values.put(HiDBHelper.KEY_BI_Weight_before,String.valueOf(Weight[0]));
                values.put(HiDBHelper.KEY_BI_Step_before,mInfoMap.IMgetString(HiDBHelper.KEY_BI_Step_before));
                values.put(HiDBHelper.KEY_BI_CalIn_before,mInfoMap.IMgetString(HiDBHelper.KEY_BI_CalIn_before));
                values.put(HiDBHelper.KEY_BI_CalOut_before,mInfoMap.IMgetString(HiDBHelper.KEY_BI_CalOut_before));
                values.put(HiDBHelper.KEY_AC_WakeTime48,mInfoMap.IMgetString(HiDBHelper.KEY_AC_WakeTime48));

                helper.HiUpdate(mInfoMap.IMgetInt(HiDBHelper.KEY_Hi_Index),values);
            }
        });
        thread.start();
        try {
            thread.join();
            Log.d(TAG,"**Thread2**join**");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (thread.isAlive()){
            Log.d(TAG,"**Alive2**");
            try {
                this.wait(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Cursor cursor = helper.HiSelect();
        cursor.moveToFirst();
        int ii=0,jj=0;
        Log.d(TAG,"**Thread**cursor.getCount()**"+cursor.getCount()+"**"+cursor.getColumnCount());
        if (cursor.getCount()>0){
            for (ii=0;ii<cursor.getColumnCount();ii++) {
                Log.d(TAG,"**AliveNo2**"+ii+"**"+cursor.getColumnName(ii)+"***"+cursor.getString(ii));
                if (cursor.getString(ii).length()==0) jj++;
            }
        }

        cursor.close();
    }

    private GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.d(TAG, "GPS_EVENT_STARTED");
                    Toast.makeText(MainActivity2.this, "GPS_EVENT_STARTED", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.d(TAG, "GPS_EVENT_STOPPED");
                    Toast.makeText(MainActivity2.this, "GPS_EVENT_STOPPED", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.d(TAG, "GPS_EVENT_FIRST_FIX");
                    Toast.makeText(MainActivity2.this, "GPS_EVENT_FIRST_FIX", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.d(TAG, "GPS_EVENT_SATELLITE_STATUS");
                    break;
            }
        }
    };

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {

        }
    };

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    //TODO----Life----
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();

        helper = new HiDBHelper(getApplicationContext());
        //volleyMethod = new VolleyMethod();
        volleyManager = new VolleyManager(this,TAG,mInfoMap,helper);
        //helper.SAID_jsonUpdate();

        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(getApplicationContext());

        //Data

        //Nav Settings
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        assert mNavView != null;
        mNavView.setNavigationItemSelectedListener(this);
        MenuItem item = mNavView.getMenu().findItem(R.id.nav_Home);
        item.setChecked(true);

        //DrawerLayout Settings
        mDrawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawer,mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView,float slideOffset) {
                super.onDrawerSlide(drawerView,slideOffset);
                //fl_Calories.fabClose(false);
                //fl_Calories.newInstance().fabClose(false);
                //fl_Diary.newInstance().fabClose(false);
                fabMainClose();
                Log.d(TAG, "**" + TAG + "**onDrawerSlide**");

                String strTmp = "";
                try {
                    strTmp = getSupportFragmentManager().findFragmentById(R.id.fl_c_MainFragment).getTag();
                } catch (Exception e1) {
                    //e1.printStackTrace();
                    Log.d(TAG, "**" + TAG + "**onDrawerSlide**Failed1");

                    try {
                        strTmp = getSupportFragmentManager().findFragmentById(R.id.fl_c_MainVP).getTag();
                    } catch (Exception e2) {
                        //e2.printStackTrace();
                        Log.d(TAG, "**" + TAG + "**onDrawerSlide**Failed2");
                    }
                }

                try {
                    if (!fl_flag_now.equals(strTmp)){
                        int nav_int = flManager.getIdFromTagString(strTmp);

                        MenuItem item = mNavView.getMenu().findItem(nav_int);
                        item.setChecked(true);
                    }
                } catch (Exception e3) {
                    //e3.printStackTrace();
                    Log.d(TAG, "**" + TAG + "**onDrawerSlide**Failed3");
                }

            }
        };
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();                //初始化

        //initView();

        //if (firstOpen) updateData();

        //Google Sign-In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */
                        , this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        initLocationProvider();
        /*mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };*/


        //getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void initView(){
        Log.d(TAG, "**initView**Start");
        //Height
        float tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Height_before);
        int tmpH, tmpL;
        if (tmp != 0) {
            tmpH = (int) Math.floor(tmp);
            tmpL = (int) ((tmp - tmpH) * 100);
        } else {
            Cursor cursorH = helper.HeightSelect();
            if (cursorH.getCount()==0){
                tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_AC_Height);
            } else {
                cursorH.moveToLast();
                tmp = cursorH.getFloat(cursorH.getColumnIndex(HiDBHelper.KEY_Height_Height));
            }
            cursorH.close();
            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before, tmp);
            tmpH = (int) Math.floor(tmp);
            tmpL = (int) ((tmp - tmpH) * 100);
        }
        Log.d(TAG,"**III**"+"Height:"+tmp);
        mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_H, tmpH);
        mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_L, tmpL);

        //Weight
        tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_BI_Weight_before);
        if (tmp != 0) {
            tmpH = (int) Math.floor(tmp);
            tmpL = (int) ((tmp - tmpH) * 100);
        } else {
            Cursor cursorW = helper.WeightSelect();
            if (cursorW.getCount()==0){
                tmp = mInfoMap.IMgetFloat(HiDBHelper.KEY_AC_Weight);
            } else {
                cursorW.moveToLast();
                tmp = cursorW.getFloat(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Weight));
            }
            cursorW.close();

            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before, tmp);
            tmpH = (int) Math.floor(tmp);
            tmpL = (int) ((tmp - tmpH) * 100);
        }
        Log.d(TAG,"**III**"+"Weight:"+tmp);
        mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_H, tmpH);
        mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_L, tmpL);

        super.initView();

        TextView ac_user_name = (TextView)findViewById(R.id.ac_user_name);
        TextView ac_user_mail = (TextView)findViewById(R.id.ac_user_mail);
        ac_user_name.setText(mInfoMap.IMgetString(HiDBHelper.KEY_AC_Name));
        ac_user_mail.setText(mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account));
        firstOpen = false;
        mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst,false);

        CircleImageView acGoogleImage = (CircleImageView)findViewById(R.id.ac_image);
        try {
            String tmpStr = personPhoto.toString();
            try {
                Bitmap bitmap = new DownloadImageTask("acGoogleImage",acGoogleImage)
                        .execute(tmpStr).get();
                acGoogleImage.setImageBitmap(bitmap);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideProgressDialog();
        Log.d(TAG, "**initView**End");

        Calendar calendar = Calendar.getInstance();
        String crash = mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account);
        crash += " ** ";
        crash += mInfoMap.IMgetString(HiDBHelper.KEY_AC_Name);
        crash += " ** ";
        crash += calendar.toString();

        FirebaseCrash.log(crash);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*Log.d(TAG,"**Yes_onActivityResult**");
        fabOpen = true;
        fabMainClose();*/

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if (firstOpen) updateData();
        showProgressDialog();

        //mAuth.addAuthStateListener(mAuthListener);

        OptionalPendingResult<GoogleSignInResult> opr
                = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    //hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProgressDialog.isShowing() && !firstOpen){
            hideProgressDialog();
        }

        //FB
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveDataSP();

        //FB
        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {

        if (fabOpen)fabMainClose();

        //saveDataSP();

        super.onStop();

        /*if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        helper = new HiDBHelper(getApplicationContext());
        Log.d(TAG,"**Yes_onRestart**");
        //fabOpen = true;
        //if (fabOpen)fabMainClose();
    }

    @Override
    protected void onDestroy() {

        //saveDataSP();
        helper.close();
        mGoogleApiClient.stopAutoManage(this);

        super.onDestroy();
    }

    //TODO----Others----
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "**" + TAG + "**onBackPressed");
        Log.d(TAG, "**key33**");
        String strTmp = "";

        /*
        * fl_flag_temp = fl_flag_now;
          fl_flag_now = fl_flag_before;
          fl_flag_before = fl_flag_temp; */

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (fabOpen) {
            fabMainClose();
        } else {
            if (fl_flag != 1) {
                try {
                    strTmp = getSupportFragmentManager().findFragmentById(R.id.fl_c_MainFragment).getTag();
                } catch (Exception e1) {
                    //e1.printStackTrace();
                    Log.d(TAG, "**" + TAG + "**onBackPressed**Failed1");

                    try {
                        strTmp = getSupportFragmentManager().findFragmentById(R.id.fl_c_MainVP).getTag();
                    } catch (Exception e2) {
                        //e2.printStackTrace();
                        Log.d(TAG, "**" + TAG + "**onBackPressed**Failed2");
                    }
                }

                Log.d(TAG, "**" + TAG + "**onBackPressed" + strTmp);

                if (fl_flag_now.equals("fl_Chat") && !strTmp.equals("fl_Chat")){
                    this.flChange("fl_Chat");
                    MenuItem item = mNavView.getMenu().findItem(R.id.nav_Chat);
                    item.setChecked(true);

                }else if (fl_flag_now.equals("fl_Heart")){
                    this.flChange("fl_navHome");
                    MenuItem item = mNavView.getMenu().findItem(R.id.nav_Home);
                    item.setChecked(true);

                    id_nav_now = R.id.nav_Home;
                    id_nav_temp = 0;
                    id_nav_before = 0;

                    //mToolbar.setNavigationIcon(R.drawable.ic_nav_humburger);
                } else {
                    this.flChange("fl_navHome");
                    MenuItem item = mNavView.getMenu().findItem(R.id.nav_Home);
                    item.setChecked(true);

                    id_nav_now = R.id.nav_Home;
                    id_nav_temp = 0;
                    id_nav_before = 0;
                }

            } else {
                MainActivity2.this.finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        int vId = v.getId();

        if (vId == R.id.fab_Main && fl_flag_now.equals("fl_Map")) {

            //initLocationProvider();
            whereAmI();
            //Location location = G_Map.getMyLocation();
            //cameraFocusOnMe(location.getLatitude(), location.getLongitude());
            /*G_Map.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(
                            new LatLng(location.getLatitude()
                                    ,location.getLongitude()),16));*/
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        G_Map = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            G_Map.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        initLocationProvider();
        whereAmI();
        //經度
        //double lng = 121.5084509;
        //緯度
        //double lat = 25.0023781;
        //"我"
        //showMarkerMe(lat, lng);
        //cameraFocusOnMe(lat, lng);
        //G_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 16));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String itemIdString = item.toString();
        id_nav_temp = id_nav_now;
        //fl_flag_now = itemIdString;

        Log.d(TAG, "**" + TAG + "**NavButtonClick**" + itemIdString);

        if (itemId != id_nav_now) {
            id_nav_before = id_nav_temp;
            id_nav_now = itemId;
            flChange(flManager.getTagStringFromId(itemId));
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //TAG
        Log.d(TAG, "**" + TAG + "**onCreateOptionsMenu");

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TAG
        Log.d(TAG, "**" + TAG + "**onOptionsItemSelected");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            if (fl_flag_now.equals("fl_Heart")){
                Log.d(TAG,"**Home**false");
                return false;
            }
            Log.d(TAG,"**Home**true");
            return true;
        }
        Log.d(TAG,"HomeNot");
        return super.onOptionsItemSelected(item);
    }*/



    @Override
    public void flChange(String nav_s) {
        super.flChange(nav_s);

        Log.d(TAG,"**fl_Change**"+fl_flag_now);
    }

    private boolean initLocationProvider() {
        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*//1.選擇最佳提供器
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = locationMgr.getBestProvider(criteria, true);

        if (provider != null) {
            return true;
        }*/

        //2.選擇使用GPS提供器
        if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
            return true;
        }

        /*//3.選擇使用網路提供器
        if (locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
            return true;
        }*/
        return false;
    }

    private void whereAmI() {
        //取得上次已知的位置
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationMgr.getLastKnownLocation(provider);//
        updateWithNewLocation(location);
        //GPS Listener
        locationMgr.addGpsStatusListener(gpsListener);
        //Location Listener
        int minTime = 2000;//ms
        int minDist = 2;//meter
        locationMgr.requestLocationUpdates(provider, minTime, minDist,locationListener);
        if (location != null) {
            //經度
            double lng = location.getLongitude();
            //緯度
            double lat = location.getLatitude();
            cameraFocusOnMe(lat, lng);
            //G_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
        }
    }

    private void cameraFocusOnMe(double lat, double lng){
        CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(16)
                .build();
        G_Map.animateCamera(
                CameraUpdateFactory.newCameraPosition(camPosition)
                ,2000,null);
    }

    private void updateWithNewLocation(Location location) {
        String where = "";
        if (location != null) {
            //經度
            double lng = location.getLongitude();
            //緯度
            double lat = location.getLatitude();
            //速度
            float speed = location.getSpeed();

            where = "經度: " + lng +
                    "緯度: " + lat +
                    "\n速度: " + speed +
                    "\nProvider: " + provider;
            //"我"
            //showMarkerMe(lat, lng);
            cameraFocusOnMe(lat, lng);
            G_Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 16));
        }else{
            where = "~ ~ ~" ;
        }
        //顯示資訊
        //txtOutput.setText(where);
    }

    //TODO----Up Down
    @Override
    public void onBackStackChanged() {
        //shouldDisplayHomeUp();
    }

    /*public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }*/

    //TODO----SignInNeed----
    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();

                String personIdToken = acct.getIdToken();
                Log.d(TAG, "**" + TAG + "**HTTP_act1**" + personId);
                Log.d(TAG, "**" + TAG + "**HTTP_act2**" + personIdToken);
                //String tmp[] = new String[1];
                //tmp[0] = personIdToken;
                //new HttpPHP("sendIdToken",tmp).execute();
                //volleyMethod.vpostGet_IdToken(personIdToken);
                volleyManager.vpostGet_IdToken(personIdToken);
                //volleyMethod.vpost_GetAccountInfo();
                //TAG
                Log.d(TAG, "**" + TAG + "**acGot**" +
                        (personPhoto != null ? personPhoto.toString() : null));
            }

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {

        //TAG
        Log.d(TAG, "**" + TAG + "**signIn");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {

        //TAG
        Log.d(TAG, "**" + TAG + "**signOut");

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {

        //TAG
        Log.d(TAG, "**" + TAG + "**revokeAccess");

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void showProgressDialog() {

        //TAG
        Log.d(TAG, "**" + TAG + "**showProgressDialog");

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {

        //TAG
        Log.d(TAG, "**" + TAG + "**hideProgressDialog");

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void updateUI(boolean signedIn) {

        //TAG
        Log.d(TAG, "**"+TAG+"**updateUI**" + signedIn);

        if (signedIn) {
            Toast.makeText(this,"Google登入成功",Toast.LENGTH_SHORT).show();

            flag_google = true;

        } else {
            Toast.makeText(this,"Google登入失敗",Toast.LENGTH_SHORT).show();

            flag_google = false;
            signInDialog1();

        }
    }

    private void signInDialog1(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(getString(R.string.signIn1_title));
        builder1.setView(R.layout.sign_in1);

        builder1.setPositiveButton(R.string.signIn1_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.fabMainClose();
                        signInDialog2();

                    }
                });
        builder1.setNegativeButton(R.string.signIn_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //setBMI();
                        //MainActivity.fabMainClose();
                        updateData();
                        initView();
                    }
                });

        AlertDialog dialog1;
        dialog1 = builder1.create();
        dialog1.show();
        dialog1.setCancelable(false);

        TextView text1 = (TextView)dialog1.findViewById(R.id.signIn1_Text);
        assert text1 != null;
        text1.setText(getString(R.string.signIn1_content));

    }

    private void signInDialog2(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle(getString(R.string.signIn2_title));
        builder2.setView(R.layout.sign_in2);

        builder2.setPositiveButton(R.string.signIn2_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.fabMainClose();
                        signInDialog1();
                    }
                });
        builder2.setNegativeButton(R.string.signIn_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateData();
                        initView();
                        //setBMI();
                        //MainActivity.fabMainClose();
                    }
                });

        final AlertDialog dialog2;
        dialog2 = builder2.create();
        dialog2.show();
        dialog2.setCancelable(false);

        TextView text2 = (TextView)dialog2.findViewById(R.id.signIn2_Text);
        assert text2 != null;
        text2.setText(getString(R.string.signIn2_content));
        Button google_bt = (Button)dialog2.findViewById(R.id.signio_google_bt);
        assert google_bt != null;
        google_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_google) {
                    signIn();
                    dialog2.cancel();
                } else {
                    signOut();
                }
            }
        });
    }

    public ArrayList<String> arrayListInfo = null;
    public void signInDialog3(final int ii){
        if (ii<arrayListInfo.size()){
            String tmpList = arrayListInfo.get(ii);
            switch (tmpList) {
                case HiDBHelper.KEY_AC_Gender:
                    BI_Dialog(ii,HiDBHelper.KEY_AC_Gender, R.string.bi_gender_title, R.layout.bi_gender);
                    break;
                case HiDBHelper.KEY_AC_Height:
                    BI_Dialog(ii,HiDBHelper.KEY_AC_Height, R.string.Dialog_Title_Height, R.layout.number_picker_sign);
                    break;
                case HiDBHelper.KEY_AC_Weight:
                    BI_Dialog(ii,HiDBHelper.KEY_AC_Weight, R.string.Dialog_Title_Weight, R.layout.number_picker_sign);
                    break;
                case HiDBHelper.KEY_AC_Birthday:
                    DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            @SuppressLint("DefaultLocale")
                            String strDate = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Birthday,strDate);
                            signInDialog3(ii+1);
                        }
                    }, 2000, 6, 7);
                    datePickerDialog.setTitle(R.string.bi_birthday_title);

                    datePickerDialog.show();
                    break;
                case HiDBHelper.KEY_AC_WakeTime:
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            @SuppressLint("DefaultLocale")
                            String strTime1 = String.format("%02d:%02d:00",hourOfDay,minute);
                            mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime,strTime1);
                            CT48 ct48 = new CT48(strTime1);
                            mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime48,ct48.getCt48());
                            String apm = "a.m.";
                            if (hourOfDay>12){
                                hourOfDay -=12;
                                apm = "p.m.";
                            }
                            @SuppressLint("DefaultLocale")
                            String strTime2 = String.format("%02d:%02d %s",hourOfDay,minute,apm);
                            signInDialog3(ii+1);
                        }
                    }, 6, 0,false);
                    timePickerDialog.setTitle(R.string.bi_wake_time_title);

                    timePickerDialog.show();

                    break;
                default:
                    signInDialog3(ii+1);
            }
        } else {
            for (int jj=0;jj<arrayListInfo.size();jj++){
                String tmpStr = arrayListInfo.get(jj);
                if (!mInfoMap.IMgetString(tmpStr).equals("null")
                        && !mInfoMap.IMgetString(tmpStr).equals(""))
                    //volleyMethod.vpostSend_ACJson(tmpStr,mInfoMap.IMgetString(tmpStr));
                    volleyManager.vpostSend_ACJson(tmpStr,mInfoMap.IMgetString(tmpStr));
            }
            arrayListInfo = null;
            saveDataSP();

            //volleyMethod.vpostSend_ACJson();
            Log.d(TAG, "**AccountInfoGet**DialogOver");
            if (mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst)){
                //volleyMethod.VVvpost_GetWeightRecord("","");
                volleyManager.VVvpost_GetWeightRecord("","");
            } else {
                initView();
            }

        }
    }


    private void BI_Dialog(final int ii, final String info, int title, int layout){
        String endStr = "";
        int NP_H = 0,NP_L = 0;
        if (info.equals(HiDBHelper.KEY_AC_Height)){
            NP_H = 165;
            NP_L = 0;
        } else if (info.equals(HiDBHelper.KEY_AC_Weight)){
            NP_H = 50;
            NP_L = 0;
        }

        final int[] tmpH = {NP_H};
        final int[] tmpL = {NP_L};

        int okbt = R.string.signIn1_button_OK;

        AlertDialog.Builder builderBI = new AlertDialog.Builder(this);
        AlertDialog dialogBI = null;

        builderBI.setTitle(getString(title));
        builderBI.setView(layout);
        if (ii == arrayListInfo.size()-1){
            okbt = R.string.signIn_button_OK;
        }

        builderBI.setPositiveButton(okbt
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.fabMainClose();

                        if (info.equals(HiDBHelper.KEY_AC_Gender)){
                            RadioGroup group = (RadioGroup)((AlertDialog)dialog).findViewById(R.id.Diary_Sex_Group);
                            assert group != null;
                            group.getCheckedRadioButtonId();

                            int p = group.getCheckedRadioButtonId();
                            switch (p){
                                case R.id.Diary_Sex_Male_RB:
                                    mInfoMap.IMput(info,"male");
                                    break;
                                case R.id.Diary_Sex_Female_RB:
                                    mInfoMap.IMput(info,"female");
                                    break;
                            }
                        } else if (info.equals(HiDBHelper.KEY_AC_Height)){
                            float tmpFloat = 0;
                            if (tmpH[0] == 0 && tmpL[0] == 0) {
                                tmpFloat = 1f;
                            } else {
                                tmpFloat = (float) (tmpH[0] + tmpL[0]*0.01);
                            }
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Height,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_H,tmpH[0]);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_L,tmpL[0]);
                        } else if (info.equals(HiDBHelper.KEY_AC_Weight)){
                            float tmpFloat = 0;
                            if (tmpH[0] == 0 && tmpL[0] == 0) {
                                tmpFloat = 1f;
                            } else {
                                tmpFloat = (float) (tmpH[0] + tmpL[0]*0.01);
                            }
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Weight,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_H,tmpH[0]);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_L,tmpL[0]);
                        }
                        signInDialog3(ii+1);
                    }
                });
        /*builderBI.setNegativeButton(R.string.signIn_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //setBMI();
                        //MainActivity.fabMainClose();
                    }
                });*/

        dialogBI = builderBI.create();
        dialogBI.show();
        dialogBI.setCancelable(false);

        if (info.equals(HiDBHelper.KEY_AC_Height) || info.equals(HiDBHelper.KEY_AC_Weight)){
            NumberPicker mNP_HW_I,mNP_HW_D;
            mNP_HW_I = (NumberPicker) dialogBI.findViewById(R.id.Diary_HW_Int);
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
            mNP_HW_I.setValue(NP_H);

            mNP_HW_D = (NumberPicker) dialogBI.findViewById(R.id.Diary_HW_Dec);
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
            mNP_HW_D.setValue(NP_L);
        }

    }

    public void AccountInfoGet(JSONObject dataObject){
        ArrayList<String> arrayList = new ArrayList<>();
        String strTmp = null;
        try {
            Log.d(TAG, "**AccountInfoGet:" + dataObject.toString());
            Log.d(TAG, "**AccountInfoGet:" + dataObject.getString("account"));
            Log.d(TAG, "**AccountInfoGet:" + dataObject.getString("register_time"));
            Log.d(TAG, "**AccountInfoGet:" + dataObject.getString("register_time").substring(0,10));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_uid);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_uid,strTmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_initDate);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_initDate,strTmp.substring(0, 10));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_LoginType);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_LoginType,strTmp);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Objects.equals(strTmp, "google")){
                        mInfoMap.IMput(HiDBHelper.KEY_AC_GoogleMail,dataObject.getString(HiDBHelper.KEY_AC_Account));
                        mInfoMap.IMput(HiDBHelper.KEY_AC_Account,dataObject.getString(HiDBHelper.KEY_AC_Account));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Locale);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Locale,strTmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {///////////////////////////////////////////
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Name);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Name,strTmp);
            } else arrayList.add(HiDBHelper.KEY_AC_Name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {///////////////////////////////////////////
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Gender);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Gender,strTmp);
            } else arrayList.add(HiDBHelper.KEY_AC_Gender);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {///////////////////////////////////////////
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Height);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Height,strTmp);
            } else arrayList.add(HiDBHelper.KEY_AC_Height);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {///////////////////////////////////////////
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Weight);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Weight,strTmp);
            } else arrayList.add(HiDBHelper.KEY_AC_Weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {///////////////////////////////////////////
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Birthday);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Birthday,strTmp);
            } else arrayList.add(HiDBHelper.KEY_AC_Birthday);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {///////////////////////////////////////////
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_WakeTime);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime,strTmp);
                CT48 ct48 = new CT48(strTmp);
                mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime48,ct48.getCt48());
            } else arrayList.add(HiDBHelper.KEY_AC_WakeTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            strTmp = dataObject.getString(HiDBHelper.KEY_AC_Image_url);
            if (strTmp!=null && !strTmp.equals("null") && !strTmp.equals("")){
                mInfoMap.IMput(HiDBHelper.KEY_AC_Image_url,strTmp);
                //volleyMethod.vpost_GetAccountImage(strTmp);
                volleyManager.vpost_GetAccountImage(strTmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "**AccountInfoGet**Dialog**"+arrayList.size());
        if (arrayList.size()>0){
            Log.d(TAG, "**AccountInfoGet**Dialog");
            arrayListInfo = arrayList;
            signInDialog3(0);
        } else {
            Log.d(TAG, "**AccountInfoGet**NoDialog");
            saveDataSP();

            if (mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst)){
                //volleyMethod.VVvpost_GetWeightRecord("","");
                volleyManager.VVvpost_GetWeightRecord("","");
            } else {
                initView();
            }
        }
    }

    /*public void FirebaseSignIn(){
        mAuth.createUserWithEmailAndPassword(mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account), mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account))
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    public void showPreProgressDialog() {

        //TAG
        Log.d(TAG, "**" + TAG + "**Pre**showProgressDialog");

        if (mPreProgressDialog == null) {
            mPreProgressDialog = new ProgressDialog(this);
            mPreProgressDialog.setMessage("伺服器運算中");
            mPreProgressDialog.setIndeterminate(true);
        }

        mPreProgressDialog.show();
    }

    public void hidePreProgressDialog() {

        //TAG
        Log.d(TAG, "**" + TAG + "**Pre**hideProgressDialog");

        if (mPreProgressDialog != null && mPreProgressDialog.isShowing()) {
            mPreProgressDialog.hide();
        }
    }


}
