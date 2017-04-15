package com.asaewing.healthimprover.app2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.asaewing.healthimprover.app2.Manager.AccountManager;
import com.asaewing.healthimprover.app2.Manager.DataManager;
import com.asaewing.healthimprover.app2.Manager.FabMainManager;
import com.asaewing.healthimprover.app2.Manager.VolleyManager;
import com.asaewing.healthimprover.app2.Manager.fl_Manager;
import com.asaewing.healthimprover.app2.ViewOthers.CircleImageView;
import com.asaewing.healthimprover.app2.Others.DownloadImageTask;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.ViewOthers.TypeTextView;
import com.asaewing.healthimprover.app2.ViewPager.CirclePageIndicator;
import com.asaewing.healthimprover.app2.ViewPager.ViewPagerAdapter;
import com.asaewing.healthimprover.app2.fl.fl_Calories2;
import com.asaewing.healthimprover.app2.fl.fl_Diary;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.Calendar;
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
        NavigationView.OnNavigationItemSelectedListener ,
        FragmentManager.OnBackStackChangedListener {

    @SuppressLint("StaticFieldLeak")
    //Manager
    protected static VolleyManager volleyManager;
    protected static DataManager dataManager;
    protected AccountManager accountManager;
    protected fl_Manager flManager;

    //TODO----Object----
    public static boolean flag_google = false, flag_facebook = false;
    public static boolean flag_FCM = false;
    protected static boolean fabOpen = false;       //判斷fab是否展開了小fab

    private HiDBHelper helper;

    //private FirebaseAnalytics mFirebaseAnalytics;
    //private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthListener;

    //Flag
    private int id_nav_now = 0, id_nav_before = 0, id_nav_temp = 0;
    public boolean firstOpen = true;

    private NavigationView mNavView;

    //ProgressDialog
    protected ProgressDialog mProgressDialog;   //進度提示
    protected ProgressDialog mPreProgressDialog;   //進度提示


    public void setVolleyManager(){
        volleyManager = new VolleyManager(this,TAG);
    }

    //TODO----Life----
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TAG = getClass().getSimpleName();

        dataManager = new DataManager(this,TAG);
        mInfoMap = dataManager.mInfoMap;
        helper = dataManager.helper;

        volleyManager = new VolleyManager(this,TAG);

        //helper.SAID_jsonUpdate();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //主Layout

        @SuppressLint("HardwareIds")
        String android_deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);                    //取得裝置Id
        mInfoMap.IMput("deviceId",MD5(android_deviceId).toUpperCase()); //存入mInfoMap
        Log.i("device id=",mInfoMap.IMgetString("deviceId"));          //Log，標出裝置Id

        //Toolbar Settings
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        this.setSupportActionBar(mToolbar);                 //請看ActionBar與Toolbar的不同
        mToolbar.setOnClickListener(this);                  //設置監聽

        //HiCard
        HiCard = (CardView)findViewById(R.id.HiCardView);
        assert HiCard != null;
        HiCard.setOnClickListener(this);
        HiCard_Text = (TypeTextView)findViewById(R.id.HiCardView_Text);
        HiCard_Text.setOnTypeViewListener( new TypeTextView.OnTypeViewListener( ) {
            @Override
            public void onTypeStart() {
                //print( "onTypeStart" );
            }

            @Override
            public void onTypeOver() {
                //print( "onTypeOver" );
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            HiCard.setTranslationZ(mToolbar.getTranslationZ()+4f);
        }

        assert HiCard_Text != null;
        HiCard_Text.start(getString(R.string.HiCard_firstOpen));

        //mCoverView
        mCoverView = findViewById(R.id.cover);
        assert mCoverView != null;
        mCoverView.setVisibility(View.GONE);
        mCoverView.setOnClickListener(this);

        //FAB Main
        fabMain = (FloatingActionButton)findViewById(R.id.fab_Main);
        assert fabMain != null;
        fabMain.setOnClickListener(this);

        flManager = new fl_Manager(this,TAG);
        fabMainManager = new FabMainManager(this,TAG,fabMain,mCoverView);


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
                fabMainManager.fabMainClose(flManager.mPagePosition);
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
                    if (!flManager.fl_flag_now.equals(strTmp)){
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

        showProgressDialog();

        accountManager = new AccountManager(this,TAG);
        accountManager.mOnCreate();

        //flManager.initView();
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

        //DisplayMetrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mInfoMap.IMput("WindowWidth",metrics.widthPixels);
        mInfoMap.IMput("WindowHeight",metrics.heightPixels);



        /*//HiCard
        HiCard = (CardView)findViewById(R.id.HiCardView);
        assert HiCard != null;
        HiCard.setOnClickListener(this);
        HiCard_Text = (TypeTextView)findViewById(R.id.HiCardView_Text);
        HiCard_Text.setOnTypeViewListener( new TypeTextView.OnTypeViewListener( ) {
            @Override
            public void onTypeStart() {
                //print( "onTypeStart" );
            }

            @Override
            public void onTypeOver() {
                //print( "onTypeOver" );
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            HiCard.setTranslationZ(mToolbar.getTranslationZ()+4f);
        }

        assert HiCard_Text != null;
        HiCard_Text.start(getString(R.string.HiCard_firstOpen));*/

        //Home初始頁面在Calories
        //mViewPager.setCurrentItem(1);
        /////////////////////////////////////////////////////

        TextView ac_user_name = (TextView)findViewById(R.id.ac_user_name);
        TextView ac_user_mail = (TextView)findViewById(R.id.ac_user_mail);
        ac_user_name.setText(mInfoMap.IMgetString(HiDBHelper.KEY_AC_Name));
        ac_user_mail.setText(mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account));
        firstOpen = false;
        mInfoMap.IMput(HiDBHelper.KEY_Flag_UseFirst,false);

        CircleImageView acGoogleImage = (CircleImageView)findViewById(R.id.ac_image);
        try {
            String tmpStr = accountManager.personPhoto.toString();
            try {
                Bitmap bitmap = new DownloadImageTask(this,"acGoogleImage",acGoogleImage)
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
        flManager.initView();

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
        accountManager.mOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if (firstOpen) updateData();
        //showProgressDialog();

        accountManager.mOnStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mProgressDialog.isShowing() && !firstOpen){
            hideProgressDialog();
        }
        accountManager.mOnResume();

        //FB
        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataManager.saveDataSP();
        accountManager.mOnPause();

        //FB
        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onStop() {

        if (fabOpen)fabMainManager.fabMainClose(flManager.mPagePosition);

        //saveDataSP();

        super.onStop();
        accountManager.mOnStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        accountManager.mOnRestart();
        //helper = new HiDBHelper(getApplicationContext());
        dataManager.onRestart();
        Log.d(TAG,"**Yes_onRestart**");

        fabOpen = false;
        fabMainManager.fabMainClose(flManager.mPagePosition);
        //if (fabOpen) fabMainManager.fabMainClose(mPagePosition);
    }

    @Override
    protected void onDestroy() {

        //saveDataSP();
        //helper.close();
        dataManager.onDestroy();
        accountManager.mOnDestroy();

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
            fabOpen = false;
            fabMainManager.fabMainClose(flManager.mPagePosition);
        } else {
            if (flManager.fl_flag != 1) {
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

                if (flManager.fl_flag_now.equals("fl_Chat") && !strTmp.equals("fl_Chat")){
                    flManager.flChange("fl_Chat");
                    MenuItem item = mNavView.getMenu().findItem(R.id.nav_Chat);
                    item.setChecked(true);

                }else if (flManager.fl_flag_now.equals("fl_Heart")){
                    flManager.flChange("fl_navHome");
                    MenuItem item = mNavView.getMenu().findItem(R.id.nav_Home);
                    item.setChecked(true);

                    id_nav_now = R.id.nav_Home;
                    id_nav_temp = 0;
                    id_nav_before = 0;

                    //mToolbar.setNavigationIcon(R.drawable.ic_nav_humburger);
                } else {
                    flManager.flChange("fl_navHome");
                    MenuItem item = mNavView.getMenu().findItem(R.id.nav_Home);
                    item.setChecked(true);

                    id_nav_now = R.id.nav_Home;
                    id_nav_temp = 0;
                    id_nav_before = 0;
                }

            } else {
                this.finish();
            }
        }
    }

    @Override
    public void onClick(View v) {

        super.onClick(v);

        int vId = v.getId();

        if (vId == R.id.toolbar) {
            fabMainManager.fabMainClose(flManager.mPagePosition);

        } else if (vId == R.id.fab_Main && !flManager.fl_flag_now.equals("fl_Map")) {
            fabOpen = !fabOpen;
            fabMainManager.fabMainClick(flManager.mPagePosition);

        } else if (vId == R.id.cover  || vId == R.id.HiCardView) {

            fabOpen = false;

            if (vId == R.id.HiCardView) {
                String tmpHi = "叫我幹嘛呢？";
                assert HiCard_Text != null;
                HiCard_Text.start(tmpHi);
            }

            fabMainManager.fabMainClose(flManager.mPagePosition);

        }

        if (vId == R.id.fab_Main && flManager.fl_flag_now.equals("fl_Map")) {

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
            flManager.flChange(flManager.getTagStringFromId(itemId));
            //flManager.flChange(itemId);
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

    private void showProgressDialog() {

        //TAG
        Log.d(TAG, "**" + TAG + "**showProgressDialog");

        if (mProgressDialog == null) {
            Log.d(TAG, "**" + TAG + "**showProgressDialog**Create");
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
            //signInDialog1();
            //accountManager.signInAccount();

        }
    }

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

    public void HiCardPlay(String whereFrom, String wantTo, String newText) {

        String HiText = "";

        if (whereFrom.equals("flChange")) {
            switch (wantTo) {
                case "fl_navHome":
                    switch (flManager.mPagePosition) {
                        case 0:
                            HiText = "有沒有忘記紀錄體重？";
                            break;

                        case 1:
                            HiText = "今天運動了嗎？";
                            break;

                        case 2:
                            HiText = "今日睡的如何？";
                            break;
                    }
                    break;

                case "fl_Map":
                    HiText = "想找找附近餐廳？";
                    break;

                case "fl_Chat":
                    HiText = "讓我來陪你聊天吧！";
                    break;

                case "fl_BasicInfo":
                    HiText = "帳戶資料要更動嗎？";
                    break;

                case "fl_MyDevice":
                    HiText = "尚未有藍牙裝置連結";
                    break;

                case "fl_Heart":
                    HiText = "來測測心律！";
                    break;

                case "fl_ProductTour":
                    HiText = "HiApp導覽";
                    break;

                case "fl_AR_Exp":
                    HiText = "權責說明";
                    break;

                case "fl_Settings":
                    HiText = "HiApp設定";
                    break;

                case "fl_Test01":
                    HiText = "程式測試1";
                    break;

                case "fl_Test02":
                    HiText = "程式測試2";
                    break;
            }

        } else if (whereFrom.equals("onClick")) {
            if (wantTo.equals("fl_navHome")) {
                HiCard_Text.start("");
            }
        } /*else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(whereFrom,"") && Objects.equals(wantTo,"")) {
                HiText = newText;
            }
        }*/ else if (whereFrom.equals("") && wantTo.equals("")) {
            HiText = newText;
        }

        HiCard_Text.start(HiText);
    }

    public static VolleyManager getVolleyManager(){
        return volleyManager;
    }

    public static DataManager getDataManager(){
        return dataManager;
    }

    public AccountManager getAccountManager(){
        return accountManager;
    }

    public FabMainManager getFabMainManager(){
        return fabMainManager;
    }

    public fl_Manager getFlManager(){
        return flManager;
    }

    public ViewPagerAdapter getViewPagerAdapter(){
        return flManager.mViewPagerAdapter;
    }

}
