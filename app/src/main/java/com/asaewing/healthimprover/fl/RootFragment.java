package com.asaewing.healthimprover.fl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 */
public class RootFragment extends Fragment {

    //TODO--宣告物件
    protected static String TAG = null;
    protected View rootView;
    protected Bundle mSavedInstanceState;

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

}
