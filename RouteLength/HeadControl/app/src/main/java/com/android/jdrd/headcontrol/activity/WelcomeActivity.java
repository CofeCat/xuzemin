package com.android.jdrd.headcontrol.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import android.util.Log;

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

import com.android.jdrd.headcontrol.R;
import com.android.jdrd.headcontrol.fragment.BatteryFragment;
import com.android.jdrd.headcontrol.fragment.CleanFragment;
import com.android.jdrd.headcontrol.fragment.MapFragment;
import com.android.jdrd.headcontrol.service.ServerSocketUtil;
import com.android.jdrd.headcontrol.service.SetStaticIPService;
import com.android.jdrd.headcontrol.util.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * http://www.cnblogs.com/smyhvae/p/3983234.html
 * Created by Administrator on 2016/10/23 0023.
 */

public class WelcomeActivity extends Activity implements Animation.AnimationListener{

    public boolean IsMap = false;
    public static boolean IsClean = false;
    RelativeLayout mRelativeLayout_Exit;//设置栏
    ImageView mImageView_Exit;//设置栏中的返回键
    TextView mTextView_Exit;//“设置”

    ImageView mImageView_Battery;//电源图标

    ImageView mImageView_Clean;//清洁图标

    ImageView mImageView_Map;//电源图标


    private RelativeLayout rl_TitleList;
    boolean flag = false;
    private View fragment;
    private ImageView imgViewBtnLift;

    MyClickListener mMyClickListener;
    List<Fragment> list;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    MapFragment.Istouch = false;
                    MapFragment.Isplan = false;
                    if(IsClean){
                        changeClean();
                    }else{
                        changeBattery();
                        IsMap = false;
                    }
                    break;
                case 2:
                    IsMap = true;
                    //do nothing
                    break;
                case 3:

                    break;
                case 4:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        //启动后台通讯服务
        Intent serverSocket = new Intent(this, ServerSocketUtil.class);
        startService(serverSocket);
        //启动socket测试Activity
        /*Intent testActivity = new Intent(this, TestActivity.class);
        startActivity(testActivity);*/
        //启动静态IP设置服务
        Intent SetStaticIPService = new Intent(this, SetStaticIPService.class);
        startService(SetStaticIPService);

        list = new ArrayList<>();
        BatteryFragment batteryFragment = new BatteryFragment(WelcomeActivity.this);
        CleanFragment cleanFragment = new CleanFragment(WelcomeActivity.this);
        MapFragment mapFragment = new MapFragment(WelcomeActivity.this);
        list.add(batteryFragment);
        list.add(cleanFragment);
        list.add(mapFragment);

//        this.getWindow().getDecorView().setSystemUiVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            initData();
        }
        initEvent();
    }

    private void initView() {

        mRelativeLayout_Exit = (RelativeLayout) findViewById(R.id.rl_Exit);
        mImageView_Exit = (ImageView) findViewById(R.id.iv_Exit);
        mTextView_Exit = (TextView) findViewById(R.id.tv_Exit);

        mImageView_Exit = (ImageView) findViewById(R.id.iv_Exit);

        mImageView_Battery = (ImageView) findViewById(R.id.iv_Battery);

        mImageView_Clean = (ImageView) findViewById(R.id.iv_Clean);

        mImageView_Map = (ImageView) findViewById(R.id.iv_Map);

        fragment=findViewById(R.id.fragment);
        rl_TitleList = (RelativeLayout) findViewById(R.id.rl_TitleList);
        imgViewBtnLift = (ImageView) findViewById(R.id.imgViewBtnLift);


    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void initData() {
        FragmentManager fragmentManager_battery = getFragmentManager();
        FragmentTransaction transaction_battery = fragmentManager_battery.beginTransaction();
        transaction_battery.replace(R.id.ll_right, list.get(0), "batteryFragment");
        transaction_battery.commit();
        mMyClickListener = new MyClickListener();
    }


    private void initEvent() {

        mImageView_Battery.setOnClickListener(mMyClickListener);
        mImageView_Clean.setOnClickListener(mMyClickListener);
        mImageView_Map.setOnClickListener(mMyClickListener);

        rl_TitleList.setOnClickListener(mMyClickListener);

    }


    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        //监听屏幕是否处于在点击状态，若被点击则发送广播到小屏'
        Intent intent=new Intent("com.jiadu.broadcast.setting.touch");
        sendBroadcast(intent);
        return super.onTouchEvent(event);
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogUtils.log("点击发送广播");
        //监听屏幕是否处于在点击状态，若被点击则发送广播到小屏'
        Intent intent = new Intent("com.jiadu.broadcast.setting.touch");
        sendBroadcast(intent);
        return super.dispatchTouchEvent(ev);
    }
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
                    IsClean = false;
//                    if(IsMap){
//                        if(MapFragment.Istouch || MapFragment.Isplan){
//                            Constant.getConstant().showWarntext(WelcomeActivity.this,handler);
//                        }else{
                            changeBattery();
                            IsMap = false;
//                        }
//                    }else{
//                        changeBattery();
//                        IsMap = false;
//                    }
                    break;
                case R.id.iv_Clean:
//                    IsClean = true;
//                    if(IsMap){
//                        if(MapFragment.Istouch || MapFragment.Isplan){
//                            Constant.getConstant().showWarntext(WelcomeActivity.this,handler);
//                        }else{
                            changeClean();
                            IsMap = false;
//                        }
//                    }else{
//                        changeClean();
//                        IsMap = false;
//                    }
                    break;
                case R.id.iv_Map:
                    if(!IsMap){
                        IsMap = true;
                        changeMap();
                    }
                    break;
                case R.id.rl_TitleList:
                    startAnimation();
                    break;



            }
        }
    }

    private void setClickable() {
        mImageView_Map.setEnabled(true);
        mImageView_Clean.setEnabled(true);
        mImageView_Battery.setEnabled(true);

    }

    private void setBackgroundColor() {
        //电源栏
        mImageView_Battery.setImageResource(R.mipmap.dianyuan_no);
        //清扫栏
        mImageView_Clean.setImageResource(R.mipmap.qingjie_no);
        //地图栏
        mImageView_Map.setImageResource(R.mipmap.ditu_no);
    }

    private void changeBattery() {
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

    private void changeClean() {
        setClickable();
        setBackgroundColor();
        mImageView_Clean.setImageResource(R.mipmap.qingjie_pre);
        FragmentManager fragmentManager_Clean = getFragmentManager();
        FragmentTransaction transaction_clean = fragmentManager_Clean.beginTransaction();
        transaction_clean.replace(R.id.ll_right, list.get(1), "cleanFragment");
        transaction_clean.commit();
        mImageView_Clean.setEnabled(false);
    }

    private void changeMap() {
        setClickable();
        setBackgroundColor();
        mImageView_Map.setImageResource(R.mipmap.ditu_pre);
        FragmentManager fragmentManager_Map = getFragmentManager();
        FragmentTransaction transaction_map = fragmentManager_Map.beginTransaction();
        transaction_map.replace(R.id.ll_right, list.get(2), "mapFragment");
        transaction_map.commit();
        mImageView_Map.setEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_MENU){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION);
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




}
