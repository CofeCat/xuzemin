package com.android.jdrd.headcontrol.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jdrd.headcontrol.R;
import com.android.jdrd.headcontrol.service.ServerSocketUtil;

public class TestActivity extends Activity {
    private TextView textView;
    private EditText editText;
    private MyReceiver receiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        //启动ServerSocketUtil服务
        Intent intent = new Intent(this, ServerSocketUtil.class);

        startService(intent);
        //注册数据接收广播用来接收socket发来的数据
        registerDateReceiver();
        initView();
    }

    private void initView() {
        editText = (EditText)this.findViewById(R.id.editTextSocket);
        textView = (TextView)this.findViewById(R.id.textViewSocket);
        findViewById(R.id.startSearchPeople).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction("com.jdrd.CursorSDKExample.TD_CAMERA");
                intent.putExtra("msg", "远");
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.buttonSocket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("com.jdrd.CursorSDKExample.TD_CAMERA");
                intent.putExtra("msg", "关闭");
                sendBroadcast(intent);

                /*try {
                    Gson gson = new Gson();
                    Map map  = new LinkedHashMap();
                    map.put("type", "command");
                    map.put("function", "peoplesearch");
                    map.put("data", "");
                    String s = gson.toJson(map);
                    ServerSocketUtil.sendDateToClient(s);

                } catch (IOException e) {
                    e.printStackTrace();
                }*/


                /*Intent intent = new Intent(TestActivity.this, Myservice_Face.class);
                startService(intent);*/
            }
        });
    }

    private void registerDateReceiver() {
        receiver = new MyReceiver();
        filter=new IntentFilter();
        filter.addAction("com.jdrd.fragment.Map");
        registerReceiver(receiver, filter);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String msg = intent.getStringExtra("msg");
//            Constant.debugLog("收到了距离角度： " + msg);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText(msg);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
