package com.toocms.drink5.boss.ui.lar;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.BaiduLBS;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.listener.LocationListener;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.util.PreferencesUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/31 11:06
 */
public class LocaAty extends BaseAty implements LocationListener, BaiduMap.OnMapTouchListener, OnLoadMoreListener, OnRefreshListener {

    @ViewInject(R.id.bmapView)
    private MapView mMapView;
    @ViewInject(R.id.loca_lv)
    private SwipeToLoadRecyclerView loca_lv;
    @ViewInject(R.id.location_center)
    private ImageView imgvCenter;
    @ViewInject(R.id.location_position)
    private ImageView imgvPosition;
    @ViewInject(R.id.loca_edit_search)
    private TextView edit_search;

    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    private List<PoiInfo> poiInfos; // 检索到的POI信息
    private MyAdapter adapter;
    private float startx;
    private float starty;
    private float displacement; // 位移距离
    private LatLng movedLatLng; // 移动地图后的坐标
    private Animation animation;
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private String city;
    private int isFirst = 0;
    private BaiduLBS baiduLBS;
    private String location2 = "";
    private int index = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_loca;
    }

    @Override
    protected void initialized() {
        poiInfos = new ArrayList<>();
        adapter = new MyAdapter();
        // 初始化动画
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_location_position);
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        baiduLBS = new BaiduLBS();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        loca_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                index = i;
                showProgressDialog();
                baiduLBS.search2(location2, LocaAty.this);
//                Intent intent = getIntent();
//                intent.putExtra("poiInfo", poiInfos.get(i));
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });
        loca_lv.setAdapter(adapter);
        // 地图初始化
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//        mBaiduMap.setMaxAndMinZoomLevel(20, 20);
        mBaiduMap.setOnMapTouchListener(this);
//        mBaiduMap.setMyLocationEnabled(false);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.disableCache(true);// 禁止启用缓存定位
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        loca_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        loca_lv.setOnLoadMoreListener(this);
        loca_lv.setOnRefreshListener(this);
//        mSearch.setOnGetGeoCodeResultListener(this);
//        edit_search.setOnEditorActionListener(this);

    }

    private String province = "";
    private String city2 = "";
    private String district = "";

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("geocoder")) {
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(result);
            Map<String, String> map1 = JSONUtils.parseKeyAndValueToMap(map.get("result"));
            Map<String, String> map2 = JSONUtils.parseKeyAndValueToMap(map1.get("addressComponent"));
            province = map2.get("province");
            city2 = map2.get("city");
            district = map2.get("district");

            Intent intent = getIntent();
            intent.putExtra("poiInfo", poiInfos.get(index));
            intent.putExtra("province", province);
            intent.putExtra("city", city2);
            intent.putExtra("district", district);
            setResult(RESULT_OK, intent);
            finish();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Event(value = {R.id.loca_imgv_back, R.id.loca_relay_search})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.loca_imgv_back:
                finish();
                break;
            case R.id.loca_relay_search:
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                startActivityForResult(SearchAddressAty.class, bundle, 0x22);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x22:
                if (data != null) {
                    String address = data.getStringExtra("address");
                    String latitude = data.getStringExtra("latitude");
                    String longitude = data.getStringExtra("longitude");
                    String province = data.getStringExtra("province");
                    String city = data.getStringExtra("city");
                    String district = data.getStringExtra("district");
//                    LatLng ll = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//                    MapStatus.Builder builder = new MapStatus.Builder();
//                    builder.target(ll).zoom(18.0f);
//                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                    getReverseGeoCodeByLatlng(ll);
                    Intent intent = getIntent();
                    intent.putExtra("address", address);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("province", province);
                    intent.putExtra("city", city);
                    intent.putExtra("district", district);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = motionEvent.getX();
                starty = motionEvent.getY();
                imgvPosition.startAnimation(animation);
                break;
            case MotionEvent.ACTION_UP:
                imgvPosition.clearAnimation();
                float x, y;
                x = Math.abs(startx - motionEvent.getX());
                y = Math.abs(starty - motionEvent.getY());
                displacement = (float) Math.sqrt(x * x + y * y);

                if (displacement > 10) {
                    movedLatLng = mBaiduMap.getProjection().fromScreenLocation(getCenterViewPoint());
                    // 设置地图中心点
                    // MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(movedLatLng);
                    // baiduMap.animateMapStatus(mapStatusUpdate);
                    getReverseGeoCodeByLatlng(movedLatLng);
                    location2 = movedLatLng.latitude + "," + movedLatLng.longitude;
                }
                break;
        }
    }

    /**
     * 获取中间圆圈的位置
     *
     * @return
     */
    private Point getCenterViewPoint() {
        int[] location = new int[2];
        imgvCenter.getLocationInWindow(location);
        return new Point(location[0] + imgvCenter.getWidth() / 2, location[1] + imgvCenter.getHeight() / 2);
    }

    @Override
    public void onLoadMore() {
        loca_lv.stopLoadingMore();
    }

    @Override
    public void onRefresh() {
        loca_lv.stopRefreshing();
    }

