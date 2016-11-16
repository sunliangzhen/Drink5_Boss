package com.toocms.drink5.boss.ui.mine.mines.setnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.squareup.picasso.Picasso;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.lar.LocaAty;
import com.toocms.drink5.boss.ui.mine.mines.ClientbeiAty;
import com.toocms.drink5.boss.ui.myselectorimg.Myselectorimg;
import com.toocms.frame.image.ImageLoader;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/5/20 12:31
 */
public class SetnewsAty extends BaseAty {
    @ViewInject(R.id.setnews_imgv_head)
    private ImageView imgv_head;
    @ViewInject(R.id.bsetnews_imgv_jian)
    private ImageView imgv_jian;
    @ViewInject(R.id.setnews_tv_addresss)
    private TextView tv_address;
    @ViewInject(R.id.bsetnews_tv_name)
    private TextView tv_name;

    private File file;
    private String name = "";
    private String latitude;
    private String longitude;
    private Courier2 courier2;
    private String path = "";
    private String path2 = "";
    private ImageLoader imageLoader;
    private String province = "";
    private String city = "";
    private String district = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_setnews;
    }

    @Override
    protected void initialized() {
        courier2 = new Courier2();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("个人信息设置");
        if (application.getUserInfo().get("c_head").contains("http")) {
            Picasso.with(SetnewsAty.this).load(application.getUserInfo().get("c_head")).into(imgv_head);
        } else {
            Picasso.with(SetnewsAty.this).load(new File(application.getUserInfo().get("c_head"))).into(imgv_head);
        }
        imageLoader.disPlay(imgv_jian, PreferencesUtils.getString(this, "cer_healthy2"));
        tv_address.setText(application.getUserInfo().get("address2"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_name.setText(application.getUserInfo().get("nickname2"));
    }

    @Event(value = {R.id.setnews_relay_name, R.id.mynews_relay_head, R.id.setnews_relay_bang, R.id.setnews_relay_address
            , R.id.bsetboss_relay_jian})
    private void onTestBaidulClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.setnews_relay_name:
                bundle = new Bundle();
                bundle.putString("type", "shui_01");
                startActivity(ClientbeiAty.class, bundle);
                break;
            case R.id.mynews_relay_head:
                bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent = new Intent(this, Myselectorimg.class);
                intent.putExtras(bundle);
                this.startActivityForResult(intent, 2083);
                break;
            case R.id.setnews_relay_bang:
                startActivity(BangAty.class, null);
                break;
            case R.id.setnews_relay_address:
                startActivityForResult(LocaAty.class, null, 26);
                break;
            case R.id.bsetboss_relay_jian:
                bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent2 = new Intent(this, Myselectorimg.class);
                intent2.putExtras(bundle);
                this.startActivityForResult(intent2, 2084);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("onHead")) {
            application.setUserInfoItem("c_head", path);
            Picasso.with(SetnewsAty.this).load(new File(path)).into(imgv_head);
        }
        if (params.getUri().contains("onHealthy")) {
            application.setUserInfoItem("cer_healthy2", path2);
            imageLoader.disPlay(imgv_jian, path2);
        }
        if (params.getUri().contains("onRess")) {
            tv_address.setText(name);
            application.setUserInfoItem("address2", name);
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case 2083:
                if (data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra("select_result");
                    path = list.get(0);
                    showProgressDialog();
                    courier2.onHead(application.getUserInfo().get("c_id"), list.get(0), SetnewsAty.this);
                }
                break;
            case 26:
                if (data != null) {
                    PoiInfo poiInfo = data.getParcelableExtra("poiInfo");
                    if (poiInfo != null) {
                        name = poiInfo.address;
                        latitude = String.valueOf(poiInfo.location.latitude);
                        longitude = String.valueOf(poiInfo.location.longitude);
                        province = data.getStringExtra("province");
                        city = data.getStringExtra("city");
                        district = data.getStringExtra("district");
                    } else {
                        name = data.getStringExtra("address");
                        latitude = data.getStringExtra("latitude");
                        longitude = data.getStringExtra("longitude");
                        province = data.getStringExtra("province");
                        city = data.getStringExtra("city");
                        district = data.getStringExtra("district");
                    }
                    LogUtil.e(province + city + district + "888888888888888888888888888888888");
                    showProgressDialog();
                    courier2.onRess(application.getUserInfo().get("c_id"), province, city, district, name, SetnewsAty.this);
                }
                break;
            case 2084:
                if (data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra("select_result");
                    path2 = list.get(0);
                    showProgressDialog();
                    courier2.onHealthy(application.getUserInfo().get("c_id"), path2, SetnewsAty.this);
                }
                break;
        }
    }
}
