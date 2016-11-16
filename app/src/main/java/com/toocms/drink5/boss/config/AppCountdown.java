package com.toocms.drink5.boss.config;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * APP倒计时处理
 *
 * @date 2016/1/22 10:28
 */
public class AppCountdown extends CountDownTimer {

    private TextView textView;
    private static long surplusTime; // 剩余时间
    private static AppCountdown appCountdown;

    public static AppCountdown getInstance() {
        if (appCountdown == null) {
            appCountdown = new AppCountdown();
        }
        return appCountdown;
    }

    private AppCountdown() {
        super(surplusTime > 0 ? surplusTime : 30000, 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        surplusTime = millisUntilFinished;
        textView.setText(millisUntilFinished / 1000 + "s后重新发送");
    }

    @Override
    public void onFinish() {
        surplusTime = 0;
        textView.setText("重新获取");
        textView.setEnabled(true);
    }

    public void play(TextView textView) {
        this.textView = textView;
        if (surplusTime > 0) {
            textView.setEnabled(false);
        } else {
            textView.setText("获取验证码");
            textView.setEnabled(true);
        }
    }

    public void reSet() {
        surplusTime = 0;
        textView.setEnabled(true);
        textView.setText("获取验证码");
        cancel();
    }
}