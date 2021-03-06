package com.android.jdrd.headcontrol.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jdrd.headcontrol.R;
import com.android.jdrd.headcontrol.dialog.FaceDialog;
import com.android.jdrd.headcontrol.fragment.BatteryFragment;
import com.android.jdrd.headcontrol.fragment.CleanFragment;
import com.android.jdrd.headcontrol.fragment.EaiFragment;
import com.android.jdrd.headcontrol.fragment.MapFragment;
import com.android.jdrd.headcontrol.service.EthernetStaticIPService;
import com.android.jdrd.headcontrol.service.ServerSocketUtil;
import com.android.jdrd.headcontrol.service.SetStaticIPService;
import com.android.jdrd.headcontrol.util.Constant;
import com.eaibot.constants.BroadcastFilter;
import com.eaibot.listener.OnTaskListener;
import com.eaibot.publisher.DashgoPublisher;
import com.eaibot.ros.RosClient;
import com.eaibot.ros.RosService;
import com.eaibot.utils.PathgoUtil;
import org.ros.node.NodeMain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * http://www.cnblogs.com/smyhvae/p/3983234.html
 * Created by Administrator on 2016/10/23 0023.
 */

public class WelcomeActivity extends Activity implements Animation.AnimationListener{

    RelativeLayout mRelativeLayout_Exit;//设置栏
    ImageView mImageView_Exit;//设置栏中的返回键
    TextView mTextView_Exit;//“设置”
    ImageView mImageView_Battery;//电源图标
    ImageView mImageView_Clean;//清洁图标
    ImageView mImageView_Map;//电源图标
    private RosService rosService;
    private NodeMain dashgoPublisher;
    ImageView mImageView_setting;//设置
    private static TimerTask task_outTime;
    private static Timer timer_outTime;

    private RelativeLayout rl_TitleList;
    boolean flag = false;
    private View fragment;
    private ImageView imgViewBtnLift;
    MyClickListener mMyClickListener;
    List<Fragment> list;
    private boolean isConnnect = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what =='\ud903'){
                PathgoUtil.navigationByAppStart(Constant.ROS_IP,"GET",handler,1010);

            }else {
                switch (msg.what) {
                    case 0:
                        FaceDialog.getDialog(WelcomeActivity.this, handler).show();
//                    if(FaceDialog.Current_Type ==0){
//                        FaceDialog.Current_Type = 1;
//                        FaceDialog.setAnimationDrawable();
//                    }else {
//                        FaceDialog.Current_Type = 0;
//                        FaceDialog.setAnimationDrawable();
//                    }
//                    resetTimer2();
                        break;
                    case 1:
//                    if(FaceDialog.Current_Type ==0){
//                        FaceDialog.Current_Type = 1;
//                        FaceDialog.setAnimationDrawable();
//                    }else {
//                        FaceDialog.Current_Type = 0;
//                        FaceDialog.setAnimationDrawable();
//                    }
                        break;
                    case 3:
                        resetTimer();
                        break;
                    case 1010:
                        connectRos();
                        break;
                }
                super.handleMessage(msg);
            }
        }
    };
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.debugLog(Constant.CURRENTINDEX+"CURRENTINDEX");
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //启动后台通讯服务
        Intent serverSocket = new Intent(this, ServerSocketUtil.class);
        startService(serverSocket);
//        //启动静态IP设置服务
//        Intent SetStaticIPService = new Intent(this, SetStaticIPService.class);
//        startService(SetStaticIPService);
//        //启动静态IP设置服务(有线连接)
//        Intent EthernetStaticIPService = new Intent(this, EthernetStaticIPService.class);
//        startService(EthernetStaticIPService);

//        启动socket测试Activity
        Intent testActivity = new Intent(this, TestActivity.class);
        startActivity(testActivity);

        list = new ArrayList<>();
        BatteryFragment batteryFragment = new BatteryFragment(WelcomeActivity.this);
        CleanFragment cleanFragment = new CleanFragment(WelcomeActivity.this);
        MapFragment mapFragment = new MapFragment(WelcomeActivity.this);
        EaiFragment eaiFragment = new EaiFragment(WelcomeActivity.this);
        list.add(batteryFragment);
        list.add(cleanFragment);
