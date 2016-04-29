package com.studio.shitou.versionupdatedemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    private AlertDialog newversion_dialog;
    private String update_info,download_url,version;
    private static final int MYVERSION=0;//该APP的version
    private static final int GETNEWVERSION=2;//获取新版本APP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckUpdateThread th = new CheckUpdateThread(handler, MYVERSION);
        th.start();
    }

    /**
     * 定义一个AlertDialog提示用户有新版本可更新
     **/
    private AlertDialog.Builder updateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("发现新版本 v"+version);
        builder.setMessage(update_info);
        builder.setCancelable(true);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message getmsg = new Message();
                getmsg.what = GETNEWVERSION;
                handler.sendMessage(getmsg);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CheckUpdateThread.NEWVERSION:
                    update_info=msg.getData().getString("update_info");
                    version=msg.getData().getString("version");
                    download_url=msg.getData().getString("downloadUrl");
                    newversion_dialog=updateDialog().create();
                    newversion_dialog.show();
                    break;
                case GETNEWVERSION:
                    /**直接调用浏览器来下载，不再自己编写下载模块，使APP更轻量级**/
                    Intent download=new Intent(Intent.ACTION_VIEW);
                    download.setData(Uri.parse(download_url));
                    startActivity(download);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
