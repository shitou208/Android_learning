package com.shitou_studio.intent_learning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ThreeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        Button btn1=(Button)findViewById(R.id.button_three1);
        Button btn2=(Button)findViewById(R.id.button_three2);
        Button btn3=(Button)findViewById(R.id.button_three3);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Intent.ACTION_VIEW);//定义一个新的intent,意图是打开网页，系统会自动查找能打开网页的应用
                intent1.setData(Uri.parse("http://www.jlu.edu.cn"));//指定要打开的网址
                startActivity(intent1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Intent.ACTION_DIAL);//定义一个新的intent,意图是打开拨号界面，系统会自动查找能打开的应用
                intent2.setData(Uri.parse("tel:10086"));//指定要拨打的电话号码
                startActivity(intent2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent("com.shitou_studio.intent_learning.MYINTENT");//调用自定义的一个View来响应intent,因为只有运行中的activity才能响应，所以在activity3中自定义标签
                startActivity(intent3);
            }
        });
    }

}
