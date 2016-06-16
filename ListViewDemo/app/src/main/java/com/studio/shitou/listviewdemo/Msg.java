package com.studio.shitou.listviewdemo;

/**
 * Created by shitou on 2016/6/16.
 * 消息的实体类
 */
public class Msg {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private String content;
    private int type;

    public Msg(String content,int type){
        this.content=content;
        this.type=type;
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }


}
