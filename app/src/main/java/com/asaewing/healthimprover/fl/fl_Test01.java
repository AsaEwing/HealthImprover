package com.asaewing.healthimprover.fl;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.asaewing.healthimprover.MainActivity;
import com.asaewing.healthimprover.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

/**
 * <com.google.android.gms.ads.AdView
 * android:id="@+id/adView"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * ads:adSize="BANNER"
 * ads:adUnitId="@string/banner_ad_unit_id"
 * android:layout_marginLeft="0dp"
 * android:layout_alignParentBottom="true"
 * android:layout_centerHorizontal="true">
 * </com.google.android.gms.ads.AdView>
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class fl_Test01 extends RootFragment {

    /*AdView mAdView;
    AdRequest adRequest;*/

    public fl_Test01() {
        // Required empty public constructor
    }

    public static fl_Test01 newInstance() {
        fl_Test01 fragment = new fl_Test01();

        return fragment;
    }

    //TODO----Data----


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
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fl_test01, container, false);

        //ADs
        /*mAdView = (AdView) rootView.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(MainActivity.mInfoMap.IMgetString("deviceId"))
                .build();
        mAdView.loadAd(adRequest);*/

        return rootView;
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

        //mAdView.destroy();
        //mAdView.destroyDrawingCache();
        //mAdView.removeAllViewsInLayout();
        //adRequest = null;
        //mAdView = null;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
