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
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getApplicationContext(), mLoaderCallback);
        Log.i("log", "onResume sucess load OpenCV...");
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
        //srcBitmap = BitmapFactory.decodeResource(getResources(),
             //   R.drawable.nanhuaijin);
        Bitmap grayBitmap = Bitmap.createBitmap(rgbBitmap.getWidth(),
                rgbBitmap.getHeight(), Bitmap.Config.RGB_565);
        Utils.bitmapToMat(rgbBitmap, rgbMat);// convert original bitmap to Mat,
        // R G B.
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);// rgbMat to
        // gray
        // grayMat
        Utils.matToBitmap(grayMat, grayBitmap); // convert mat to bitmap
       //
        // Log.i(TAG, "procSrc2Gray sucess...");
        return grayBitmap;
    }
}
