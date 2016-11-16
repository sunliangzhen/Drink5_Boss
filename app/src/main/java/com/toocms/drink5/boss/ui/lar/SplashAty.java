package com.toocms.drink5.boss.ui.lar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.baidu.mapapi.SDKInitializer;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.MainAty;
import com.toocms.frame.config.Config;
import com.toocms.frame.config.WeApplication;
import com.toocms.frame.tool.AppManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * @date 2016/4/9 12:55
 */
public class SplashAty extends Activity {

    @ViewInject(R.id.linlay_splash)
    private LinearLayout linlay_splash;

    private MyCountDownTimer myCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_splash);
        x.view().inject(this);
        myCountDownTimer = new MyCountDownTimer();
        AppManager.getInstance().addActivity(this);
        if (ActivityCompat.checkSelfPermission(SplashAty.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashAty.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 122);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 128);
        }
        SDKInitializer.initialize(getApplicationContext());
        myCountDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().killActivity(this);
        myCountDownTimer.cancel();
    }

    class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer() {
            super(4000, 1000);
        }

        public void onFinish() {
            if (Config.isLogin()) {
                Intent intent = new Intent(SplashAty.this, staService.class);
                startService(intent);
                Intent intent1 = new Intent(SplashAty.this, MainAty.class);
                startActivity(intent1);
                finish();
            } else {
                if (!PreferencesUtils.getBoolean(SplashAty.this, "FirstG0")) {
                    Intent intent1 = new Intent(SplashAty.this, GuideAty.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent1 = new Intent(SplashAty.this, LarAty.class);
                    startActivity(intent1);
                    finish();
                }
            }
        }

        public void onTick(long millisUntilFinished) {
        }
    }
}
