package com.asaewing.healthimprover.app2.Others;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asaewing.healthimprover.app2.R;

/**
 * Created by asaewing on 2017/1/28.
 */

public class Record_ViewHolder
        extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    String TAG = "Record_ViewHolder";

    // each data item is just a string in this case
    TextView textView_number,textView_time,textView_text;
    ImageView imageView_cloud;

    public Record_ViewHolder(View rootView) {
        super(rootView);
        rootView.setOnClickListener(this);
        textView_number = (TextView)rootView.findViewById(R.id.record_item_number);
        textView_time = (TextView)rootView.findViewById(R.id.record_item_time);
        textView_text = (TextView)rootView.findViewById(R.id.record_item_text);
        imageView_cloud = (ImageView)rootView.findViewById(R.id.record_item_cloud);
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG,"**Record_ViewHolder**onClick"+getAdapterPosition());
        Toast.makeText(view.getContext(), "position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
    }
}
