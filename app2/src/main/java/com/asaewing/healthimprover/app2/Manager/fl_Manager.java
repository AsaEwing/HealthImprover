package com.asaewing.healthimprover.app2.Manager;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.asaewing.healthimprover.app2.R;
import com.asaewing.healthimprover.app2.fl.fl_AR_Exp;
import com.asaewing.healthimprover.app2.fl.fl_BasicInfo;
import com.asaewing.healthimprover.app2.fl.fl_Camera;
import com.asaewing.healthimprover.app2.fl.fl_Chat;
import com.asaewing.healthimprover.app2.fl.fl_Heart;
import com.asaewing.healthimprover.app2.fl.fl_MyDevice;
import com.asaewing.healthimprover.app2.fl.fl_Note;
import com.asaewing.healthimprover.app2.fl.fl_Notification;
import com.asaewing.healthimprover.app2.fl.fl_ProductTour;
import com.asaewing.healthimprover.app2.fl.fl_Record;
import com.asaewing.healthimprover.app2.fl.fl_Settings;
import com.asaewing.healthimprover.app2.fl.fl_Test01;
import com.asaewing.healthimprover.app2.fl.fl_Test02;

public class fl_Manager {

    private Context mContext;
    private String mTAG;

    public fl_Manager(Context context, String TAG){
        mContext = context;
        mTAG = TAG;
    }

    /** getTagStringFromId>>負責將nav傳過來的id轉成string **/
    public String getTagStringFromId(int nav_id) {
        //getTagStringFromId > getTagStringFromId
        String nav_s = "OS";

        switch (nav_id) {
            case R.id.nav_Home:
                nav_s = "fl_navHome";
                break;
            case R.id.nav_Map:
                nav_s = "fl_Map";
                break;
            case R.id.nav_Chat:
                nav_s = "fl_Chat";
                break;
            case R.id.nav_BasicInfo:
                nav_s = "fl_BasicInfo";
                break;
            //case R.id.nav_MyDevice:
            //    nav_s = "fl_MyDevice";
            //    break;
            case R.id.nav_Heart:
                nav_s = "fl_Heart";
                break;
            /*case R.id.nav_ProductTour:
                nav_s = "fl_ProductTour";
                break;
            case R.id.nav_AR_Exp:
                nav_s = "fl_AR_Exp";
                break;
            case R.id.nav_Settings:
                nav_s = "fl_Settings";
                break;
            case R.id.nav_Test01:
                nav_s = "fl_Test01";
                break;
            case R.id.nav_Test02:
                nav_s = "fl_Test02";
                break;*/
            case R.id.nav_Note:
                nav_s = "fl_Note";
                break;
            case R.id.nav_Camera:
                nav_s = "fl_Camera";
                break;
            case R.id.nav_Notification:
                nav_s = "fl_Notification";
                break;
            case R.id.nav_Record:
                nav_s = "fl_Record";
                break;
        }

        Log.d(mTAG,"**"+mTAG+"**getTagStringFromId**"+nav_id+"**"+nav_s);

        return nav_s;
    }

    /** getTagStringFromId>>負責將nav傳過來的String轉成Id **/
    public int getIdFromTagString(String nav_s) {
        //getIdFromTagString > getIdFromTagString
        int nav_int = 0;

        switch (nav_s) {
            case "fl_navHome":
                nav_int = R.id.nav_Home;
                break;
            case "fl_Map":
                nav_int = R.id.nav_Map;
                break;
            case "fl_Chat":
                nav_int = R.id.nav_Chat;
                break;
            case "fl_BasicInfo":
                nav_int = R.id.nav_BasicInfo;
                break;
            //case "fl_MyDevice":
            //    nav_int = R.id.nav_MyDevice;
            //    break;
            case "fl_Heart":
                nav_int = R.id.nav_Heart;
                break;
            /*case "fl_ProductTour":
                nav_int = R.id.nav_ProductTour;
                break;
            case "fl_AR_Exp":
                nav_int = R.id.nav_AR_Exp;
                break;
            case "fl_Settings":
                nav_int = R.id.nav_Settings;
                break;
            case "fl_Test01":
                nav_int = R.id.nav_Test01;
                break;
            case "fl_Test02":
                nav_int = R.id.nav_Test02;
                break;*/
            case "fl_Note":
                nav_int = R.id.nav_Note;
                break;
            case "fl_Camera":
                nav_int = R.id.nav_Camera;
                break;
            case "fl_Notification":
                nav_int = R.id.nav_Notification;
                break;
            case "fl_Record":
                nav_int = R.id.nav_Record;
                break;
        }

        return nav_int;
    }

    /** getFragmentFromTagString>>負責fl轉換，Fragment轉換 **/
    @Nullable
    public Fragment getFragmentFromTagString(String nav_s) {
        //getFragmentFromTagString > getFragmentFromTagString
        switch (nav_s) {
            case "fl_Chat":
                return fl_Chat.newInstance();

            case "fl_BasicInfo":
                return fl_BasicInfo.newInstance();

            case "fl_MyDevice":
                return fl_MyDevice.newInstance();

            case "fl_Heart":
                return fl_Heart.newInstance();

            case "fl_ProductTour":
                return fl_ProductTour.newInstance();

            case "fl_AR_Exp":
                return fl_AR_Exp.newInstance();

            case "fl_Settings":
                return fl_Settings.newInstance();

            case "fl_Test01":
                return fl_Test01.newInstance();

            case "fl_Test02":
                return fl_Test02.newInstance();

            /*case "fl_Item":
                return fl_Item.newInstance();

            case "fl_Record":
                return fl_Record.newInstance();

            case "fl_Picture":
                return fl_Picture.newInstance();*/


            case "fl_Note":
                return fl_Note.newInstance();
            case "fl_Camera":
                return fl_Camera.newInstance();
            case "fl_Notification":
                return fl_Notification.newInstance();
            case "fl_Record":
                return fl_Record.newInstance();

        }

        return null;
    }

    /** getTitleFromTagString>>負責fl轉換，Title轉換 **/
    @Nullable
    public String getTitleFromTagString(String nav_s) {
        //getTitleFromTagString > getTitleFromTagString
        switch (nav_s) {
            case "fl_Diary":
                return mContext.getString(R.string.nav_Diary);

            case "fl_Calories":
                return mContext.getString(R.string.nav_Calories);

            case "fl_Sleep":
                return mContext.getString(R.string.nav_Sleep);

            case "fl_Chat":
                return mContext.getString(R.string.nav_Chat);

            case "fl_BasicInfo":
                return mContext.getString(R.string.nav_BasicInfo);

            case "fl_MyDevice":
                return mContext.getString(R.string.nav_MyDevice);

            case "fl_Heart":
                return mContext.getString(R.string.nav_Heart);

            case "fl_ProductTour":
                return mContext.getString(R.string.nav_ProductTour);

            case "fl_AR_Exp":
                return mContext.getString(R.string.nav_AR_Exp);

            case "fl_Settings":
                return mContext.getString(R.string.nav_Settings);

            case "fl_Test01":
                return mContext.getString(R.string.nav_Test01);

            case "fl_Test02":
                return mContext.getString(R.string.nav_Test02);

            //  case "fl_Item":
            //      return getString(R.string.nav_Item);


            case "fl_Note":
                return mContext.getString(R.string.nav_Note);
            case "fl_Camera":
                return mContext.getString(R.string.nav_Camera);
            case "fl_Notification":
                return mContext.getString(R.string.nav_Notification);
            case "fl_Record":
                return mContext.getString(R.string.nav_Record);

        }

        return null;
    }
}
