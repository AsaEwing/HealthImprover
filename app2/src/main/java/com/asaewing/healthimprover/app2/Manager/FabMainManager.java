package com.asaewing.healthimprover.app2.Manager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.R;
import com.asaewing.healthimprover.app2.RootActivity2;

public class FabMainManager {

    private MainActivity2 mContext;
    private String mTAG;
    private boolean fabMain_NeedGONE;
    private boolean fabMain_isGONE;
    private boolean fabOpen;

    private String fl_flag_now;
    public FloatingActionButton fabMain;

    private int mPagePosition;
    private View mCoverView;

    public FabMainManager(MainActivity2 context,
                          String TAG,
                          FloatingActionButton fab,
                          View CoverView){
        this.mContext = context;
        this.mTAG = TAG+" , FabMainManager";
        this.fabMain = fab;

        this.fabOpen = false;
        this.fabMain_isGONE = false;
        this.fabMain_NeedGONE = false;
        this.mCoverView = CoverView;
    }

    public void setAction(boolean NeedGONE){

    }

    public void setPagePosition(int PagePosition){
        this.mPagePosition = PagePosition;
    }

    public void setFl_FlagNow(String fl_flag_now){
        this.fl_flag_now = fl_flag_now;
    }

    /** fabMainChange>>負責把主fab在更換fl或頁面時，進行互動動畫與標誌轉換 **/
    public void fabMainChange() {
        fabMainChange(mPagePosition);
    }

    /** fabMainChange>>負責把主fab在更換fl或頁面時，進行互動動畫與標誌轉換 **/
    public void fabMainChange(final int pagePositionVP) {
        fabMain_NeedGONE = false;

        if (fabMain_isGONE) {
            //原本fabMain是GONE，所以要看他待會要不要回復，以及要的話應該長怎樣
            switch (fl_flag_now) {
                case "fl_navHome":
                    fabMain.setVisibility(View.VISIBLE);
                    fabMain.setClickable(false);
                    fabMain_isGONE = false;
                    switch (pagePositionVP) {
                        case 0:
                            fabMain.setImageResource(R.drawable.ic_fab_paint_bk);
                            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0x3f, 0xc0, 0x06)));
                            break;
                        case 1:
                            fabMain.setImageResource(R.drawable.ic_menu_calories);
                            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0xff, 0x8f, 0x00)));
                            break;
                        case 2:
                            fabMain.setImageResource(R.drawable.ic_menu_sleep);
                            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0xff, 0xd2, 0xf2)));
                            break;
                    }
                    break;

                case "fl_Map":
                    fabMain.setVisibility(View.VISIBLE);
                    fabMain.setClickable(false);
                    fabMain_isGONE = false;
                    fabMain.setImageResource(R.drawable.ic_fab_mylocation_blue);
                    fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0xff, 0xff, 0xff)));
                    break;

                default:
                    fabMain_NeedGONE = true;
                    break;
            }

            if (!fabMain_NeedGONE) {
                //而且待會也不需要隱藏，所以動畫開始把fabMain從無變到有
                //記得要等到動畫結束，才能使fabMain可以點擊
                fabMain.setRotation(0);
                fabMain.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(150)
                        .setInterpolator(new OvershootInterpolator())
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                fabMain.setClickable(false);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                fabMain.setClickable(true);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
            }

        } else {
            //原本fabMain是VISIBLE，所以要看他待會要不要回復，以及要的話應該長怎樣，所以先變成無
            fabMain.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(150)
                    .setInterpolator(new OvershootInterpolator())
                    .setListener(new Animator.AnimatorListener() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            fabMain.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabMain.setRotation(0);
                            switch (fl_flag_now) {
                                case "fl_navHome":
                                    switch (pagePositionVP) {
                                        case 0:
                                            fabMain.setImageResource(R.drawable.ic_fab_paint_bk);
                                            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0x3f, 0xc0, 0x06)));
                                            break;
                                        case 1:
                                            fabMain.setImageResource(R.drawable.ic_menu_calories);
                                            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0xff, 0x8f, 0x00)));
                                            break;
                                        case 2:
                                            fabMain.setImageResource(R.drawable.ic_menu_sleep);
                                            fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0xff, 0xd2, 0xf2)));
                                            break;
                                    }
                                    break;

                                case "fl_Map":
                                    fabMain.setImageResource(R.drawable.ic_fab_mylocation_blue);
                                    fabMain.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(0xff, 0xff, 0xff)));
                                    break;

                                default:
                                    fabMain_NeedGONE = true;
                                    break;
                            }
                            fabMain.setRotation(0);

                            if (fabMain_NeedGONE) {
                                fabMain.setVisibility(View.GONE);
                                fabMain_isGONE = true;
                            } else {
                                fabMain.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(150)
                                        .setInterpolator(new OvershootInterpolator())
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                fabMain.setClickable(true);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }
    }

    /** fabMainClick>> **/
    public void fabMainClick(int mPagePosition) {
        fabOpen = !fabOpen;

        if (fabOpen) {
            mCoverView.setVisibility(View.VISIBLE);
            mCoverView.setClickable(false);
            //mCoverView.setClickable(true);
        } else {
            //String tmpHi = "";
            //assert HiCard_Text != null;
            //HiCard_Text.start(tmpHi);
        }

        CoverViewAnimate(fabOpen);

        switch (fl_flag_now) {
            case "fl_navHome":

                switch (mPagePosition){
                    case 0:
                        mContext.getViewPagerAdapter().getSelfFragment(0).fabMainClick();
                        break;
                    case 1:
                        mContext.getViewPagerAdapter().getSelfFragment(1).fabMainClick();
                        break;
                    case 2:
                        break;
                }

                break;

        }
    }

    /** fabMainClose>>負責將展開的小fab收回的動畫 **/
    public void fabMainClose(int mPagePosition) {
        if (fabOpen) {
            fabOpen = false;

            CoverViewAnimate(false);

            switch (fl_flag_now) {
                case "fl_navHome":
                    switch (mPagePosition) {
                        case 0:
                            mContext.getViewPagerAdapter().getSelfFragment(0)
                                    .fabClose(false,R.drawable.ic_fab_paint_bk);
                            break;
                        case 1:
                            mContext.getViewPagerAdapter().getSelfFragment(1)
                                    .fabClose(false,R.drawable.ic_menu_calories);
                            break;
                        case 2:
                            //fl_Sleep.newInstance().fabClose(false);
                            break;
                    }
                    break;

                case "fl_Map":
                    //dimBackground(1.0f,0.5f);
                    break;
            }
        }
    }

    public void CoverViewAnimate(final boolean needOpen){
        mCoverView.animate().scaleX(needOpen ? 100F : 0F)
                .scaleY(needOpen ? 100F : 0F)
                .alpha(needOpen ? 1F : 0F)
                .setDuration(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mCoverView.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!needOpen) {
                            mCoverView.setVisibility(View.GONE);
                        } else {
                            mCoverView.setClickable(true);
                        }
                    }
                });
    }
}
