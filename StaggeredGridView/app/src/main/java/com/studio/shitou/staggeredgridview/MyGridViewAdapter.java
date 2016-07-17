package com.studio.shitou.staggeredgridview;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.studio.shitou.staggeredgridview.R;

public class MyGridViewAdapter extends BaseAdapter {


    //private ImageLoader mLoader;
    private Context context;
    private int layoutId;
    private int listData[];
    private int to[];


    public MyGridViewAdapter(Context context, int layoutId,
                             int[] listData, int to[]) {
        this.context = context;
        this.layoutId = layoutId;
        this.listData = listData;
        this.to = to;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listData.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listData[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(context);
            convertView = layoutInflator.inflate(R.layout.custom_grid_view_item,
                    null);
            holder = new ViewHolder();
            holder.imageView = (DynamicHeightImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();

        //mLoader.DisplayImage(getItem(position), holder.imageView);
        holder.imageView.setImageResource(listData[position]);

        return convertView;
    }

    static class ViewHolder {
        DynamicHeightImageView imageView;
    }

}