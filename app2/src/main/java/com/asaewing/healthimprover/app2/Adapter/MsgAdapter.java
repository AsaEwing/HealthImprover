package com.asaewing.healthimprover.app2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.asaewing.healthimprover.app2.R;
import com.asaewing.healthimprover.app2.Others.HiMsgItem;

import java.text.SimpleDateFormat;
import java.util.List;

public class MsgAdapter extends ArrayAdapter<HiMsgItem> {

    private int resourceId = 0;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat msgDateFormat = new SimpleDateFormat ("a hh:mm");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat midDateFormat = new SimpleDateFormat ("E yyyy.MM.dd");


    public MsgAdapter(Context context, int textViewResourceId,
                      List<HiMsgItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HiMsgItem msg = getItem(position);
        View view = null;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();

            viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
            viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.right_layout);

            viewHolder.leftRL = (RelativeLayout)view.findViewById(R.id.left_RL);
            viewHolder.rightRL = (RelativeLayout)view.findViewById(R.id.right_RL);
            viewHolder.midRL = (RelativeLayout)view.findViewById(R.id.mid_RL);

            viewHolder.leftMsg = (TextView)view.findViewById(R.id.left_msg);
            viewHolder.rightMsg = (TextView)view.findViewById(R.id.right_msg);

            viewHolder.leftMsgDate = (TextView)view.findViewById(R.id.left_msg_date);
            viewHolder.rightMsgDate = (TextView)view.findViewById(R.id.right_msg_date);
            viewHolder.midDate = (TextView)view.findViewById(R.id.mid_date);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (msg.getType() == HiMsgItem.TYPE_RECEIVED) {
            viewHolder.leftRL.setVisibility(View.VISIBLE);
            viewHolder.rightRL.setVisibility(View.GONE);
            viewHolder.midRL.setVisibility(View.GONE);

            viewHolder.leftMsg.setText(msg.getContent());
            viewHolder.leftMsgDate.setText(msgDateFormat.format(msg.getDate()));

        } else if (msg.getType() == HiMsgItem.TYPE_SENT) {
            viewHolder.leftRL.setVisibility(View.GONE);
            viewHolder.rightRL.setVisibility(View.VISIBLE);
            viewHolder.midRL.setVisibility(View.GONE);

            viewHolder.rightMsg.setText(msg.getContent());
            viewHolder.rightMsgDate.setText(msgDateFormat.format(msg.getDate()));

        }else if (msg.getType() == HiMsgItem.TYPE_MID_DAY) {
            viewHolder.leftRL.setVisibility(View.GONE);
            viewHolder.rightRL.setVisibility(View.GONE);
            viewHolder.midRL.setVisibility(View.VISIBLE);

            viewHolder.midDate.setText(midDateFormat.format(msg.getDate()));
        }

        return view;
    }

    class ViewHolder {

        LinearLayout leftLayout;
        LinearLayout rightLayout;

        RelativeLayout leftRL;
        RelativeLayout rightRL;
        RelativeLayout midRL;

        TextView leftMsg;
        TextView rightMsg;

        TextView leftMsgDate;
        TextView rightMsgDate;
        TextView midDate;
    }
}
