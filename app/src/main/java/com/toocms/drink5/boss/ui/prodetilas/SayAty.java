package com.toocms.drink5.boss.ui.prodetilas;

import android.os.Bundle;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;

import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/6/30 15:40
 */
public class SayAty extends BaseAty {

    @ViewInject(R.id.bmapView)
    private TextureMapView mMapView;

    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private double lat;
    private double lon;
    private double c_lat;
    private double c_lon;
    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.che);

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_say;
    }

    @Override
    protected void initialized() {
        lat = Double.parseDouble(getIntent().getStringExtra("lat"));
        lon = Double.parseDouble(getIntent().getStringExtra("lon"));
        c_lat = Double.parseDouble(getIntent().getStringExtra("c_lat"));
        c_lon = Double.parseDouble(getIntent().getStringExtra("c_lon"));
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);

        // 地图初始化
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层

        MarkerOptions ooA = new MarkerOptions().position(new LatLng(lat, lon)).icon(bdA)
                .zIndex(9).draggable(true);
        Marker marker = (Marker) (mBaiduMap.addOverlay(ooA));
        MarkerOptions ooA2 = new MarkerOptions().position(new LatLng(c_lat, c_lon)).icon(bdA)
                .zIndex(9).draggable(true);
        Marker marker2 = (Marker) (mBaiduMap.addOverlay(ooA));

        LatLng latLng = new LatLng(lat, lon);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(12.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }
}
