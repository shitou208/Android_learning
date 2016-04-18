package com.shitou_studio.intent_learning;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
           requestWindowFeature(Window.FEATURE_NO_TITLE);
          调用该方法实现activity中不带标题栏，这样可以增大内容显示空间，一定要在setContentView()之前调用
           但是不知为何，实际测试调用此方法后并没有效果，所以推荐直接在AndroidManifest.xml中设置，android:theme=@style/Theme.AppCompat.DayNight.NoActionBar
           这样是全局设置，对所有的activity都可以实现不带actionbar效果
         **/
        setContentView(R.layout.activity_main);
        Button button1=(Button)findViewById(R.id.button1);
        Button button2=(Button)findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(MainActivity.this,com.shitou_studio.intent_learning.OneActivity.class);//为intent添加执行意图
                startActivity(intent1);
                //Toast.makeText(MainActivity.this,"测试Toast",Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(MainActivity.this,com.shitou_studio.intent_learning.TwoActivity.class);
                intent2.putExtra("username","admin");//利用putExtra方法存入要传递的数据
                intent2.putExtra("password", "123456");//利用putExtra方法存入要传递的数据
                startActivityForResult(intent2, 1);//调用startActivityForResult()可以实现跳转之后再回到主界面时返回数据
                                                                  //这样不仅可以“发”数据，还可以"收"数据。1是requestCode，最后在进行收数据时用来区分是从哪个activity返回的
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data) {
        switch (requestCode){
            case 1:{//先判断requestCode，然后根据回传的来源进行处理.
                if(resultCode==RESULT_OK) {//同一个activity可能回传多个intent，通过resultCode来区分
                    String result = data.getStringExtra("check_result");//取出回传的数据
                    Toast.makeText(MainActivity.this, "回传结果是：" + result, Toast.LENGTH_LONG).show();//显示回传的结果
                    break;
                }
            }
        }
    }
}
