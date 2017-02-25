package com.asaewing.healthimprover.fl;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asaewing.healthimprover.MainActivity;
import com.asaewing.healthimprover.Others.CT48;
import com.asaewing.healthimprover.Others.DividerItemDecoration;
import com.asaewing.healthimprover.Others.HiDBHelper;
import com.asaewing.healthimprover.Others.RecordItem;
import com.asaewing.healthimprover.Others.Record_RV_Adapter;
import com.asaewing.healthimprover.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asaewing on 2017/1/23.
 */

public class fl_Record extends RootFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRV_Adapter;
    private RecyclerView.LayoutManager mRV_LayoutManager;

    public fl_Record() {
        // Required empty public constructor
    }

    public static fl_Record newInstance() {
        fl_Record fragment = new fl_Record();

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
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.fl_record, container, false);

        //Spinner
        final String[] recordChoice = {"飲食", "體重", "身高", "運動", "目標"};

        Spinner spinner = (Spinner)rootView.findViewById(R.id.record_spinner);

        ArrayAdapter<String> lunchList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                recordChoice);
        spinner.setAdapter(lunchList);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "你選的是" + recordChoice[position], Toast.LENGTH_SHORT).show();
                replaceRecordList(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initRecordList(0);

        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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

    private void initRecordList(int position){

        String[] recordChoice = {"飲食", "體重", "身高", "運動", "目標"};
        ArrayList<RecordItem> mRecord = new ArrayList<>();

        int countList = 0;

        switch (position){
            case 0:
                Cursor cursorCI = MainActivity.helper.CalInSelect();
                int countCI = cursorCI.getCount();
                countList = countCI;

                for (int ii=0;ii<countCI;ii++){
                    cursorCI.moveToPosition(ii);

                    String time = cursorCI.getString(cursorCI.getColumnIndex(HiDBHelper.KEY_CalIn_Date));
                    time += " ";
                    time += cursorCI.getString(cursorCI.getColumnIndex(HiDBHelper.KEY_CalIn_Time));

                    String text = cursorCI.getString(cursorCI.getColumnIndex(HiDBHelper.KEY_CalIn_oneCal));
                    text += "\n";
                    text += cursorCI.getString(cursorCI.getColumnIndex(HiDBHelper.KEY_CalIn_Fname));

                    boolean tmpCloud = false;
                    if ((countCI-ii)%2==1){
                        tmpCloud = true;
                    }

                    mRecord.add(0,new RecordItem(String.valueOf(countCI-ii),time,text,tmpCloud));
                }

                cursorCI.close();
                break;

            case 1:
                Cursor cursorW = MainActivity.helper.WeightSelect();
                int countW = cursorW.getCount();
                countList = countW;

                /*String[] stringListW = new String[]{"無任何體重資料"};

                if (countW>0){
                    stringListW = new String[16];
                    stringListW[0] = "共有"+countW+"筆體重資料，最近15筆：";
                    for (){
                        cursorW.moveToPosition(ii);
                        String time = cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Time));
                        CT48 ct48 = new CT48(time);

                        String tmpStr= "Time:"+cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Date)).substring(5)+
                                " "+time.substring(0,5)+
                                "\n   "+cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Weight))
                                +" kg";
                        stringListW[cursorW.getCount()-ii] = tmpStr;

                    }
                }*/


                for (int ii=0;ii<countW;ii++){
                    cursorW.moveToPosition(ii);

                    String time = cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Date));
                    time += " ";
                    time += cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Time));

                    String text = cursorW.getString(cursorW.getColumnIndex(HiDBHelper.KEY_Weight_Weight));

                    boolean tmpCloud = false;
                    if ((countW-ii)%2==1){
                        tmpCloud = true;
                    }

                    mRecord.add(0,new RecordItem(String.valueOf(countW-ii),time,text,tmpCloud));
                }

                cursorW.close();
                break;

            case 2:
                Cursor cursorH = MainActivity.helper.HeightSelect();
                int countH = cursorH.getCount();
                countList = countH;

                for (int ii=0;ii<countH;ii++){
                    cursorH.moveToPosition(ii);

                    String time = cursorH.getString(cursorH.getColumnIndex(HiDBHelper.KEY_Height_Date));
                    time += " ";
                    time += cursorH.getString(cursorH.getColumnIndex(HiDBHelper.KEY_Height_Time));

                    String text = cursorH.getString(cursorH.getColumnIndex(HiDBHelper.KEY_Height_Height));

                    boolean tmpCloud = false;
                    if ((countH-ii)%2==1){
                        tmpCloud = true;
                    }

                    mRecord.add(0,new RecordItem(String.valueOf(countH-ii),time,text,tmpCloud));
                }

                cursorH.close();
                break;

            case 3:
                countList = 0;
                break;

            case 4:
                countList = 0;
                break;
        }

        //RecycleView
        //int recordCount = 50;

        String recordListTitle = "無任何"+recordChoice[position]+"資料";
        if (countList>0){
            recordListTitle = "共有"+String.valueOf(countList)+"筆"+recordChoice[position]+"資料：";
        }

        TextView recordListTitle_Text = (TextView)rootView.findViewById(R.id.record_text);
        recordListTitle_Text.setText(recordListTitle);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.record_list);

        mRecyclerView.setHasFixedSize(true);

        mRV_LayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mRV_LayoutManager);

        mRV_Adapter = new Record_RV_Adapter(mRecord);
        mRecyclerView.setAdapter(mRV_Adapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void replaceRecordList(int position){

        initRecordList(position);

    }

}