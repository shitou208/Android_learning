package com.example.shitou.filedownload;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

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
    private int fileSize;//文件大小
    private String filePath;//文件路径
    private String filename;//文件名
    private Handler handler ;//定义handler
    private String file_url;//文件下载地址
    private int threadId;//线程id
    private int startPos;//起始位置
    private int endPos;//结束位置
    private File file;
    private int currentdlSize=0;
    public final static int CURRENTSIZE1=0;
    public final static int CURRENTSIZE2=2;
    public final static int FINISH=1;
    public final static int FULLSIZE=3;
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
            URLConnection conn = url.openConnection();//打开URL请求a
            //URLConnection conn=null;
            conn.setAllowUserInteraction(true);
            //conn.setRequestProperty("Method","GET");
           // conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            //conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setDoInput(true);
            URLConnection conn2 = url.openConnection();//打开URL请求
            fileSize=conn2.getContentLength();//文件大小
            fullsizeMsg(FULLSIZE);
            //fileSize = conn.getContentLength();//获取文件大小
            /****
             * 一个重大的坑！！多次失败后查找各种可能原因，主要猜测有：
             * 1、我的nginx服务器不支持Range，但测试之后发现支持
             * 2、是不是因为红米手机执行不给力，最后也否决
             * 最终发现的原因，重要提醒！！！！在捕获异常时catch(Exception e)，一定要打印错误信息，这样可以帮助你快速的判断错误类型
             * 在打印错误信息后发现，错误提示为： Cannot set property after connection is made，建立连接后就不允许在对header进行设置了！
             * 难道是这一步URLConnection conn = url.openConnection()就已经建立了连接？？这一步只是把连接的信息告诉了conn，但并没有开始实质性的连接
             * 问题就在于，我先调用了fileSize = conn.getContentLength()，仔细一想，要想获得连接内容的全部长度，那就得从服务器读数据啊，那必定要建立连接的！！
             * 所以在这一步的时候连接就已经建立了，那么后边我再调用setRequestProperty就华丽丽的报错了！所以，要想一想，如果conn的某个方法的执行需要建立连接，
             * 那么调用该方法后就不能再执行setmethod setproperity等方法
             * **/



            filename = file_url.substring(file_url.lastIndexOf("/")+1);;//获取文件名
            System.out.println(threadId+"文件大小："+fileSize);
            System.out.println(threadId+"文件类型："+filename);
            //filePath = Environment.getExternalStorageDirectory()+"/myfile";//获取文件存放路径
            //File path=new File(filePath);
           //if(!path.exists()){//如果该文件夹不存在，则创建
           //     path.mkdirs();
           // }
           // filename=filePath+"/"+filename;
            //raf = new RandomAccessFile(file, "rwd");


            //raf.setLength(fileSize);//本地文件大小设成和要下载文件大小一致
            //raf.close();
            //根据开启的线程数，为每个线程分配要下载的size，确定下载起始、结束位置(本例中以2个线程为例)
            System.out.println(threadId+"已经设置好raf");

            int center_pos=fileSize/2;//确定中间位置
            if(threadId==1) {
                startPos=0;
                endPos=center_pos-1;
               // System.out.println(threadId+"location 1, "+"bytes=" + startPos + "-" + endPos);
               //conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);// 设置获取实体数据的范围
               // System.out.println(threadId+"location 2");
                //raf.seek(startPos);
                //System.out.println(threadId+"location 3");
            }
            if (threadId==2){
                startPos=center_pos;
                endPos=fileSize-1;
               // System.out.println(threadId+"location 1, "+"bytes=" + startPos + "-" + endPos);
                //conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);// 设置获取实体数据的范围
               // raf.seek(startPos);
            }
            System.out.println(threadId+"已经设置好position");
            conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
            //conn.setRequestProperty("Range", "bytes=0-1024");
            //conn.connect();
            RandomAccessFile raf = new RandomAccessFile(file, "rw");//创建随机读写文件
            raf.seek(startPos);
            System.out.println(threadId+"已经找到文件起始位置");
            //conn.connect();//获取指定部分的文件
            InputStream is = conn.getInputStream();//文件输入流
            System.out.println(threadId+"连接，并获得输入流");
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1)
            {
                raf.write(buf, 0, len);
                currentdlSize=currentdlSize+len;//已经下载的size
                if(threadId==1) {
                    senddlMsg(CURRENTSIZE1);
                }
                if(threadId==2){
                    senddlMsg(CURRENTSIZE2);
                }
            }
            System.out.println(threadId+"写入完成");
            raf.close();
            is.close();
            sendMsg(FINISH);
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
            //e.getMessage();
            //e.toString();
            System.out.println("download fail，error："+e.getMessage());
        }
        super.run();
    }
    private void sendMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        handler.sendMessage(msg);
    }
    private void senddlMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        Bundle data=new Bundle();
        data.putInt("donesize",currentdlSize);
        msg.setData(data);
        handler.sendMessage(msg);
    }
    private void fullsizeMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        Bundle data=new Bundle();
        data.putInt("fullsize",fileSize);
        msg.setData(data);
        handler.sendMessage(msg);
    }
    public float getdlSize(){
        return this.currentdlSize/fileSize;
    }
}
