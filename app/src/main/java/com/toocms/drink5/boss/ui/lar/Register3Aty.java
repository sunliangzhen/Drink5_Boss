package com.toocms.drink5.boss.ui.lar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.pro.ProtypeAty;
import com.toocms.drink5.boss.ui.myselectorimg.Myselectorimg;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.tool.Commonly;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Zero
 * @date 2016/5/30 10:37
 */
public class Register3Aty extends BaseAty {

    @ViewInject(R.id.res3_imgv_card)
    private ImageView imgv_card;
    @ViewInject(R.id.res3_imgv_card2)
    private ImageView imgv_card2;
    @ViewInject(R.id.res3_imgv_cancel)
    private ImageView imgv_cancel;
    @ViewInject(R.id.res3_imgv_cancel2)
    private ImageView imgv_cancel2;
    @ViewInject(R.id.res3_rgp)
    private RadioGroup res3_rgp;
    ;
    @ViewInject(R.id.res2_etxt_name)
    private EditText etxt_name;
    @ViewInject(R.id.res3_tv_address)
    private TextView tv_address;
    @ViewInject(R.id.res3_tv_phone)
    private EditText etxt_phone;
    @ViewInject(R.id.res3_tv_pingpai)
    private TextView tv_ping;
    @ViewInject(R.id.res3_tv_jieshao)
    private TextView tv_jieshao;
    @ViewInject(R.id.res3_tv_address2)
    private EditText etxt_address2;

    private File file = null, file2 = null;
    private String path1 = null, path2 = null;
    private int type = 0;
    private ImageLoader imageLoader;
    private String name = "";
    private String latitude;
    private String longitude;
    private String province = "";
    private String city = "";
    private String district = "";
    private String pass;
    private String yhm = "";
    private String phone;
    private String ver;
    private String typeS = 1 + "";
    private RegisterLog registerLog;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_register3;
    }

    @Override
    protected void initialized() {
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        if (getIntent().hasExtra("pass")) {
            pass = getIntent().getStringExtra("pass");
            yhm = getIntent().getStringExtra("yhm");
            phone = getIntent().getStringExtra("phone");
            ver = getIntent().getStringExtra("ver");
        }
        registerLog = new RegisterLog();
    }

    @Override
    protected void requestData() {

    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("注册信息");
        res3_rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.res3_rbtn_01) {
                    typeS = 1 + "";
                } else if (checkedId == R.id.res3_rbtn_02) {
                    typeS = 2 + "";
                }
            }
        });
    }

    @Event(value = {R.id.res3_fb_ok, R.id.res3_imgv_card, R.id.res3_imgv_cancel, R.id.res3_imgv_cancel2, R.id.res3_imgv_card2,
            R.id.res3_relay_type, R.id.res3relay_loca, R.id.res3_relay_jieshao, R.id.res3_tv_address2})
    private void onTestBaidulClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.res3_imgv_card:
                type = 0;
                bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent = new Intent(this, Myselectorimg.class);
                intent.putExtras(bundle);
                this.startActivityForResult(intent, 2083);
                break;
            case R.id.res3_imgv_card2:
                type = 1;
                bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent2 = new Intent(this, Myselectorimg.class);
                intent2.putExtras(bundle);
                this.startActivityForResult(intent2, 2083);
                break;
            case R.id.res3_imgv_cancel:
                imgv_card.setImageResource(R.drawable.ic_photo);
                imgv_cancel.setVisibility(View.GONE);
                imgv_card.setClickable(true);
                path1 = null;
                break;
            case R.id.res3_imgv_cancel2:
                imgv_card2.setImageResource(R.drawable.ic_photo);
                imgv_cancel2.setVisibility(View.GONE);
                imgv_card2.setClickable(true);
                path2 = null;
                break;
            case R.id.res3_relay_type:
                bundle = new Bundle();
                bundle.putString("type", "ping");
                Intent intent3 = new Intent(this, ProtypeAty.class);
                intent3.putExtras(bundle);
                this.startActivityForResult(intent3, 123);
                break;
            case R.id.res3relay_loca:
                startActivityForResult(LocaAty.class, null, 26);
                break;
            case R.id.res3_relay_jieshao:
                Intent intent4 = new Intent(this, JieshaoAty.class);
                this.startActivityForResult(intent4, 456);
                break;
            case R.id.res3_fb_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_name))) {
                    showToast("请填写水站名称");
                    return;
                }
                if (TextUtils.isEmpty(tv_address.getText().toString())) {
                    showToast("请选择水站地址");
                    return;
                }
                if (TextUtils.isEmpty(etxt_address2.getText().toString())) {
                    showToast("请填写水站详细地址");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_phone))) {
                    showToast("请填写联系电话");
                    return;
                }
                if (Commonly.getViewText(etxt_phone).length() < 11) {
                    showToast("请核对手机号位数");
                    return;
                }
                if (TextUtils.isEmpty(tv_ping.getText().toString())) {
                    showToast("请选择销售品牌");
                    return;
                }
                if (TextUtils.isEmpty(tv_jieshao.getText().toString())) {
                    showToast("请填写水站介绍信息");
                    return;
                }
                if (path2 == null) {
                    showToast("请上传营业执照");
                    return;
                }
                if (path2 == null) {
                    showToast("请上传卫生证明");
                    return;
                }
                showProgressDialog();
                registerLog.register(ver, phone, pass, yhm, Commonly.getViewText(etxt_name), tv_address.getText().toString(), longitude, latitude, Commonly.getViewText(etxt_phone),
                        tv_ping.getText().toString(), tv_jieshao.getText().toString(), typeS, path1, path2, province, city, district, Commonly.getViewText(etxt_address2), this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("register")) {
            startActivity(LoginAty.class, null);
            finish();
            showToast("正在审核中");
            AppManager.getInstance().killActivity(RegisterAty.class);
            AppManager.getInstance().killActivity(Register2Aty.class);
            AppManager.getInstance().killActivity(LarAty.class);
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
                    if (type == 0) {
                        file = new File(list.get(0));
                        path1 = list.get(0);
                        imageLoader.disPlay(imgv_card, file.getAbsolutePath());
                        imgv_cancel.setVisibility(View.VISIBLE);
                        imgv_card.setClickable(false);
                    } else if (type == 1) {
                        path2 = list.get(0);
                        file2 = new File(list.get(0));
                        imageLoader.disPlay(imgv_card2, file2.getAbsolutePath());
                        imgv_card2.setClickable(false);
                        imgv_cancel2.setVisibility(View.VISIBLE);
                    }
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
                    tv_address.setText(name);
                }
                break;
            case 123:
                tv_ping.setText(data.getStringExtra("name"));
                break;
            case 456:
                tv_jieshao.setText(data.getStringExtra("jieshao"));
                break;
        }
    }
}
