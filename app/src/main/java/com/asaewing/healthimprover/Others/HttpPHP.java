package com.asaewing.healthimprover.Others;

import android.os.AsyncTask;
import android.util.Log;

import com.asaewing.healthimprover.MainActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HttpPHP extends AsyncTask<Void, Void, String> {

    private String TAG = HttpPHP.class.getSimpleName();
    private String sURL = "http://asa-asa.noip.me:1024/weight_control/app_web_vers/google_verified.php";
    //private String sURL = "http://hiapp.ddns.net/weight_control/app_web_vers/google_verified.php";
    //private String sURL = "http://192.168.20.4/weight_control/app_web_vers/google_verified.php";
    private String action, args[];

    /* 建立HTTP Post連線 */
    private HttpClient httpClient = new DefaultHttpClient();
    private HttpPost httpPost = new HttpPost(sURL);

    public HttpPHP(String action,String args[]) {
        this.action = action;
        this.args = args;

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            List<NameValuePair> param = new ArrayList<>();

            if (action.equals("sendIdToken")) {
                String tmp = args[0];
                param.add(new BasicNameValuePair("idtoken",args[0]));
                Log.d(TAG, "**" + TAG + "**HTTP_send**" + args[0]);
            } else if (action.equals("GET")) {
                param.add(new BasicNameValuePair("activity","GET"));
                param.add(new BasicNameValuePair("databaseName","test01"));
                param.add(new BasicNameValuePair("tableName","t2"));
                param.add(new BasicNameValuePair("condition","user_id=D31"));
                param.add(new BasicNameValuePair("limit",String.valueOf(1)));
                param.add(new BasicNameValuePair("orderCol","user_id"));
                param.add(new BasicNameValuePair("ASC/DESC","ASC"));
                param.add(new BasicNameValuePair("colNum",String.valueOf(2)));
                param.add(new BasicNameValuePair("colName0","user_name"));
                param.add(new BasicNameValuePair("colName1","imgTest"));
                param.add(new BasicNameValuePair("Submit","Submit"));
            }

          /* 發出HTTP request */
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(param, HTTP.UTF_8);
            httpPost.setEntity(entity);
            //TAG
            Log.d(TAG, "**" + TAG + "**HTTP1**" + param);
          /* 取得HTTP response */
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity resEntity = httpResponse.getEntity();
            if (resEntity != null) Log.d(TAG, "**" + TAG + "**HTTP2**resEntity**" + resEntity);
          /* 若狀態碼為200 ok */
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {/* 取出回應字串 */
                String strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                //MainActivity.mInfoMap.IMput("BI_FB_name",strResult.indexOf("user_name"));
                //MainActivity.mInfoMap.IMput("BI_FB_image",strResult.indexOf("imgTest"));
                Log.d(TAG, "**" + TAG + "**HTTP3**" + strResult);
                // 回傳回應字串
                return strResult;
            }
            else {
                Log.d(TAG, "**" + TAG + "**HTTP4**" + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.d(TAG, "**" + TAG + "**HTTP5**Failed");
        }
        return null;
    }


}