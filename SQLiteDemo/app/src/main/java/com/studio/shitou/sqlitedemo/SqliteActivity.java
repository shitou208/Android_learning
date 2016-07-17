package com.studio.shitou.sqlitedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.studio.shitou.sqlitedemo.util.SQLiteOperation;


public class SqliteActivity extends AppCompatActivity {
    private Button btnInitDatabase;
    private EditText dataId;
    private EditText dataTitle;
    private EditText dataDate;
    private EditText dataTime;
    private EditText dataPlace;
    private Button btnInsert;
    private Button btnDelete;
    private Button btnUpdate;
    private Button btnShow;
    private SQLiteOperation sq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sqliteactivity);
        sq=new SQLiteOperation();
        btnInitDatabase = (Button) findViewById(R.id.btn_initDatabase);
        dataId = (EditText) findViewById(R.id.data_id);
        dataTitle = (EditText) findViewById(R.id.data_title);
        dataDate = (EditText) findViewById(R.id.data_date);
        dataTime = (EditText) findViewById(R.id.data_time);
        dataPlace = (EditText) findViewById(R.id.data_place);
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnShow = (Button) findViewById(R.id.btn_show);

        btnInitDatabase.setOnClickListener(listener);
        btnInsert.setOnClickListener(listener);
        btnDelete.setOnClickListener(listener);
        btnUpdate.setOnClickListener(listener);
        btnShow.setOnClickListener(listener);



    }
    Button.OnClickListener listener = new Button.OnClickListener(){//创建监听对象
        public void onClick(View v){
            switch(v.getId()){
                case R.id.btn_initDatabase:
                    sq.init_DB();
                    break;
                case R.id.btn_insert:
                    sq.insert_DB(Integer.parseInt(dataId.getText().toString()),dataTitle.getText().toString(),"2016-07-14","16:00","南区经信C205");
                    break;
                case R.id.btn_delete:
                    sq.delete_DB(1);
                    break;
                case R.id.btn_update:
                    sq.updateDB(1,1);
                    break;
                case R.id.btn_show:
                    Toast.makeText(SqliteActivity.this,sq.query().get(0).toString(),Toast.LENGTH_LONG).show();
                    break;
            }
        }

    };
}
