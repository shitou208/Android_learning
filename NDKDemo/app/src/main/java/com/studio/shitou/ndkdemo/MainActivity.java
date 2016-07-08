package com.studio.shitou.ndkdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("JniTest");//导入生成的链接库，此处名称必须和build.gradle里面设置的.so名字一致
    }
    public native String getStringFromNative();//native表示声明的是一个本地方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t=(TextView)findViewById(R.id.textview);
        t.setText(getStringFromNative());
    }
}
