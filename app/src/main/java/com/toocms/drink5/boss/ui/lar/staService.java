package com.toocms.drink5.boss.ui.lar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.frame.web.ApiListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.Map;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/6/8 17:33
 */
public class staService extends Service {

    private Alarmreceiver alarmreceiver;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private int kk = 0;
    private Courier2 courier;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        courier = new Courier2();
        PreferencesUtils.putString(staService.this, "city", "定位中");
        registerBroadCast();
        ada();
        startLocation();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alarmreceiver != null) {
            unregisterReceiver(alarmreceiver);
        }
    }

    private void registerBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("sun2");
        alarmreceiver = new Alarmreceiver();
        registerReceiver(alarmreceiver, filter);
    }

    private void ada() {
        Intent intent = new Intent();
        intent.setAction("sun2");
        PendingIntent sender = PendingIntent.getBroadcast(staService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) staService.this.getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.cancel(sender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 3, sender);
    }

    private class Alarmreceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (kk == 0) {
                kk = 1;
                courier.changePos(PreferencesUtils.getString(staService.this, "c_id"), PreferencesUtils.getString(staService.this, "longitude"),
                        PreferencesUtils.getString(staService.this, "latitude"), new ApiListener() {
                            @Override
                            public void onCancelled(Callback.CancelledException e) {
                                kk = 0;
                            }

                            @Override
                            public void onComplete(RequestParams requestParams, String s) {
                                kk = 0;
                            }

                            @Override
                            public void onError(Map<String, String> map) {
                                kk = 0;
                            }

                            @Override
                            public void onException(Throwable throwable) {
                                kk = 0;
                            }
                        });
            }
        }
    }

    public void startLocation() {
        // 定位初始化
        mLocClient = new LocationClient(staService.this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.disableCache(true);// 禁止启用缓存定位
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            Address address = location.getAddress();

            PreferencesUtils.putString(staService.this, "latitude", location.getLatitude() + "");
            PreferencesUtils.putString(staService.this, "longitude", location.getLongitude() + "");
            PreferencesUtils.putString(staService.this, "city", address.city);
            PreferencesUtils.putString(staService.this, "district", address.district);
            PreferencesUtils.putString(staService.this, "province", address.province);
//            LogUtil.e("province" + address.province + "city" + address.city + "district" + address.district);
        }
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }


}
