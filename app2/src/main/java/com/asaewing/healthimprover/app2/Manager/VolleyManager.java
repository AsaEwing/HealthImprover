package com.asaewing.healthimprover.app2.Manager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
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
import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.CT48;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.Others.InfoMap;
import com.asaewing.healthimprover.app2.fl.fl_Diary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class VolleyManager implements Parcelable {

    private MainActivity2 mContext;
    private String mTAG;
    private InfoMap mInfoMap;
    private HiDBHelper helper;
    private DataManager dataManager;

    public VolleyManager(MainActivity2 context,
                         String TAG){
        mContext = context;
        mTAG = TAG+" , VolleyManager";
        this.dataManager = mContext.getDataManager();
        mInfoMap = dataManager.mInfoMap;
        helper = dataManager.helper;

        //this.mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
        mRequestQueue.addRequestFinishedListener(
                new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request request) {

                if (request.getTag()=="vpostGet_IdToken"){
                    if (mContext.firstOpen){
                        Log.d(mTAG,"**Data22**");
                        boolean update = mContext.getDataManager().updateData();
                        if (update){
                            Log.d(mTAG,"**Data33**");
                            vpost_GetAccountInfo();
                            //vpostSend_FCMId();
                        } else {
                            mContext.initView();
                        }
                    }
                } else if (request.getTag()=="vpost_GetAccountInfo"){
                        /*Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, uid);
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, mInfoMap.IMgetString(HiDBHelper.KEY_AC_Name));
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                        */
                    vpostSend_FCMId();
                } else if (request.getTag()=="vpostSend_FCMId"){
                    //VVvpost_GetWeightRecord("","");

                } else if (request.getTag()=="vpost_GetWeightRecord"){
                    if (countOp>1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetWeightRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
                        Log.d(mTAG,"**W_countOp0**"+countOp);
                    } else if (countOp==1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetWeightRecord(df.format(tmpcStartW.getTime()),"");
                        Log.d(mTAG,"**W_countOp1**"+countOp);
                    } else {
                        dayDiffW = 0;
                        countOp = 0;
                        cStartW.clear();
                        cEndW.clear();
                        tmpcStartW.clear();
                        tmpcEndW.clear();
                        Log.d(mTAG,"**W_countOp2**"+countOp);
                        //if (firstOpen) initView();
                        VVvpost_GetCalInRecord("","");
                    }
                    Log.d(mTAG,"**W_countOp3**"+countOp);
                    countOp--;
                } else if (request.getTag()=="vpost_GetCalInRecord"){
                    if (countOp>1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetCalInRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
                        Log.d(mTAG,"**Ci_countOp0**"+countOp);
                    } else if (countOp==1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetCalInRecord(df.format(tmpcStartW.getTime()),"");
                        Log.d(mTAG,"**Ci_countOp1**"+countOp);
                    } else {
                        dayDiffW = 0;
                        countOp = 0;
                        cStartW.clear();
                        cEndW.clear();
                        tmpcStartW.clear();
                        tmpcEndW.clear();
                        Log.d(mTAG,"**Ci_countOp2**"+countOp);
                        //if (firstOpen) initView();
                        VVvpost_GetCalOutRecord("","");
                    }
                    Log.d(mTAG,"**Ci_countOp3**"+countOp);
                    countOp--;
                } else if (request.getTag()=="vpost_GetCalOutRecord"){
                    if (countOp>1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetCalOutRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
                        Log.d(mTAG,"**Co_countOp0**"+countOp);
                    } else if (countOp==1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetCalOutRecord(df.format(tmpcStartW.getTime()),"");
                        Log.d(mTAG,"**Co_countOp1**"+countOp);
                    } else {
                        dayDiffW = 0;
                        countOp = 0;
                        cStartW.clear();
                        cEndW.clear();
                        tmpcStartW.clear();
                        tmpcEndW.clear();
                        Log.d(mTAG,"**Co_countOp2**"+countOp);
                        //if (firstOpen) initView();
                        VVvpost_GetTargetRecord("","");
                    }
                    Log.d(mTAG,"**Co_countOp3**"+countOp);
                    countOp--;
                } else if (request.getTag()=="vpost_GetTargetRecord"){
                    if (countOp>1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetTargetRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
                        Log.d(mTAG,"**T_countOp0**"+countOp);
                    } else if (countOp==1){
                        tmpcStartW.add(Calendar.DAY_OF_YEAR,6);
                        tmpcEndW.add(Calendar.DAY_OF_YEAR,6);
                        vpost_GetTargetRecord(df.format(tmpcStartW.getTime()),"");
                        Log.d(mTAG,"**T_countOp1**"+countOp);
                    } else {
                        dayDiffW = 0;
                        countOp = 0;
                        cStartW.clear();
                        cEndW.clear();
                        tmpcStartW.clear();
                        tmpcEndW.clear();
                        Log.d(mTAG,"**T_countOp2**"+countOp);

                        if (mContext.firstOpen) mContext.initView();
                    }
                    Log.d(mTAG,"**T_countOp3**"+countOp);
                    countOp--;
                } else if (request.getTag()=="vpostGet_Pre"){
                    mContext.hidePreProgressDialog();
                }
            }
        });
    }

    public RequestQueue mRequestQueue;
    private String sURL_Server = "http://asa-asa.noip.me:4";
    //private String sURL_Server = "http://192.168.1.4";
    private String sURL_getGoogleIdToken = sURL_Server+"/weight_control/app_web_vers/google_verified.php";
    private String sURL_InfoPutGet = sURL_Server+"/weight_control/app_web_vers/record_modify.php";
    private String sURL_sendAccountInfo = sURL_Server+"/weight_control/app_web_vers/profile_modify.php";
    private String sURL_FCMID_Send = sURL_Server+"/weight_control/app_web_vers/fcm_register.php";
    private String sURL_Recommend_Send = sURL_Server+"/weight_control/app_web_vers/recommend_accepter.php";
    private String sURL_Pre_Get = sURL_Server+"/weight_control/app_web_vers/command_handler.php";

    public String uid = "";
    public String idToken = "";

    private String TAG = "VolleyMethod";

    //private String pre_Target = "PreActivity.py %d %04d/%02d/%02d %d 2 %.2f";
    //private String pre_NoTarget = "PreActivity.py %d %04d/%02d/%02d %d 3";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    int dayDiffW,countOp;
    Calendar cStartW,cEndW,tmpcStartW,tmpcEndW;
    int dayDiffC,countOpC;
    Calendar cStartC,cEndC,tmpcStartC,tmpcEndC;


    public void VVvpost_GetWeightRecord(String startDate,String endDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Log.d(mTAG,"**GetWeightRecord**Day**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetWeightRecord**Day**02**"+df.format(c2.getTime()));

        String date1=startDate;
        if (startDate.equals("")){
            date1 = mInfoMap.IMgetString(HiDBHelper.KEY_AC_initDate);
        }
        try {
            c1.setTime(df.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!endDate.equals("")){
            try {
                c2.setTime(df.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d(mTAG,"**GetWeightRecord**Day**03**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetWeightRecord**Day**04**"+df.format(c2.getTime()));

        int rangeYear = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        dayDiffW = day2-day1;
        for (int ii=c1.get(Calendar.YEAR);ii<c1.get(Calendar.YEAR)+rangeYear;ii++){
            if ((ii%4)==0)dayDiffW += 366;
            else dayDiffW += 365;
        }
        countOp = (int)((double)dayDiffW/6)+1;
        cStartW = Calendar.getInstance();
        cEndW = Calendar.getInstance();
        cStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        cEndW.set(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.HOUR_OF_DAY));
        tmpcStartW = Calendar.getInstance();
        tmpcEndW = Calendar.getInstance();
        tmpcStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.add(Calendar.DAY_OF_YEAR,5);

        Log.d(mTAG,"**GetWeightRecord**Day**05**"+dayDiffW);
        Log.d(mTAG,"**GetWeightRecord**Day**00**count**"+countOp);
        Log.d(mTAG,"**GetWeightRecord**Day**DayTest**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetWeightRecord**Day**DayTest**02**"+df.format(c2.getTime()));
        Log.d(mTAG,"**GetWeightRecord**Day**DayTest**01**"+df.format(tmpcStartW.getTime()));
        Log.d(mTAG,"**GetWeightRecord**Day**DayTest**02**"+df.format(tmpcEndW.getTime()));

        vpost_GetWeightRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
    }

    public void VVvpost_GetCalInRecord(String startDate,String endDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Log.d(mTAG,"**GetCalInRecord**Day**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetCalInRecord**Day**02**"+df.format(c2.getTime()));

        String date1=startDate;
        if (startDate.equals("")){
            date1 = mInfoMap.IMgetString(HiDBHelper.KEY_AC_initDate);
        }
        try {
            c1.setTime(df.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!endDate.equals("")){
            try {
                c2.setTime(df.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d(mTAG,"**GetCalInRecord**Day**03**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetCalInRecord**Day**04**"+df.format(c2.getTime()));

        int rangeYear = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        dayDiffW = day2-day1;
        for (int ii=c1.get(Calendar.YEAR);ii<c1.get(Calendar.YEAR)+rangeYear;ii++){
            if ((ii%4)==0)dayDiffW += 366;
            else dayDiffW += 365;
        }
        countOp = (int)((double)dayDiffW/6)+1;
        cStartW = Calendar.getInstance();
        cEndW = Calendar.getInstance();
        cStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        cEndW.set(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.HOUR_OF_DAY));
        tmpcStartW = Calendar.getInstance();
        tmpcEndW = Calendar.getInstance();
        tmpcStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.add(Calendar.DAY_OF_YEAR,5);

        Log.d(mTAG,"**GetCalInRecord**Day**05**"+dayDiffW);
        Log.d(mTAG,"**GetCalInRecord**Day**00**count**"+countOp);
        Log.d(mTAG,"**GetCalInRecord**Day**DayTest**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetCalInRecord**Day**DayTest**02**"+df.format(c2.getTime()));
        Log.d(mTAG,"**GetCalInRecord**Day**DayTest**01**"+df.format(tmpcStartW.getTime()));
        Log.d(mTAG,"**GetCalInRecord**Day**DayTest**02**"+df.format(tmpcEndW.getTime()));

        vpost_GetCalInRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
    }

    public void VVvpost_GetCalOutRecord(String startDate,String endDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Log.d(mTAG,"**GetCalOutRecord**Day**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetCalOutRecord**Day**02**"+df.format(c2.getTime()));

        String date1=startDate;
        if (startDate.equals("")){
            date1 = mInfoMap.IMgetString(HiDBHelper.KEY_AC_initDate);
        }
        try {
            c1.setTime(df.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!endDate.equals("")){
            try {
                c2.setTime(df.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d(mTAG,"**GetCalOutRecord**Day**03**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetCalOutRecord**Day**04**"+df.format(c2.getTime()));

        int rangeYear = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        dayDiffW = day2-day1;
        for (int ii=c1.get(Calendar.YEAR);ii<c1.get(Calendar.YEAR)+rangeYear;ii++){
            if ((ii%4)==0)dayDiffW += 366;
            else dayDiffW += 365;
        }
        countOp = (int)((double)dayDiffW/6)+1;
        cStartW = Calendar.getInstance();
        cEndW = Calendar.getInstance();
        cStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        cEndW.set(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.HOUR_OF_DAY));
        tmpcStartW = Calendar.getInstance();
        tmpcEndW = Calendar.getInstance();
        tmpcStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.add(Calendar.DAY_OF_YEAR,5);

        Log.d(mTAG,"**GetCalOutRecord**Day**05**"+dayDiffW);
        Log.d(mTAG,"**GetCalOutRecord**Day**00**count**"+countOp);
        Log.d(mTAG,"**GetCalOutRecord**Day**DayTest**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetCalOutRecord**Day**DayTest**02**"+df.format(c2.getTime()));
        Log.d(mTAG,"**GetCalOutRecord**Day**DayTest**01**"+df.format(tmpcStartW.getTime()));
        Log.d(mTAG,"**GetCalOutRecord**Day**DayTest**02**"+df.format(tmpcEndW.getTime()));

        vpost_GetCalOutRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
    }

    public void VVvpost_GetTargetRecord(String startDate,String endDate){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        Log.d(mTAG,"**GetTargetRecord**Day**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetTargetRecord**Day**02**"+df.format(c2.getTime()));

        String date1=startDate;
        if (startDate.equals("")){
            date1 = mInfoMap.IMgetString(HiDBHelper.KEY_AC_initDate);
        }
        try {
            c1.setTime(df.parse(date1));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!endDate.equals("")){
            try {
                c2.setTime(df.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Log.d(mTAG,"**GetTargetRecord**Day**03**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetTargetRecord**Day**04**"+df.format(c2.getTime()));

        int rangeYear = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        dayDiffW = day2-day1;
        for (int ii=c1.get(Calendar.YEAR);ii<c1.get(Calendar.YEAR)+rangeYear;ii++){
            if ((ii%4)==0)dayDiffW += 366;
            else dayDiffW += 365;
        }
        countOp = (int)((double)dayDiffW/6)+1;
        cStartW = Calendar.getInstance();
        cEndW = Calendar.getInstance();
        cStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        cEndW.set(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.HOUR_OF_DAY));
        tmpcStartW = Calendar.getInstance();
        tmpcEndW = Calendar.getInstance();
        tmpcStartW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.set(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.HOUR_OF_DAY));
        tmpcEndW.add(Calendar.DAY_OF_YEAR,5);

        Log.d(mTAG,"**GetTargetRecord**Day**05**"+dayDiffW);
        Log.d(mTAG,"**GetTargetRecord**Day**00**count**"+countOp);
        Log.d(mTAG,"**GetTargetRecord**Day**DayTest**01**"+df.format(c1.getTime()));
        Log.d(mTAG,"**GetTargetRecord**Day**DayTest**02**"+df.format(c2.getTime()));
        Log.d(mTAG,"**GetTargetRecord**Day**DayTest**01**"+df.format(tmpcStartW.getTime()));
        Log.d(mTAG,"**GetTargetRecord**Day**DayTest**02**"+df.format(tmpcEndW.getTime()));

        vpost_GetTargetRecord(df.format(tmpcStartW.getTime()),df.format(tmpcEndW.getTime()));
    }

    public void vpostGet_IdToken(final String sendStr) {
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, sURL_getGoogleIdToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("请求结果:" + response);
                        Log.d(mTAG, "**vpostGet_IdToken1**:" + response);

                        if (response.contains("&")){
                            uid = response.substring(0,response.indexOf("&"));
                            idToken = response.substring(response.indexOf("&")+1);
                        }

                        Toast.makeText(mContext,"MS登入成功",Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //System.out.println("请求错误:" + error.toString());
                        Log.d(mTAG, "vpostGet_IdToken_Error:" + error.toString());

                        Toast.makeText(mContext,"MS登入失敗",Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("idtoken", sendStr);
                return hashMap;
            }

            // Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            /*public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }*/

        };
        mStringRequest.setTag("vpostGet_IdToken");
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                2,
                2f));
        mRequestQueue.add(mStringRequest);
    }

    public void vpost_GetAccountInfo(){
        Log.d(mTAG, "**vpost_GetAccountInfo**:" );
        JSONObject Info_JO = new JSONObject();
        try {
            Info_JO.put("uid",uid);
            Info_JO.put("token",idToken);
            Info_JO.put("action", "read");
            Info_JO.put("read_options", "profile");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpost_GetAccountInfo**:" + Info_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, Info_JO,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //mTxtDisplay.setText("Response: " + response.toString());
                                Log.d(mTAG, "vpost_GetAccountInfo:" + response.toString());
                                try {
                                    JSONObject dataObject = response.getJSONArray("data").getJSONObject(0);

                                    Log.d(mTAG, "vpost_GetAccountInfo22:" + dataObject.toString());
                                    Log.d(mTAG, "vpost_GetAccountInfo22:" + dataObject.getString("account"));
                                    mContext.getDataManager().AccountInfoGet(dataObject);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d(mTAG, "vpost_GetAccountInfo_Error:" + error.toString());
                            }
                        });
        jsObjRequest.setTag("vpost_GetAccountInfo");
        mRequestQueue.add(jsObjRequest);
    }

    public void vpost_GetAccountImage(String urlImage){
        ImageRequest imageRequest = new ImageRequest(
                urlImage,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        String strTmp= response.toString();
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (!Objects.equals(strTmp, "")){
                                mInfoMap.IMput(HiDBHelper.KEY_AC_Image,response);
                            }
                        }*/
                        if (!strTmp.equals("")){
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Image,response);
                        }

                        Log.d(mTAG, "vpost_GetAccountImage:OK**" + strTmp);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(mTAG, "vpost_GetAccountImage:error**" + error.toString());
            }
        });

        imageRequest.setTag("vpost_GetAccountImage");
        mRequestQueue.add(imageRequest);
    }

    public void vpost_GetWeightRecord(String startDate,String endDate){
        Log.d(mTAG, "**vpost_GetWeightRecord**:" );
        Log.d(mTAG,"**vpost_GetWeightRecord**DayTest**0**"+dayDiffW);
        Log.d(mTAG,"**vpost_GetWeightRecord**DayTest**0**count**"+countOp);
        Log.d(mTAG,"**vpost_GetWeightRecord**DayTest**1**"+startDate);
        Log.d(mTAG,"**vpost_GetWeightRecord**DayTest**2**"+endDate);
        JSONObject Info_JO = new JSONObject();
        try {
            Info_JO.put("uid",uid);
            Info_JO.put("token",idToken);
            Info_JO.put("action", "read");
            Info_JO.put("read_options", "weight");
            Info_JO.put("start_date", startDate);
            Info_JO.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpost_GetWeightRecord**:" + Info_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, Info_JO,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //mTxtDisplay.setText("Response: " + response.toString());
                                Log.d(mTAG, "vpost_GetWeightRecord:" + response.toString());
                                try {
                                    final JSONArray jsonArray = response.getJSONArray("data");
                                    final int count = jsonArray.length();
                                    if (count>0){
                                        Thread thread = new Thread(new Runnable(){
                                            @Override
                                            public void run() {
                                                // TODO Auto-generated method stub
                                                int count = jsonArray.length();
                                                for (int ii=0;ii<count;ii++){
                                                    ContentValues values = new ContentValues();
                                                    try {
                                                        String date = jsonArray.getJSONObject(ii).getString("date");
                                                        String time = jsonArray.getJSONObject(ii).getString("time");
                                                        String weight = jsonArray.getJSONObject(ii).getString("weight");
                                                        CT48 ct48 = new CT48(time);
                                                        Log.d(mTAG,"**Weight**"+date+"**"+time+"**"+weight);
                                                        values.put(HiDBHelper.KEY_Weight_Date, date);
                                                        values.put(HiDBHelper.KEY_Weight_Time, time);
                                                        values.put(HiDBHelper.KEY_Weight_Time48, ct48.getCt48());
                                                        values.put(HiDBHelper.KEY_Weight_Weight, weight);
                                                        helper.WeightInsert(values);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }


                                                }
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
                                    }
                                    Log.d(mTAG, "vpost_GetWeightRecord:count=" + count);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d(mTAG, "vpost_GetWeightRecord_Error:" + error.toString());
                            }
                        });
        jsObjRequest.setTag("vpost_GetWeightRecord");
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                2,
                2f));
        mRequestQueue.add(jsObjRequest);
    }

    public void vpost_GetCalInRecord(String startDate,String endDate){
        Log.d(mTAG, "**vpost_GetCalInRecord**:" );
        Log.d(mTAG,"**vpost_GetCalInRecord**DayTest**0**"+dayDiffW);
        Log.d(mTAG,"**vpost_GetCalInRecord**DayTest**0**count**"+countOp);
        Log.d(mTAG,"**vpost_GetCalInRecord**DayTest**1**"+startDate);
        Log.d(mTAG,"**vpost_GetCalInRecord**DayTest**2**"+endDate);
        JSONObject Info_JO = new JSONObject();
        try {
            Info_JO.put("uid",uid);
            Info_JO.put("token",idToken);
            Info_JO.put("action", "read");
            Info_JO.put("read_options", "eat");
            Info_JO.put("start_date", startDate);
            Info_JO.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpost_GetCalInRecord**:" + Info_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, Info_JO,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //mTxtDisplay.setText("Response: " + response.toString());
                                Log.d(mTAG, "vpost_GetCalInRecord:" + response.toString());
                                try {
                                    JSONArray data = response.getJSONArray("data");

                                    for (int ii=0;ii<data.length();ii++){
                                        JSONObject oneObject = data.getJSONObject(ii);
                                        String time = oneObject.getString("time");
                                        CT48 ct48 = new CT48(time);
                                        ContentValues values = new ContentValues();
                                        values.put(HiDBHelper.KEY_CalIn_Date, oneObject.getString("date"));
                                        values.put(HiDBHelper.KEY_CalIn_Time, time);
                                        values.put(HiDBHelper.KEY_CalIn_Time48, ct48.getCt48());
                                        values.put(HiDBHelper.KEY_CalIn_Fname, oneObject.getString("items"));
                                        values.put(HiDBHelper.KEY_CalIn_oneCal, oneObject.getString("calorie"));
                                        values.put(HiDBHelper.KEY_CalIn_Amount, "1");
                                        values.put(HiDBHelper.KEY_CalIn_CG, oneObject.getString("items_weight"));
                                        helper.CalInInsert(values);
                                        //Log.d(mTAG, "vpost_GetCalInRecord0:" + data.getJSONObject(ii));
                                        Log.d(mTAG, "vpost_GetCalInRecord1:" + values.toString());
                                    }

                                    //Log.d(mTAG, "vpost_GetWeightRecord:" + dataObject.getString("account"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d(mTAG, "vpost_GetCalInRecord_Error:" + error.toString());
                            }
                        });
        jsObjRequest.setTag("vpost_GetCalInRecord");
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                2,
                2f));
        mRequestQueue.add(jsObjRequest);
    }

    public void vpost_GetCalOutRecord(String startDate,String endDate){
        Log.d(mTAG, "**vpost_GetCalOutRecord**:" );
        Log.d(mTAG,"**vpost_GetCalOutRecord**DayTest**0**"+dayDiffW);
        Log.d(mTAG,"**vpost_GetCalOutRecord**DayTest**0**count**"+countOp);
        Log.d(mTAG,"**vpost_GetCalOutRecord**DayTest**1**"+startDate);
        Log.d(mTAG,"**vpost_GetCalOutRecord**DayTest**2**"+endDate);
        JSONObject Info_JO = new JSONObject();
        try {
            Info_JO.put("uid",uid);
            Info_JO.put("token",idToken);
            Info_JO.put("action", "read");
            Info_JO.put("read_options", "sport");
            Info_JO.put("start_date", startDate);
            Info_JO.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpost_GetCalOutRecord**:" + Info_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, Info_JO,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //mTxtDisplay.setText("Response: " + response.toString());
                                Log.d(mTAG, "vpost_GetCalOutRecord:" + response.toString());
                                try {
                                    JSONArray data = response.getJSONArray("data");

                                    for (int ii=0;ii<data.length();ii++){
                                        JSONObject oneObject = data.getJSONObject(ii);
                                        String time = oneObject.getString("time");
                                        CT48 ct48 = new CT48(time);
                                        ContentValues values = new ContentValues();
                                        values.put(HiDBHelper.KEY_CalOut_Date, oneObject.getString("date"));
                                        values.put(HiDBHelper.KEY_CalOut_Time, time);
                                        values.put(HiDBHelper.KEY_CalOut_Time48, ct48.getCt48());
                                        values.put(HiDBHelper.KEY_CalOut_Sport, oneObject.getString("sport"));
                                        values.put(HiDBHelper.KEY_CalOut_Cal, oneObject.getString("cal"));
                                        values.put(HiDBHelper.KEY_CalOut_HeartRate, oneObject.getString("heart_rate"));
                                        values.put(HiDBHelper.KEY_CalOut_Strength, oneObject.getString("strength"));
                                        values.put(HiDBHelper.KEY_CalOut_Continue, oneObject.getString("continue_time"));
                                        values.put(HiDBHelper.KEY_CalOut_Distance, oneObject.getString("distance"));
                                        helper.CalOutInsert(values);
                                        //Log.d(mTAG, "vpost_GetCalInRecord0:" + data.getJSONObject(ii));
                                        Log.d(mTAG, "vpost_GetCalOutRecord:" + values.toString());
                                    }

                                    //Log.d(mTAG, "vpost_GetWeightRecord:" + dataObject.getString("account"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d(mTAG, "vpost_GetCalOutRecord_Error:" + error.toString());
                            }
                        });
        jsObjRequest.setTag("vpost_GetCalOutRecord");
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                2,
                2f));
        mRequestQueue.add(jsObjRequest);
    }

    public void vpost_GetTargetRecord(String startDate,String endDate){
        Log.d(mTAG, "**vpost_GetTargetRecord**:" );
        Log.d(mTAG,"**vpost_GetTargetRecord**DayTest**0**"+dayDiffW);
        Log.d(mTAG,"**vpost_GetTargetRecord**DayTest**0**count**"+countOp);
        Log.d(mTAG,"**vpost_GetTargetRecord**DayTest**1**"+startDate);
        Log.d(mTAG,"**vpost_GetTargetRecord**DayTest**2**"+endDate);
        JSONObject Info_JO = new JSONObject();
        try {
            Info_JO.put("uid",uid);
            Info_JO.put("token",idToken);
            Info_JO.put("action", "read");
            Info_JO.put("read_options", "target");
            Info_JO.put("start_date", startDate);
            Info_JO.put("end_date", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpost_GetTargetRecord**:" + Info_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, Info_JO,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //mTxtDisplay.setText("Response: " + response.toString());
                                Log.d(mTAG, "vpost_GetTargetRecord:" + response.toString());
                                try {
                                    JSONArray data = response.getJSONArray("data");

                                    for (int ii=0;ii<data.length();ii++){
                                        JSONObject oneObject = data.getJSONObject(ii);
                                        ContentValues values = new ContentValues();
                                        values.put(HiDBHelper.KEY_Target_initDate, oneObject.getString("start_date"));
                                        values.put(HiDBHelper.KEY_Target_initTime, oneObject.getString("start_time"));
                                        values.put(HiDBHelper.KEY_Target_endDate, oneObject.getString("end_date"));
                                        values.put(HiDBHelper.KEY_Target_endTime, oneObject.getString("end_time"));
                                        values.put(HiDBHelper.KEY_Target_conDay, oneObject.getString("continue_day"));
                                        values.put(HiDBHelper.KEY_Target_conHour, oneObject.getString("continue_hour"));
                                        values.put(HiDBHelper.KEY_Target_Target, oneObject.getString("target_name"));
                                        values.put(HiDBHelper.KEY_Target_TargetValue, oneObject.getString("target_value"));
                                        helper.TargetInsert(values);
                                        //Log.d(mTAG, "vpost_GetCalInRecord0:" + data.getJSONObject(ii));
                                        Log.d(mTAG, "vpost_GetTargetRecord:" + values.toString());
                                    }

                                    //Log.d(mTAG, "vpost_GetWeightRecord:" + dataObject.getString("account"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d(mTAG, "vpost_GetTargetRecordError:" + error.toString());
                            }
                        });
        jsObjRequest.setTag("vpost_GetTargetRecord");
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                2,
                2f));
        mRequestQueue.add(jsObjRequest);
    }

    public void vpostGet_Pre(final int mode, final int endYear, final int endMonth, final int endDay, final int preDay, final double needWeight) {
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, sURL_Pre_Get,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("请求结果:" + response);
                        Log.d(mTAG, "**vpostGet_Pre1**:" + response);

                        try {
                            if (mode==2){
                                //addCal=3900&date=2016-11-08&weight=54.07914
                                int andIndex = response.indexOf("&");

                                String tmp = response.substring(response.indexOf("addCal=")+7,andIndex);
                                Log.d(mTAG, "**vpostGet_Pre2**1:addCal=" + tmp);

                                andIndex = response.indexOf("&",andIndex+1);
                                tmp = response.substring(response.indexOf("date=")+5,andIndex);
                                Log.d(mTAG, "**vpostGet_Pre2**2:date=" + tmp);

                                tmp = response.substring(response.indexOf("weight=")+7);
                                Log.d(mTAG, "**vpostGet_Pre2**3:weight=" + tmp);

                            } else if (mode==3){
                                //date=2016-11-08&weight=52.33512
                                int andIndex = response.indexOf("&");

                                String tmp = response.substring(response.indexOf("date=")+5,andIndex);
                                Log.d(mTAG, "**vpostGet_Pre3**1:date=" + tmp);

                                tmp = response.substring(response.indexOf("weight=")+7);
                                Log.d(mTAG, "**vpostGet_Pre3**2:weight=" + tmp);

                            }

                            fl_Diary.newInstance();
                            fl_Diary.PreUpdate(mode,response);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                            /*if (response.contains("&")){
                                uid = response.substring(0,response.indexOf("&"));
                                idToken = response.substring(response.indexOf("&")+1);
                            }*/
                        //Toast.makeText(mContext,"OK:"+response,Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //System.out.println("请求错误:" + error.toString());
                        Log.d(mTAG, "vpostGet_Pre_Error:" + error.toString());
                        //Toast.makeText(mContext,"Err:"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            // 携带参数
            @SuppressLint("DefaultLocale")
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                String pre_Target = "python3.4 PreActivity.py %s %04d/%02d/%02d %s 2 %.2f";
                String pre_NoTarget = "python3.4 PreActivity.py %s %04d/%02d/%02d %s 3";
                String command = "";
                Log.d(mTAG, "vpostGet_Pre_mode:" + mode);
                //Log.d(mTAG, "vpostGet_Pre_Command1:" + command);
                if (mode==2){
                    command = String.format(pre_Target,uid,endYear,endMonth,endDay,preDay,needWeight);
                } else if (mode==3){
                    command = String.format(pre_NoTarget,uid,endYear,endMonth,endDay,preDay);
                }

                Log.d(mTAG, "vpostGet_Pre_Command2:" + command);
                //Toast.makeText(mContext,"Err:"+command,Toast.LENGTH_LONG).show();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("uid", uid);
                hashMap.put("token", idToken);
                hashMap.put("command", command);
                return hashMap;
            }

            // Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            /*public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }*/

        };
        mStringRequest.setTag("vpostGet_Pre");
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                2,
                2f));
        mRequestQueue.add(mStringRequest);
        mContext.showPreProgressDialog();
    }
    ////////////////////////////////////////////////////////////////////////////////////
    public void vpostSend_FCMId() {
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, sURL_FCMID_Send,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("请求结果:" + response);
                        Log.d(mTAG, "**vpostSend_FCMId**OK:" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //System.out.println("请求错误:" + error.toString());
                        Log.d(mTAG, "**vpostSend_FCMId**Error:" + error.toString());
                    }
                }
        ) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("uid",uid);
                hashMap.put("token",idToken);
                hashMap.put("reg_id",mInfoMap.IMgetString(HiDBHelper.KEY_AC_FCM_id));
                return hashMap;
            }

            // Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            /*public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }*/

        };
        mStringRequest.setTag("vpostSend_FCMId");
        mRequestQueue.add(mStringRequest);
    }

    public void vpostSend_Recommend(final String recommend) {
        Log.d(mTAG,"**Recommend**"+recommend);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, sURL_Recommend_Send,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("请求结果:" + response);
                        Log.d(mTAG, "**vpostSend_Recommend**OK:" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //System.out.println("请求错误:" + error.toString());
                        Log.d(mTAG, "**vpostSend_Recommend**Error:" + error.toString());
                    }
                }
        ) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("uid",uid);
                hashMap.put("recommend",recommend);
                return hashMap;
            }

            // Volley请求类提供了一个 getHeaders（）的方法，重载这个方法可以自定义HTTP 的头信息。（也可不实现）
            /*public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }*/

        };
        mStringRequest.setTag("vpostSend_Recommend");
        mRequestQueue.add(mStringRequest);
    }

    public void vpostSend_FoodJson(JSONObject[] jsonObjectAll){
        JSONArray JA_All = new JSONArray();
        for (JSONObject aJsonObjectAll : jsonObjectAll) {
            JA_All.put(aJsonObjectAll);
        }
        JSONObject end_JO = new JSONObject();
        try {
            end_JO.put("uid",uid);
            end_JO.put("token",idToken);
            end_JO.put("action","insert");
            end_JO.put("record_options","1");
            end_JO.put("record",JA_All);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpostSend_FoodJson**:" + end_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, end_JO, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d(mTAG, "vpostSend_FoodJson**OK:" + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(mTAG, "vpostSend_FoodJson**Error:" + error.toString());
                    }
                });
        jsObjRequest.setTag("vpostSend_FoodJson");
        mRequestQueue.add(jsObjRequest);
    }

    public void vpostSend_WeightJson(String[] date,String[] time,double[] weight){
        int countWeight = date.length;
        JSONArray jaAll =new JSONArray();
        for (int ii=0;ii<countWeight;ii++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("date",date[ii]);
                jsonObject.put("time",time[ii]);
                jsonObject.put("value",weight[ii]);
                jaAll.put(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        JSONObject end_JO = new JSONObject();
        try {
            end_JO.put("uid",uid);
            end_JO.put("token",idToken);
            end_JO.put("action","insert");
            end_JO.put("record_options","2");
            end_JO.put("record",jaAll);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpostSend_WeightJson**:" + end_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, end_JO, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d(mTAG, "vpostSend_WeightJson**OK:" + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(mTAG, "vpostSend_WeightJson**Error:" + error.toString());
                    }
                });
        jsObjRequest.setTag("vpostSend_WeightJson");
        mRequestQueue.add(jsObjRequest);
    }

    public void vpostSend_ACJson(final String key, final String value){
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, sURL_sendAccountInfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("请求结果:" + response);
                        Log.d(mTAG, "**vpostSend_ACJson**OK**:" + response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //System.out.println("请求错误:" + error.toString());
                        Log.d(mTAG, "vpostSend_ACJson_Error:" + error.toString());
                    }
                }
        ) {
            // 携带参数
            @Override
            protected HashMap<String, String> getParams()
                    throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("uid",uid);
                hashMap.put("token",idToken);
                hashMap.put("col",key);
                hashMap.put("val",value);
                return hashMap;
            }

        };
        mStringRequest.setTag("vpostSend_ACJson");
        mRequestQueue.add(mStringRequest);
    }

    public void vpostSend_TargetJson(String initDay,String initTime
            ,String conDay,String conHour
            ,String endDay,String endTime,String target,String targetValue){

        JSONArray jaAll =new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start_date",initDay);
            jsonObject.put("start_time",initTime);
            jsonObject.put("continue_day",conDay);
            jsonObject.put("continue_hour",conHour);
            jsonObject.put("end_date",endDay);
            jsonObject.put("end_time",endTime);
            jsonObject.put("target_name",target);
            jsonObject.put("target_value",targetValue);
            jaAll.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject end_JO = new JSONObject();
        try {
            end_JO.put("uid",uid);
            end_JO.put("token",idToken);
            end_JO.put("action","insert");
            end_JO.put("record_options","5");
            end_JO.put("record",jaAll);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpostSend_TargetJson**:" + end_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, end_JO, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d(mTAG, "vpostSend_TargetJson**OK:" + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(mTAG, "vpostSend_TargetJson**Error:" + error.toString());
                    }
                });
        jsObjRequest.setTag("vpostSend_TargetJson");
        mRequestQueue.add(jsObjRequest);
    }

    public void vpostSend_SportJson(String initDay,String initTime
            ,String sport,String cal
            ,String heartRate,String strength,String contTime,String distance){

        JSONArray jaAll =new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("date",initDay);
            jsonObject.put("time",initTime);
            jsonObject.put("sport",sport);
            jsonObject.put("cal",cal);
            jsonObject.put("heart_rate",heartRate);
            jsonObject.put("strength",strength);
            jsonObject.put("continue_time",contTime);
            jsonObject.put("distance",distance);
            jaAll.put(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject end_JO = new JSONObject();
        try {
            end_JO.put("uid",uid);
            end_JO.put("token",idToken);
            end_JO.put("action","insert");
            end_JO.put("record_options","4");
            end_JO.put("record",jaAll);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(mTAG, "**vpostSend_SportJson**:" + end_JO.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, sURL_InfoPutGet, end_JO, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //mTxtDisplay.setText("Response: " + response.toString());
                        Log.d(mTAG, "vpostSend_SportJson**OK:" + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d(mTAG, "vpostSend_SportJson**Error:" + error.toString());
                    }
                });
        jsObjRequest.setTag("vpostSend_SportJson");
        mRequestQueue.add(jsObjRequest);
    }

    public static final Parcelable.Creator<VolleyManager> CREATOR
            = new Parcelable.Creator<VolleyManager>() {
        public VolleyManager createFromParcel(Parcel in) {
            return new VolleyManager(in);
        }

        public VolleyManager[] newArray(int size) {
            return new VolleyManager[size];
        }
    };

    private VolleyManager(Parcel in) {
        //mData = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
