package com.asaewing.healthimprover.app2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.asaewing.healthimprover.app2.Manager.VolleyManager;
import com.asaewing.healthimprover.app2.Others.CT48;
import com.asaewing.healthimprover.app2.Others.HiDBHelper;
import com.asaewing.healthimprover.app2.Adapter.ListAdapter;
import com.asaewing.healthimprover.app2.ViewOthers.MyAutoView;
import com.asaewing.healthimprover.app2.ViewOthers.ScrollDisabledListView;
import com.asaewing.healthimprover.app2.ViewOthers.TypeTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.asaewing.healthimprover.app2.Interface.RL_Action;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;


public class FoodActivity extends AppCompatActivity
        implements View.OnClickListener
        ,AdapterView.OnItemClickListener
        ,View.OnTouchListener, RL_Action{

    protected String TAG = null;
    protected boolean fabOpen = false;       //判斷fab是否展開了小fab
    protected boolean setFabUse = true;
    private String date = "";
    private String time = "";

    private VolleyManager mVolleyManager;

    protected Toolbar mToolbar;
    private static HiDBHelper helper;
    private MyAutoView AutoText_R,AutoText_F,AutoText_Amount,AutoText_Size,AutoText_Sugar,AutoText_HotCold;
    private EditText AutoText_selfCal;
    private Button AutoBt_Add,AutoBt_Delete;
    private Button AutoDate,AutoTime;
    private ScrollDisabledListView AutoList;
    private FloatingActionButton fabMain,fabSend,fabCancel;
    private RelativeLayout RL_Send,RL_Cancel;
    private LinearLayout AutoLL_Size,AutoLL_Sugar,AutoLL_HotCold,AutoLL_selfCal;
    private View mCoverView;

    ListAdapter mListAdapter;
    //ListAdapter mListAdapter = new ListAdapter(getApplication());
    ArrayAdapter<String> adapterR,adapterF,adapterAmount;

    final boolean filterFlag = true;
    final int[] CountAutoFinish = {0};
    

    //Auto Text
    String[] AutoItemT_Amount = {"1","2","3","4","5","6","7","8","9","10"
            ,"11","12","13","14","15","16","17","18","19","20"
            ,"21","22","23","24","25","26","27","28","29","30"};
    String[] AutoItemT_R = new String[0];
    String[] AutoItemT_F = new String[0];
    //Auto Id
    String[] AutoItemId_R = new String[0];
    String[] AutoItemId_F = new String[0];
    //Range F
    int[] AutoRangeIndex_F = new int[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        Log.d(TAG, "**" + TAG + "**onCreate");  //Log，onCreate Start
        setContentView(R.layout.food_activity); //主Layout

        mVolleyManager = MainActivity2.getVolleyManager();

        helper = new HiDBHelper(getApplicationContext());

        //Toolbar Settings
        mToolbar = (Toolbar)findViewById(R.id.Food_toolbar);
        this.setSupportActionBar(mToolbar);
        mToolbar.setOnClickListener(this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("飲食紀錄");

        fabMain = (FloatingActionButton)findViewById(R.id.Food_fab_Main);
        fabSend = (FloatingActionButton)findViewById(R.id.Food_Send);
        fabCancel = (FloatingActionButton)findViewById(R.id.Food_Cancel);
        fabMain.setOnClickListener(this);
        fabSend.setOnClickListener(this);
        fabCancel.setOnClickListener(this);
        
        RL_Send = (RelativeLayout)findViewById(R.id.Food_Send_RL);
        RL_Cancel = (RelativeLayout)findViewById(R.id.Food_Cancel_RL);
        AutoLL_Size = (LinearLayout)findViewById(R.id.AT_LL_Size);
        AutoLL_Sugar = (LinearLayout)findViewById(R.id.AT_LL_Sugar);
        AutoLL_HotCold = (LinearLayout)findViewById(R.id.AT_LL_HotCold);
        AutoLL_selfCal = (LinearLayout)findViewById(R.id.AT_LL_selfCal);
        RL_Send.setOnClickListener(this);
        RL_Cancel.setOnClickListener(this);
        AutoLL_Size.setOnClickListener(this);
        AutoLL_Sugar.setOnClickListener(this);
        AutoLL_HotCold.setOnClickListener(this);
        AutoLL_selfCal.setOnClickListener(this);

        mCoverView = findViewById(R.id.Food_cover);
        mCoverView.setVisibility(View.GONE);

        CardView HiCard = (CardView)findViewById(R.id.Food_HiCardView);
        assert HiCard != null;
        HiCard.setOnClickListener(this);
        TypeTextView HiCard_Text = (TypeTextView)findViewById(R.id.Food_HiCardView_Text);
        HiCard_Text.setOnTypeViewListener( new TypeTextView.OnTypeViewListener( ) {
            @Override
            public void onTypeStart() {
                //print( "onTypeStart" );
            }

            @Override
            public void onTypeOver() {
                //print( "onTypeOver" );
            }
        });
        HiCard_Text.setText("記下你剛才吃了什麼");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            HiCard.setTranslationZ(mToolbar.getTranslationZ()+4f);
        }

        mListAdapter = new ListAdapter(this);

        Cursor cursorR = helper.SAIDSelect();
        Cursor cursorF = helper.FAIDSelect();
        //Auto Text
        AutoItemT_Amount = new String[]{"1","2","3","4","5","6","7","8","9","10"
                ,"11","12","13","14","15","16","17","18","19","20"
                ,"21","22","23","24","25","26","27","28","29","30"};
        AutoItemT_R = new String[cursorR.getCount()];
        AutoItemT_F = new String[cursorF.getCount()];
        //Auto Id
        AutoItemId_R = new String[cursorR.getCount()];
        AutoItemId_F = new String[cursorF.getCount()];
        //Range F
        AutoRangeIndex_F = new int[cursorR.getCount()+1];
        //String[] ARItemT_F = AutoItemT_F;
        //final String[] ARItemId_F = AutoItemId_F;

        Log.d(TAG, "**onCursor**"+cursorR.getCount()+"***"+cursorF.getCount());

        for (int ii=0;ii<cursorR.getCount();ii++){
            cursorR.moveToPosition(ii);
            AutoItemT_R[ii] =
                    cursorR.getString(cursorR.getColumnIndex(HiDBHelper.KEY_SAID_Store_Name));
            AutoItemId_R[ii] =
                    cursorR.getString(cursorR.getColumnIndex(HiDBHelper.KEY_SAID_Store_ID));
        }

        for (int jj=0;jj<cursorF.getCount();jj++){
            cursorF.moveToPosition(jj);
            AutoItemT_F[jj] =
                    cursorF.getString(cursorF.getColumnIndex(HiDBHelper.KEY_FAID_Basic_Name));
            AutoItemId_F[jj] =
                    cursorF.getString(cursorF.getColumnIndex(HiDBHelper.KEY_FAID_Basic_ID));
        }

        cursorR.close();
        cursorF.close();

        int ii = 0,jj=0;

        boolean FoodIdFirst = true;
        int indexFind = 0;
        AutoRangeIndex_F[cursorR.getCount()] = cursorF.getCount();
        for (ii=0;ii<cursorR.getCount();ii++){
            FoodIdFirst = true;

            for (jj=indexFind;jj<cursorF.getCount();jj++){
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (Objects.equals(AutoItemId_F[jj].substring(0, 4), AutoItemId_R[ii])
                            && FoodIdFirst){
                        AutoRangeIndex_F[ii]=jj;
                        FoodIdFirst = false;

                    } else if (!Objects.equals(AutoItemId_F[jj].substring(0, 4), AutoItemId_R[ii])){
                        if (!FoodIdFirst) {
                            indexFind = jj;

                        } else {
                            if (ii-1>=0){
                                AutoRangeIndex_F[ii]=-1;
                            } else {
                                AutoRangeIndex_F[ii]=0;
                            }
                        }

                        break;
                    }
                }*/
                if (AutoItemId_F[jj].substring(0, 4).equals(AutoItemId_R[ii])
                        && FoodIdFirst){
                    AutoRangeIndex_F[ii]=jj;
                    FoodIdFirst = false;

                } else if (!AutoItemId_F[jj].substring(0, 4).equals(AutoItemId_R[ii])){
                    if (!FoodIdFirst) {
                        indexFind = jj;

                    } else {
                        if (ii-1>=0){
                            AutoRangeIndex_F[ii]=-1;
                        } else {
                            AutoRangeIndex_F[ii]=0;
                        }
                    }

                    break;
                }
            }
        }

        initView();
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
    public void onStop(){
        helper.close();
        super.onStop();
    }
    @Override
    public void onBackPressed() {
        Log.d(TAG, "**" + TAG + "**onBackPressed");
        Log.d(TAG, "**key33**");

        returnMainActivity();

    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                Log.d(TAG,"**BackTask**");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                returnMainActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void returnMainActivity(){

        if (fabOpen) {
            fabOpen = false;

            long tmpTime2;
            tmpTime2 = 280;
            //MainActivity.fabMain.setClickable(false);
            fabSend.setClickable(false);
            fabCancel.setClickable(false);

            fabMain.animate().rotationBy(180f)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            /*MainActivity.fabMain.setClickable(false);
                            fab_Height.setClickable(false);
                            fab_Weight.setClickable(false);*/
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabMain.setImageResource(R.drawable.ic_fab_paint_bk);
                            fabMain.animate().rotationBy(180f)
                                    .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            fabMain.setClickable(true);
                                            fabMain.setRotation(0);

                                            fabMain.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(125)
                                                    .setInterpolator(new OvershootInterpolator())
                                                    .setListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {
                                                            fabMain.setClickable(false);
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            fabMain.setVisibility(View.GONE);

                                                            NavUtils.navigateUpFromSameTask(FoodActivity.this);
                                                            Intent upIntent = NavUtils.getParentActivityIntent(FoodActivity.this);
                                                            if (NavUtils.shouldUpRecreateTask(FoodActivity.this, upIntent)) {
                                                                // This activity is NOT part of this app's task, so create a new task
                                                                // when navigating up, with a synthesized back stack.
                                                                TaskStackBuilder.create(FoodActivity.this)
                                                                        // Add all of this activity's parents to the back stack
                                                                        .addNextIntentWithParentStack(upIntent)
                                                                        // Navigate up to the closest parent
                                                                        .startActivities();
                                                            } else {
                                                                // This activity is part of this app's task, so simply
                                                                // navigate up to the logical parent activity.
                                                                NavUtils.navigateUpTo(FoodActivity.this, upIntent);
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

            closeRL(setFabUse, RL_Cancel, tmpTime2);
            closeRL(setFabUse, RL_Send, tmpTime2);

            mCoverView.animate().scaleX(0F)
                    .scaleY(0F)
                    .alpha(0F)
                    .setDuration(100)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mCoverView.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mCoverView.setVisibility(View.GONE);
                        }
                    });

        } else {
            fabMain.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(125)
                    .setInterpolator(new OvershootInterpolator())
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            fabMain.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabMain.setVisibility(View.GONE);

                            NavUtils.navigateUpFromSameTask(FoodActivity.this);
                            Intent upIntent = NavUtils.getParentActivityIntent(FoodActivity.this);
                            if (NavUtils.shouldUpRecreateTask(FoodActivity.this, upIntent)) {
                                // This activity is NOT part of this app's task, so create a new task
                                // when navigating up, with a synthesized back stack.
                                TaskStackBuilder.create(FoodActivity.this)
                                        // Add all of this activity's parents to the back stack
                                        .addNextIntentWithParentStack(upIntent)
                                        // Navigate up to the closest parent
                                        .startActivities();
                            } else {
                                // This activity is part of this app's task, so simply
                                // navigate up to the logical parent activity.
                                NavUtils.navigateUpTo(FoodActivity.this, upIntent);
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

    @SuppressLint("DefaultLocale")
    private void initView(){

        Log.d(TAG,"**AutoRange**"+ Arrays.toString(AutoRangeIndex_F));

        //ListAdapter mListAdapter = new ListAdapter(getApplication());

        adapterR = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                AutoItemT_R);

        adapterF = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                AutoItemT_F);

        adapterAmount = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                AutoItemT_Amount);

        //input
        AutoText_R = (MyAutoView) findViewById(R.id.AT_AT_R);
        assert AutoText_R != null;
        AutoText_R.setInputType(TYPE_CLASS_TEXT);
        AutoText_R.setThreshold(1);
        AutoText_R.setAdapter(adapterR);

        AutoText_F = (MyAutoView) findViewById(R.id.AT_AT_F);
        assert AutoText_F != null;
        AutoText_F.setInputType(TYPE_CLASS_TEXT);
        AutoText_F.setThreshold(1);
        AutoText_F.setAdapter(adapterF);

        AutoText_Amount = (MyAutoView) findViewById(R.id.AT_AT_Amount);
        assert AutoText_Amount != null;
        AutoText_Amount.setInputType(TYPE_CLASS_NUMBER);
        AutoText_Amount.setThreshold(1);
        AutoText_Amount.setAdapter(adapterAmount);
        
        AutoText_Size = (MyAutoView) findViewById(R.id.AT_AT_Size);
        assert AutoText_Size != null;
        AutoText_Size.setInputType(TYPE_CLASS_TEXT);
        AutoText_Size.setThreshold(1);
        
        AutoText_Sugar = (MyAutoView) findViewById(R.id.AT_AT_Sugar);
        assert AutoText_Sugar != null;
        AutoText_Sugar.setInputType(TYPE_CLASS_TEXT);
        AutoText_Sugar.setThreshold(1);
        
        AutoText_HotCold = (MyAutoView) findViewById(R.id.AT_AT_HotCold);
        assert AutoText_HotCold != null;
        AutoText_HotCold.setInputType(TYPE_CLASS_TEXT);
        AutoText_HotCold.setThreshold(1);
        
        AutoText_selfCal = (EditText) findViewById(R.id.AT_AT_selfCal); 

        AutoBt_Add = (Button) findViewById(R.id.AutoBt_Add);
        AutoBt_Delete = (Button) findViewById(R.id.AutoBt_Delete);

        AutoDate = (Button) findViewById(R.id.AT_text_Date2);
        AutoTime = (Button) findViewById(R.id.AT_text_Time2);
        //AutoDate.setAlpha(0.1f);
        AutoDate.getBackground().setAlpha(20);
        AutoTime.getBackground().setAlpha(20);

        AutoList = (ScrollDisabledListView) findViewById(R.id.Food_List);

        AutoText_R.setOnClickListener(this);
        AutoText_F.setOnClickListener(this);
        AutoText_Amount.setOnClickListener(this);
        AutoText_Size.setOnClickListener(this);
        AutoText_Sugar.setOnClickListener(this);
        AutoText_HotCold.setOnClickListener(this);
        AutoText_selfCal.setOnClickListener(this);
        AutoBt_Add.setOnClickListener(this);
        AutoBt_Delete.setOnClickListener(this);

        AutoDate.setClickable(true);
        AutoDate.setOnClickListener(this);
        AutoTime.setClickable(true);
        AutoTime.setOnClickListener(this);

        AutoText_R.setOnItemClickListener(this);
        AutoText_F.setOnItemClickListener(this);
        AutoText_Amount.setOnItemClickListener(this);

        AutoList.setAdapter(mListAdapter);

        //AutoBt_Add.setClickable(false);

        Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int month = c.get(Calendar.MONTH)+1;
        String apm = "a.m.";
        if (hourOfDay>12){
            hourOfDay -=12;
            apm = "p.m.";
        } else if (hourOfDay==12){
            apm = "p.m.";
        }

        String strDate = String.format("%04d/%02d/%02d",c.get(Calendar.YEAR),month,c.get(Calendar.DAY_OF_MONTH));
        String strTime = String.format("%02d:%02d %s",hourOfDay,c.get(Calendar.MINUTE),apm);
        AutoDate.setText(strDate);
        AutoTime.setText(strTime);
        date = String.format("%04d-%02d-%02d"
                ,c.get(Calendar.YEAR)
                ,c.get(Calendar.MONTH)+1
                ,c.get(Calendar.DAY_OF_MONTH));
        time = String.format("%02d:%02d:00"
                ,c.get(Calendar.HOUR_OF_DAY)
                ,c.get(Calendar.MINUTE));


        fabMain.setVisibility(View.INVISIBLE);
        fabMain.setRotation(0);
        fabMain.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        fabMain.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fabMain.setVisibility(View.VISIBLE);
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

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        mToolbar.setOnTouchListener(this);
        AutoText_R.setOnTouchListener(this);
        AutoText_F.setOnTouchListener(this);
        AutoText_Amount.setOnTouchListener(this);
        AutoText_Size.setOnTouchListener(this);
        AutoText_Sugar.setOnTouchListener(this);
        AutoText_HotCold.setOnTouchListener(this);
        AutoText_selfCal.setOnTouchListener(this);
        AutoBt_Add.setOnTouchListener(this);
        AutoBt_Delete.setOnTouchListener(this);
        AutoDate.setOnTouchListener(this);
        AutoTime.setOnTouchListener(this);
        AutoList.setOnTouchListener(this);
        fabMain.setOnTouchListener(this);
        fabSend.setOnTouchListener(this);
        fabCancel.setOnTouchListener(this);
        RL_Send.setOnTouchListener(this);
        RL_Cancel.setOnTouchListener(this);
        AutoLL_Size.setOnTouchListener(this);
        AutoLL_Sugar.setOnTouchListener(this);
        AutoLL_HotCold.setOnTouchListener(this);
        AutoLL_selfCal.setOnTouchListener(this);
    }
    
    @SuppressLint("DefaultLocale")
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.AT_AT_R:
                Log.d("**Auto_AT_R**", "Hi Hi");
                AutoText_F.dismissDropDown();
                AutoText_Amount.dismissDropDown();
                //AutoText_R.setFocusable(true);
                //AutoText_R.showDropDown();
                if (!AutoText_R.isPopupShowing()){
                    AutoText_R.showDropDown();
                    AutoText_R.setDropDownHeight(WRAP_CONTENT);
                }

                break;

            case R.id.AT_AT_F:
                Log.d("**Auto_AT_F**", "Hi Hi");
                AutoText_R.dismissDropDown();
                AutoText_Amount.dismissDropDown();
                //AutoText_F.setFocusable(true);
                //AutoText_F.showDropDown();

                if (!AutoText_F.isPopupShowing()){
                    AutoText_F.showDropDown();
                    AutoText_F.setDropDownHeight(WRAP_CONTENT);
                }
                break;

            case R.id.AT_AT_Amount:
                Log.d("**Auto_AT_Amount**", "Hi Hi");
                AutoText_R.dismissDropDown();
                AutoText_F.dismissDropDown();
                //AutoText_F.setFocusable(true);
                //AutoText_F.showDropDown();
                if (!AutoText_Amount.isPopupShowing()){
                    AutoText_Amount.showDropDown();
                    AutoText_Amount.setDropDownHeight(WRAP_CONTENT);
                }
                break;

            case R.id.AutoBt_Add:
                String strR = AutoText_R.getText().toString();
                String strF = AutoText_F.getText().toString();
                String strAmount = AutoText_Amount.getText().toString();
                String tmpStr = "";
                String tmpAllCal = "";

                Map<String, String> item = new HashMap<>();

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Objects.equals(strR, "") && !Objects.equals(strR, "<無>")
                            && (!Objects.equals(strF, "") && !Objects.equals(strF, "<無>"))){
                        tmpStr += helper.FoodNameFindId(strR,strF);
                        if (tmpStr.length()>0){
                            double normalCal = Double.parseDouble(helper.FoodIdFindCal(tmpStr)[0]);
                            double amountCal = Double.parseDouble(strAmount);
                            double allCal = normalCal*amountCal;
                            tmpAllCal += String.valueOf(allCal);

                            item.put(ListAdapter.KEY_FI_Type,"Y");
                        } else {
                            tmpStr += AutoText_R.getText().toString();
                            tmpStr += "/";
                            tmpStr += AutoText_F.getText().toString();
                            double normalCal = Double.parseDouble(AutoText_selfCal.getText().toString());
                            double amountCal = Double.parseDouble(strAmount);
                            double allCal = normalCal*amountCal;
                            tmpAllCal += String.valueOf(allCal);

                            item.put(ListAdapter.KEY_FI_Type,"N");
                        }
                    }
                }*/
                if (!strR.equals("") && !strR.equals("<無>")
                        && (!strF.equals("") && !strF.equals("<無>"))){
                    tmpStr += helper.FoodNameFindId(strR,strF);
                    if (tmpStr.length()>0){
                        double normalCal = Double.parseDouble(helper.FoodIdFindCal(tmpStr)[0]);
                        double amountCal = Double.parseDouble(strAmount);
                        double allCal = normalCal*amountCal;
                        tmpAllCal += String.valueOf(allCal);

                        item.put(ListAdapter.KEY_FI_Type,"Y");
                    } else {
                        tmpStr += AutoText_R.getText().toString();
                        tmpStr += "/";
                        tmpStr += AutoText_F.getText().toString();
                        double normalCal = Double.parseDouble(AutoText_selfCal.getText().toString());
                        double amountCal = Double.parseDouble(strAmount);
                        double allCal = normalCal*amountCal;
                        tmpAllCal += String.valueOf(allCal);

                        item.put(ListAdapter.KEY_FI_Type,"N");
                    }
                }

                item.put(ListAdapter.KEY_FI_R,strR);
                item.put(ListAdapter.KEY_FI_F,strF);
                item.put(ListAdapter.KEY_FI_Amount,strAmount);
                item.put(ListAdapter.KEY_FI_Id,tmpStr);
                item.put(ListAdapter.KEY_FI_AllCal,tmpAllCal);

                mListAdapter.setItem(item);
                mListAdapter.notifyDataSetChanged();

                Log.d("**AutoBt_Add**", "Hi Hi");

                break;

            case R.id.AutoBt_Delete:
                Log.d("**AutoBt_Delete**", "Hi Hi");
                //AutoText_R.clearListSelection();
                //AutoText_F.clearListSelection();
                //AutoText_Amount.clearListSelection();
                //AutoText_R.clearComposingText();
                //AutoText_F.clearComposingText();
                //AutoText_Amount.clearComposingText();
                CountAutoFinish[0] = 0;
                AutoText_R.setText("",filterFlag);
                AutoText_F.setText("",filterFlag);
                AutoText_Amount.setText("",filterFlag);
                AutoText_R.clearListSelection();
                AutoText_F.clearListSelection();
                AutoText_Amount.clearListSelection();

                AutoText_R.setAdapter(adapterR);
                AutoText_F.setAdapter(adapterF);
                //AutoBt_Add.setClickable(false);
                AutoLL_selfCal.setVisibility(View.GONE);
                break;

            case R.id.AT_text_Date2:
                Log.d("**AT_text_Date2**", "Hi Hi");
                Calendar c1 = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strDate1 = String.format("%04d/%02d/%02d",year,monthOfYear+1,dayOfMonth);
                        date = String.format("%04d-%02d-%02d",year,monthOfYear+1,dayOfMonth);
                        AutoDate.setText(strDate1);
                    }
                }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;

            case R.id.AT_text_Time2:
                Log.d("**AT_text_Date2**", "Hi Hi");
                Calendar c2 = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = String.format("%02d:%02d:00",hourOfDay,minute);
                        String apm = "a.m.";
                        if (hourOfDay>12){
                            hourOfDay -=12;
                            apm = "p.m.";
                        }
                        String strTime2 = String.format("%02d:%02d %s",hourOfDay,minute,apm);
                        AutoTime.setText(strTime2);
                    }
                }, c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE),false);

                timePickerDialog.show();
                break;

            case R.id.Food_fab_Main:
                Log.d(TAG,"**Food_fab_Main**"+fabOpen);
                if (fabOpen) fabMainClose();
                else fabMainOpen();
                break;
            case R.id.Food_Send:
                Log.d(TAG,"**Food_fab_Send**"+fabOpen);
                //fabMainClose();
                int mListCount = mListAdapter.getCount();

                JSONObject tmpJO = new JSONObject();

                String[] tmpItem_Id = new String[mListCount];
                String[] tmpNote = new String[mListCount];
                String[] tmpTimes = new String[mListCount];
                String[] tmpValue = new String[mListCount];
                String[] tmpType = new String[mListCount];
                for (int ii=0;ii<mListCount;ii++){
                    Map<String,String> tmpMap = mListAdapter.getItemMap(ii);

                    tmpItem_Id[ii] = tmpMap.get(ListAdapter.KEY_FI_Id);
                    tmpNote[ii] = tmpMap.get(ListAdapter.KEY_FI_O);
                    tmpTimes[ii] = tmpMap.get(ListAdapter.KEY_FI_Amount);
                    //tmpValue[ii] = tmpMap.get(ListAdapter.KEY_FI_AllCal);
                    tmpValue[ii] = tmpMap.get(ListAdapter.KEY_FI_AllCal);
                    tmpType[ii] = tmpMap.get(ListAdapter.KEY_FI_Type);

                    ContentValues values = new ContentValues();
                    values.put(HiDBHelper.KEY_CalIn_Date, date);
                    values.put(HiDBHelper.KEY_CalIn_Time, time);
                    values.put(HiDBHelper.KEY_CalIn_Time48, new CT48(time).getCt48());
                    values.put(HiDBHelper.KEY_CalIn_FoodID, tmpMap.get(ListAdapter.KEY_FI_Id));
                    //values.put(HiDBHelper., tmpMap.get(ListAdapter.KEY_FI_O));
                    values.put(HiDBHelper.KEY_CalIn_Amount, tmpMap.get(ListAdapter.KEY_FI_Amount));
                    values.put(HiDBHelper.KEY_CalIn_oneCal, tmpMap.get(ListAdapter.KEY_FI_AllCal));
                    //values.put(HiDBHelper., tmpMap.get(ListAdapter.KEY_FI_Type));

                    Log.d(TAG,"**Food_fab_Send**"+values);
                    MainActivity2.getDataManager().helper.CalInInsert(values);
                }

                /*
                * String[] tmpItem_Id = new String[]{tmpStr};
                String[] tmpNote = new String[]{""};
                String[] tmpTimes = new String[]{""};
                String[] tmpValue = new String[]{"500"};
                String[] tmpType = new String[]{"Y"};*/

                try {
                    tmpJO.put("date",date);
                    tmpJO.put("time",time);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        tmpJO.put("item", new JSONArray(tmpItem_Id));
                        tmpJO.put("note", new JSONArray(tmpNote));
                        tmpJO.put("times",new JSONArray(tmpTimes));
                        tmpJO.put("value",new JSONArray(tmpValue));
                        tmpJO.put("type", new JSONArray(tmpType));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //MainActivity2.volleyMethod.vpostSend_FoodJson(new JSONObject[]{tmpJO});
                mVolleyManager.vpostSend_FoodJson(new JSONObject[]{tmpJO});
                fabMainClose();
                break;
            case R.id.Food_Cancel:
                Log.d(TAG,"**Food_fab_Cancel**"+fabOpen);
                //fabMainClose();
                returnMainActivity();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        
        if (parent.getAdapter()==AutoText_R.getAdapter()){
            Log.d("**AutoItemClick**", "**RRR**");
            String tmpF = AutoText_F.getText().toString();

            if (tmpF.equals("")){
                int ii = position;
                int tmpFirst = AutoRangeIndex_F[ii];
                int tmpEnd = AutoRangeIndex_F[ii+1];

                String[] tmpARItemT_F = {"<無>"};
                String[] tmpARItemId_F = {"None"};
                AutoText_F.setText("",filterFlag);

                if (tmpFirst != -1){
                    ii = position+2;
                    while (tmpEnd == -1){
                        tmpEnd = AutoRangeIndex_F[ii];
                        ii++;
                    }
                    int tmpRange = tmpEnd-tmpFirst;

                    if (tmpRange>0){
                        tmpARItemT_F = new String[tmpRange];
                        tmpARItemId_F = new String[tmpRange];

                        for (int jj=tmpFirst;jj<tmpEnd;jj++){
                            tmpARItemT_F[jj-tmpFirst] = AutoItemT_F[jj];
                            tmpARItemId_F[jj-tmpFirst] = AutoItemId_F[jj];
                        }
                    }
                }

                ArrayAdapter<String> tmpAdapterF = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        tmpARItemT_F);

                AutoText_F.setAdapter(tmpAdapterF);
                AutoText_F.showDropDown();

                CountAutoFinish[0] = 1;
                AutoText_R.clearFocus();
                //AutoText_F.setNextFocusDownId(AutoText_F.getId());
                AutoText_F.requestFocus();
                //AutoText_F.setCursorVisible(true);
            } else {
                String strR = AutoText_R.getText().toString();
                String strF = AutoText_F.getText().toString();
                String tmpStr = "";

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!Objects.equals(strR, "") && !Objects.equals(strR, "<無>")
                            && (!Objects.equals(strF, "") && !Objects.equals(strF, "<無>"))){
                        if (helper.FoodNameFindId(strR,strF)==""){
                            AutoText_F.setText("");

                            int ii = position;
                            int tmpFirst = AutoRangeIndex_F[ii];
                            int tmpEnd = AutoRangeIndex_F[ii+1];

                            String[] tmpARItemT_F = {"<無>"};
                            String[] tmpARItemId_F = {"None"};
                            AutoText_F.setText("",filterFlag);

                            if (tmpFirst != -1) {
                                ii = position + 2;
                                while (tmpEnd == -1) {
                                    tmpEnd = AutoRangeIndex_F[ii];
                                    ii++;
                                }
                                int tmpRange = tmpEnd - tmpFirst;

                                if (tmpRange > 0) {
                                    tmpARItemT_F = new String[tmpRange];
                                    tmpARItemId_F = new String[tmpRange];

                                    for (int jj = tmpFirst; jj < tmpEnd; jj++) {
                                        tmpARItemT_F[jj - tmpFirst] = AutoItemT_F[jj];
                                        tmpARItemId_F[jj - tmpFirst] = AutoItemId_F[jj];
                                    }
                                }
                            }

                            ArrayAdapter tmpAdapterF = new ArrayAdapter<>(this,
                                    android.R.layout.simple_dropdown_item_1line,
                                    tmpARItemT_F);

                            AutoText_F.setAdapter(tmpAdapterF);
                            AutoText_F.showDropDown();

                            CountAutoFinish[0] = 1;
                            AutoText_R.clearFocus();
                            //AutoText_F.setNextFocusDownId(AutoText_F.getId());
                            AutoText_F.requestFocus();
                            //AutoText_F.setCursorVisible(true);
                        }
                        CountAutoFinish[0] = 2;
                    }
                }*/

                if (!strR.equals("") && !strR.equals("<無>")
                        && (!strF.equals("") && !strF.equals("<無>"))){
                    if (helper.FoodNameFindId(strR,strF)==""){
                        AutoText_F.setText("");

                        int ii = position;
                        int tmpFirst = AutoRangeIndex_F[ii];
                        int tmpEnd = AutoRangeIndex_F[ii+1];

                        String[] tmpARItemT_F = {"<無>"};
                        String[] tmpARItemId_F = {"None"};
                        AutoText_F.setText("",filterFlag);

                        if (tmpFirst != -1) {
                            ii = position + 2;
                            while (tmpEnd == -1) {
                                tmpEnd = AutoRangeIndex_F[ii];
                                ii++;
                            }
                            int tmpRange = tmpEnd - tmpFirst;

                            if (tmpRange > 0) {
                                tmpARItemT_F = new String[tmpRange];
                                tmpARItemId_F = new String[tmpRange];

                                for (int jj = tmpFirst; jj < tmpEnd; jj++) {
                                    tmpARItemT_F[jj - tmpFirst] = AutoItemT_F[jj];
                                    tmpARItemId_F[jj - tmpFirst] = AutoItemId_F[jj];
                                }
                            }
                        }

                        ArrayAdapter tmpAdapterF = new ArrayAdapter<>(this,
                                android.R.layout.simple_dropdown_item_1line,
                                tmpARItemT_F);

                        AutoText_F.setAdapter(tmpAdapterF);
                        AutoText_F.showDropDown();

                        CountAutoFinish[0] = 1;
                        AutoText_R.clearFocus();
                        //AutoText_F.setNextFocusDownId(AutoText_F.getId());
                        AutoText_F.requestFocus();
                        //AutoText_F.setCursorVisible(true);
                    }
                    CountAutoFinish[0] = 2;
                }
                CountAutoFinish[0] = 2;
            }

        } else if (parent.getAdapter()==AutoText_F.getAdapter()){
            Log.d("**AutoItemClick**", "**FFF**");
            String tmpR = AutoText_R.getText().toString();
            String tmpF = AutoText_F.getText().toString();
            if (tmpR.equals("")){
                int tmpCount = 0;
                String tmpRId = "";
                for (int ii=0;ii<AutoItemT_F.length;ii++){
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Objects.equals(AutoItemT_F[ii], tmpF)){
                            tmpCount++;
                            tmpRId = tmpRId+"+"+AutoItemId_F[ii].substring(0,4);
                            Log.d("**AutoItemClick**",
                                    "**FFF**AutoItemId_F"+AutoItemId_F[ii]
                                            +"**"+AutoItemId_F[ii].substring(0,4));

                        }
                    }*/
                    if (AutoItemT_F[ii].equals(tmpF)){
                        tmpCount++;
                        tmpRId = tmpRId+"+"+AutoItemId_F[ii].substring(0,4);
                        Log.d("**AutoItemClick**",
                                "**FFF**AutoItemId_F"+AutoItemId_F[ii]
                                        +"**"+AutoItemId_F[ii].substring(0,4));

                    }
                }

                Log.d("**AutoItemClick**", "**FFF**tmpCount==="+tmpCount);

                String[] tmpARItemT_R;
                String[] tmpARItemId_R;
                if (tmpCount == 0){
                    tmpARItemT_R = new String[]{"<無>"};
                    tmpARItemId_R = new String[]{"None"};

                } else if (tmpCount>1){
                    String[] tmpARItemId_R2 = new String[tmpCount];
                    tmpARItemId_R2[0] = tmpRId.substring(1,5);
                    Log.d("**AutoItemClick**", "**FFF**tmpItemId==="+tmpRId.substring(1,5));
                    int tmpCountIndex = 1;
                    for (int ii=1;ii<tmpCount;ii++){
                        int kk = ii*5+1;

                        String tmpOneId = tmpRId.substring(kk,kk+4);
                        Log.d("**AutoItemClick**", "**FFF**tmpItemId==="+tmpOneId);
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (!Objects.equals(tmpARItemId_R2[tmpCountIndex], tmpOneId)){
                                tmpARItemId_R2[tmpCountIndex] = tmpOneId;
                                tmpCountIndex++;
                            }
                        }*/
                        if (!tmpARItemId_R2[tmpCountIndex].equals(tmpOneId)){
                            tmpARItemId_R2[tmpCountIndex] = tmpOneId;
                            tmpCountIndex++;
                        }
                    }
                    Log.d("**AutoItemClick**", "**FFF**tmpItemId2==="+ Arrays.toString(tmpARItemId_R2));

                    tmpARItemT_R = new String[tmpCountIndex+1];
                    tmpARItemId_R = new String[tmpCountIndex+1];
                    tmpARItemT_R[0] = "<無>";
                    tmpARItemId_R[0] = "None";

                    for (int ii=1;ii<tmpCountIndex+1;ii++){
                        for (int jj=0;jj<AutoItemId_R.length;jj++){
                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                if (Objects.equals(tmpARItemId_R2[ii-1], AutoItemId_R[jj])){
                                    tmpARItemT_R[ii] = AutoItemT_R[jj];
                                    tmpARItemId_R[ii] = AutoItemId_R[jj];
                                }
                            }*/
                            if (tmpARItemId_R2[ii-1].equals(AutoItemId_R[jj])){
                                tmpARItemT_R[ii] = AutoItemT_R[jj];
                                tmpARItemId_R[ii] = AutoItemId_R[jj];
                            }
                        }
                    }

                } else {
                    tmpARItemT_R = new String[tmpCount+1];
                    tmpARItemId_R = new String[tmpCount+1];
                    tmpARItemT_R[0] = "<無>";
                    tmpARItemId_R[0] = "None";
                    tmpARItemT_R[1] = "<無>";
                    tmpARItemId_R[1] = tmpRId.substring(1,5);
                    for (int jj=0;jj<AutoItemId_R.length;jj++){
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (Objects.equals(tmpARItemId_R[1], AutoItemId_R[jj])){
                                tmpARItemT_R[1] = AutoItemT_R[jj];
                            }
                        }*/
                        if (tmpARItemId_R[1].equals(AutoItemId_R[jj])){
                            tmpARItemT_R[1] = AutoItemT_R[jj];
                        }
                    }

                }
                Log.d("**AutoItemClick**", "**FFF**tmpItem_R**"+tmpARItemT_R.length);
                Log.d("**AutoItemClick**", "**FFF**tmpItem_R**"+ Arrays.toString(tmpARItemT_R));

                ArrayAdapter tmpAdapterR = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        tmpARItemT_R);

                assert AutoText_R!=null;
                AutoText_R.setAdapter(tmpAdapterR);
                AutoText_R.showDropDown();
                CountAutoFinish[0] = 1;
            } else {
                CountAutoFinish[0] = 2;
            }

        } else if (parent.getAdapter()==AutoText_Amount.getAdapter()){
            Log.d("**AutoItemClick**", "**AAA**");
        }

        if (CountAutoFinish[0]==2){
            Log.d("**AutoItemClick**", "**Finish**Finish**");
            //AutoText_Amount.setListSelection(0);
            //AutoText_Amount.setSelection(0);
            //AutoText_Amount.showDropDown();

            if (parent.getAdapter()!=AutoText_Amount.getAdapter()){
                AutoText_Amount.setText("1",filterFlag);
                AutoText_Amount.onFilterComplete(3);

                AutoText_R.clearFocus();
                AutoText_F.clearFocus();
                AutoText_Amount.clearFocus();
            }

            //AutoBt_Add.setNextFocusDownId(AutoBt_Add.getId());
            AutoBt_Add.setCursorVisible(true);
            AutoBt_Add.requestFocus();
            AutoBt_Add.setClickable(true);
            //AutoBt_Add.setCursorVisible(true);

            String strR = AutoText_R.getText().toString();
            String strF = AutoText_F.getText().toString();
            String tmpStr = "";

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!Objects.equals(strR, "") && !Objects.equals(strR, "<無>")
                        && (!Objects.equals(strF, "") && !Objects.equals(strF, "<無>"))){
                    tmpStr += helper.FoodNameFindId(strR,strF);
                    if (tmpStr.length()==0){
                        AutoLL_selfCal.setVisibility(View.VISIBLE);
                    }
                }
            }*/
            if (!strR.equals("") && !strR.equals("<無>")
                    && (!strF.equals("") && !strF.equals("<無>"))){
                tmpStr += helper.FoodNameFindId(strR,strF);
                if (tmpStr.length()==0){
                    AutoLL_selfCal.setVisibility(View.VISIBLE);
                }
            }

        } else  {
            //AutoBt_Add.setClickable(false);
            AutoLL_selfCal.setVisibility(View.GONE);
            //AutoText_Amount.setText("",filterFlag);
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String tmpStrR = AutoText_R.getText().toString();
        String tmpStrF = AutoText_F.getText().toString();
        String tmpStrId = "";

        if (tmpStrR.length()>0 && tmpStrF.length()>0){
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (!Objects.equals(tmpStrR, "") && !Objects.equals(tmpStrR, "<無>")
                        && (!Objects.equals(tmpStrF, "") && !Objects.equals(tmpStrF, "<無>"))){
                    tmpStrId += helper.FoodNameFindId(tmpStrR,tmpStrF);
                    if (tmpStrId.length()==0){
                        AutoLL_selfCal.setVisibility(View.VISIBLE);
                    }
                }
            }*/
            if (!tmpStrR.equals("") && !tmpStrR.equals("<無>")
                    && (!tmpStrF.equals("") && !tmpStrF.equals("<無>"))){
                tmpStrId += helper.FoodNameFindId(tmpStrR,tmpStrF);
                if (tmpStrId.length()==0){
                    AutoLL_selfCal.setVisibility(View.VISIBLE);
                }
            }
        } else AutoLL_selfCal.setVisibility(View.GONE);
        return false;
    }

    public void fabMainOpen() {
        if (!fabOpen) {
            fabOpen = true;

            //fabMain.setVisibility(View.VISIBLE);
            fabMain.setClickable(false);

            RL_Send.setVisibility(View.VISIBLE);
            RL_Cancel.setVisibility(View.VISIBLE);
            fabMain.setClickable(false);
            fabSend.setClickable(false);
            fabCancel.setClickable(false);

            fabMain.animate().rotationBy(180f)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                                /*MainActivity.fabMain.setClickable(false);
                                fab_Height.setClickable(false);
                                fab_Weight.setClickable(false);*/
                        }

                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabMain.setImageResource(R.drawable.ic_fab_cancel_bk);

                            fabMain.animate().rotationBy(180f)
                                    .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            fabMain.setClickable(true);
                                            fabSend.setClickable(true);
                                            fabCancel.setClickable(true);
                                            fabMain.setRotation(0);
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

            double tmpWidth = fabMain.getWidth()*1.15;
            openMoveRL(setFabUse, RL_Cancel,tmpWidth*1);
            openMoveRL(setFabUse, RL_Send,tmpWidth*2);

            mCoverView.setVisibility(View.VISIBLE);

            mCoverView.animate().scaleX(100F)
                    .scaleY(100F)
                    .alpha(1F)
                    .setDuration(100)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mCoverView.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mCoverView.setClickable(true);
                        }
                    });


        }
    }

    public void fabMainClose() {
        if (fabOpen) {
            fabOpen = false;

            long tmpTime2;
            tmpTime2 = 280;
            //MainActivity.fabMain.setClickable(false);
            fabSend.setClickable(false);
            fabCancel.setClickable(false);

            fabMain.animate().rotationBy(180f)
                    .setInterpolator(new AccelerateInterpolator()).setDuration(210)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            /*MainActivity.fabMain.setClickable(false);
                            fab_Height.setClickable(false);
                            fab_Weight.setClickable(false);*/
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fabMain.setImageResource(R.drawable.ic_fab_paint_bk);
                            fabMain.animate().rotationBy(180f)
                                    .setInterpolator(new OvershootInterpolator()).setDuration(210)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            fabMain.setClickable(true);
                                            fabMain.setRotation(0);
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

            closeRL(setFabUse, RL_Cancel, tmpTime2);
            closeRL(setFabUse, RL_Send, tmpTime2);

            mCoverView.animate().scaleX(0F)
                    .scaleY(0F)
                    .alpha(0F)
                    .setDuration(100)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mCoverView.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mCoverView.setVisibility(View.GONE);
                        }
                    });

        }
    }

    @Override
    public void openMoveRL(boolean setFabUse, RelativeLayout relativeLayout, double moveY) {
        superAction.openMoveRL(setFabUse,relativeLayout,moveY);
    }

    @Override
    public void closeRL(boolean setFabUse, RelativeLayout relativeLayout, long Time) {
        superAction.closeRL(setFabUse,relativeLayout,Time);
    }
}
