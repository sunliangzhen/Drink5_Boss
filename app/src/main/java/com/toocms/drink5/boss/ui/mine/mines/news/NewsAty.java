package com.toocms.drink5.boss.ui.mine.mines.news;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Courier;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/20 10:15
 */
public class NewsAty extends BaseAty {

    //nox_adb.exe connect 127.0.0.1:62001
    //adb.exe connect 127.0.0.1:6555

    @ViewInject(R.id.news_relay_bottow)
    private RelativeLayout relay_bottow;

    @ViewInject(R.id.news_imgv_head)
    private ImageView imav_head;
    @ViewInject(R.id.news_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.news_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.news_tv_piao)
    private TextView tv_piao;
    @ViewInject(R.id.news_tv_hu)
    private TextView tv_hu;
    @ViewInject(R.id.news_tv_station)
    private TextView tv_station;
    @ViewInject(R.id.news_imgv_ju)
    private ImageView imgv_ju;
    @ViewInject(R.id.news_01)
    private ImageView news_01;
    @ViewInject(R.id.news_02)
    private ImageView news_02;
    @ViewInject(R.id.news_03)
    private ImageView news_03;
    @ViewInject(R.id.news_04)
    private ImageView news_04;
    @ViewInject(R.id.news_05)
    private ImageView news_05;

    private ImageView[] imgv;
    private String type = "";
    private String c_id = "";
    private Courier courier;
    private Courier2 courier2;
    private Site site;
    private ImageLoader imageLoader;
    private Map<String, String> map;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_news;
    }

    @Override
    protected void initialized() {
        courier = new Courier();
        courier2 = new Courier2();
        site = new Site();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
            if (type.equals("boss")) {
                c_id = getIntent().getStringExtra("c_id");
            }
        }

        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        if (type.equals("boss")) {
            courier.find(c_id, this);
        } else {
            courier2.isInfo(application.getUserInfo().get("c_id"), this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("find")) {
            map = JSONUtils.parseDataToMap(result);
            imageLoader.disPlay(imav_head, map.get("head"));
            imageLoader.disPlay(imgv_ju, map.get("cer_healthy"));
            tv_name.setText(map.get("nickname"));
//           int ti =  (int ) Double.parseDouble(map.get("avg_time"));
            tv_time.setText(map.get("avg_time"));
            tv_piao.setText(map.get("total_order") + "单");
            tv_station.setText(map.get("site_name"));
            tv_hu.setText(map.get("curr_member") + "单");
            setXin(Integer.parseInt(map.get("average")));
        }
        if (params.getUri().contains("cancelBind")) {
            finish();
        }
        if (params.getUri().contains("freeze")) {
            finish();
        }
        if (params.getUri().contains("isInfo")) {
            map = JSONUtils.parseDataToMap(result);
            imageLoader.disPlay(imav_head, map.get("head"));
            imageLoader.disPlay(imgv_ju, map.get("cer_healthy"));
            tv_name.setText(map.get("nickname"));
            tv_time.setText(map.get("avg_time")+"分钟");
            tv_piao.setText(map.get("count") + "单");
            tv_station.setText(map.get("site_name"));
            tv_hu.setText(map.get("mem") + "单");
            setXin(Integer.parseInt(map.get("avg_evaluate")));
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (type.equals("boss")) {
            mActionBar.setTitle("水工信息");
            relay_bottow.setVisibility(View.VISIBLE);
        } else {
            mActionBar.setTitle("我的信息");
        }
        imgv = new ImageView[]{news_01, news_02, news_03, news_04, news_05};
    }

    public void setXin(int index) {
        for (int i = 0; i < imgv.length; i++) {
            if (i < index) {
                imgv[i].setVisibility(View.VISIBLE);
            } else {
                imgv[i].setVisibility(View.GONE);
            }
        }
    }

    @Event(value = {R.id.nes_relay_ping, R.id.news_fb_dongjie, R.id.news_fb_jiechu})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.nes_relay_ping:
                if (type.equals("boss")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("c_id", c_id);
                    bundle.putString("type", "boss");
                    startActivity(PingAty.class, bundle);
                } else {
                    startActivity(PingAty.class, null);
                }
                break;
            case R.id.news_fb_dongjie:
                showBuilderok(1);
                break;
            case R.id.news_fb_jiechu:
                showBuilderok(2);
                break;
        }
    }

    public void showBuilderok(final int index) {
        View view = View.inflate(NewsAty.this, R.layout.dlg_jie, null);
        final Dialog dialog = new Dialog(NewsAty.this, R.style.dialog);
        TextView tv_no = (TextView) view.findViewById(R.id.builderpay_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderpay_tv_ok);
        TextView tv_content = (TextView) view.findViewById(R.id.dlg_tv_content);
        if (index == 1) {
            tv_content.setText("亲，你确定要冻结该水工账户吗？");
        } else if (index == 2) {
            tv_content.setText("亲，你确定要解除绑定吗？");
        }
        tv_no.setText("取消");
        tv_ok.setText("确定");
        final EditText et_pass = (EditText) view.findViewById(R.id.modify_pwd_pwdview);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                showProgressDialog();
                if (index == 1) {
                    site.freeze(c_id, application.getUserInfo().get("site_id"), NewsAty.this);
                } else if (index == 2) {
                    site.cancelBind(c_id, application.getUserInfo().get("site_id"), NewsAty.this);
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
