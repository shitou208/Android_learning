package com.example.shitou.uitestnew;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_frame = (Button) findViewById(R.id.btn_frame);
        Button btn_table = (Button) findViewById(R.id.btn_table);
        Button btn_grid = (Button) findViewById(R.id.btn_grid);

        btn_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1 = new Intent(MainActivity.this, com.example.shitou.uitestnew.FrameActivity.class);
                startActivity(in1);
            }
        });
        btn_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(MainActivity.this, com.example.shitou.uitestnew.TableActivity.class);
                startActivity(in2);
            }
        });
        btn_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3 = new Intent(MainActivity.this, com.example.shitou.uitestnew.GridActivity.class);
                startActivity(in3);
            }
        });
    }
}
