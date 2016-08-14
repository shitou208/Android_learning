package com.studio.shitou.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private ImageView imageview;
    private Button button;
    private static final String TAG="调试信息";

    /**************************************
     * 尝试下面方法，测试是否可以实现不安装openCVmanager即可运行程序
     * 结论：经测试，用OpenCVLoader.initDebug()替代OpenCVLoader.initAsync()来进行openCV库的初始化
     * 就可以实现不用安装opencvManager，但要把相应的.so文件拷贝到jinLibs目录
     ***********************/

    static {
        if(!OpenCVLoader.initDebug()){
            //初始化，可以打印日志判断初始化是否成功
            Log.v("OPENCV","opencv初始化失败");
        }
        else {
            Log.v("OPENCV","opencv初始化成功");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview = (ImageView) findViewById(R.id.imageview);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               imageview.setImageBitmap(rgb2gray(BitmapFactory.decodeResource(getResources(),
                        R.drawable.test)));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load OpenCV engine and init OpenCV library
       // OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getApplicationContext(), mLoaderCallback);
        //Log.i("log", "onResume sucess load OpenCV...");
    }

    // OpenCV库加载并初始化成功后的回调函数
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {

        @Override
        public void onManagerConnected(int status) {
            // TODO Auto-generated method stub
            switch (status) {
                case BaseLoaderCallback.SUCCESS:
                    Log.i("打印日志", "成功加载");
                    break;
                default:
                    super.onManagerConnected(status);
                    Log.i("打印日志", "加载失败");
                    break;
            }

        }
    };

    //灰度转换函数
    public Bitmap rgb2gray(Bitmap rgbBitmap) {
        Mat rgbMat = new Mat();
        Mat grayMat = new Mat();
        Mat hsvMat= new Mat();
        //srcBitmap = BitmapFactory.decodeResource(getResources(),
             //   R.drawable.nanhuaijin);
        Bitmap grayBitmap = Bitmap.createBitmap(rgbBitmap.getWidth(),
                rgbBitmap.getHeight(), Bitmap.Config.RGB_565);
        Bitmap hsvBitmap = Bitmap.createBitmap(rgbBitmap.getWidth(),
                rgbBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(rgbBitmap, rgbMat);// convert original bitmap to Mat,
        // R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);// rgbMat to
        Imgproc.cvtColor(rgbMat,hsvMat,Imgproc.COLOR_RGB2HSV);
        // gray
        // grayMat
        //Utils.matToBitmap(hsvMat, grayBitmap); // convert mat to bitmap
       //打印调试信息
        int col=hsvMat.cols();
        int row=hsvMat.rows();

        for(int i=0;i<row;i++){
            for(int j=0;j<col;j++){
                //rgbMat.put(i,j,new double[]{rgbMat.get(i,j)[0],rgbMat.get(i,j)[1],rgbMat.get(i,j)[2],10});
                if(hsvMat.get(i,j)[0]>=100){
                    grayMat.put(i,j,new double[]{255});
                }
                else{
                    grayMat.put(i,j,new double[]{0});
                }
                //rgbMat.get(i,j)[2]=100;
            }
        }

        //printLog("RGB_mat的(0,0)像素："+rgbMat.get(0,199)[0]+","+rgbMat.get(0,199)[1]+","+rgbMat.get(0,199)[2]+","+rgbMat.get(0,199)[3]);
        //printLog("hsv_mat的通道："+hsvMat.channels());
        Utils.matToBitmap(grayMat, grayBitmap); // convert mat to bitmap

        return grayBitmap;
    }

    private void printLog(String msg){
        Log.v(TAG,msg);
    }
}
