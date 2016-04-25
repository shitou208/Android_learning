package com.example.shitou.filedownload;

import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * 多线程下载类
 */
public class MultiDownloadThread extends Thread {
    private long fileSize;//文件大小
    private String filePath;//文件路径
    private String filename;//文件名
    private Handler handler ;//定义handler
    private String file_url;//文件下载地址
    private int threadId;//线程id
    private long startPos;//起始位置
    private long endPos;//结束位置
    private File file;
    public MultiDownloadThread(Handler handler,String url,int id,File file){
        this.handler=handler;
        this.file_url=url;
        this.threadId=id;
        this.file=file;
    }
    @Override
    public void run() {
        try {
            URL url = new URL(file_url);//定义一个URL请求
            URLConnection conn = url.openConnection();//打开URL请求
            conn.setAllowUserInteraction(true);
            fileSize = conn.getContentLength();//获取文件大小
            filename = file_url.substring(file_url.lastIndexOf("/")+1);;//获取文件名
            System.out.println(threadId+"文件大小："+fileSize);
            System.out.println(threadId+"文件类型："+filename);
            //filePath = Environment.getExternalStorageDirectory()+"/myfile";//获取文件存放路径
            //File path=new File(filePath);
           //if(!path.exists()){//如果该文件夹不存在，则创建
           //     path.mkdirs();
           // }
           // filename=filePath+"/"+filename;
            RandomAccessFile raf = new RandomAccessFile(file, "rw");//创建随机读写文件
            //raf.setLength(fileSize);//本地文件大小设成和要下载文件大小一致
            //raf.close();
            //根据开启的线程数，为每个线程分配要下载的size，确定下载起始、结束位置(本例中以2个线程为例)
            System.out.println(threadId+"已经设置好raf");

            long center_pos=fileSize/2;//确定中间位置
            if(threadId==1) {
                startPos=0;
                endPos=center_pos-1;
                System.out.println(threadId+"location 1, "+"bytes=" + startPos + "-" + endPos);
                conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);// 设置获取实体数据的范围
                System.out.println(threadId+"location 2");
                raf.seek(startPos);
                System.out.println(threadId+"location 3");
            }
            if (threadId==2){
                startPos=center_pos;
                endPos=fileSize-1;
                System.out.println(threadId+"location 1, "+"bytes=" + startPos + "-" + endPos);
                conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);// 设置获取实体数据的范围
                raf.seek(startPos);
            }
            System.out.println(threadId+"已经设置好position");
            conn.connect();//获取指定部分的文件
            InputStream is = conn.getInputStream();//文件输入流
            System.out.println(threadId+"连接，并获得输入流");
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1)
            {
                raf.write(buf, 0, len);
            }
            System.out.println(threadId+"写入完成");
            raf.close();
            is.close();

            /*
            filePath = Environment.getExternalStorageDirectory()+"/myfile";//获取文件存放路径
            //System.out.println("location 4");
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
            */
        } catch (Exception e) {
            System.out.println("download fail!");
        }
        super.run();
    }
}
