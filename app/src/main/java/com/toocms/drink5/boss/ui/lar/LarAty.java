package com.toocms.drink5.boss.ui.lar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.MainAty;
import com.toocms.frame.config.Config;

import org.xutils.view.annotation.Event;

/**
 * @author Zero
 * @date 2016/5/13 16:50
 */
public class LarAty extends BaseAty {


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_lar;
    }

    @Override
    protected void initialized() {
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 2;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        if (Config.isLogin()) {
            Intent intent = new Intent(this, staService.class);
            startService(intent);
            startActivity(MainAty.class, null);
            finish();
        }
    }


    @Event(value = {R.id.lar_tv_login, R.id.lar_tv_register})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.lar_tv_login:
                application.setUserInfoItem("cer_healthy2", "");
//                if (Config.isLogin()) {
//                    Intent intent = new Intent(this, staService.class);
//                    startService(intent);
//                    startActivity(MainAty.class, null);
//                    LogUtil.e(application.getUserInfo().get("tel") + "," + application.getUserInfo().get("mobile"));
//                } else {
//                    startActivity(LoginAty.class, null);
//                }
                startActivity(LoginAty.class, null);
                break;
            case R.id.lar_tv_register:
                Bundle bundle = new Bundle();
                bundle.putString("type", "resPass");
                startActivity(RegisterAty.class, bundle);
                break;
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 123:
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
//                    }
//                } else {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 122);
//                    }
//                }
//                break;
//            case 122:
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 122);
//                    }
//                } else {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 128);
//                    }
//                }
//                break;
//            case 128:
//                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 128);
//                    }
//                } else {
//
//                }
//                break;
//        }
//    }
}
