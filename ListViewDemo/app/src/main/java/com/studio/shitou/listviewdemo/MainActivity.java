package com.studio.shitou.listviewdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView msgListView;
    private EditText input;
    private Button send;
    private MsgAdapter adapter;
    private List<Msg> msgList=new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs();//初始化消息数据
        adapter=new MsgAdapter(MainActivity.this,R.layout.list_item,msgList);
        input=(EditText)findViewById(R.id.msg_input);
        send=(Button)findViewById(R.id.send);
        msgListView=(ListView)findViewById(R.id.listview);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=input.getText().toString();
                Msg msg=new Msg(content,Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.notifyDataSetChanged();//当有新的消息时，listview将刷新数据显示
                msgListView.setSelection(msgList.size());//将listview定位到最后一行，显示最新的消息
                new HttpPostThread(handler,1,input.getText().toString()).start();
                input.setText("");//将输入框消息清空
            }
        });

        input.addTextChangedListener(watcher);//添加编辑框内容变化的监听


    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
                 //System.out.println("this is onchange……");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
           // System.out.println("this is beforechange……");
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
           // System.out.println("this is afterchange……");
            if(input.getText().toString().equals("")){
                send.setBackgroundResource(R.drawable.bg_send_disable);
                send.setEnabled(false);
            }
            else{
                send.setBackgroundResource(R.drawable.bg_send);
                send.setEnabled(true);
            }
        }
    };

    private void initMsgs(){
       // Msg msg1=new Msg("Hello",Msg.TYPE_RECEIVED);
        //msgList.add(msg1);
        //Msg msg2=new Msg("22222Hello",Msg.TYPE_SENT);
       // msgList.add(msg2);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println(msg.getData().getString("response"));
                    /**显示服务器返回的消息**/
                    Msg msg2=new Msg(msg.getData().getString("response"),Msg.TYPE_RECEIVED);
                    msgList.add(msg2);
                    adapter.notifyDataSetChanged();//当有新的消息时，listview将刷新数据显示
                    msgListView.setSelection(msgList.size());//将listview定位到最后一行，显示最新的消息
                    break;

            }
            super.handleMessage(msg);
        }
    };
}
