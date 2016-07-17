package com.studio.shitou.coordinatorlayout;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar mtoolbar;
    private int currentPosition=0;//当前的position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mtoolbar=(Toolbar)findViewById(R.id.toolbar);
        mtoolbar.setTitle("JluJob-Demo");
        mtoolbar.setTitleTextColor(getResources().getColor(R.color.textColorPrimary));

        setSupportActionBar(mtoolbar);
        mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_about:
                        Toast.makeText(MainActivity.this,"关于",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_setting:
                        Toast.makeText(MainActivity.this,"设置",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(MainActivity.this,"测试",Toast.LENGTH_SHORT).show();
                Snackbar.make(view, "去体验CollapsingToolbarLayout效果？", Snackbar.LENGTH_LONG).setAction("GO",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, com.studio.shitou.coordinatorlayout.Main2Activity.class);
                                startActivity(intent);
                            }
                        }).show();

            }
        });

        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

        //下拉刷新部分
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        //设置卷内的颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        //停止刷新动画
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(swipeRefreshLayout,"刷新完成！",Snackbar.LENGTH_SHORT).show();
                    }
                }, 3000);
            }
        });
    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.item_home, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.tv.setText(mDatas.get(position));
            holder.itemlayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == motionEvent.ACTION_DOWN) {
                            //按钮按下去的效果
                        view.findViewById(R.id.itemLayout).setBackgroundColor(getResources().getColor(R.color.colorGray));
                        Log.v("TouchEvent","按下了"+motionEvent.getEventTime());
                    }
                    if (motionEvent.getAction() == motionEvent.ACTION_CANCEL) {
                        //按钮按下去的效果
                        view.findViewById(R.id.itemLayout).setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
                        Log.v("TouchEvent","事件取消");
                    }
                    if (motionEvent.getAction() == motionEvent.ACTION_UP) {
                        //手抬起来效果
                        view.findViewById(R.id.itemLayout).setBackgroundColor(getResources().getColor(R.color.textColorPrimary));
                        Log.v("TouchEvent","抬起");
                    }
                    //return true;// 返回true的话，单击事件、长按事件不可以被触发
                    return false;
                }
            });
            /**绑定长按监听事件**/
            holder.itemlayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Toast.makeText(MainActivity.this,"点击了item "+position,Toast.LENGTH_SHORT).show();
                    registerForContextMenu(view);
                    currentPosition=position;
                    openContextMenu(view);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv;
            LinearLayout itemlayout;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
                itemlayout=(LinearLayout)view.findViewById(R.id.itemLayout);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        //返回true才会显示overflow按钮
        return true;
    }
    /**生成上下文菜单**/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                   ContextMenu.ContextMenuInfo menuInfo) {
        // set context menu title
        menu.setHeaderTitle("将此招聘会添加到您的行程中？");
        // add context menu item
        menu.add(0, 1, Menu.NONE, "朕不需要！");
        menu.add(0, 2, Menu.NONE, "好，帮我添加");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case 1:
                Log.v("ContextMenu","不需要添加");
                break;
            case 2:
                Log.v("ContextMenu","已经添加，item"+getCurrentItemPosition());
                break;
        }
        //unregisterForContextMenu(view);
        return true;
    }
    /**获取当前点击的item的position**/
    public int getCurrentItemPosition(){
        return this.currentPosition;
    }
}
