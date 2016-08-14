package com.studio.shitou.okhttpdownloaddemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity{
    private TextView textview1;
    private ProgressBar bar1;
    private TextView textview2;
    private ProgressBar bar2;
    private TextView textview3;
    private ProgressBar bar3;
    private TextView textview4;
    private ProgressBar bar4;
    private TextView textview5;
    private ProgressBar bar5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview1 = (TextView) findViewById(R.id.textview1);
        bar1 = (ProgressBar) findViewById(R.id.bar1);
        textview2 = (TextView) findViewById(R.id.textview2);
        bar2 = (ProgressBar) findViewById(R.id.bar2);
        textview3 = (TextView) findViewById(R.id.textview3);
        bar3 = (ProgressBar) findViewById(R.id.bar3);
        textview4 = (TextView) findViewById(R.id.textview4);
        bar4 = (ProgressBar) findViewById(R.id.bar4);
        textview5 = (TextView) findViewById(R.id.textview5);
        bar5 = (ProgressBar) findViewById(R.id.bar5);

        OkHttpUtils
                .get()
                .url("http://www.shitou-studio.com/static/jobassistant.apk")
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "demo.apk") {
                   // @Override
                    public void inProgress(float progress)
                    {
                        System.out.println("下载进度"+progress);
                        bar1.setProgress((int) (100 * progress));
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                });
    }
}
