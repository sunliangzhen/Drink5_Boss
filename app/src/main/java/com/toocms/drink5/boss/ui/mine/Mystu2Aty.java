package com.toocms.drink5.boss.ui.mine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Courier;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/6/7 16:36
 */
public class Mystu2Aty extends BaseAty {

    @ViewInject(R.id.stu_mapView)
    private MapView mMapView;

    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.che);
    private Intent intent;
    private List<Marker> list2;
    private List<LatLng> list;
    private Alarmreceiver alarmreceiver;
    private Courier courier;
    private boolean isFirst = true;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_mystu2;
    }

    @Override
    protected void initialized() {
        list = new ArrayList<>();
        courier = new Courier();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        courier.index(application.getUserInfo().get("site_id"), 1, this);

    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("Courier/index")) {
            maps = JSONUtils.parseDataToMapList(result);
            if (isFirst) {
                isFirst = false;
                for (int i = 0; i < maps.size(); i++) {
                    double lat = Double.parseDouble(maps.get(i).get("lat"));
                    double lon = Double.parseDouble(maps.get(i).get("lon"));
                    LatLng latLng = new LatLng(lat, lon);
                    list.add(latLng);
                }
                initOverlay();
                ada();
            } else {
                for (int i = 0; i < maps.size(); i++) {
                    double lat = Double.parseDouble(maps.get(i).get("lat"));
                    double lon = Double.parseDouble(maps.get(i).get("lon"));
                    LatLng llNew = new LatLng(lat, lon);
                    list2.get(i).setPosition(llNew);
                }
            }
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
        if (alarmreceiver != null) {
            unregisterReceiver(alarmreceiver);
        }
    }
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("我的水工");
        // 地图初始化
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(1500).build()));
        mBaiduMap.setMyLocationEnabled(true);
        registerBroadCast();
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        mBaiduMap.setMaxAndMinZoomLevel(20, 20);
//        mBaiduMap.setMyLocationEnabled(false);
        // 定位初始化
//        mLocClient = new LocationClient(this);
//        mLocClient.registerLocationListener(myListener);
//        LocationClientOption option = new LocationClientOption();
//        option.setIsNeedAddress(true);
//        option.setOpenGps(true); // 打开gps
//        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(1000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                View view = View.inflate(Mystu2Aty.this, R.layout.map_stu, null);
                ImageView imgv_phone = (ImageView) view.findViewById(R.id.mapstu_imgv_phone);
                ImageView imgv_head = (ImageView) view.findViewById(R.id.mapstu_imgv_head);
                TextView tv_name = (TextView) view.findViewById(R.id.mapstu_tv_name);
                TextView tv_tong = (TextView) view.findViewById(R.id.mapstu_tv_tong);
                TextView tv_piao = (TextView) view.findViewById(R.id.mapstu_tv_piao);
                TextView tv_hu = (TextView) view.findViewById(R.id.mapstu_tv_hu);
                imgv_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(phone)) {
                            Mystu2Aty.this.callPhone(phone);
                        }
                    }
                });
                for (int i = 0; i < list2.size(); i++) {
                    if (marker == list2.get(i)) {
                        tv_name.setText(maps.get(i).get("nickname"));
                        tv_tong.setText(maps.get(i).get("curr_order"));
                        tv_piao.setText(maps.get(i).get("curr_ticket"));
                        tv_hu.setText(maps.get(i).get("curr_member"));
                        phone = maps.get(i).get("account");
                        imageLoader.disPlay(imgv_head, maps.get(i).get("head"));
                        LatLng ll = marker.getPosition();
                        InfoWindow mInfoWindow = new InfoWindow(view, ll, -47);
                        mBaiduMap.showInfoWindow(mInfoWindow);

                        LatLng latLng = list.get(i);
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.target(latLng).zoom(18.0f);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    }
                }
                return true;
            }
        });
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {

                return false;
            }
        });
    }
    @Event(value = {R.id.mystu2_btn_all})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.mystu2_btn_all:
                if(list!=null){
                    if(list.size()>0){
                        LatLng latLng = list.get(0);
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.target(latLng).zoom(7.0f);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                    }
                }
                break;
        }
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


    public void initOverlay() {
        // add marker overlay
        LatLng latLng = list.get(0);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(7.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions ooA = new MarkerOptions().position(list.get(i)).icon(bdA)
                    .zIndex(9).draggable(true);
            mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
            list2.add(mMarkerA);
        }

    }

    private void registerBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("sun");
        alarmreceiver = new Alarmreceiver();
        registerReceiver(alarmreceiver, filter);
    }

    private void ada() {
        LogUtil.e("ppppppppppppp");
        Intent intent = new Intent();
        intent.setAction("sun");
        PendingIntent sender = PendingIntent.getBroadcast(Mystu2Aty.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) Mystu2Aty.this.getSystemService(ALARM_SERVICE);
        //5秒一个周期，不停的发送广播
        am.cancel(sender);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3000, sender);
    }

    private class Alarmreceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            for (int i = 0; i < list.size(); i++) {
//                LatLng position = list2.get(i).getPosition();
//                LatLng llNew = new LatLng(position.latitude + 0.005,
//                        position.longitude + 0.005);
//                list2.get(i).setPosition(llNew);
//            }
            courier.index(application.getUserInfo().get("site_id"), 1, Mystu2Aty.this);

//            LatLng ll = list2.get(0).getPosition();
//            LatLng llNew = new LatLng(ll.latitude + 0.005,
//                    ll.longitude + 0.005);
//            list2.get(0).setPosition(llNew);
//
//            LatLng llNew2 = new LatLng(list2.get(1).getPosition().latitude - 0.005,
//                    list2.get(1).getPosition().longitude - 0.005);
//            list2.get(1).setPosition(llNew2);
//
//            LatLng llNew3 = new LatLng(list2.get(2).getPosition().latitude - 0.005,
//                    list2.get(2).getPosition().longitude + 0.005);
//            list2.get(2).setPosition(llNew3);
//
//            LatLng llNew4 = new LatLng(list2.get(3).getPosition().latitude + 0.005,
//                    list2.get(3).getPosition().longitude - 0.005);
//            list2.get(3).setPosition(llNew4);
//            courier.index(application.getUserInfo().get("site_id"), 1, this);
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(20.0f);

                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

}
