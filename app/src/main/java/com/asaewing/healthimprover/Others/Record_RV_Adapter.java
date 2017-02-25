package com.asaewing.healthimprover.Others;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.asaewing.healthimprover.R;

import java.util.List;

/**
 * Created by asaewing on 2017/1/26.
 */

public class Record_RV_Adapter
        extends RecyclerView.Adapter<Record_ViewHolder> {

    String TAG = "Record_RV_Adapter";

    private List<RecordItem> mDataRecord;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    

    public Record_RV_Adapter() {
    }

    public Record_RV_Adapter(List<RecordItem> mDataRecord) {
        this.mDataRecord = mDataRecord;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Record_ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...
        Record_ViewHolder vh = new Record_ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(Record_ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView_number.setText(mDataRecord.get(position).getNumbers());
        holder.textView_time.setText(mDataRecord.get(position).getTimes());
        holder.textView_text.setText(mDataRecord.get(position).getTexts());
        if (mDataRecord.get(position).getIsInCloud()){
            holder.imageView_cloud.setImageResource(R.drawable.cloud_yes);
        } else {
            holder.imageView_cloud.setImageResource(R.drawable.cloud_no);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataRecord.size();
    }

    public void replaceRecordData(List<RecordItem> mDataRecord) {
        this.mDataRecord = mDataRecord;
        notify();
    }
    
    public void addRecordData(int position,RecordItem recordItem){
        mDataRecord.add(position, recordItem);
        notifyItemInserted(position);
    }

    public void addRecordData(RecordItem recordItem){
        int position = mDataRecord.size();
        this.addRecordData(position,recordItem);
    }
}
