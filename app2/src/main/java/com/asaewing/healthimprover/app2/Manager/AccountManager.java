package com.asaewing.healthimprover.app2.Manager;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.system.ErrnoException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.CT48;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.Others.InfoMap;
import com.asaewing.healthimprover.app2.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;

public class AccountManager implements Parcelable {

    private MainActivity2 mContext;
    private String mTAG;

    private boolean isAccount = false;

    private InfoMap mInfoMap;
    private HiDBHelper helper;
    private VolleyManager volleyManager;
    private DataManager dataManager;

    public String personName;
    public String personEmail;
    public String personId;
    public Uri personPhoto;

    public GoogleAccount googleAccount;
    public FacebookAccount facebookAccount;
    public AccountChild accountChild;

    public AccountManager(MainActivity2 context,
                          String TAG){
        this.mContext = context;
        this.mTAG = TAG+" , AC Manager";
        //this.volleyManager = mContext.getVolleyManager();
        this.dataManager = mContext.getDataManager();
        this.mInfoMap = dataManager.mInfoMap;
        this.helper = dataManager.helper;

        Cursor hiCursor= helper.HiSelect();
        hiCursor.moveToFirst();
        String tmpAC_Google = "";
        String tmpAC_Facebook = "";
        try {
            tmpAC_Google = hiCursor.getString(
                    hiCursor.getColumnIndex(HiDBHelper.KEY_AC_GoogleMail));
            tmpAC_Facebook = hiCursor.getString(
                    hiCursor.getColumnIndex(HiDBHelper.KEY_AC_FacebookMail));
        } catch (Exception e){
            e.printStackTrace();
        }

        hiCursor.close();

        int tmpCount_Google = tmpAC_Google.length();
        int tmpCount_Facebook = tmpAC_Facebook.length();

        String tmpShow = "";

        if ((tmpCount_Google+tmpCount_Facebook)==0){
            tmpShow = "No Account";
            accountChild = null;
            signInAccount();
        } else if (tmpCount_Google>0 && tmpCount_Facebook>0){
            tmpShow = "Two had";
            accountChild = new GoogleAccount(mContext,TAG,this);
            isAccount = true;
        } else {
            if (tmpCount_Google>0){
                tmpShow = "Google";
                accountChild = new GoogleAccount(mContext,TAG,this);
                isAccount = true;
            } else if (tmpCount_Facebook>0){
                tmpShow = "Facebook";
                accountChild = new FacebookAccount(mContext,TAG,this);
                isAccount = true;
            }
        }

        Toast.makeText(mContext,tmpShow, Toast.LENGTH_LONG).show();

    }

    public void signInAccount(){
        signInDialog1();
    }

