package com.example.shitou.filedownload;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.math.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DownloadThread mythread  = new DownloadThread();
    private MultiDownloadThread multithread1  = null, multithread2 = null;
    private int fileSize;//文件大小
    private String filePath;//文件路径
    private String filename;//文件名
    private ProgressBar pro1;
    private int finish=0;
    private int a=0,b=0;
    private int fullsize=-1;
    private TextView percent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pro1=(ProgressBar)findViewById(R.id.progressBar1);
        percent=(TextView)findViewById(R.id.text_percent);
        pro1.setProgress(0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Button btn_download=(Button)findViewById(R.id.btn_down);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file_url="http://www.shitou-studio.com/static/storage/258157computer.pdf";
                //String file_url="http://www.shitou-studio.com/static/storage/tessdata/chi_sim.traineddata";
                //String file_url="http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
                String filename = file_url.substring(file_url.lastIndexOf("/")+1);;//获取文件名
                //System.out.println("文件大小："+fileSize);
                //System.out.println("文件类型："+filename);
                String filePath = Environment.getExternalStorageDirectory()+"/myfile";//获取文件存放路径
                File path=new File(filePath);
                if(!path.exists()){//如果该文件夹不存在，则创建
                    path.mkdirs();
               }
               filename=filePath+"/"+filename;
               // RandomAccessFile raf = null;//创建随机读写文件
               /// try {
                 //   raf = new RandomAccessFile(filename, "rw");
               // } catch (FileNotFoundException e) {
                //    e.printStackTrace();
              //  }
                File file = new File(filename);
                multithread1=new MultiDownloadThread(handler,file_url,1,file);
                multithread2=new MultiDownloadThread(handler,file_url,2,file);
                multithread1.start();
                multithread2.start();



                //Toast.makeText(MainActivity.this,"正常显示",Toast.LENGTH_SHORT).show();
                /*
                new Thread(){
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
                        } catch (Exception e) {
                            System.out.println("download fail!");
                        }
                    }
                }.start();
                */
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MultiDownloadThread.CURRENTSIZE1:
                    a=msg.getData().getInt("donesize");
                    pro1.setProgress(a+b);
                    float re=((float)a+(float)b)/(float)fullsize;
                    percent.setText(Math.round(re*100)+"%");
                    break;
                case MultiDownloadThread.CURRENTSIZE2:
                    b=msg.getData().getInt("donesize");
                    pro1.setProgress(a+b);
                    float re2=((float)a+(float)b)/(float)fullsize;
                    percent.setText(Math.round(re2*100)+"%");
                    break;
                case MultiDownloadThread.FINISH:
                    finish=finish+1;
                    if(finish==2){
                        Toast.makeText(getApplicationContext(),"下载完成",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MultiDownloadThread.FULLSIZE:
                    if(fullsize==-1){
                        fullsize=msg.getData().getInt("fullsize");
                        pro1.setMax(fullsize);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

}
