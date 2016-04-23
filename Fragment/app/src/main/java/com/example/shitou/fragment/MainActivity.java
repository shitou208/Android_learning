package com.example.shitou.fragment;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();//初始化布局
        final TextView t1=(TextView)findViewById(R.id.tabitem1);
        final TextView t2=(TextView)findViewById(R.id.tabitem2);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals("weixin")){
                //Toast.makeText(MainActivity.this,tabId,Toast.LENGTH_SHORT).show();
                    t1.setBackgroundColor(getResources().getColor(R.color.colorbg));
                    t2.setBackgroundColor(getResources().getColor(R.color.colororigin));
            }
                if(tabId.equals("google")){
                    t2.setBackgroundColor(getResources().getColor(R.color.colorbg));
                    t1.setBackgroundColor(getResources().getColor(R.color.colororigin));
                }
            }
        });

    }
    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);
        // 找到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        TabHost.TabSpec tabSpec1 = mTabHost.newTabSpec("weixin").setIndicator(mLayoutInflater.inflate(R.layout.tab_item_view1, null));
        TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("google").setIndicator(mLayoutInflater.inflate(R.layout.tab_item_view2, null));
        //mLayoutInflater.inflate(R.layout.tab_item_view1, null);
        mTabHost.addTab(tabSpec1, Fragment1.class, null);
        mTabHost.addTab(tabSpec2, Fragment2.class, null);

    }
}
