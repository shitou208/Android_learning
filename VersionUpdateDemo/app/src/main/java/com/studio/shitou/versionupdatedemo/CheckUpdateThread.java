package com.studio.shitou.versionupdatedemo;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by shitou on 16-4-29.
 */
public class CheckUpdateThread extends Thread {
    private int currentversion;//APP当前的版本
    private Handler handler;
    private String version;
    private String download_url;
    private String update_info;
    public static final int NEWVERSION=1;
    public CheckUpdateThread(Handler handler, int currentversion) {
        this.handler = handler;
        this.currentversion = currentversion;
    }

    @Override
    public void run() {
        try {
            URL updateurl = new URL("URL");
            HttpURLConnection conn = (HttpURLConnection) updateurl.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            // 获取响应的输入流对象
            InputStream is = conn.getInputStream();
            // 创建字节输出流对象
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义缓冲区
            byte buffer[] = new byte[1024];
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                os.write(buffer, 0, len);
            }
            // 释放资源
            is.close();
            os.close();
            // 返回字符串
            String res = new String(os.toByteArray());

            /**对服务器返回的json数据进行解析**/
            JSONArray jsonArray = new JSONArray(res);
            JSONObject checkres = (JSONObject)jsonArray.opt(0);
            version=checkres.getString("version");
            download_url=checkres.getString("download_url");
            update_info=checkres.getString("update_info");
            /**与本地版本比较，若本地版本是旧的，则发送更新提醒到UI线程**/
            if(currentversion<Float.parseFloat(checkres.getString("version"))){
                //有新版本，提示用户
                System.out.println("发现新版本");
                sendMsg(NEWVERSION);
            }
            else{
                //无新版本，不做处理
                System.out.println("没有新版本");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("请求失败！");
        }
        super.run();
    }
    private void sendMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        //使用Bundle绑定数据，将新版本APP的信息传到主线程（UI线程）
        Bundle bundleData = new Bundle();
        bundleData.putString("downloadUrl", download_url);
        bundleData.putString("version", version);
        bundleData.putString("update_info", update_info);
        msg.setData(bundleData);
        handler.sendMessage(msg);
    }
}