    private void signInDialog1(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle(mContext.getString(R.string.signIn1_title));
        builder1.setView(R.layout.sign_in1);

        builder1.setPositiveButton(R.string.signIn1_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.fabMainClose();
                        signInDialog2();

                    }
                });
        builder1.setNegativeButton(R.string.signIn_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //setBMI();
                        //MainActivity.fabMainClose();
                        mContext.getDataManager().updateData();
                        mContext.initView();
                    }
                });

        AlertDialog dialog1;
        dialog1 = builder1.create();
        dialog1.show();
        dialog1.setCancelable(false);

        TextView text1 = (TextView)dialog1.findViewById(R.id.signIn1_Text);
        assert text1 != null;
        text1.setText(mContext.getString(R.string.signIn1_content));

        mContext.hidePreProgressDialog();

    }

    private void signInDialog2(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
        builder2.setTitle(mContext.getString(R.string.signIn2_title));
        builder2.setView(R.layout.sign_in2);

        builder2.setPositiveButton(R.string.signIn2_button_OK
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.fabMainClose();
                        signInDialog1();
                    }
                });
        builder2.setNegativeButton(R.string.signIn_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContext.getDataManager().updateData();
                        mContext.initView();
                        //setBMI();
                        //MainActivity.fabMainClose();
                    }
                });

        final AlertDialog dialog2;
        dialog2 = builder2.create();
        dialog2.show();
        dialog2.setCancelable(false);

        TextView text2 = (TextView)dialog2.findViewById(R.id.signIn2_Text);
        assert text2 != null;
        text2.setText(mContext.getString(R.string.signIn2_content));

        Button google_bt = (Button)dialog2.findViewById(R.id.signio_google_bt);
        assert google_bt != null;
        google_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountChild = new GoogleAccount(mContext,mTAG,AccountManager.this);
                if (!mContext.flag_google) {
                    //googleAccount.signIn();

                    accountChild.signIn();
                    dialog2.cancel();
                } else {
                    //googleAccount.signOut();
                    accountChild.signOut();
                }
            }
        });

        Button facebook_bt = (Button)dialog2.findViewById(R.id.signio_facebook_bt);
        assert facebook_bt != null;
        facebook_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountChild = new FacebookAccount(mContext,mTAG,AccountManager.this);
                if (!mContext.flag_facebook) {
                    //googleAccount.signIn();
                    accountChild.signIn();
                    dialog2.cancel();
                } else {
                    //googleAccount.signOut();
                    accountChild.signOut();
                }
            }
        });
    }

    public ArrayList<String> arrayListInfo = null;

    public void signInDialog3(final int ii){
        if (ii<arrayListInfo.size()){
            String tmpList = arrayListInfo.get(ii);
            switch (tmpList) {
                case HiDBHelper.KEY_AC_Gender:
                    BI_Dialog(ii,HiDBHelper.KEY_AC_Gender, R.string.bi_gender_title, R.layout.bi_gender);
                    break;
                case HiDBHelper.KEY_AC_Height:
                    BI_Dialog(ii,HiDBHelper.KEY_AC_Height, R.string.Dialog_Title_Height, R.layout.number_picker_sign);
                    break;
                case HiDBHelper.KEY_AC_Weight:
                    BI_Dialog(ii,HiDBHelper.KEY_AC_Weight, R.string.Dialog_Title_Weight, R.layout.number_picker_sign);
                    break;
                case HiDBHelper.KEY_AC_Birthday:
                    DatePickerDialog datePickerDialog =
                            new DatePickerDialog(mContext.getApplicationContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            @SuppressLint("DefaultLocale")
                            String strDate = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Birthday,strDate);
                            signInDialog3(ii+1);
                        }
                    }, 2000, 6, 7);
                    datePickerDialog.setTitle(R.string.bi_birthday_title);

                    datePickerDialog.show();
                    break;
                case HiDBHelper.KEY_AC_WakeTime:
                    TimePickerDialog timePickerDialog = new TimePickerDialog(mContext.getApplicationContext(),new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            @SuppressLint("DefaultLocale")
                            String strTime1 = String.format("%02d:%02d:00",hourOfDay,minute);
                            mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime,strTime1);
                            CT48 ct48 = new CT48(strTime1);
                            mInfoMap.IMput(HiDBHelper.KEY_AC_WakeTime48,ct48.getCt48());
                            String apm = "a.m.";
                            if (hourOfDay>12){
                                hourOfDay -=12;
                                apm = "p.m.";
                            }
                            @SuppressLint("DefaultLocale")
                            String strTime2 = String.format("%02d:%02d %s",hourOfDay,minute,apm);
                            signInDialog3(ii+1);
                        }
                    }, 6, 0,false);
                    timePickerDialog.setTitle(R.string.bi_wake_time_title);

                    timePickerDialog.show();

                    break;
                default:
                    signInDialog3(ii+1);
            }
        } else {
            for (int jj=0;jj<arrayListInfo.size();jj++){
                String tmpStr = arrayListInfo.get(jj);
                if (!mInfoMap.IMgetString(tmpStr).equals("null")
                        && !mInfoMap.IMgetString(tmpStr).equals(""))
                    //volleyMethod.vpostSend_ACJson(tmpStr,mInfoMap.IMgetString(tmpStr));
                    volleyManager.vpostSend_ACJson(tmpStr,mInfoMap.IMgetString(tmpStr));
            }
            arrayListInfo = null;
            mContext.getDataManager().saveDataSP();

            //volleyMethod.vpostSend_ACJson();
            Log.d(mTAG, "**AccountInfoGet**DialogOver");
            if (mInfoMap.IMgetBoolean(HiDBHelper.KEY_Flag_UseFirst)){
                //volleyMethod.VVvpost_GetWeightRecord("","");
                volleyManager.VVvpost_GetWeightRecord("","");
            } else {
                mContext.initView();
            }

        }
    }

    private void BI_Dialog(final int ii, final String info, int title, int layout){
        String endStr = "";
        int NP_H = 0,NP_L = 0;
        if (info.equals(HiDBHelper.KEY_AC_Height)){
            NP_H = 165;
            NP_L = 0;
        } else if (info.equals(HiDBHelper.KEY_AC_Weight)){
            NP_H = 50;
            NP_L = 0;
        }

        final int[] tmpH = {NP_H};
        final int[] tmpL = {NP_L};

        int okbt = R.string.signIn1_button_OK;

        AlertDialog.Builder builderBI = new AlertDialog.Builder(mContext.getApplicationContext());
        AlertDialog dialogBI = null;

        builderBI.setTitle(mContext.getString(title));
        builderBI.setView(layout);
        if (ii == arrayListInfo.size()-1){
            okbt = R.string.signIn_button_OK;
        }

        builderBI.setPositiveButton(okbt
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MainActivity.fabMainClose();

                        if (info.equals(HiDBHelper.KEY_AC_Gender)){
                            RadioGroup group = (RadioGroup)((AlertDialog)dialog).findViewById(R.id.Diary_Sex_Group);
                            assert group != null;
                            group.getCheckedRadioButtonId();

                            int p = group.getCheckedRadioButtonId();
                            switch (p){
                                case R.id.Diary_Sex_Male_RB:
                                    mInfoMap.IMput(info,"male");
                                    break;
                                case R.id.Diary_Sex_Female_RB:
                                    mInfoMap.IMput(info,"female");
                                    break;
                            }
                        } else if (info.equals(HiDBHelper.KEY_AC_Height)){
                            float tmpFloat = 0;
                            if (tmpH[0] == 0 && tmpL[0] == 0) {
                                tmpFloat = 1f;
                            } else {
                                tmpFloat = (float) (tmpH[0] + tmpL[0]*0.01);
                            }
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Height,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_H,tmpH[0]);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Height_before_L,tmpL[0]);
                        } else if (info.equals(HiDBHelper.KEY_AC_Weight)){
                            float tmpFloat = 0;
                            if (tmpH[0] == 0 && tmpL[0] == 0) {
                                tmpFloat = 1f;
                            } else {
                                tmpFloat = (float) (tmpH[0] + tmpL[0]*0.01);
                            }
                            mInfoMap.IMput(HiDBHelper.KEY_AC_Weight,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before,tmpFloat);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_H,tmpH[0]);
                            mInfoMap.IMput(HiDBHelper.KEY_BI_Weight_before_L,tmpL[0]);
                        }
                        signInDialog3(ii+1);
                    }
                });
        /*builderBI.setNegativeButton(R.string.signIn_button_Cancel
                ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //setBMI();
                        //MainActivity.fabMainClose();
                    }
                });*/

        dialogBI = builderBI.create();
        dialogBI.show();
        dialogBI.setCancelable(false);

        if (info.equals(HiDBHelper.KEY_AC_Height) || info.equals(HiDBHelper.KEY_AC_Weight)){
            NumberPicker mNP_HW_I,mNP_HW_D;
            mNP_HW_I = (NumberPicker) dialogBI.findViewById(R.id.Diary_HW_Int);
            assert mNP_HW_I != null;
            mNP_HW_I.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    String tmpStr = String.valueOf(value);
                    if (value < 10) {
                        tmpStr = "0" + tmpStr;
                    }
                    return tmpStr;
                }
            });
            mNP_HW_I.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    tmpH[0] = newVal;
                }
            });
            mNP_HW_I.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker view, int scrollState) {

                }
            });
            mNP_HW_I.setMaxValue(250);
            mNP_HW_I.setMinValue(0);
            mNP_HW_I.setValue(NP_H);

            mNP_HW_D = (NumberPicker) dialogBI.findViewById(R.id.Diary_HW_Dec);
            assert mNP_HW_D != null;
            mNP_HW_D.setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    String tmpStr = String.valueOf(value);
                    if (value < 10) {
                        tmpStr = "0" + tmpStr;
                    }
                    return tmpStr;
                }
            });
            mNP_HW_D.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    tmpL[0] = newVal;
                }
            });
            mNP_HW_D.setOnScrollListener(new NumberPicker.OnScrollListener() {
                @Override
                public void onScrollStateChange(NumberPicker view, int scrollState) {

                }
            });
            mNP_HW_D.setMaxValue(99);
            mNP_HW_D.setMinValue(0);
            mNP_HW_D.setValue(NP_L);
        }

    }

    //TODO----Life----
    public void mOnCreate() {
        //super.mOnCreate(savedInstanceState);
        if (isAccount) accountChild.mOnCreate();
        //mContext.setVolleyManager();
    }

    public void mOnActivityResult(int requestCode, int resultCode, Intent data) {
        //super.mOnActivityResult(requestCode, resultCode, data);
        if (isAccount) accountChild.mOnActivityResult(requestCode,resultCode,data);
    }

    public void mOnStart() {
        //super.mOnStart();

        if (isAccount) accountChild.mOnStart();
    }

    public void mOnResume() {
        //super.mOnResume();
        if (isAccount) accountChild.mOnResume();
    }

    public void mOnPause() {
        //super.mOnPause();
        if (isAccount) accountChild.mOnPause();
    }

    public void mOnStop() {
        //super.mOnStop();
        if (isAccount) accountChild.mOnStop();
    }

    public void mOnRestart() {
        //super.mOnRestart();
        if (isAccount) accountChild.mOnRestart();
    }

    public void mOnDestroy() {
        //super.mOnDestroy();
        if (isAccount) accountChild.mOnDestroy();
    }

    public void signIn(){
        if (isAccount) accountChild.signIn();
    }

    public void signOut(){
        if (isAccount) accountChild.signOut();
    }

    public static final Parcelable.Creator<AccountManager> CREATOR
            = new Parcelable.Creator<AccountManager>() {
        public AccountManager createFromParcel(Parcel in) {
            return new AccountManager(in);
        }

        public AccountManager[] newArray(int size) {
            return new AccountManager[size];
        }
    };

    private AccountManager(Parcel in) {
        //mData = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public class GoogleAccount extends AccountChild {

        private String mTAG;
        private MainActivity2 mContext;
        private AccountManager mAccountManager;

        private static final int RC_SIGN_IN = 9001;
        public GoogleApiClient mGoogleApiClient;
        public GoogleSignInOptions gso;

        public GoogleAccount(MainActivity2 context, String TAG, AccountManager accountManager){
            this.mTAG = TAG;
            this.mContext = context;
            this.mAccountManager = accountManager;

        }

        private void handleSignInResult(GoogleSignInResult result) {

            Log.d(mTAG, "handleSignInResult:" + result.isSuccess());

            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    mAccountManager.personName = acct.getDisplayName();
                    mAccountManager.personEmail = acct.getEmail();
                    mAccountManager.personId = acct.getId();
                    mAccountManager.personPhoto = acct.getPhotoUrl();

                    String personIdToken = acct.getIdToken();
                    Log.d(mTAG, "**" + mTAG + "**HTTP_act1**" + mAccountManager.personId);
                    Log.d(mTAG, "**" + mTAG + "**HTTP_act2**" + personIdToken);
                    //String tmp[] = new String[1];
                    //tmp[0] = personIdToken;
                    //new HttpPHP("sendIdToken",tmp).execute();
                    //volleyMethod.vpostGet_IdToken(personIdToken);
                    //mAccountManager.volleyManager.vpostGet_IdToken(personIdToken);
                    mContext.getVolleyManager().vpostGet_IdToken(personIdToken);
                    //volleyMethod.vpost_GetAccountInfo();
                    //TAG
                    Log.d(mTAG, "**" + mTAG + "**acGot**" +
                            (mAccountManager.personPhoto != null
                                    ? mAccountManager.personPhoto.toString() : null));
                }

                mContext.updateUI(true);
            } else {
                // Signed out, show unauthenticated UI.
                mContext.updateUI(false);
            }
        }

        @Override
        public void signIn() {

            //TAG
            Log.d(mTAG, "**" + mTAG + "**signIn");
            mOnCreate();
            isAccount = true;

            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            mContext.startActivityForResult(signInIntent, RC_SIGN_IN);
            mOnStart();

            mOnResume();
        }

        @Override
        public void signOut() {

            //TAG
            Log.d(mTAG, "**" + mTAG + "**signOut");

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            mContext.updateUI(false);
                            // [END_EXCLUDE]
                        }
                    });
        }

        private void revokeAccess() {

            //TAG
            Log.d(mTAG, "**" + mTAG + "**revokeAccess");

            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            mContext.updateUI(false);
                            // [END_EXCLUDE]
                        }
                    });
        }

        //TODO----Life----
        @Override
        public void mOnCreate() {
            super.mOnCreate();

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestId()
                    .requestIdToken(mContext.getString(R.string.server_client_id))
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .enableAutoManage(mContext /* FragmentActivity */
                            , mContext /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        @Override
        public void mOnActivityResult(int requestCode, int resultCode, Intent data) {
            super.mOnActivityResult(requestCode,resultCode,data);

            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

        @Override
        public void mOnStart() {
            super.mOnStart();

            OptionalPendingResult<GoogleSignInResult> opr
                    = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(mTAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                //showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        //hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }

        @Override
        public void mOnResume() {
            super.mOnResume();
        }

        @Override
        public void mOnPause() {
            super.mOnPause();
        }

        @Override
        public void mOnStop() {
            super.mOnStop();
        }

        @Override
        protected void mOnRestart() {
            super.mOnRestart();
        }

        @Override
        public void mOnDestroy() {
            super.mOnDestroy();

            mGoogleApiClient.stopAutoManage(mContext);
        }

    }

    public class FacebookAccount extends AccountChild {

        private String mTAG;
        private MainActivity2 mContext;
        private AccountManager mAccountManager;

        public FacebookAccount(MainActivity2 context,
                               String TAG,
                               AccountManager accountManager){
            this.mTAG = TAG;
            this.mContext = context;
            this.mAccountManager = accountManager;
        }

        @Override
        public void signIn() {

        }

        @Override
        public void signOut() {

        }

        //TODO----Life----
        @Override
        public void mOnCreate() {
            super.mOnCreate();
        }

        @Override
        public void mOnActivityResult(int requestCode, int resultCode, Intent data) {
            super.mOnActivityResult(requestCode,resultCode,data);
        }

        @Override
        public void mOnStart() {
            super.mOnStart();
        }

        @Override
        public void mOnResume() {
            super.mOnResume();
        }

        @Override
        public void mOnPause() {
            super.mOnPause();
        }

        @Override
        public void mOnStop() {
            super.mOnStop();
        }

        @Override
        protected void mOnRestart() {
            super.mOnRestart();
        }

        @Override
        public void mOnDestroy() {
            super.mOnDestroy();
        }
    }

    protected class AccountChild {

        public AccountChild(){

        }

        public void signIn() {

        }

        public void signOut() {

        }

        //TODO----Life----
        public void mOnCreate() {
            //super.mOnCreate(savedInstanceState);
        }

        public void mOnActivityResult(int requestCode, int resultCode, Intent data) {
            //super.mOnActivityResult(requestCode, resultCode, data);
        }

        public void mOnStart() {
            //super.mOnStart();
        }

        public void mOnResume() {
            //super.mOnResume();
        }

        public void mOnPause() {
            //super.mOnPause();
        }

        public void mOnStop() {
            //super.mOnStop();
        }

        protected void mOnRestart() {
            //super.mOnRestart();
        }

        public void mOnDestroy() {
            //super.mOnDestroy();
        }

    }

}
