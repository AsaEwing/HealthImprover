package com.asaewing.healthimprover.fl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asaewing.healthimprover.MainActivity;
import com.asaewing.healthimprover.Others.CircularProgressBar;
import com.asaewing.healthimprover.R;

/**
 *
 */
public class fl_Sleep extends RootFragment implements View.OnClickListener {

    public fl_Sleep() {
        // Required empty public constructor
    }

    public static fl_Sleep newInstance() {
        fl_Sleep fragment = new fl_Sleep();

        return fragment;
    }

    //TODO----Data----
    @Override
    public void mSaveState() {
        super.mSaveState();
    }

    //TODO----生命週期----
    @Override
    public void onAttach(Context context) {
        TAG = getClass().getSimpleName();

        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        rootView=inflater.inflate(R.layout.fl_sleep, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG,"**"+TAG+"**onClick");

    }

    public void fabMainClick() {
        Log.d(TAG,"**"+TAG+"**buttonClick**fabMain******");
    }

}