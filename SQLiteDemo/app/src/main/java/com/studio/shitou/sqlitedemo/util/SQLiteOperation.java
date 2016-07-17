package com.studio.shitou.sqlitedemo.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;

/**
 * @Created on 2016/7/17.
 * @Author shitou.yang
 * @描述：SQLite数据库操作类
 * @Copyright©shitou-studio.com
 */
public class SQLiteOperation {

    /********************
     * 对数据库进行初始化，建立新的数据库和相应的表、字段等
     ***/
    public void init_DB() {

        //打开指定数据库，如果不存在则创建一个新的
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/JluInfo/jobinfo.db", null);
        //创建表jobinfo
        db.execSQL("DROP TABLE IF EXISTS jobinfo");
        db.execSQL("CREATE TABLE jobinfo (_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, id INT, date_info VARCHAR, time_info VARCHAR, place_info VARCHAR, ismarked SMALLINT, isexpired SMALLINT)");
    }

    /*****************
     * 插入数据
     ***************/
    public void insert_DB(int id, String title, String date_info, String time_info, String place_info) {
        /***
         插入数据有两种方法：
         1、SQLiteDatabase的insert(String table,String nullColumnHack,ContentValues  values)方法，
         参数1  表名称，
         参数2  空列的默认值
         参数3  ContentValues类型的一个封装了列名称和列值的Map；
         2、编写插入数据的SQL语句，直接调用SQLiteDatabase的execSQL()方法来执行
         （第一种较为简洁，所以用第一种）
         ***/
        //先打开数据库
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/JluInfo/jobinfo.db", null);
        //实例化常量值
        ContentValues cValue = new ContentValues();
        //添加要插入的数据
        cValue.put("id", id);
        cValue.put("title", title);
        cValue.put("date_info", date_info);
        cValue.put("time_info", time_info);
        cValue.put("place_info", place_info);
        cValue.put("ismarked", 0);//每条信息在插入时，肯定是未被标记过的
        cValue.put("isexpired", 0);//每条信息在插入时，肯定是未过期失效的
        //调用insert()方法插入数据
        db.insert("jobinfo", null, cValue);
    }

    /**************
     * 删除数据
     */
    public void delete_DB(int id) {
/***
 删除数据也有两种方法：

 1、调用SQLiteDatabase的delete(String table,String whereClause,String[]  whereArgs)方法
 参数1  表名称
 参数2  删除条件
 参数3  删除条件值数组

 2、编写删除SQL语句，调用SQLiteDatabase的execSQL()方法来执行删除。
 （第二种较为简洁，所以用第二种）
 ***/
        //先进行删除条件的筛选（附件功能）

        //先打开数据库
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/JluInfo/jobinfo.db", null);
        //删除SQL语句
        String sql = "delete from jobinfo where id = " + id;
        //执行SQL语句
        db.execSQL(sql);
    }

    /***************
     * 更新数据
     */
    public void updateDB(int flag, int id) {
        /***
         * 也是SQL语句和Android_API两种操作方法，SQL语句较为简洁
         * ***/
        //先打开数据库
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/JluInfo/jobinfo.db", null);
        //SQL语句 (flag标识两种更新类型，flag=1更新ismarked字段，flag=2更新isexpired字段)
        if (flag == 1) {
            String sql = "update jobinfo set ismarked = 1 where id = " + id;
            //执行SQL
            db.execSQL(sql);
        }
        if (flag == 2) {
            String sql = "update jobinfo set isexpired = 1 where id = " + id;
            //执行SQL
            db.execSQL(sql);
        }
    }
    /***************
         查询数据（取数据）
     */
    public ArrayList<String> query(){
        //先打开数据库
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/JluInfo/jobinfo.db", null);
        //查询获得游标
        Cursor cursor = db.query ("jobinfo",null,null,null,null,null,null);
        ArrayList<String> result=new ArrayList<String>();
        //判断游标是否为空
        if(cursor.moveToFirst()){
           //遍历游标
            for(int i=0;i<cursor.getCount();i++){
                cursor.move(i);
                //获得ID
                int id = cursor.getInt(0);
                //获得标题
                String title=cursor.getString(1);
                result.add("ID:"+id+",title:"+title);
              //输出用户信息 System.out.println(id+":"+sname+":"+snumber);
            }
        }
        return result;
    }
}