//        list.add(mapFragment);
        list.add(eaiFragment);

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(BroadcastFilter.ROS_SHUTDOWN_FINISHED);
        intentfilter.addAction(BroadcastFilter.ROS_INIT_FINISHED);
        registerReceiver(rosServiceBroadcastRceiver,intentfilter);

//        mImageView_Map.setClickable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
//        rosService.shutdownRosService();
//        unregisterReceiver(rosServiceBroadcastRceiver);
//        mImageView_Map.setClickable(false);
        super.onDestroy();
    }

    public void connectRos(){
        new RosClient(this, Constant.ROS_IP, new OnTaskListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"连接成功",Toast.LENGTH_SHORT).show();
                rosService = RosService.getRosServiceInstance(WelcomeActivity.this);
                dashgoPublisher = new DashgoPublisher(true);
                rosService.addRosNode(dashgoPublisher);
            }

            @Override
            public void onFailure(Exception e) {
                connectRos();
//                mImageView_Map.setClickable(false);
            }
        });
    }
    private BroadcastReceiver rosServiceBroadcastRceiver =new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BroadcastFilter.ROS_SHUTDOWN_FINISHED)){
                if(intent.getBooleanExtra("rosShutdownFinished" , false)){
                    //ROS关闭处理
                    Toast.makeText(context,"广播"+intent.getAction(),Toast.LENGTH_SHORT).show();
                    connectRos();
//                    mImageView_Map.setClickable(false);
                    isConnnect = false;
                }
            }else if(intent.getAction().equals(BroadcastFilter.ROS_INIT_FINISHED)){
                Toast.makeText(context,"初始化"+intent.getBooleanExtra("rosInitFinished" , false),Toast.LENGTH_SHORT).show();
                if(intent.getBooleanExtra("rosInitFinished" , false)){
                    mImageView_Map.setClickable(true);
                    isConnnect = true;
//                    rosService.addRosNode(dashgoPublisher,"eaibot/dashgo_activity");
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Constant.debugLog(Constant.CURRENTINDEX+"onResumeCURRENTINDEX");
            switch (Constant.CURRENTINDEX){
                case 0:
                    initData_battrey();
                    break;
                case 1:
                    initData_clean();
                    break;
                case 2:
                    initData_map();
                    break;
                default:
                    break;
            }
        }
        initEvent();
        if(!isConnnect){
            PathgoUtil.navigationByAppStart(Constant.ROS_IP,"GET",handler,1010);
            mImageView_Map.setClickable(false);
        }else{
            mImageView_Map.setClickable(true);
        }

    }

    private void initView() {
        mRelativeLayout_Exit = (RelativeLayout) findViewById(R.id.rl_Exit);
        mImageView_Exit = (ImageView) findViewById(R.id.iv_Exit);
        mTextView_Exit = (TextView) findViewById(R.id.tv_Exit);
        mImageView_Exit = (ImageView) findViewById(R.id.iv_Exit);
        mImageView_Battery = (ImageView) findViewById(R.id.iv_Battery);
        mImageView_Clean = (ImageView) findViewById(R.id.iv_Clean);
        mImageView_Map = (ImageView) findViewById(R.id.iv_Map);
        mImageView_setting = (ImageView) findViewById(R.id.iv_settings);

        fragment=findViewById(R.id.fragment);
        rl_TitleList = (RelativeLayout) findViewById(R.id.rl_TitleList);
        imgViewBtnLift = (ImageView) findViewById(R.id.imgViewBtnLift);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initData_battrey() {
        mImageView_Battery.setImageResource(R.mipmap.dianyuan_pre);
        FragmentManager fragmentManager_battery = getFragmentManager();
        FragmentTransaction transaction_battery = fragmentManager_battery.beginTransaction();
        transaction_battery.replace(R.id.ll_right, list.get(0), "batteryFragment");
        transaction_battery.commit();
        mMyClickListener = new MyClickListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initData_clean() {

        mImageView_Clean.setImageResource(R.mipmap.qingjie_pre);
        FragmentManager fragmentManager_battery = getFragmentManager();
        FragmentTransaction transaction_battery = fragmentManager_battery.beginTransaction();
        transaction_battery.replace(R.id.ll_right, list.get(1), "cleanFragment");
        transaction_battery.commit();
        mMyClickListener = new MyClickListener();
    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
//    private void initData_map() {
//        mImageView_Map.setImageResource(R.mipmap.ditu_pre);
//        FragmentManager fragmentManager_battery = getFragmentManager();
//        FragmentTransaction transaction_battery = fragmentManager_battery.beginTransaction();
//        transaction_battery.replace(R.id.ll_right, list.get(2), "mapFragment");
//        transaction_battery.commit();
//        mMyClickListener = new MyClickListener();
//    }

    private void initData_map() {
        mImageView_Map.setImageResource(R.mipmap.ditu_pre);
        FragmentManager fragmentManager_battery = getFragmentManager();
        FragmentTransaction transaction_battery = fragmentManager_battery.beginTransaction();
        transaction_battery.replace(R.id.ll_right, list.get(2), "eaiFragment");
        transaction_battery.commit();
        mMyClickListener = new MyClickListener();
    }


    private void initEvent() {
        mImageView_Battery.setOnClickListener(mMyClickListener);
        mImageView_Clean.setOnClickListener(mMyClickListener);
        mImageView_Map.setOnClickListener(mMyClickListener);
        mImageView_setting.setOnClickListener(mMyClickListener);
        rl_TitleList.setOnClickListener(mMyClickListener);
    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        //监听屏幕是否处于在点击状态，若被点击则发送广播到小屏'
        Intent intent=new Intent("com.jiadu.broadcast.setting.touch");
        sendBroadcast(intent);
        return super.onTouchEvent(event);
    }*/

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        LogUtils.log("点击发送广播");
//        //监听屏幕是否处于在点击状态，若被点击则发送广播到小屏'
////        Intent intent = new Intent("com.jiadu.broadcast.setting.touch");
////        sendBroadcast(intent);
//        if(!FaceDialog.getDialog(this,handler).isShowing()){
//            resetTimer();
//        }
//        return super.dispatchTouchEvent(ev);
//    }
    //动画需实现的接口
    @Override
    public void onAnimationStart(Animation animation) {

    }
    //  开启动画时 是否显示隐藏 做标记
    @Override
    public void onAnimationEnd(Animation animation) {
        rl_TitleList.clearAnimation();
        if (flag){
            flag = false;
            imgViewBtnLift.setImageResource(R.mipmap.zuo_yc);
        }else {
            flag = true;
            fragment.setVisibility(View.GONE);
            imgViewBtnLift.setImageResource(R.mipmap.zuo_xs);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public class MyClickListener implements View.OnClickListener {
        @SuppressLint("NewApi")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_Exit:
                case R.id.tv_Exit:
                case R.id.iv_Exit:
                    WelcomeActivity.this.finish();
                    break;
                //点击了电源栏
                case R.id.iv_Battery:
//                    startAnimation();
                    changeBattery();
                    break;
                case R.id.iv_Clean:
//                    startAnimation();
                    changeClean();
                    break;
                case R.id.iv_Map:
//                    startAnimation();
                    changeMap();
                    break;
                case R.id.rl_TitleList:
                    startAnimation();
                    break;
                case R.id.iv_settings:
                    startAnimation();
                    break;
            }
        }
    }

    private void setClickable() {
//        mImageView_Map.setEnabled(true);
        mImageView_Clean.setEnabled(true);
        mImageView_Battery.setEnabled(true);
        mImageView_setting.setEnabled(true);
    }

    private void setBackgroundColor() {
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        //电源栏
        mImageView_Battery.setImageResource(R.mipmap.dianyuan_no);
        //清扫栏
        mImageView_Clean.setImageResource(R.mipmap.qingjie_no);
        //地图栏
        mImageView_Map.setImageResource(R.mipmap.ditu_no);
        //设置
        mImageView_setting.setImageResource(R.mipmap.shezhi_no);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void changeBattery() {
        BatteryFragment.flag = false;
        Constant.CURRENTINDEX  = 0;
        setClickable();
        setBackgroundColor();
        //电源栏
        mImageView_Battery.setImageResource(R.mipmap.dianyuan_pre);
        FragmentManager fragmentManager_battery = getFragmentManager();
        FragmentTransaction transaction_battery = fragmentManager_battery.beginTransaction();
        transaction_battery.replace(R.id.ll_right, list.get(0), "batteryFragment");
        transaction_battery.commit();
        mImageView_Battery.setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void changeClean() {
        CleanFragment.flag_rr_bar = false;
        Constant.CURRENTINDEX  = 1;
        setClickable();
        setBackgroundColor();
        mImageView_Clean.setImageResource(R.mipmap.qingjie_pre);
        FragmentManager fragmentManager_Clean = getFragmentManager();
        FragmentTransaction transaction_clean = fragmentManager_Clean.beginTransaction();
        transaction_clean.replace(R.id.ll_right, list.get(1), "cleanFragment");
        transaction_clean.commit();
        mImageView_Clean.setEnabled(false);
    }

    //    private void changeMap() {
//        Constant.CURRENTINDEX = 2;
//        MapFragment.IsRight = false;
//        setClickable();
//        setBackgroundColor();
//        mImageView_Map.setImageResource(R.mipmap.ditu_pre);
//        FragmentManager fragmentManager_Map = getFragmentManager();
//        FragmentTransaction transaction_map = fragmentManager_Map.beginTransaction();
//        transaction_map.replace(R.id.ll_right, list.get(2), "mapFragment");
//        transaction_map.commit();
//        mImageView_Map.setEnabled(false);
//    }
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void changeMap() {
        Constant.CURRENTINDEX = 2;
//        MapFragment.IsRight = false;
        setClickable();
        setBackgroundColor();
        mImageView_Map.setImageResource(R.mipmap.ditu_pre);
        FragmentManager fragmentManager_Map = getFragmentManager();
        FragmentTransaction transaction_map = fragmentManager_Map.beginTransaction();
        transaction_map.replace(R.id.ll_right, list.get(2), "eaiFragment");
        transaction_map.commit();
//        mImageView_Map.setEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK||keyCode == KeyEvent.KEYCODE_MENU||super.onKeyDown(keyCode, event);
    }
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    //左边动画
    private void startAnimation(){
        if (flag){
            fragment.setVisibility(View.VISIBLE);
            TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE,-fragment.getWidth(),
                    Animation.ABSOLUTE,0.0f,
                    Animation.ABSOLUTE,0.0f,
                    Animation.ABSOLUTE,0.0f
            );
            translate.setDuration(500);//动画时间500毫秒
            translate.setFillAfter(true);//动画出来控件可以点击
            translate.setAnimationListener(WelcomeActivity.this);
            rl_TitleList.startAnimation(translate);//开始动画
        }else {
            TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE,0.0f,
                    Animation.ABSOLUTE,-fragment.getWidth(),
                    Animation.ABSOLUTE,0.0f,
                    Animation.ABSOLUTE,0.0f);
            translate.setDuration(500);
            translate.setFillAfter(false);//设置动画结束后控件不可点击
            translate.setAnimationListener(WelcomeActivity.this);
            rl_TitleList.startAnimation(translate);
        }
    }


    public  void resetTimer(){
        if (task_outTime != null){
            task_outTime.cancel();  //将原任务从队列中移除
        }
        timer_outTime = new Timer();
        task_outTime = new TimerTask() {
            public void run () {
                handler.sendEmptyMessage(0);
            }
        };
        timer_outTime.schedule(task_outTime, 30 * 60 * 1000);
    }
//    public  void resetTimer2(){
//        if (task_outTime != null){
//            task_outTime.cancel();  //将原任务从队列中移除
//        }
//        timer_outTime = new Timer();
//        task_outTime = new TimerTask() {
//            public void run () {
//                handler.sendEmptyMessage(1);
//            }
//        };
//        timer_outTime.schedule(task_outTime, 5 * 1000);
//    }

    @Override
    protected void onPause() {
        if(MapFragment.thread !=null){
            if(MapFragment.thread.isAlive()){
                MapFragment.thread = new Thread();
            }
        }
        super.onPause();
    }

}
