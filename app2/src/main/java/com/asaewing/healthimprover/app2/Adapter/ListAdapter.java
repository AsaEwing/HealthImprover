package com.asaewing.healthimprover.app2.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asaewing.healthimprover.app2.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by asa on 2016/11/16.
 */

public class ListAdapter extends BaseAdapter {

    private LayoutInflater mLayInf;
    private List<Map<String,String>> mItemList = new ArrayList<>();

    public static String KEY_FI_Id = "KEY_FI_Id";
    public static String KEY_FI_R = "KEY_FI_R";
    public static String KEY_FI_F = "KEY_FI_F";
    public static String KEY_FI_AllCal = "KEY_FI_AllCal";
    public static String KEY_FI_Amount = "KEY_FI_Amount";

    public static String KEY_FI_Type = "KEY_FI_Type";

    public static String KEY_FI_O = "KEY_FI_O";
    public static String KEY_FI_Size = "KEY_FI_Size";
    public static String KEY_FI_Sugar = "KEY_FI_Sugar";
    public static String KEY_FI_HotCold = "KEY_FI_HotCold";


    public ListAdapter(Context context) {
        mLayInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItem(Map<String,String> item){
        mItemList.add(item);
    }

    public List getAllList(){
        return mItemList;
    }

    public Map<String, String> getItemMap(int position) {
        return mItemList.get(position);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //設定與回傳 convertView 作為顯示在這個 position 位置的 Item 的 View。
        View v = mLayInf.inflate(R.layout.food_list_item, parent, false);

        TextView textR = (TextView)v.findViewById(R.id.FI_text_R);
        TextView textF = (TextView)v.findViewById(R.id.FI_text_F);
        TextView textAmount = (TextView)v.findViewById(R.id.FI_text_Amount);
        TextView textO = (TextView)v.findViewById(R.id.FI_text_O);

        String tmpO = "";
        String tmpO2 = mItemList.get(position).get(KEY_FI_Size);
        if (tmpO2!=null) tmpO += (tmpO2+"，");
        tmpO2 = mItemList.get(position).get(KEY_FI_Sugar);
        if (tmpO2!=null) tmpO += (tmpO2+"，");
        tmpO2 = mItemList.get(position).get(KEY_FI_HotCold);
        if (tmpO2!=null) tmpO += tmpO2;

        Log.d("List","**List**"+tmpO.length()+"**"+tmpO);

        if (tmpO.length()>0){
            if(Objects.equals(tmpO.substring(tmpO.length() - 1), "，")){
                tmpO = tmpO.substring(0,tmpO.length() - 1);
                tmpO = "備註："+tmpO;
                textO.setText(tmpO);
            }
        } else {
            textO.setVisibility(View.GONE);
        }

        textR.setText(mItemList.get(position).get(KEY_FI_R));
        textF.setText(mItemList.get(position).get(KEY_FI_F));
        textAmount.setText("共"+mItemList.get(position).get(KEY_FI_Amount)+"份");


        return v;
    }
}
