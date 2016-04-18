package com.shitou_studio.intent_learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 16-4-18.
 */
public class TwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        TextView data=(TextView)findViewById(R.id.text_data);
        Intent intent1=getIntent();//获取从上一activity传递过来的Intent
        String user=intent1.getStringExtra("username");//取出数据
        String pwd=intent1.getStringExtra("password");//取出数据
        data.setText("从上一activity得到的数据为：\nusername："+user+"\npassword："+pwd);//将取出的数据显示出来
        String res="";
        if(user.equals("admin")&&pwd.equals("123456")){//进行一个简单验证，返回验证结果。!!!注意：java中判断字符串相等要用equals，不能使用==
            res="您提交的用户名和密码验证正确！";
        }
        else{
            res="您提交的用户名和密码验证错误！";
        }
        //将验证结果写入新的intent，以便回传
        Intent intent2=new Intent();//这个intent只保存数据，不需指定跳转信息
        intent2.putExtra("check_result",res);//将验证结果存入intent
        setResult(RESULT_OK,intent2);//这一步就是封装好要回传的intent，以便在上一activity的onActivityResult中可以捕获该intent
        Button button1=(Button)findViewById(R.id.button_two);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          TwoActivity.this.finish();
            }
        });
    }

}
