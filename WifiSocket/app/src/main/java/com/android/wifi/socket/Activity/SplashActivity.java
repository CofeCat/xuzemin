package com.android.wifi.socket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.android.wifi.socket.wifisocket.R;

/**
 * Created by Administrator on 2016/8/27.
 */
public class SplashActivity extends BaseActivity{
    private Handler handler;
    public static final int GOTOWELCOME = 1;
    public static final int GOTOFIRST = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.splash_view, null);
        setContentView(view);
        startAnimation(view);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
                        SplashActivity.this.finish();
                        break;
                    case 2:
                        startActivity(new Intent(SplashActivity.this,MainActivity.class));
                        SplashActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }
        };

    }
    private void startAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                int newVersion = chkVersion();
                if (newVersion == SplashActivity.GOTOWELCOME) {
                    handler.sendEmptyMessage(GOTOWELCOME);
                    return;
                }else{
                    handler.sendEmptyMessage(GOTOFIRST);
                }
            }
        });
    }

}