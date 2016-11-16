package com.toocms.drink5.boss.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.lar.LocaAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

/**
 * @author Zero
 * @date 2016/7/25 16:30
 */
public class Updapeaddress extends BaseAty {

    @ViewInject(R.id.updad_tv_address)
    private TextView updad_tv_address;
    @ViewInject(R.id.updad_tv_address2)
    private EditText updad_tv_address2;

    private Site site;
    private String name = "";
    private String name2 = "";
    private String latitude;
    private String longitude;
    private String province = "";
    private String city = "";
    private String district = "";
    private Map<String, String> userInfo;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_updatea;
    }

    @Override
    protected void initialized() {
        site = new Site();
        userInfo = application.getUserInfo();

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("水站地址");
        name = userInfo.get("address");
        name2 = userInfo.get("address_code");
        province = userInfo.get("province_name");
        city = userInfo.get("city_name");
        district = userInfo.get("area_name");
        longitude = userInfo.get("lon");
        latitude = userInfo.get("lat");
        updad_tv_address.setText(application.getUserInfo().get("address"));
        updad_tv_address2.setText(application.getUserInfo().get("address_code"));
    }

    @Event(value = {R.id.updad_loca, R.id.upd_fb_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.updad_loca:
                startActivityForResult(LocaAty.class, null, 26);
                break;
            case R.id.upd_fb_ok:
                name2 = Commonly.getViewText(updad_tv_address2);
                if (TextUtils.isEmpty(name2)) {
                    showToast("详细地址不能为空");
                    return;
                }
                showProgressDialog();
                site.updateAddress(application.getUserInfo().get("site_id"), name, name2,
                        longitude, latitude, province, city, district, this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("updateAddress")) {
            application.setUserInfoItem("address", name);
            application.setUserInfoItem("address_code", name2);
            application.setUserInfoItem("province_name", province);
            application.setUserInfoItem("city_name", city);
            application.setUserInfoItem("area_name", district);
            application.setUserInfoItem("lon", longitude);
            application.setUserInfoItem("lat", latitude);
            finish();
        }

        super.onComplete(params, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
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
                    updad_tv_address.setText(name);
                }
                break;
        }
    }
}