//    @Override
//    public void onGetGeoCodeResult(GeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(LocaAty.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        mBaiduMap.clear();
////        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
////                .icon(BitmapDescriptorFactory
////                        .fromResource(R.drawable.icon_marka)));
////        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
////                .getLocation()));
//
//        LatLng ll = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
//
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(ll).zoom(18.0f);
//        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//        getReverseGeoCodeByLatlng(ll);
//    }
//
//    @Override
//    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
//
//    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH) {
//            if (TextUtils.isEmpty(Commonly.getViewText(edit_search))) {
//                showToast("请输入搜索内容");
//                return false;
//            }
//            //隐藏软键盘
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(
//                    edit_search.getWindowToken(), 0);
//            mSearch.geocode(new GeoCodeOption().city(
//                    edit_search.getText().toString()).address(edit_search.getText().toString()));
//
//        }
//        return false;
//    }


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
                city = location.getCity();
                Address address = location.getAddress();
                PreferencesUtils.putString(LocaAty.this, "city", address.city);
                PreferencesUtils.putString(LocaAty.this, "district", address.district);
                PreferencesUtils.putString(LocaAty.this, "province", address.province);
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                location2 = ll.latitude + "," + ll.longitude;
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                // 检索周边信息
//                String address = location.getCity() + location.getDistrict() + location.getStreet();
                getReverseGeoCodeByLatlng(ll);
            }


//            LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
//            // 设置地图中心点
//            MapStatusUpdate statusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
//            baiduMap.animateMapStatus(statusUpdate);
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private void retrievalNearInfo(LatLng latLng, String keyWord) {
        final PoiSearch poiSearch = PoiSearch.newInstance();
        PoiNearbySearchOption option = new PoiNearbySearchOption();
        option.location(latLng); // 具体坐标点
        option.keyword(keyWord); // 搜索关键字
        option.radius(1000); // 设置搜索半径为1KM
        option.sortType(PoiSortType.distance_from_near_to_far); // 类型排序
        option.pageCapacity(20); // 每次加载的条数
        poiSearch.searchNearby(option);
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                poiInfos = poiResult.getAllPoi();
                if (poiInfos != null) {
                    adapter.notifyDataSetChanged();
                }
                poiSearch.destroy();
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }
        });
    }

    private void getReverseGeoCodeByLatlng(LatLng latLng) {




        GeoCoder geoCoder = GeoCoder.newInstance();
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(latLng);
        geoCoder.reverseGeoCode(option);
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                PoiInfo poiInfo = new PoiInfo();
                poiInfo.name = reverseGeoCodeResult.getAddressDetail().street;
                poiInfo.address = reverseGeoCodeResult.getAddress();
                poiInfos = reverseGeoCodeResult.getPoiList();
                if (isFirst == 0) {
                    isFirst = 1;
                }
                if (poiInfos != null && poiInfos.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(LocaAty.this).inflate(R.layout.item_loca_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_local.setText(poiInfos.get(position).address);
        }

        @Override
        public int getItemCount() {
            return poiInfos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_localv_tv)
            TextView tv_local;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }


}
