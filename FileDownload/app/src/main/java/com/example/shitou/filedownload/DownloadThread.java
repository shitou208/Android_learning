package com.example.shitou.filedownload;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 单线程下载类
 */
public class DownloadThread extends Thread {
    private int fileSize;//文件大小
    private String filePath;//文件路径
    private String filename;//文件名
    @Override
    public void run() {
        try {
            URL url = new URL("http://images.cnblogs.com/cnblogs_com/hanyonglu/show.jpg");//定义一个URL请求
            System.out.println("location 1");
            URLConnection conn = url.openConnection();//打开URL请求
            System.out.println("location 2");
            fileSize = conn.getContentLength();//获取文件大小
            System.out.println("location 3");
            filePath = Environment.getExternalStorageDirectory()+"/myfile";//获取文件存放路径
            System.out.println("location 4");
            filename=filePath+"/test.jpg";//定义文件名
            File download_file=new File(filename);
            //if (!download_file.exists()) {
            //  download_file.mkdirs();
            // }
            System.out.println("location 5");
            InputStream is = conn.getInputStream();//获取输入流
            byte[] bs = new byte[1024];//1K的数据缓冲
            int len;//读取到的数据长度
            System.out.println("location 6");
            OutputStream os = new FileOutputStream(download_file);//打开文件的输出流
            //开始读取
            System.out.println("location 7");
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            //完毕，关闭所有链接
            System.out.println("location 8");
            os.close();
            is.close();
            System.out.println("download successful!");
        } catch (Exception e) {
            System.out.println("download fail!");
        }
        super.run();
    }
}
