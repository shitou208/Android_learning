package com.studio.shitou.staggeredgridview;
/**
 * @Created on 2016/7/13.
 * @Author shitou.yang
 * @描述：使用开源项目StaggeredGridView实现的瀑布流Demo
 * @Copyright©shitou-studio.com
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;


import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    //定义图片资源数组
    private int images[] = {
            R.drawable.grid_view_1,
            R.drawable.grid_view_2,
            R.drawable.grid_view_3,
            R.drawable.grid_view_4,
            R.drawable.grid_view_5,
            R.drawable.grid_view_6,
            R.drawable.grid_view_7,
            R.drawable.grid_view_4,
            R.drawable.grid_view_1,
            R.drawable.grid_view_2,
            R.drawable.grid_view_3,
            R.drawable.grid_view_6,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StaggeredGridView gridView = (StaggeredGridView) this.findViewById(R.id.staggeredGridView1);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image"};
        int [] to = {R.id.imageview};
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item1, from, to);
        //配置适配器
        gridView.setAdapter(sim_adapter);
    }
    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<images.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            data_list.add(map);
        }

        return data_list;
    }

}
