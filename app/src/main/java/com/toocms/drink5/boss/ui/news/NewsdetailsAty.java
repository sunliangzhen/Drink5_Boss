package com.toocms.drink5.boss.ui.news;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
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
 * @date 2016/5/20 13:27
 */
public class NewsdetailsAty extends BaseAty {

    @ViewInject(R.id.newsde_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.newsde_tv_title)
    private TextView tv_title;
    @ViewInject(R.id.newsdetails_imgv_head)
    private ImageView imgv_head;
    @ViewInject(R.id.newsdetails_imgv_name)
    private TextView tv_name;
    @ViewInject(R.id.newsde_tv_avgtime)
    private TextView tv_avgtime;
    @ViewInject(R.id.newsde_tv_order)
    private TextView tv_order;
    @ViewInject(R.id.newsde_img_01)
    private ImageView imgv_01;
    @ViewInject(R.id.newsde_img_02)
    private ImageView imgv_02;
    @ViewInject(R.id.newsde_img_03)
    private ImageView imgv_03;
    @ViewInject(R.id.newsde_img_04)
    private ImageView imgv_04;
    @ViewInject(R.id.newsde_img_05)
    private ImageView imgv_05;
    @ViewInject(R.id.newsde_linlay_02)
    private LinearLayout linlay_bang;
    @ViewInject(R.id.newsde_relay_01)
    private RelativeLayout relay_01;
    @ViewInject(R.id.newsde_tv_content)
    private TextView tv_content;
    @ViewInject(R.id.newsdetails_linlay_01)
    private LinearLayout newsdetails_linlay_01;
    @ViewInject(R.id.newsde_linlay_04)
    private LinearLayout newsde_linlay_04;


    private Site site;
    private String message_id;
    private ImageLoader imageLoader;
    private ImageView[] imgv;
    private Map<String, String> maps;
    private int kk = 0;
    private String sender_type = "";
    private Map<String, String> map;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_newsdetails;
    }

    @Override
    protected void initialized() {
        site = new Site();
        message_id = getIntent().getStringExtra("message_id");
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        imgv = new ImageView[]{imgv_01, imgv_02, imgv_03, imgv_04, imgv_05};
    }

    @Override
    protected void requestData() {
        showProgressContent();
        site.messageDetail(message_id, application.getUserInfo().get("site_id"), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("消息详情");
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("messageDetail")) {
            map = JSONUtils.parseDataToMap(result);
            tv_time.setText(map.get("create_time"));
            sender_type = map.get("sender_type");
            maps = JSONUtils.parseKeyAndValueToMap(map.get("courier"));
            switch (map.get("reply")) {
                case "1":
                    tv_title.setText("[" + map.get("title") + "]");
                    linlay_bang.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    linlay_bang.setVisibility(View.GONE);
                    tv_title.setText("[已同意]");
                    break;
                case "3":
                    linlay_bang.setVisibility(View.GONE);
                    tv_title.setText("[已拒绝]");
                    break;
            }
            if (sender_type.equals("2")) {
                if (maps != null) {
                    imageLoader.disPlay(imgv_head, maps.get("head"));
                    tv_name.setText(maps.get("nickname"));
                    tv_avgtime.setText(maps.get("avg_time"));
                    tv_order.setText(maps.get("total_order"));
                    int average = (int) Double.parseDouble(maps.get("average"));
                    setXin(average);
                    if (map.get("reply").equals("1")) {
                        linlay_bang.setVisibility(View.VISIBLE);
                    } else {
                        linlay_bang.setVisibility(View.GONE);
                    }
                } else {
                    relay_01.setVisibility(View.GONE);
                    linlay_bang.setVisibility(View.GONE);
                    tv_content.setVisibility(View.VISIBLE);
                    tv_title.setText("[" + map.get("title") + "]");
                    tv_content.setText(map.get("content"));
                }
            } else {
                if (maps == null) {
                    linlay_bang.setVisibility(View.GONE);
                    relay_01.setVisibility(View.GONE);
                    tv_content.setVisibility(View.VISIBLE);
                    tv_content.setText(map.get("content"));
                } else {
                    newsdetails_linlay_01.setVisibility(View.GONE);
                    newsde_linlay_04.setVisibility(View.GONE);
                    if (map.get("reply").equals("1")) {
                        linlay_bang.setVisibility(View.VISIBLE);
                    } else {
                        linlay_bang.setVisibility(View.GONE);
                    }
                    if (maps != null) {
                        tv_name.setText(maps.get("nickname"));
                        imageLoader.disPlay(imgv_head, maps.get("head"));
                    }
                }
            }
        }
        if (params.getUri().contains("bind")) {
            kk = 0;
            finish();
        }
        super.onComplete(params, result);
    }

//    @Override
//    public void onError(Map<String, String> error) {
//        removeProgressDialog();
//        removeProgressContent();
//        if (kk == 1) {
//            kk = 0;
//            showProgressContent();
//            site.messageDetail(message_id, application.getUserInfo().get("site_id"), this);
//        }
//
//    }

    @Event(value = {R.id.id_newsde_noagree, R.id.id_newsde_agree})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.id_newsde_agree:
                if (sender_type.equals("2")) {
                    showProgressDialog();
                    site.bind(message_id, maps.get("c_id"), application.getUserInfo().get("site_id"), "2", this);
                    kk = 1;
                } else {
                    showProgressDialog();
                    site.bindM(this, maps.get("c_id"), maps.get("m_id"), map.get("message_id"),
                            "2", application.getUserInfo().get("site_id"), maps.get("nickname"), this);
                    kk = 1;
                }
                break;
            case R.id.id_newsde_noagree:
                if (sender_type.equals("2")) {
                    showProgressDialog();
                    site.bind(message_id, maps.get("c_id"), application.getUserInfo().get("site_id"), "3", this);
                    kk = 1;
                } else {
                    showProgressDialog();
                    site.bindM(this, maps.get("c_id"), maps.get("m_id"), map.get("message_id"),
                            "3", application.getUserInfo().get("site_id"), maps.get("nickname"), this);
                    kk = 1;
                }
                break;
        }
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
}
