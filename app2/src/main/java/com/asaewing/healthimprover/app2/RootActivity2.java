package com.asaewing.healthimprover.app2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.asaewing.healthimprover.app2.Manager.FabMainManager;
import com.asaewing.healthimprover.app2.Others.InfoMap;
import com.asaewing.healthimprover.app2.ViewOthers.TypeTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import Interface.RL_Action;

/**
 *  1.Click監聽，皆由class一次寫好，方便處理觸發順序、優先。
 *  2.由於Map需直接寄生於Activity，故不會有另外的fl處理。
 *  3.
 */
public class RootActivity2 extends AppCompatActivity
        implements View.OnClickListener,
        OnMapReadyCallback, RL_Action {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //TODO----Object----
    //標示、判別
    protected String TAG = null;                    //除錯用的Log、TAG

    //記憶資料
    protected InfoMap mInfoMap; //進入程式後，會將資料全數轉進此資料容器，
                                                    //提供各class間存取
    //GoogleMap
    public GoogleMap G_Map;
    protected GoogleMapOptions G_MapOptions;
    protected LocationManager locationMgr;
    protected String provider;

    //MainLayout
    protected DrawerLayout mDrawer;             //左側拖拉的側邊欄
    protected ActionBarDrawerToggle mToggle;    //mDrawer的觸發
    protected Toolbar mToolbar;                 //上方bar
    protected FloatingActionButton fabMain; //主fab
    protected CardView HiCard;                  //bar下方的HiApp對話框的Layout
    protected TypeTextView HiCard_Text;     //bar下方的HiApp對話框，有打字動畫效果

    //mCoverView
    protected View mCoverView;    //當fab展開時，用來呈現fab的覆蓋頁面，
                                        //用來提醒使用者的界面使用優先

    protected FabMainManager fabMainManager;

    private GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.d(TAG, "GPS_EVENT_STARTED");
                    Toast.makeText(getApplicationContext(), "GPS_EVENT_STARTED", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.d(TAG, "GPS_EVENT_STOPPED");
                    Toast.makeText(getApplicationContext(), "GPS_EVENT_STOPPED", Toast.LENGTH_SHORT).show();
                    break;
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.d(TAG, "GPS_EVENT_FIRST_FIX");
                    Toast.makeText(getApplicationContext(), "GPS_EVENT_FIRST_FIX", Toast.LENGTH_SHORT).show();
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

    private ArrayList<MyOnTouchListener> onTouchListeners =
            new ArrayList<>(10);

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

    @Override
    public void openMoveRL(boolean setFabUse, RelativeLayout relativeLayout, double moveY) {
        superAction.openMoveRL(setFabUse,relativeLayout,moveY);
    }

    @Override
    public void closeRL(boolean setFabUse, RelativeLayout relativeLayout, long Time) {
        superAction.closeRL(setFabUse,relativeLayout,Time);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    //TODO----Life----
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        Log.d(TAG,"**"+TAG+"**onCreate");
    }

    protected void initView(){

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG,"**"+TAG+"**onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG,"**"+TAG+"**onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG,"**"+TAG +"**onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG,"**"+TAG+"**onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d(TAG,"**"+TAG+"**onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"**"+TAG+"**onDestroy");
    }


    //TODO----deviceID----for Ads
    /** MD5>>負責找出裝置Id **/
    protected static String MD5(String deviceId) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(deviceId.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest)
                hexString.append(Integer.toHexString(0xFF & aMessageDigest));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //TODO----Others----
    /** onBackPressed>>當Back鍵被按下時 **/
    @Override
    public void onBackPressed() {

    }

    /** onClick>>ClickListener **/
    @Override
    public void onClick(View v) {

    }

    /** onMapReady>>是GoogleMap的 **/
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    protected boolean initLocationProvider() {
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

    protected void whereAmI() {
        //取得上次已知的位置
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    protected void cameraFocusOnMe(double lat, double lng){
        CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(16)
                .build();
        G_Map.animateCamera(
                CameraUpdateFactory.newCameraPosition(camPosition)
                ,2000,null);
    }

    protected void updateWithNewLocation(Location location) {
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

}
