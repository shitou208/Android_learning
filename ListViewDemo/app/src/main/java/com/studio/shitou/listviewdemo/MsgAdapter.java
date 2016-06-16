package com.studio.shitou.listviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shitou on 2016/6/16.
 */
public class MsgAdapter extends ArrayAdapter<Msg>{
    private int resourceId;

    public MsgAdapter(Context context,int textViewResourceId,List<Msg> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Msg msg=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.leftLayout=(LinearLayout)view.findViewById(R.id.left);
            viewHolder.rightLayout=(LinearLayout)view.findViewById(R.id.right);
            viewHolder.leftMsg=(TextView)view.findViewById(R.id.msg_left);
            viewHolder.rightMsg=(TextView)view.findViewById(R.id.msg_right);
            view.setTag(viewHolder);
        }
        else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        if(msg.getType()==Msg.TYPE_RECEIVED){
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);
            viewHolder.leftMsg.setText(msg.getContent());
        }
        if(msg.getType()==Msg.TYPE_SENT){
            viewHolder.leftLayout.setVisibility(View.GONE);
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
        }
        return view;
    }

     class ViewHolder{
         LinearLayout leftLayout;
         LinearLayout rightLayout;
         TextView leftMsg;
         TextView rightMsg;
     }
}
