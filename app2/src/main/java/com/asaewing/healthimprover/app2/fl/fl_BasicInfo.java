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
import com.asaewing.healthimprover.app2.Others.CircleImageView;
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

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private ProgressDialog mProgressDialog;
    boolean flag_google = false, flag_facebook = false;

    private String personName;
    private String personEmail;
    private String personId;
    private Uri personPhoto;

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
        //TAG = fl_BasicInfo.class.getSimpleName();
        TAG = getClass().getSimpleName();

        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Google Sign-In
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/
        //mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
        //        .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
        //        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        //        .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        flag_google = MainActivity2.flag_google;

        rootView=inflater.inflate(R.layout.fl_bi2_main, container, false);

        // Button listeners
        rootView.findViewById(R.id.signio_google_bt).setOnClickListener(this);
        rootView.findViewById(R.id.signio_facebook_bt).setOnClickListener(this);

        //TAG
        Log.d(TAG, "**" + TAG + "**SignInBtCreate");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Google Sign-In
        //GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        //        .requestEmail()
        //        .build();
        //mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
        //        .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
        //        //.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        //        .build();*/

        gso = MainActivity2.gso;
        mGoogleApiClient = MainActivity2.mGoogleApiClient;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr
                = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
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

        mGoogleApiClient.stopAutoManage(getActivity());
        //mGoogleApiClient.clearDefaultAccountAndReconnect();
        //gso = null;
        //mGoogleApiClient = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.signio_google_bt:

                if (!flag_google) {
                    signIn();
                } else {
                    signOut();
                }

                //TAG
                Log.d(TAG, "**" + TAG + "**signio_google_bt**" + flag_google);
                break;
            case R.id.signio_facebook_bt:

                //new HttpPHP(2,"GET", "","").execute();
                /*try {
                    String strResult = new HttpPHP(2,"GET", "","").execute().get();
                    ImageView acFbImage = (ImageView)rootView.findViewById(R.id.signio_ac_image_fb);
                    TextView acFbName = (TextView)rootView.findViewById(R.id.signio_ac_name_fb);

                    Bitmap bitmap = BitmapFactory.decodeStream(strResult.indexOf("imgTest"));
                    acFbImage.setImageBitmap(bitmap);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }*/

                //TAG
                Log.d(TAG, "**" + TAG + "**signio_facebook_bt**" + flag_facebook);
                break;
        }

    }

    //TODO----SignInNeed----
    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                personName = acct.getDisplayName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();
                //TAG
                Log.d(TAG, "**" + TAG + "**acGot**" +
                        (personPhoto != null ? personPhoto.toString() : null));
            }

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void signIn() {

        //TAG
        Log.d(TAG, "**" + TAG + "**signIn");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {

        //TAG
        Log.d(TAG, "**" + TAG + "**signOut");

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void revokeAccess() {

        //TAG
        Log.d(TAG, "**" + TAG + "**revokeAccess");

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void showProgressDialog() {

        //TAG
        Log.d(TAG, "**" + TAG + "**showProgressDialog");

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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
                Bitmap bitmap = new DownloadImageTask("acGoogleImage",acGoogleImage)
                        .execute(personPhoto.toString()).get();
                acGoogleImage.setImageBitmap(bitmap);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            acGoogleName.setText(personName);

            String tmpA = personEmail.substring(0, personEmail.indexOf("@"));
            String tmpB = personEmail.substring(personEmail.indexOf("@"));
            acGoogleMail.setText("Account: "+tmpA);

            flag_google = true;

            //HttpPHP(1,personName);

            //因為伺服器斷網
            //new HttpPHP(2,"ADD",
            //        personName,MainActivity.mInfoMap.IMgetString("acGoogleImage")).execute();

        } else {
            google_bt.setText(R.string.signio_google_bt_in);

            acGoogleImage.setImageDrawable(null);
            acGoogleName.setText("false**Name");
            acGoogleMail.setText("false**ID");

            flag_google = false;
        }
    }


}