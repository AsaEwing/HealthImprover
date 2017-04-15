package com.asaewing.healthimprover.app2.Manager;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.InfoMap;
import com.asaewing.healthimprover.app2.R;
import com.asaewing.healthimprover.app2.RootActivity2;
import com.asaewing.healthimprover.app2.ViewPager.CirclePageIndicator;
import com.asaewing.healthimprover.app2.ViewPager.ViewPagerAdapter;
import com.asaewing.healthimprover.app2.ViewPager.ViewPagerItem;
import com.asaewing.healthimprover.app2.fl.fl_AR_Exp;
import com.asaewing.healthimprover.app2.fl.fl_BasicInfo;
import com.asaewing.healthimprover.app2.fl.fl_Calories2;
import com.asaewing.healthimprover.app2.fl.fl_Camera;
import com.asaewing.healthimprover.app2.fl.fl_Chat;
import com.asaewing.healthimprover.app2.fl.fl_Diary;
import com.asaewing.healthimprover.app2.fl.fl_Heart;
import com.asaewing.healthimprover.app2.fl.fl_MyDevice;
import com.asaewing.healthimprover.app2.fl.fl_Note;
import com.asaewing.healthimprover.app2.fl.fl_Notification;
import com.asaewing.healthimprover.app2.fl.fl_ProductTour;
import com.asaewing.healthimprover.app2.fl.fl_Record;
import com.asaewing.healthimprover.app2.fl.fl_Settings;
import com.asaewing.healthimprover.app2.fl.fl_Test01;
import com.asaewing.healthimprover.app2.fl.fl_Test02;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class fl_Manager {

    private MainActivity2 mContext;
    private String mTAG;

    public String fl_flag_now = null;
    private String fl_flag_before = null;
    protected String fl_flag_temp = null;     //現在所處的fl(Fragment & Layout)
    public int fl_flag = 0;                      //現在所處fl的階層，用於Back鍵
    private boolean fl_flag_before_map = false;   //紀錄前一次的fl是不是Map，因為Layout的不同
    public int mPagePosition = 0;         //紀錄ViewPager的當前頁數

    //Fragment
    protected Fragment fragment = null;

    //ViewPager
    private List<ViewPagerItem> mListVPItem = new ArrayList<>();  //收集有哪些fl要變成VP，且照順序
    public ViewPagerAdapter mViewPagerAdapter;                   //VP的適配
    private ViewPager mViewPager;
    private ViewPagerListener mViewPagerListener;                 //VP的監聽
    private CirclePageIndicator mViewPagerIndicator;              //VP的指示，頁面中間下方的圓形頁數指示

    private RelativeLayout VP_RL;             //用以實現多頁
    private FrameLayout Fragment_RL;          //用以實現單頁及Map

    public fl_Manager(MainActivity2 context, String TAG){
        mContext = context;
        mTAG = TAG+" , fl_Manager";
    }

    public void initView(){
        InfoMap mInfoMap = mContext.getDataManager().mInfoMap;

        //用以實現單頁及Map
        Fragment_RL = (FrameLayout)mContext.findViewById(R.id.fl_c_MainFragment);
        assert Fragment_RL != null;
        Fragment_RL.setVisibility(View.GONE);

        //用以實現多頁
        VP_RL = (RelativeLayout)mContext.findViewById(R.id.fl_c_MainVP);
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
        mViewPagerAdapter = new ViewPagerAdapter(mContext,mContext.getSupportFragmentManager(),mListVPItem);
        mViewPager = (ViewPager)mContext.findViewById(R.id.fl_c_ViewPager);
        assert mViewPager != null;
        mViewPager.removeAllViewsInLayout();
        mViewPager.removeAllViews();
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setClickable(false);
        mViewPagerListener = new ViewPagerListener();
        mViewPager.addOnPageChangeListener(mViewPagerListener);

        Log.d(mTAG,"**fl_Change**"+fl_flag_now);

        //ViewPagerIndicator
        mViewPagerIndicator = (CirclePageIndicator)mContext.findViewById(R.id.mainCirclePageIndicator);
        assert mViewPagerIndicator != null;
        mViewPagerIndicator.setViewPager(mViewPager);
        mViewPagerIndicator.setOnPageChangeListener(mViewPagerListener);
        mViewPagerIndicator.setClickable(false);
        showIndicator(true);

        //Home初始頁面在Calories
        mViewPager.setCurrentItem(1);
    }

    public void flChange(String nav_s) {
        //String nav_s
        //String nav_s = getTagStringFromId(nav_id);
        String sTitle = null;
        fl_flag_before = fl_flag_now;
        InfoMap mInfoMap = mContext.getDataManager().mInfoMap;

        setFl_FlagNow(nav_s);

        if (nav_s.equals("OS")) return;

        Log.d(mTAG,"**"+mTAG+"**flChange**轉換前**"+fl_flag_before);
        Log.d(mTAG,"**"+mTAG+"**flChange**預備轉換成**"+nav_s);

        //先判斷fl的階級
        switch (fl_flag) {
            case 0:
                try {
                    mContext.G_Map.clear();
                } catch (Exception e){
                    e.printStackTrace();
                }

                SupportMapFragment mapFragment =
                        (SupportMapFragment) mContext.getSupportFragmentManager()
                        .findFragmentByTag("fl_Map");
                mContext.getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
                Fragment_RL.removeAllViewsInLayout();
                break;

            case 1:
                mContext.getDataManager().mInfoMap.IMput("VPhomeBefore", mViewPager.getCurrentItem());
                Log.d(mTAG, "**flChange**CIA****" + mViewPager.getCurrentItem());
                break;

            case 2:
                mContext.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
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

            mViewPagerAdapter = new ViewPagerAdapter(mContext, mContext.getSupportFragmentManager(), mListVPItem);
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
                sTitle = mContext.getString(R.string.nav_Map);
            } else {
                fragment = this.getFragmentFromTagString(nav_s);
                sTitle = this.getTitleFromTagString(nav_s);
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
            mContext.getFabMainManager().fabMainChange(mInfoMap.IMgetInt("VPhomeBefore"));

            boolean show1 = mViewPagerAdapter.getCount() >= 2;
            showIndicator(show1);

        } else {
            Fragment_RL.setVisibility(View.VISIBLE);

            if (fl_flag_now.equals("fl_Map")) {
                fl_flag = 0;
                fl_flag_before_map = true;

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = SupportMapFragment.newInstance();
                mContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_c_MainFragment, mapFragment, fl_flag_now).commit();
                mapFragment.getMapAsync(mContext);

            } else {
                fl_flag = 2;
                fl_flag_before_map = false;
                mContext.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_c_MainFragment, fragment, fl_flag_now).commit();
            }

            ActionBar supportActionBar = mContext.getSupportActionBar();
            assert supportActionBar != null;
            supportActionBar.setTitle(sTitle);

            mContext.getFabMainManager().fabMainChange();

            /*if (fl_flag_now.equals("fl_Heart")){
                //supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                mToolbar.setNavigationIcon(R.drawable.ic_nav_up);
            } else {
                //supportActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                mToolbar.setNavigationIcon(R.drawable.ic_nav_humburger);
            }*/
        }

        mContext.HiCardPlay("flChange",fl_flag_now,"");
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


    /** showIndicator>>負責將nav傳過來的id轉成string **/
    private void showIndicator(boolean showIndicator_flag) {
        mViewPagerIndicator.setVisibility(showIndicator_flag ? View.VISIBLE : View.GONE);
        mViewPagerIndicator.setClickable(false);
    }

    private void setFl_FlagNow(String fl_flagNow){
        fl_flag_now = fl_flagNow;
        mContext.getFabMainManager().setFl_FlagNow(fl_flag_now);
    }

    private void setPagePosition(int PagePosition){
        mPagePosition = PagePosition;
        mContext.getFabMainManager().setPagePosition(PagePosition);
    }

    /** addPage>>負責多頁面的fl存儲到List **/
    private List<ViewPagerItem> addPage(int titleResource, Fragment fragment, String TAG) {
        ViewPagerItem item = new ViewPagerItem();
        item.setTitle(mContext, titleResource);
        item.setFragment(fragment);
        item.setFl_TAG(TAG);
        mListVPItem.add(item);
        return mListVPItem;
    }


    //TODO----ViewPagerListener----
    private class ViewPagerListener implements ViewPager.OnPageChangeListener{

        private int mPagePosition_before=0;

        @Override
        public void onPageScrolled(int position,
                                   float positionOffset,int positionOffsetPixels) {
            mContext.getFabMainManager().fabMainClose(mPagePosition);
        }

        @Override
        public void onPageSelected(int position) {
            //mPagePosition = position;
            setPagePosition(position);

            replaceTitle(String.valueOf(mViewPagerAdapter.getPageTitle(position)));
            //fabMainChange(mPagePosition);
            if (fl_flag_now.equals("fl_navHome")){
                mContext.getFabMainManager().fabMainChange(mPagePosition);
            }

            Log.d(mTAG, "**ViewPagerListener**"+mViewPagerAdapter.getPageTitle(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                mPagePosition_before = mPagePosition;

            } else if (state == ViewPager.SCROLL_STATE_IDLE
                    && mPagePosition!=mPagePosition_before
                    && fl_flag_now.equals("fl_navHome")){
                //fabMainChange(mPagePosition);

                mContext.HiCardPlay("flChange","fl_navHome","");
            }
        }

        private ViewPagerListener replaceTitle(String title) {
            ActionBar supportActionBar = mContext.getSupportActionBar();
            assert supportActionBar != null;
            supportActionBar.setTitle(title);
            return this;
        }
    }
}
