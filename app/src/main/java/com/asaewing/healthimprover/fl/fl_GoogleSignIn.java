package com.asaewing.healthimprover.fl;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asaewing.healthimprover.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 *
 */
public class fl_GoogleSignIn extends Fragment implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    //TODO--宣告物件
    private String TAG = getClass().getSimpleName();
    private View rootView;
    
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    public fl_GoogleSignIn() {
        // Required empty public constructor
    }

    public static fl_GoogleSignIn newInstance() {
        fl_GoogleSignIn fragment = new fl_GoogleSignIn();

        return fragment;
    }

    //TODO----生命週期----
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //TAG
        Log.d(TAG, "**"+TAG+"**onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TAG
        Log.d(TAG, "**" + TAG + "**onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //TAG
        Log.d(TAG, "**"+TAG+"**onCreateView");

        if(rootView==null){
            //rootView=inflater.inflate(R.layout.content_main, null);
            rootView=inflater.inflate(R.layout.fl_googlesignin, container, false);

            // Views
            mStatusTextView = (TextView) rootView.findViewById(R.id.status);

            // Button listeners
            rootView.findViewById(R.id.sign_in_button).setOnClickListener(this);
            rootView.findViewById(R.id.sign_out_button).setOnClickListener(this);
            rootView.findViewById(R.id.disconnect_button).setOnClickListener(this);

            //TAG
            Log.d(TAG, "**" + TAG + "**SignInBtCreate");

            SignInButton signInButton = (SignInButton) rootView.findViewById(R.id.sign_in_button);
            signInButton.setSize(SignInButton.SIZE_STANDARD);
            //signInButton.setScopes(gso.getScopeArray());


        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //TAG
        Log.d(TAG, "**" + TAG + "**onActivityResult");

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //TAG
        Log.d(TAG, "**" + TAG + "**onActivityCreated");

        //Google Sign-In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        //TAG
        Log.d(TAG, "**"+TAG+"**onStart");

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
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //TAG
        Log.d(TAG, "**" + TAG + "**onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        //TAG
        Log.d(TAG, "**"+TAG+"**onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        //TAG
        Log.d(TAG, "**"+TAG+"**onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //TAG
        Log.d(TAG, "**"+TAG+"**onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TAG
        Log.d(TAG, "**"+TAG+"**onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //TAG
        Log.d(TAG, "**"+TAG+"**onDetach");

        //mGoogleApiClient.stopAutoManage(getActivity());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                //TAG
                Log.d(TAG, "**" + TAG + "**sign_in_button");

                signIn();
                break;
            case R.id.sign_out_button:
                //TAG
                Log.d(TAG, "**" + TAG + "**sign_out_button");

                signOut();
                break;
            case R.id.disconnect_button:
                //TAG
                Log.d(TAG, "**" + TAG + "**disconnect_button");

                revokeAccess();
                break;
        }
    }


    //TODO----SignInNeed----
    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
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
        Log.d(TAG, "**"+TAG+"**hideProgressDialog");

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {

        //TAG
        Log.d(TAG, "**"+TAG+"**updateUI");

        if (signedIn) {
            rootView.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            rootView.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            rootView.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
}