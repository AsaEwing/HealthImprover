package com.asaewing.healthimprover.app2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.asaewing.healthimprover.app2.Manager.FabMainManager;
import com.asaewing.healthimprover.app2.Others.InfoMap;
import com.asaewing.healthimprover.app2.ViewOthers.TypeTextView;
import com.asaewing.healthimprover.app2.ViewPager.CirclePageIndicator;
import com.asaewing.healthimprover.app2.ViewPager.ViewPagerAdapter;
import com.asaewing.healthimprover.app2.ViewPager.ViewPagerItem;
import com.asaewing.healthimprover.app2.fl.fl_Calories2;
import com.asaewing.healthimprover.app2.fl.fl_Diary;
import com.asaewing.healthimprover.app2.Manager.fl_Manager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  1.Click監聽，皆由class一次寫好，方便處理觸發順序、優先。
 *  2.由於Map需直接寄生於Activity，故不會有另外的fl處理。
 *  3.
 */
public class RootActivity2 extends AppCompatActivity
        implements View.OnClickListener,OnMapReadyCallback {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //TODO----Object----
    //標示、判別
    protected String TAG = null;                    //除錯用的Log、TAG
    protected static String fl_flag_now = null
            ,fl_flag_before = null
            ,fl_flag_temp = null;     //現在所處的fl(Fragment & Layout)
    protected int fl_flag = 0;                      //現在所處fl的階層，用於Back鍵
                                                    //0是Map，1是Home，2是其餘
    protected boolean fl_flag_before_map = false;   //紀錄前一次的fl是不是Map，因為Layout的不同
    protected static int mPagePosition = 0;         //紀錄ViewPager的當前頁數
    protected static boolean fabOpen = false;       //判斷fab是否展開了小fab

    //記憶資料
    public static InfoMap mInfoMap; //進入程式後，會將資料全數轉進此資料容器，
                                                    //提供各class間存取
    //Fragment
    protected Fragment fragment = null;

    //ViewPager
    protected List<ViewPagerItem> mListVPItem = new ArrayList<>();  //收集有哪些fl要變成VP，且照順序
    public ViewPagerAdapter mViewPagerAdapter;                   //VP的適配
    protected ViewPager mViewPager;
    protected ViewPagerListener mViewPagerListener;                 //VP的監聽
    protected CirclePageIndicator mViewPagerIndicator;              //VP的指示，
                                                                    //頁面中間下方的圓形頁數指示
    //GoogleMap
    protected GoogleMap G_Map;
    protected GoogleMapOptions G_MapOptions;

    //MainLayout
    protected DrawerLayout mDrawer;             //左側拖拉的側邊欄
    protected ActionBarDrawerToggle mToggle;    //mDrawer的觸發
    protected Toolbar mToolbar;                 //上方bar
    protected RelativeLayout VP_RL;             //用以實現多頁
    protected FrameLayout Fragment_RL;          //用以實現單頁及Map
    public static FloatingActionButton fabMain; //主fab
    protected CardView HiCard;                  //bar下方的HiApp對話框的Layout
    public static TypeTextView HiCard_Text;     //bar下方的HiApp對話框，有打字動畫效果

    //mCoverView
    protected static View mCoverView;    //當fab展開時，用來呈現fab的覆蓋頁面，
                                        //用來提醒使用者的界面使用優先

    protected fl_Manager flManager;
    protected static FabMainManager fabMainManager;

    //TODO----Life----
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        Log.d(TAG,"**"+TAG+"**onCreate");  //Log，onCreate Start
        setContentView(R.layout.activity_main); //主Layout



        //mInfoMap = new InfoMap();

        //Ads deviceId to get
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
    }

    protected void initView(){
        //用以實現單頁及Map
        Fragment_RL = (FrameLayout)findViewById(R.id.fl_c_MainFragment);
        assert Fragment_RL != null;
        Fragment_RL.setVisibility(View.GONE);

        //用以實現多頁
        VP_RL = (RelativeLayout)findViewById(R.id.fl_c_MainVP);
        assert VP_RL != null;
        VP_RL.setVisibility(View.VISIBLE);



        //初始頁面
        addPage(R.string.nav_Diary, fl_Diary.newInstance(),"fl_Diary");
        addPage(R.string.nav_Calories, fl_Calories2.newInstance(),"fl_Calories");
        //addPage(R.string.nav_Sleep, fl_Sleep.newInstance());
        mInfoMap.IMput("VPhomeBefore",1);
        fl_flag = 1;
        //fl_flag_now = "fl_navHome";
        //fabMainManager.setFl_FlagNow(fl_flag_now);
        setFl_FlagNow("fl_navHome");

        //ViewPager
        mViewPagerAdapter = new ViewPagerAdapter(this,getSupportFragmentManager(),mListVPItem);
        mViewPager = (ViewPager)findViewById(R.id.fl_c_ViewPager);
        assert mViewPager != null;
        mViewPager.removeAllViewsInLayout();
        mViewPager.removeAllViews();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setClickable(false);
        mViewPagerListener = new ViewPagerListener();
        mViewPager.addOnPageChangeListener(mViewPagerListener);

        Log.d(TAG,"**fl_Change**"+fl_flag_now);

        //ViewPagerIndicator
        mViewPagerIndicator = (CirclePageIndicator)findViewById(R.id.mainCirclePageIndicator);
        assert mViewPagerIndicator != null;
        mViewPagerIndicator.setViewPager(mViewPager);
        mViewPagerIndicator.setOnPageChangeListener(mViewPagerListener);
        mViewPagerIndicator.setClickable(false);
        showIndicator(true);

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
        mViewPager.setCurrentItem(1);
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
    private static String MD5(String deviceId) {
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
        int vId = v.getId();

        if (vId == R.id.toolbar) {
            fabMainManager.fabMainClose(mPagePosition);

        } else if (vId == R.id.fab_Main && !fl_flag_now.equals("fl_Map")) {
            fabOpen = !fabOpen;
            fabMainManager.fabMainClick(mPagePosition);

        } else if (vId == R.id.cover  || vId == R.id.HiCardView) {

            fabOpen = false;

            if (vId == R.id.HiCardView) {
                String tmpHi = "叫我幹嘛呢？";
                assert HiCard_Text != null;
                HiCard_Text.start(tmpHi);
            }

            fabMainManager.fabMainClose(mPagePosition);

        }
    }

    /** onMapReady>>是GoogleMap的 **/
    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    /** flChange>>負責fl轉換，即頁面轉換 **/
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void flChange(String nav_s) {
        String sTitle = null;
        fl_flag_before = fl_flag_now;

        setFl_FlagNow(nav_s);
        
        if (nav_s.equals("OS")) return;
        
        Log.d(TAG,"**"+TAG+"**flChange**"+fl_flag_now);

        //先判斷fl的階級
        switch (fl_flag) {
            case 0:
                try {
                    G_Map.clear();
                } catch (Exception e){
                    e.printStackTrace();
                }

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentByTag("fl_Map");
                getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
                Fragment_RL.removeAllViewsInLayout();
                break;

            case 1:
                mInfoMap.IMput("VPhomeBefore", mViewPager.getCurrentItem());
                Log.d(TAG, "**flChange**CIA****" + mViewPager.getCurrentItem());
                break;

            case 2:
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                Fragment_RL.removeAllViewsInLayout();
                break;
        }

        //轉換前製作業
        if (nav_s.equals("fl_navHome")) {
            Fragment_RL.removeAllViewsInLayout();
            Fragment_RL.setVisibility(View.GONE);
            VP_RL.setVisibility(View.VISIBLE);

            addPage(R.string.nav_Diary, fl_Diary.newInstance(),"fl_Diary");
            addPage(R.string.nav_Calories, fl_Calories2.newInstance(),"fl_Calories");
            //addPage(R.string.nav_Sleep, fl_Sleep.newInstance());

            mViewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), mListVPItem);
            //mViewPager = (ViewPager) findViewById(R.id.fl_c_ViewPager);
            assert mViewPager != null;
            mViewPager.setClickable(false);
            mViewPager.setAdapter(mViewPagerAdapter);
            //mViewPagerListener = new ViewPagerListener();
            //mViewPager.addOnPageChangeListener(mViewPagerListener);

            //mViewPagerIndicator = (CirclePageIndicator) findViewById(R.id.mainCirclePageIndicator);
            //assert mViewPagerIndicator != null;
            //mViewPagerIndicator.setViewPager(mViewPager);
            //mViewPagerIndicator.setOnPageChangeListener(mViewPagerListener);
            //mViewPagerIndicator.setClickable(false);
        } else {
            mViewPager.removeAllViewsInLayout();
            mListVPItem.clear();
            VP_RL.setVisibility(View.GONE);
            Fragment_RL.setVisibility(View.VISIBLE);

            if (nav_s.equals("fl_Map")) {
                sTitle = getString(R.string.nav_Map);
            } else {
                fragment = flManager.getFragmentFromTagString(nav_s);
                sTitle = flManager.getTitleFromTagString(nav_s);
            }
        }

        //轉換後製作業
        if (fl_flag_now.equals("fl_navHome")) {
            fl_flag = 1;
            fl_flag_before_map = false;

            mViewPager.setCurrentItem(mInfoMap.IMgetInt("VPhomeBefore"));
            if (mInfoMap.IMgetInt("VPhomeBefore") == 0) {
                new ViewPagerListener().replaceTitle(mListVPItem.get(0).getTitle());
            }
            fabMainManager.fabMainChange(mInfoMap.IMgetInt("VPhomeBefore"));

            boolean show1 = mViewPagerAdapter.getCount() >= 2;
            showIndicator(show1);

        } else {
            Fragment_RL.setVisibility(View.VISIBLE);

            if (fl_flag_now.equals("fl_Map")) {
                fl_flag = 0;
                fl_flag_before_map = true;

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_c_MainFragment, mapFragment, fl_flag_now).commit();
                mapFragment.getMapAsync(RootActivity2.this);

            } else {
                fl_flag = 2;
                fl_flag_before_map = false;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_c_MainFragment, fragment, fl_flag_now).commit();
            }

            ActionBar supportActionBar = getSupportActionBar();
            assert supportActionBar != null;
            supportActionBar.setTitle(sTitle);

            fabMainManager.fabMainChange();

            /*if (fl_flag_now.equals("fl_Heart")){
                //supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                mToolbar.setNavigationIcon(R.drawable.ic_nav_up);
            } else {
                //supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                mToolbar.setNavigationIcon(R.drawable.ic_nav_humburger);
            }*/
        }

        HiCardPlay("flChange",fl_flag_now,"");
    }

    /** addPage>>負責多頁面的fl存儲到List **/
    public List<ViewPagerItem> addPage(int titleResource, Fragment fragment, String TAG) {
        ViewPagerItem item = new ViewPagerItem();
        item.setTitle(this, titleResource);
        item.setFragment(fragment);
        item.setFl_TAG(TAG);
        mListVPItem.add(item);
        return mListVPItem;
    }

    /** showIndicator>>負責將nav傳過來的id轉成string **/
    private void showIndicator(boolean showIndicator_flag) {
        mViewPagerIndicator.setVisibility(showIndicator_flag ? View.VISIBLE : View.GONE);
        mViewPagerIndicator.setClickable(false);
    }

    protected void setFl_FlagNow(String fl_flagNow){
        fl_flag_now = fl_flagNow;
        fabMainManager.setFl_FlagNow(fl_flag_now);
    }

    protected void setPagePosition(int PagePosition){
        mPagePosition = PagePosition;
        fabMainManager.setPagePosition(PagePosition);
    }

    public static void HiCardPlay(String whereFrom,String wantTo,String newText) {

        String HiText = "";

        if (whereFrom.equals("flChange")) {
            switch (wantTo) {
                case "fl_navHome":
                    switch (mPagePosition) {
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
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(whereFrom,"") && Objects.equals(wantTo,"")) {
                HiText = newText;
            }
        }

        HiCard_Text.start(HiText);
    }

    //TODO----ViewPagerListener----
    private class ViewPagerListener implements ViewPager.OnPageChangeListener{

        private int mPagePosition_before=0;

        @Override
        public void onPageScrolled(int position,
                                   float positionOffset,int positionOffsetPixels) {
            fabMainManager.fabMainClose(mPagePosition);
        }

        @Override
        public void onPageSelected(int position) {
            //mPagePosition = position;
            setPagePosition(position);

            replaceTitle(String.valueOf(mViewPagerAdapter.getPageTitle(position)));
            //fabMainChange(mPagePosition);
            if (fl_flag_now.equals("fl_navHome")){
                fabMainManager.fabMainChange(mPagePosition);
            }

            Log.d(TAG, "**ViewPagerListener**"+mViewPagerAdapter.getPageTitle(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                mPagePosition_before = mPagePosition;

            } else if (state == ViewPager.SCROLL_STATE_IDLE
                    && mPagePosition!=mPagePosition_before
                    && fl_flag_now.equals("fl_navHome")){
                //fabMainChange(mPagePosition);

                HiCardPlay("flChange","fl_navHome","");
            }
        }

        private ViewPagerListener replaceTitle(String title) {
            ActionBar supportActionBar = getSupportActionBar();
            assert supportActionBar != null;
            supportActionBar.setTitle(title);
            return this;
        }
    }
}
