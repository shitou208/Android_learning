package com.studio.shitou.lockviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TranslateAnimation wrongAnimation1,wrongAnimation2;
    private TextView textView_wrong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LockView lock=(LockView)this.findViewById(R.id.mylockview);
        textView_wrong=(TextView)findViewById(R.id.textview_wrong);
        setWrongAnimation();
        lock.setOnDrawFinishedListener(new LockView.OnDrawFinishedListener() {
            @Override
            public boolean OnDrawFinished(String password) {
                if(password.equals("1235789")){
                    Toast.makeText(MainActivity.this,"密码正确",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(MainActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    textView_wrong.setVisibility(View.VISIBLE);
                    textView_wrong.startAnimation(wrongAnimation1);
                    textView_wrong.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }
    public void setWrongAnimation(){
        //wrongAnimation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
             //   Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            //    0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        //wrongAnimation1.setDuration(200);


       wrongAnimation1 = new TranslateAnimation(textView_wrong.getWidth(),
                textView_wrong.getWidth() + 10, textView_wrong.getHeight(), textView_wrong.getHeight());

        // 利用 CycleInterpolator 参数 为float 的数  表示 抖动的次数，而抖动的快慢是由 duration 和 CycleInterpolator 的参数的大小 联合确定的
        wrongAnimation1.setInterpolator(new CycleInterpolator(3f));
        wrongAnimation1.setDuration( 500 );
    }
}
