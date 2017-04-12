package com.asaewing.healthimprover.app2.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.CT48;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.Others.InfoMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DataManager {

    private MainActivity2 mContext;
    private String mTAG;
    private InfoMap mInfoMap;
    private HiDBHelper helper;

    public DataManager(MainActivity2 context, 
                       String TAG,
                       InfoMap infoMap,
                       HiDBHelper hiDBHelper){
        mContext = context;
        mTAG = TAG;
        mInfoMap = infoMap;
        helper = hiDBHelper;
    }

    public boolean updateData(){
        Log.d(mTAG, "**" + mTAG + "**upDialog");

        final int[] CA_Count = {0};
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                double tmpDouble = 0;

                Cursor cursorAll = helper.HiSelect();
                CA_Count[0] = cursorAll.getCount();
                Log.d(mTAG,"**InfoMap**"+cursorAll.getCount());
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
                    if (!mContext.flag_FCM){
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
                    if (!mContext.flag_FCM){
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
                    if (mContext.flag_FCM){
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
                Log.d(mTAG,"**III**"+"Height:"+tmp);
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
                Log.d(mTAG,"**III**"+"Weight:"+tmp);
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
            Log.d(mTAG,"**Thread**join**");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (thread.isAlive()){
            Log.d(mTAG,"**Alive**");
        }

        //initView();

        Cursor cursor = helper.HiSelect();
        cursor.moveToFirst();
        int ii=0,jj=0;
        Log.d(mTAG,"**Thread**cursor.getCount()**"+cursor.getCount()+"**"+cursor.getColumnCount());
        if (cursor.getCount()>0){
            for (ii=0;ii<cursor.getColumnCount();ii++) {
                Log.d(mTAG,"**AliveNo1**"+ii+"**"+cursor.getColumnName(ii)+"***"+cursor.getString(ii));
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
            Log.d(mTAG,"**Thread**false**"+jj);
            //initView();
            return false;

        } else {
            Log.d(mTAG,"**Thread**true**"+jj);
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

                Log.d(mTAG,"**InfoMap2**"+ Height[0] +"**"+ Weight[0]);

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
            Log.d(mTAG,"**Thread2**join**");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (thread.isAlive()){
            Log.d(mTAG,"**Alive2**");
            try {
                this.wait(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Cursor cursor = helper.HiSelect();
        cursor.moveToFirst();
        int ii=0,jj=0;
        Log.d(mTAG,"**Thread**cursor.getCount()**"+cursor.getCount()+"**"+cursor.getColumnCount());
        if (cursor.getCount()>0){
            for (ii=0;ii<cursor.getColumnCount();ii++) {
                Log.d(mTAG,"**AliveNo2**"+ii+"**"+cursor.getColumnName(ii)+"***"+cursor.getString(ii));
                if (cursor.getString(ii).length()==0) jj++;
            }
        }

        cursor.close();
    }
    
    public void AccountInfoGet(JSONObject dataObject){
        ArrayList<String> arrayList = new ArrayList<>();
        String strTmp = null;
        try {
            Log.d(mTAG, "**AccountInfoGet:" + dataObject.toString());
            Log.d(mTAG, "**AccountInfoGet:" + dataObject.getString("account"));
            Log.d(mTAG, "**AccountInfoGet:" + dataObject.getString("register_time"));
            Log.d(mTAG, "**AccountInfoGet:" + dataObject.getString("register_time").substring(0,10));

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
                mContext.volleyManager.vpost_GetAccountImage(strTmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**AccountInfoGet**Dialog**"+arrayList.size());
        if (arrayList.size()>0){
            Log.d(mTAG, "**AccountInfoGet**Dialog");
            mContext.arrayListInfo = arrayList;
            mContext.signInDialog3(0);
        } else {
            Log.d(mTAG, "**AccountInfoGet**NoDialog");
            saveDataSP();

            if (mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst)){
                //volleyMethod.VVvpost_GetWeightRecord("","");
                mContext.volleyManager.VVvpost_GetWeightRecord("","");
            } else {
                mContext.initView();
            }
        }
    }
}
