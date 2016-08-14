package com.studio.shitou.listviewdemo;



import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView msgListView;
    private EditText input;
    private Button send, input_type,voice;
    private MsgAdapter adapter;
    private List<Msg> msgList = new ArrayList<Msg>();
    // 语音识别对象
    private SpeechRecognizer mAsr;
    private String mEngineType = null;
    // 函数调用返回值
    private int ret = 0;
    private RecognizerDialog mDialog;
    ApkInstaller mInstaller ;
    //识别结果
    private String shibie_result="";
    //输入模式
    private int inputType=1;//默认为1，1是文本模式，2是语音模式

    private RequestQueue requestQueue=null;
    private String httpurl= "http://www.tuling123.com/openapi/api";// 请求的地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue= Volley.newRequestQueue(getApplicationContext());
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=57638605");
        // 初始化识别对象
        mAsr = SpeechRecognizer.createRecognizer(MainActivity.this, mInitListener);
        mEngineType = SpeechConstant.TYPE_CLOUD;
        mDialog = new RecognizerDialog(this, mInitListener);
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ENGINE_TYPE,null);
        // 设置返回结果格式
        mDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        mDialog.setListener(mRecognizerDialogListener);

        adapter = new MsgAdapter(MainActivity.this, R.layout.list_item, msgList);
        input = (EditText) findViewById(R.id.msg_input);
        send = (Button) findViewById(R.id.send);
        input_type = (Button) findViewById(R.id.btn_changeinputtype);
        voice=(Button)findViewById(R.id.btn_voice);
        msgListView = (ListView) findViewById(R.id.listview);
        msgListView.setAdapter(adapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = input.getText().toString();
                Msg msg = new Msg(content, Msg.TYPE_SENT);
                msgList.add(msg);
                adapter.notifyDataSetChanged();//当有新的消息时，listview将刷新数据显示
                msgListView.setSelection(msgList.size());//将listview定位到最后一行，显示最新的消息
                //new HttpPostThread(handler, 1, input.getText().toString()).start();
                //将网络请求放入请求队列中后，volley会自动安排调度，可以放入多个，最多支持？个并发数
                requestQueue.add(HttpVolley(input.getText().toString()));
                input.setText("");//将输入框消息清空
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
            }
        });
        input_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputType==1){
                    input_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.text));
                    input.setVisibility(View.GONE);
                    voice.setVisibility(View.VISIBLE);
                    inputType=2;
                    InputMethodManager inputManager =
                            (InputMethodManager)input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(input.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                else{
                    input_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.voice));
                    voice.setVisibility(View.GONE);
                    input.setVisibility(View.VISIBLE);
                    inputType=1;
                    input.setFocusable(true);
                    input.setFocusableInTouchMode(true);
                    input.requestFocus();
                    InputMethodManager inputManager =
                            (InputMethodManager)input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(input, 0);
                }

              // mDialog.show();
                //mInstaller.install();
               // Uri uri = Uri.parse(SpeechUtility.getUtility().getComponentUrl());
               // Intent it = new Intent(Intent.ACTION_VIEW, uri);
               // startActivity(it);
            }
        });

        input.addTextChangedListener(watcher);//添加编辑框内容变化的监听


    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            //System.out.println("this is onchange……");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            // System.out.println("this is beforechange……");
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            // System.out.println("this is afterchange……");
            if (input.getText().toString().equals("")) {
                send.setBackgroundColor(getResources().getColor(R.color.disable));
                //send.setBackgroundResource(R.drawable.bg_send_disable);
                send.setEnabled(false);
            } else {
                send.setBackgroundColor(getResources().getColor(R.color.enable));
                //send.setBackgroundResource(R.drawable.bg_send);
                send.setEnabled(true);
            }
        }
    };

    private void initMsgs() {
        // Msg msg1=new Msg("Hello",Msg.TYPE_RECEIVED);
        //msgList.add(msg1);
        //Msg msg2=new Msg("22222Hello",Msg.TYPE_SENT);
        // msgList.add(msg2);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println(msg.getData().getString("response"));
                    /**显示服务器返回的消息**/
                    Msg msg2 = new Msg(msg.getData().getString("response"), Msg.TYPE_RECEIVED);
                    msgList.add(msg2);
                    adapter.notifyDataSetChanged();//当有新的消息时，listview将刷新数据显示
                    msgListView.setSelection(msgList.size());//将listview定位到最后一行，显示最新的消息
                    break;
                case 2:
                    String content = msg.getData().getString("text");
                    Msg msg3 = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg3);
                    adapter.notifyDataSetChanged();//当有新的消息时，listview将刷新数据显示
                    msgListView.setSelection(msgList.size());//将listview定位到最后一行，显示最新的消息
                    shibie_result="";
                    //语音识别完成后，立刻发送该消息
                    //new HttpPostThread(handler, 1, msg.getData().getString("text")).start();
                    //将网络请求放入请求队列中后，volley会自动安排调度，可以放入多个，最多支持？个并发数
                   requestQueue.add(HttpVolley(msg.getData().getString("text")));
                    //HttpVolley(msg.getData().getString("text"));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("Log", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                //showTip("初始化失败,错误码："+code);
            }
        }
    };

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public boolean setParam() {
        boolean result = false;
        //设置识别引擎
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        //设置返回结果为json格式
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mAsr.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mAsr.cancel();
        mAsr.destroy();
    }

    /**
     * 识别监听器。
     */

    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (!isLast) {
                String text = results.getResultString();
                shibie_result=shibie_result+text;
            }
            System.out.println("识别结果返回");
            if(isLast){
                System.out.println("识别结果是："+parseResult(shibie_result));
                Message mymsg=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("text",parseResult(shibie_result));
                mymsg.setData(bundle);
                mymsg.what=2;
                handler.sendMessage(mymsg);
            }
        }

        public void onError(SpeechError error) {
            System.out.println("识别发生错误");
        }
    };

     public String parseResult(String json){
         String result="";
         try {
             JSONObject jsonObject = new JSONObject(json);
             JSONArray jsonArray = jsonObject.getJSONArray("ws");
             for (int i = 0; i < jsonArray.length(); i++) {
                 JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                 JSONArray jsonArray2 = jsonObject2.getJSONArray("cw");
                 JSONObject resultObj=jsonArray2.getJSONObject(0);
                 result=result+resultObj.getString("w");

             }
         } catch (Exception e) {
             // TODO: handle exception
             System.out.println("json解析出错，"+e.toString());
         }
         return result;
     }
    //封装一个Post方式的volley请求
    public StringRequest HttpVolley(final String question){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /**对服务器返回的json数据进行解析**/
                        //JSONArray jsonArray = new JSONArray(jsonresult);
                        try {
                            JSONObject res = new JSONObject(response);
                            String answer=res.getString("text");
                            Message msg=new Message();
                            msg.what=1;
                            Bundle da=new Bundle();
                            da.putString("response",answer);
                            msg.setData(da);
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley请求出错：", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", "872d030c8103c53dbaf93be2304c6efb");
                params.put("info", question);
                return params;
            }
        };
        return stringRequest;
    }
}

