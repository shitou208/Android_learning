package com.studio.shitou.listviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * 封装了一个Http的POST请求模块
 */
public class HttpPostThread extends Thread {
    private Handler handler;//回传消息的handler
    private int MessageFlag;//回传的消息的标志
    private String sendmsg;//提交的消息（问题）
    private String HttpResponse;//服务器返回的结果

    public HttpPostThread(Handler handler, int MessageFlag,String sendmsg) {
        this.handler = handler;
        this.MessageFlag=MessageFlag;
        this.sendmsg=sendmsg;
    }

    @Override
    public void run() {
        try {

            // 请求的地址
            String spec = "http://www.tuling123.com/openapi/api";
            // 根据地址创建URL对象
            URL url = new URL(spec);
            // 根据URL对象打开链接
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 传递的数据
            String data = "key=" + URLEncoder.encode("872d030c8103c53dbaf93be2304c6efb", "UTF-8")
                    + "&info=" + URLEncoder.encode(sendmsg, "UTF-8");
            // 设置请求的头
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Length",
                    String.valueOf(data.getBytes().length));
            // 设置请求的头
            urlConnection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            //setDoInput的默认值就是true
            //获取输出流
            OutputStream os = urlConnection.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            if (urlConnection.getResponseCode() == 200) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                String jsonresult = new String(baos.toByteArray());
                /**对服务器返回的json数据进行解析**/
                //JSONArray jsonArray = new JSONArray(jsonresult);
                JSONObject res = new JSONObject(jsonresult);
                HttpResponse=res.getString("text");
                sendMsg(MessageFlag);
            } else {
                System.out.println("连接失败.........");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.run();
    }

    private void sendMsg(int what) {
            Message msg = new Message();
            msg.what = what;
            //使用Bundle绑定数据
            Bundle bundleData = new Bundle();
            bundleData.putString("response", HttpResponse);
            msg.setData(bundleData);
            handler.sendMessage(msg);
    }
}
