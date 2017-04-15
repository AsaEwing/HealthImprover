package com.asaewing.healthimprover.app2.fl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Manager.AccountManager;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.ViewOthers.CircleImageView;
import com.asaewing.healthimprover.app2.Others.DownloadImageTask;
import com.asaewing.healthimprover.app2.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.concurrent.ExecutionException;

/**
 *
 */
public class fl_BasicInfo extends RootFragment implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    boolean flag_google = false, flag_facebook = false;

    //private String personName = "";
    //private String personEmail = "";
    //private String personId = "";
    //private Uri personPhoto = Uri.parse("");

    public fl_BasicInfo() {
        // Required empty public constructor
    }

    public static fl_BasicInfo newInstance() {
        fl_BasicInfo fragment = new fl_BasicInfo();

        return fragment;
    }

    //TODO----Data----
    @Override
    public void mSaveState() {

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
        flag_google = getMainActivity().flag_google;

        rootView=inflater.inflate(R.layout.fl_bi2_main, container, false);

        // Button listeners
        rootView.findViewById(R.id.signio_google_bt).setOnClickListener(this);
        rootView.findViewById(R.id.signio_facebook_bt).setOnClickListener(this);

        if (flag_google){
            updateUI(true);
        } else {
            updateUI(false);
        }
        //TAG
        Log.d(TAG, "**" + TAG + "**SignInBtCreate");

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
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signio_google_bt:

                if (!flag_google) {
                    getMainActivity().getAccountManager().signIn();
                } else {
                    getMainActivity().getAccountManager().signOut();
                }

                //TAG
                Log.d(TAG, "**" + TAG + "**signio_google_bt**" + flag_google);
                break;
            case R.id.signio_facebook_bt:

                //TAG
                Log.d(TAG, "**" + TAG + "**signio_facebook_bt**" + flag_facebook);
                break;
        }

    }

    private void updateUI(boolean signedIn) {

        //TAG
        Log.d(TAG, "**"+TAG+"**updateUI**" + signedIn);
        Button google_bt = (Button)rootView.findViewById(R.id.signio_google_bt);
        CircleImageView acGoogleImage = (CircleImageView)rootView.findViewById(R.id.signio_ac_image);
        TextView acGoogleName = (TextView)rootView.findViewById(R.id.signio_ac_name);
        TextView acGoogleMail = (TextView)rootView.findViewById(R.id.signio_ac_mail);

        if (signedIn) {
            google_bt.setText(R.string.signio_google_bt_out);

            try {
                String tmpBitmap = getMainActivity().getDataManager().
                        mInfoMap.IMgetString(HiDBHelper.KEY_AC_Image_url);
                Bitmap bitmap = new DownloadImageTask(getMainActivity(),"acGoogleImage",acGoogleImage)
                        .execute(tmpBitmap).get();
                acGoogleImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                //InterruptedException | ExecutionException
                e.printStackTrace();
            }

            acGoogleName.setText(getMainActivity().getDataManager().mInfoMap.IMgetString(HiDBHelper.KEY_AC_Name));
            acGoogleMail.setText(getMainActivity().getDataManager().mInfoMap.IMgetString(HiDBHelper.KEY_AC_Account));

            flag_google = true;

        } else {
            google_bt.setText(R.string.signio_google_bt_in);

            acGoogleImage.setImageDrawable(null);
            acGoogleName.setText("false**Name");
            acGoogleMail.setText("false**ID");

            flag_google = false;
        }
    }


}