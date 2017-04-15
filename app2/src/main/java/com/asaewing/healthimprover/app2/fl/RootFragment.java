package com.asaewing.healthimprover.app2.fl;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.R;

import java.util.ArrayList;

import com.asaewing.healthimprover.app2.Interface.RL_Action;

/**
 * 
 */
public class RootFragment extends Fragment implements RL_Action{

    //TODO--宣告物件
    protected static String TAG = null;
    protected View rootView;
    protected Bundle mSavedInstanceState;

    public boolean setFabUse = false;
    public boolean fabFlag = false;
    protected ArrayList<FloatingActionButton> fab_List;
    protected ArrayList<RelativeLayout> fabRL_List;
    protected ArrayList<TextView> fabText_List;
    protected ArrayList<String> fabString_List;

    public RootFragment() {
        // Required empty public constructor
    }

    public static RootFragment newInstance() {
        RootFragment fragment = new RootFragment();

        return fragment;
    }

    //TODO----Data----
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        Log.d(TAG,"**"+TAG+"**onSaveInstanceState");
        super.onSaveInstanceState(savedInstanceState);

    }

    public void mSaveState() {

        Log.d(TAG,"**"+TAG+"**mSaveState");
    }

    //TODO----生命週期----
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d(TAG,"**"+TAG+"**onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"**"+TAG+"**onCreate");
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"**"+TAG+"**onCreateView");

        rootView = container.getRootView();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG,"**"+TAG+"**onActivityCreated");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG,"**"+TAG+"**onActivityResult");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG,"**"+TAG+"**onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG,"**"+TAG+"**onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG,"**"+TAG+"**onPause");
    }

    @Override
    public void onStop() {
        mSaveState();

        Log.d(TAG,"**"+TAG+"**onStop");

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG,"**"+TAG+"**onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG,"**"+TAG+"**onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG,"**"+TAG+"**onDetach");
    }

    public MainActivity2 getMainActivity(){
        //super.getActivity();
        /*if(getContext().getClass() == MainActivity2.class){
            return (MainActivity2) getContext();
        }*/
        if(getContext() instanceof MainActivity2){
            return (MainActivity2) getContext();
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(Objects.equals(MainActivity2.class, getActivity())){
                return (MainActivity2) getActivity();
            }
        }*/

        return null;
    }

    /*public VolleyManager getVolleyManager(){
        if (getMainActivity() == null) {
            return null;
        }
        return getMainActivity().getVolleyManager();
    }*/

    /*public DataManager getDataManager(){
        if (getMainActivity() != null){
            return getMainActivity().getDataManager();
        }
        return null;
    }*/

    /*public AccountManager getAccountManager(){
        if (getMainActivity() != null){
            return getMainActivity().getAccountManager();
        }
        return null;
    }*/

    @Override
    public void openMoveRL(boolean setFabUse, RelativeLayout relativeLayout, double moveY) {
        superAction.openMoveRL(setFabUse,relativeLayout,moveY);
    }

    @Override
    public void closeRL(boolean setFabUse, RelativeLayout relativeLayout, long Time) {
        superAction.closeRL(setFabUse,relativeLayout,Time);
    }

    public void fabMainClick(){

    }

    public void fabClose(boolean fabMain, final int fabImage) {
        if (setFabUse) {

            long tmpTime2;
            if (!fabFlag) {
                if (fabMain) {
                    tmpTime2 = 360;
                } else {
                    tmpTime2 = 280;
                }

                getMainActivity().getFabMainManager().fabMain.setClickable(false);

                for (int ii=0;ii<fab_List.size();ii++){
                    fab_List.get(ii).setClickable(false);
                }

                getMainActivity().getFabMainManager().fabMain.animate().rotationBy(180f)
                        .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            /*MainActivity2.fabMain.setClickable(false);
                            fab_Height.setClickable(false);
                            fab_Weight.setClickable(false);*/
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                getMainActivity().getFabMainManager().fabMain.setImageResource(fabImage);

                                getMainActivity().getFabMainManager().fabMain.animate().rotationBy(180f)
                                        .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                getMainActivity().getFabMainManager().fabMain.setClickable(true);
                                                fabFlag = true;
                                                getMainActivity().getFabMainManager().fabMain.setRotation(0);
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                for (int ii=0;ii<fabRL_List.size();ii++){
                    closeRL(setFabUse,fabRL_List.get(ii), tmpTime2);
                }

                //fabFlag = true;
            }
        }
    }

    public void fabOpen(boolean fabMain) {
        if (setFabUse) {
            if (fabFlag) {
                getMainActivity().getFabMainManager().fabMain.setClickable(false);

                for (int ii=0;ii<fab_List.size();ii++){
                    fab_List.get(ii).setClickable(false);
                }
                for (int ii=0;ii<fabText_List.size();ii++){
                    fabText_List.get(ii).setText(fabString_List.get(ii));
                }

                if (fabMain) {
                    getMainActivity().getFabMainManager().fabMain.animate().rotationBy(180f)
                            .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                /*MainActivity2.fabMain.setClickable(false);
                                fab_Height.setClickable(false);
                                fab_Weight.setClickable(false);*/
                                }

                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    getMainActivity().getFabMainManager().fabMain.setImageResource(R.drawable.ic_fab_cancel_bk);

                                    getMainActivity().getFabMainManager().fabMain.animate().rotationBy(180f)
                                            .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                            .setListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    getMainActivity().getFabMainManager().fabMain.setClickable(true);

                                                    for (int ii=0;ii<fab_List.size();ii++){
                                                        fab_List.get(ii).setClickable(true);
                                                    }

                                                    fabFlag = false;
                                                    getMainActivity().getFabMainManager().fabMain.setRotation(0);
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {

                                                }
                                            });
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                }

                double tmpWidth = getMainActivity().getFabMainManager().fabMain.getWidth() * 1.15;

                for (int ii=0;ii<fabRL_List.size();ii++){
                    openMoveRL(setFabUse,fabRL_List.get(ii), tmpWidth * (ii+1));
                }

                //fabFlag = false;
            }
        }
    }

}
