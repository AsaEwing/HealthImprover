package com.asaewing.healthimprover.app2.fl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.asaewing.healthimprover.app2.Adapter.MsgAdapter;
import com.asaewing.healthimprover.app2.MainActivity2;
import com.asaewing.healthimprover.app2.Others.HiMsgItem;
import com.asaewing.healthimprover.app2.Others.InfoMap;
import com.asaewing.healthimprover.app2.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class fl_Chat extends RootFragment {

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter ;
    private List<HiMsgItem> msgList = new ArrayList<>();
    private double []numTmp = {0,0,0,0,0,0,0,0,0,0};

    public fl_Chat() {
        // Required empty public constructor
    }

    public static fl_Chat newInstance() {
        fl_Chat fragment = new fl_Chat();

        return fragment;
    }

    //TODO----Data----

    private void initMsgs() {
        HiMsgItem hiMsgItem = new HiMsgItem("", HiMsgItem.TYPE_MID_DAY,new Date());
        msgList.add(hiMsgItem);
        HiMsgItem hiMsgItemSay = new HiMsgItem("Hello! 我是HiBot！", HiMsgItem.TYPE_RECEIVED,new Date());
        msgList.add(hiMsgItemSay);
    }

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
                             final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initMsgs();

        rootView = inflater.inflate(R.layout.fl_chat, container, false);

        adapter = new MsgAdapter(getActivity(),R.layout.msg_item_layout,msgList);
        inputText = (EditText)rootView.findViewById(R.id.input_text);
        send = (Button) rootView.findViewById(R.id.send);
        msgListView = (ListView)rootView.findViewById(R.id.msg_list_view);
        msgListView.setClickable(false);

        try {
            msgListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();

            Log.d(TAG,"**"+TAG+"**Chat**Failed**adapter");
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                HiMsgItem msg;
                if (!content.equals("")) {
                    msg = new HiMsgItem(content, HiMsgItem.TYPE_SENT, new Date());
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                }

                String [][]keyWord = new String[][]
                        {       {"高","矮","胖","瘦"},
                                {"+","-","*","/","^","×","÷"},
                                {"記事","記一下"},
                                {"定時","設定鬧鐘","設鬧鐘"},
                                {"開啟相機"}
                        };

                int keyWord_space = -1;

                for (int i=0;i<5;i++) {
                    int k = 0;

                    switch (i) {
                        case 0:
                            k = 4;
                            break;
                        case 1:
                            k = 7;
                            break;
                        case 2 :
                            k = 2;
                            break;
                        case 3 :
                            k = 3;
                            break;
                        case 4 :
                            k = 1;
                            break;
                    }

                    String msgString = "";

                    for (int j=0;j<k;j++){
                        if(content.contains(keyWord[i][j])){
                            keyWord_space = content.indexOf(keyWord[i][j]);

                            if (i==0) {
                                int keyNumTotal = getNumber(content);

                                switch (keyWord[0][j]) {
                                    case "高":
                                        if (keyNumTotal == -10) {

                                            msgString = getAsk(content,keyWord[0][j]);
                                        } else if (keyNumTotal < 2 ) {
                                            msgString = getUnit(content,keyWord[0][j],keyNumTotal);
                                        }
                                        break;

                                    case "矮":
                                        if(keyNumTotal == -10){

                                            msgString = getAsk(content,keyWord[0][j]);
                                        }else if(keyNumTotal < 2){
                                            msgString = getUnit(content,keyWord[0][j],keyNumTotal);
                                        }
                                        break;

                                    case "胖":
                                        if(keyNumTotal == -10){

                                            msgString = getAsk(content,keyWord[0][j]);
                                        }else if(keyNumTotal < 2){
                                            msgString = getUnit(content,keyWord[0][j],keyNumTotal);
                                        }
                                        break;

                                    case "瘦":
                                        if(keyNumTotal == -10){

                                            msgString = getAsk(content,keyWord[0][j]);
                                        }else if(keyNumTotal < 2){
                                            msgString = getUnit(content,keyWord[0][j],keyNumTotal);
                                        }
                                        break;
                                }
                            } else if (i==1) {

                                double ans=0;                       //答案
                                double num1 = 0,num2 = 1;           //數字1 數字2

                                try {
                                    num1 = Double.parseDouble(content.substring(0, keyWord_space).trim());     //取得數字1
                                    num2 = Double.parseDouble(content.substring((keyWord_space + 1)).trim());     //取得數字2
                                } catch (Exception e) {
                                    keyWord_space = -1;
                                    break;
                                }

                                switch (keyWord[1][j]) {
                                    case "+":
                                        ans = num1 + num2;
                                        break;
                                    case "-":
                                        ans = num1 - num2;
                                        break;
                                    case "*":
                                        ans = num1 * num2;
                                        break;
                                    case "/":
                                        ans = num1 / num2;
                                        break;
                                    case "×":
                                        ans = num1 * num2;
                                        break;
                                    case "÷":
                                        ans = num1 / num2;
                                        break;
                                    case "^":
                                        ans = Math.pow(num1, num2);       //ans=num1^num2
                                        break;
                                }
                                msgString = String.valueOf(ans);
                            }
                            else if (i == 2){
                                switch (keyWord[2][j]){
                                    case "記事" :
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fl_c_MainFragment,fl_Note.newInstance(),"fl_Note").commit();
                                        break;
                                    case "記一下" :
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fl_c_MainFragment,fl_Note.newInstance(),"fl_Note").commit();
                                        break;
                                }
                            }
                            else if (i == 3){
                                switch (keyWord[3][j]){
                                    case "定時":
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fl_c_MainFragment,fl_Notification.newInstance(),"fl_Notification").commit();
                                        break;
                                    case "設定鬧鐘":
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fl_c_MainFragment,fl_Notification.newInstance(),"fl_Notification").commit();
                                        break;
                                    case "設鬧鐘":
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fl_c_MainFragment,fl_Notification.newInstance(),"fl_Notification").commit();
                                        break;
                                }
                            }
                            else if (i == 4){
                                switch (keyWord[4][j]){
                                    case "開啟相機":
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.fl_c_MainFragment,fl_Camera.newInstance(),"fl_Camera").commit();
                                        break;
                                }
                            }

                            msg = new HiMsgItem(msgString, HiMsgItem.TYPE_RECEIVED, new Date());
                            msgList.add(msg);
                        }
                    }
                }



                if (keyWord_space == -1) {
                    msg = new HiMsgItem("對不起，我還不了解這句子的意思！", HiMsgItem.TYPE_RECEIVED, new Date());
                    msgList.add(msg);
                }
            }
        });

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


    public String getUnit(String content,String keyword,int numTotal) {
        InfoMap mInfoMap = getMainActivity().getDataManager().mInfoMap;

        String[] keyUnit = new String[]{""};
        int keyWordUnit_space = -1;

        if (keyword == "高" || keyword == "矮") {
            keyUnit = new String[]{"公尺","m","公分","cm"};
        } else if (keyword == "胖" || keyword == "瘦") {
            keyUnit = new String[]{"公克","g","公斤","kg"};
        }

        String msgString = "" ;
        for (int i = 0; i < 4; i++) {
            if (content.indexOf(keyUnit[i]) != -1) {
                keyWordUnit_space = content.indexOf(keyUnit[i]);
                if (keyUnit[i].equals("公克") || keyUnit[i].equals("g")) {
                    double weightBefore =
                            mInfoMap.IMgetInt("BI_Weight_H")
                                    + mInfoMap.IMgetInt("BI_Weight_L") * 0.01;
                    double gNum = numTmp[0]/1000;
                    double gNum1 = numTmp[1]/1000;
                    double gDelta = gNum - weightBefore;
                    if(keyword == "胖"){
                        if(numTotal == 1){
                            if(gNum > gNum1){
                                double gSub = gNum -gNum1 ;
                                msgString = "哦~ 不!!您比以前胖了"+ gSub +"公斤了";
                            }else if(gNum < gNum1){
                                double gSub = gNum1 -gNum ;
                                msgString = "哦~ 不!!您比以前胖了"+ gSub +"公斤了";
                            }
                        }else if(gNum > 200 || gNum < 0 ){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(gNum < 20){
                            msgString ="這真是個糟糕的消息，您比之前胖了"+ gNum +"公斤了" +
                                    "，最近吃比較好哦";
                        }else if(gNum >=20 && gNum <=200 ){
                            if (gDelta > 0){
                                msgString = "您現在的體重為"+ gNum +"比以前胖了"+ gDelta +"公斤"
                                        +"，該出去動一動了!!";
                            }else if (gDelta < 0) {
                                msgString = "您現在的體重為" + gNum + "比以前瘦了" + Math.abs(gDelta)
                                        + "公斤，繼續保持哦!!加油!!";
                            }
                        }
                    }
                    else if(keyword == "瘦"){
                        if(numTotal == 1 ){
                            if(gNum > gNum1){
                                double gSub = gNum -gNum1 ;
                                msgString = "恭喜您!!您比以前瘦了"+ gSub +"公斤了，繼續保持哦!!";
                            }else if(gNum < gNum1){
                                double gSub = gNum1 -gNum ;
                                msgString = "恭喜您!!您比以前瘦了"+ gSub +"公斤了，繼續保持哦!!";
                            }
                        }else if(gNum > 200 || gNum < 0 ){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(gNum < 20){
                            msgString ="太棒了!!您比之前瘦了"+ gNum +"公斤了" +
                                    "，要繼續保持哦!!";
                        }else if(gNum >=20 && gNum <=200 ){
                            if (gDelta > 0){
                                msgString = "您現在的體重為"+ gNum +"比以前胖了"+ gDelta +"公斤"
                                        +"，該出去動一動了!!";
                            }else if (gDelta < 0){
                                msgString = "您現在的體重為"+ gNum +"比以前瘦了"+ Math.abs(gDelta)
                                        +"公斤，繼續保持哦!!加油!!";
                            }
                        }

                    }

                } else if (keyUnit[i].equals("公斤") || keyUnit[i].equals("kg")) {
                    double weightBefore =
                            mInfoMap.IMgetInt("BI_Weight_H")
                                    + mInfoMap.IMgetInt("BI_Weight_L") * 0.01;
                    double KgNum = numTmp[0];
                    double KgNum1 = numTmp[1];
                    double KgDelta = KgNum - weightBefore;
                    if(keyword == "胖"){
                        if(numTotal == 1){
                            if(KgNum > KgNum1){
                                double KgSub = KgNum -KgNum1 ;
                                msgString = "哦~ 不!!您比以前胖了"+ KgSub +"公斤了";
                            }else if(KgNum < KgNum1){
                                double KgSub = KgNum1 -KgNum ;
                                msgString = "哦~ 不!!您比以前胖了"+ KgSub +"公斤了";
                            }
                        }else if(KgNum > 200 || KgNum < 0 ){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(KgNum < 20){
                            msgString ="這真是個糟糕的消息，您比之前胖了"+ KgNum +"公斤了" +
                                    "，最近吃比較好哦";
                        }else if(KgNum >=20 && KgNum <=200 ){
                            if (KgDelta > 0){
                                msgString = "您現在的體重為"+ KgNum +"比以前胖了"+ KgDelta +"公斤"
                                        +"，該出去動一動了!!";
                            }else if (KgDelta < 0) {
                                msgString = "您現在的體重為" + KgNum + "比以前瘦了" + Math.abs(KgDelta)
                                        + "公斤，繼續保持哦!!加油!!";
                            }
                        }
                    }else if(keyword == "瘦"){
                        if(numTotal == 1 ){
                            if(KgNum > KgNum1){
                                double KgSub = KgNum -KgNum1 ;
                                msgString = "恭喜您!!您比以前瘦了"+ KgSub +"公斤了，繼續保持哦!!";
                            }else if(KgNum < KgNum1){
                                double KgSub = KgNum1 -KgNum ;
                                msgString = "恭喜您!!您比以前瘦了"+ KgSub +"公斤了，繼續保持哦!!";
                            }
                        }else if(KgNum > 200 || KgNum < 0 ){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(KgNum < 20){
                            msgString ="太棒了!!你比之前瘦了"+ KgNum +"公斤了" +
                                    "，要繼續保持哦!!";
                        }else if(KgNum >=20 && KgNum <=200 ){
                            if (KgDelta > 0){
                                msgString = "您現在的體重為"+ KgNum +"比以前胖了"+ KgDelta +"公斤"
                                        +"，該出去動一動了!!";
                            }else if (KgDelta < 0){
                                msgString = "您現在的體重為"+ KgNum +"比以前瘦了"+ Math.abs(KgDelta)
                                        +"公斤，繼續保持哦!!加油!!";
                            }
                        }
                    }

                }else if( keyUnit[i].equals("公分") || keyUnit[i].equals("cm")) {
                    double heightBefore =
                            mInfoMap.IMgetInt("BI_Height_H")
                                    + mInfoMap.IMgetInt("BI_Height_L") * 0.01;
                    double CmNum = numTmp[0];
                    double CmNum1 = numTmp[1];
                    double CmDelta = CmNum - heightBefore;
                    if(keyword == "高"){
                        if(numTotal == 1){
                            if(CmNum > CmNum1){
                                double CmSub = CmNum -CmNum1;
                                msgString = "恭喜您!!您比以前高了"+ CmSub +
                                        "公分，繼續保持哦!!";
                            }else if(CmNum < CmNum1){
                                double CmSub = CmNum1 - CmNum ;
                                msgString = "恭喜您!!您比以前高了"+ CmSub +
                                        "公分，繼續保持哦!!";
                            }
                        }else if(CmNum > 300 || CmNum < 0){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(CmNum < 20){
                            msgString = "恭喜您比以前高了"+ CmNum + "公分了!!";
                        }else if(CmNum >=20 && CmNum <= 300){
                            if(CmDelta > 0){
                                msgString = "您現在的身高為"+ CmNum +"公分哦!!比以前高了"+
                                        CmDelta +"公分，繼續加油哦!!";
                            }else if(CmDelta < 0){
                                msgString = "您現在的身高為"+ CmNum +"公分哦!!比以前矮了"+
                                        CmDelta +"公分，是不是有錯呢? 在量一次吧!!";
                            }
                        }
                    }else if(keyword == "矮"){
                        if(numTotal == 1){
                            if(CmNum > CmNum1){
                                double CmSub = CmNum -CmNum1;
                                msgString = "呃..是不是哪裡錯了!!您比以前矮了"+ CmSub +
                                        "公分";
                            }else if(CmNum < CmNum1){
                                double CmSub = CmNum1 - CmNum ;
                                msgString = "呃..是不是哪裡錯了!!您比以前矮了"+ CmSub +
                                        "公分";
                            }
                        }else if(CmNum > 300 || CmNum < 0){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(CmNum < 20){
                            msgString = "真糟糕，您比以前矮了"+ CmNum + "公分，是不是哪裡出了錯";
                        }else if(CmNum >=20 && CmNum <= 300){
                            if(CmDelta> 0){
                                msgString = "您現在的身高為"+ CmNum +"公分哦!!比以前高了"+
                                        CmDelta +"公分，繼續加油哦!!";
                            }else if(CmDelta < 0){
                                msgString = "您現在的身高為"+ CmNum +"公分哦!!比以前矮了"+
                                        CmDelta +"公分，是不是有錯呢? 在量一次吧!!";
                            }
                        }
                    }

                } else if (keyUnit[i].equals("公尺") || keyUnit[i].equals("m")) {
                    double heightBefore =
                            mInfoMap.IMgetInt("BI_Height_H")
                                    + mInfoMap.IMgetInt("BI_Height_L") * 0.01;
                    double mNum = numTmp[0] * 100;
                    double mNum1 = numTmp[1] * 100;
                    double mDelta = mNum - heightBefore;
                    if(keyword == "高"){
                        if(numTotal == 1){
                            if(mNum > mNum1){
                                double mSub = mNum -mNum1;
                                msgString = "恭喜您!!您比以前高了"+ mSub +
                                        "公分，繼續保持哦!!";
                            }else if(mNum < mNum1){
                                double mSub = mNum1 - mNum ;
                                msgString = "恭喜您!!您比以前高了"+ mSub +
                                        "公分，繼續保持哦!!";
                            }
                        }else if(mNum > 300 || mNum < 0){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(mNum < 20){
                            msgString = "恭喜您比以前高了"+ mNum + "公分了!!";
                        }else if(mNum >=20 && mNum <= 300){
                            if(mDelta > 0){
                                msgString = "您現在的身高為"+ mNum +"公分哦!!比以前高了"+
                                        mDelta +"公分，繼續加油哦!!";
                            }else if(mDelta < 0){
                                msgString = "您現在的身高為"+ mNum +"公分哦!!比以前矮了"+
                                        mDelta +"公分，是不是有錯呢? 在量一次吧!!";
                            }
                        }
                    }else if(keyword == "矮"){
                        if(numTotal == 1){
                            if(mNum > mNum1){
                                double CmSub = mNum -mNum1;
                                msgString = "呃..是不是哪裡錯了!!您比以前矮了"+ CmSub +
                                        "公分";
                            }else if(mNum < mNum1){
                                double mSub = mNum1 - mNum ;
                                msgString = "呃..是不是哪裡錯了!!您比以前矮了"+ mSub +
                                        "公分";
                            }
                        }else if(mNum > 300 || mNum < 0){
                            msgString = "是不是打錯了呢 ? 再試一次吧 !!";
                        }else if(mNum < 20){
                            msgString = "真糟糕，您比以前矮了"+ mNum + "公分，是不是哪裡出了錯";
                        }else if(mNum >=20 && mNum <= 300){
                            if(mDelta > 0){
                                msgString = "您現在的身高為"+ mNum +"公分哦!!比以前高了"+
                                        mDelta +"公分，繼續加油哦!!";
                            }else if(mDelta < 0){
                                msgString = "您現在的身高為"+ mNum +"公分哦!!比以前矮了"+
                                        mDelta +"公分，是不是有錯呢? 在量一次吧!!";
                            }
                        }
                    }
                }
            }else if (keyWordUnit_space == -1){
                msgString = "記得輸入單位哦";
            }
            Log.d(TAG, "**" + TAG + "**Chat**getUnit**" + keyword + "**"+ Arrays.toString(keyUnit) );
        }
        return msgString ;
    }

    public String getAsk(String content,String keyword) {
        InfoMap mInfoMap = getMainActivity().getDataManager().mInfoMap;

        String[] keyAsk = new String[]{"多少", "多重", "多高", "多胖"};
        int keyAsk_space = -1;
        String msgString = "";
        for (int i = 0; i < 4; i++) {
            if (content.indexOf(keyAsk[i]) != -1) {
                keyAsk_space = content.indexOf(keyAsk[i]);
                if (keyAsk[i].equals("多少")) {
                    double weightBefore =
                            mInfoMap.IMgetInt("BI_Weight_H")
                                    + mInfoMap.IMgetInt("BI_Weight_L") * 0.01;
                    double heightBefore =
                            mInfoMap.IMgetInt("BI_Height_H")
                                    + mInfoMap.IMgetInt("BI_Height_L") * 0.01;
                    if (keyword == "高" || keyword == "矮") {
                        msgString = "您目前的身高為" + heightBefore + "公分";
                    } else if (keyword == "胖" || keyword == "瘦") {
                        msgString = "您目前的體重為" + weightBefore + "公斤";
                    }
                } else if (keyAsk[i].equals("多重")) {
                    double weightBefore =
                            mInfoMap.IMgetInt("BI_Weight_H")
                                    + mInfoMap.IMgetInt("BI_Weight_L") * 0.01;
                    msgString = "您目前的體重為" + weightBefore + "公斤";
                } else if (keyAsk[i].equals("多高")) {
                    double heightBefore =
                            mInfoMap.IMgetInt("BI_Height_H")
                                    + mInfoMap.IMgetInt("BI_Height_L") * 0.01;
                    msgString = "您目前的身高為" + heightBefore + "公分";

                } else if (keyAsk[i].equals("多胖")) {
                    double weightBefore =
                            mInfoMap.IMgetInt("BI_Weight_H")
                                    + mInfoMap.IMgetInt("BI_Weight_L") * 0.01;
                    msgString = "您目前的體重為" + weightBefore + "公斤";
                }
            }else if (keyAsk_space == -1){
                msgString = "那是"+ keyword +"多少呢?";
            }
        }
        return msgString;
    }

    public int getNumber(String content) {
        char[] keyNum = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
        int numFlag = 0;
        int numTotal = -10;
        int keyword_space = -1;
        int k;


        for (int i = content.length() - 1; i >= 0; i--) {
            k = numFlag == 0 ? 10 : 11;

            for (int j=0;j<k+1;j++) {
                if (j == k) {
                    if (keyword_space != -1) {
                        try {
                            numTotal++;

                            if (keyword_space + numFlag >= content.length()) {
                                numTmp[numTotal] = Double.parseDouble(
                                        content.substring(keyword_space).trim());
                            } else {
                                numTmp[numTotal] = Double.parseDouble(
                                        content.substring(keyword_space, keyword_space + numFlag).trim());
                            }

                        } catch (Exception e) {
                            numTotal = -11;

                            break;
                        }
                    }

                    keyword_space=-1;
                    numFlag=0;

                } else if (content.charAt(i) == keyNum[j]) {
                    keyword_space = i;
                    numFlag++;

                    if (numTotal == -10) numTotal=-1;

                    if (i == 0) {
                        try {
                            numTotal++;

                            if (keyword_space + numFlag >= content.length()) {
                                numTmp[numTotal] = Double.parseDouble(
                                        content.substring(keyword_space).trim());
                            } else {
                                numTmp[numTotal] = Double.parseDouble(
                                        content.substring(keyword_space, keyword_space + numFlag).trim());
                            }

                        } catch (Exception e) {
                            numTotal = -11;

                            break;
                        }
                    }

                    break;
                }
            }

        }


        return numTotal;
    }

}
