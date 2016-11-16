package com.toocms.drink5.boss.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import com.toocms.drink5.boss.R;
import com.toocms.frame.ui.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.zero.android.common.util.StatusBarUtil;

/**
 * 所有Activity的父类，实现一些仅对该项目的方法
 *
 * @author Zero
 * @date 2016/4/20 11:21
 */
public abstract class BaseAty extends BaseActivity {

    public int isBule = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBule == 0) {
            changeStatusBar();
        } else if (isBule == 1) {
            changeStatusBar2();
        } else if (isBule == 2) {
            changeStatusBar3();
        }
    }

    // 更改状态栏颜色、文字颜色
    private void changeStatusBar() {
        if (Build.MANUFACTURER.equals("Xiaomi") || Build.MANUFACTURER.equals("Meizu") || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            StatusBarUtil.setColor(this, getResources().getColor(R.color.baise), 0);
        else
            StatusBarUtil.setColor(this, getResources().getColor(R.color.baise), 0);
        if (Build.MANUFACTURER.equals("Xiaomi"))
            setMiuiStatusBarDarkMode(true);
        if (Build.MANUFACTURER.equals("Meizu"))
            setMeizuStatusBarDarkIcon(true);
    }

    // 更改状态栏颜色、文字颜色
    public void changeStatusBar2() {
        if (Build.MANUFACTURER.equals("Xiaomi") || Build.MANUFACTURER.equals("Meizu") || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            StatusBarUtil.setColor(this, getResources().getColor(R.color.action_bg2), 0);
        else
            StatusBarUtil.setColor(this, getResources().getColor(R.color.action_bg2), 0);
        if (Build.MANUFACTURER.equals("Xiaomi"))
            setMiuiStatusBarDarkMode(true);
        if (Build.MANUFACTURER.equals("Meizu"))
            setMeizuStatusBarDarkIcon(true);
    }

    // 更改状态栏颜色、文字颜色
    public void changeStatusBar3() {
        if (Build.MANUFACTURER.equals("Xiaomi") || Build.MANUFACTURER.equals("Meizu") || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            StatusBarUtil.setColor(this, getResources().getColor(R.color.action_bg3), 0);
        else
            StatusBarUtil.setColor(this, getResources().getColor(R.color.action_bg3), 0);
        if (Build.MANUFACTURER.equals("Xiaomi"))
            setMiuiStatusBarDarkMode(true);
        if (Build.MANUFACTURER.equals("Meizu"))
            setMeizuStatusBarDarkIcon(true);
    }

    private boolean setMiuiStatusBarDarkMode(boolean darkmode) {
        Class<? extends Window> clazz = getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setMeizuStatusBarDarkIcon(boolean dark) {
        try {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) value |= bit;
            else value &= ~bit;
            meizuFlags.setInt(lp, value);
            getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Intent intent;

    public void callPhone(String phone) {
        intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(BaseAty.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseAty.this, new String[]{Manifest.permission.CALL_PHONE}, 123);
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
