package com.toocms.drink5.boss.ui.mine.set;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.MainAty;
import com.toocms.drink5.boss.ui.lar.LarAty;
import com.toocms.drink5.boss.ui.mine.mines.date.TimePicker;
import com.toocms.frame.config.Config;
import com.toocms.frame.config.Constants;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.tool.AppManager;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import cn.zero.android.common.util.FileManager;

/**
 * @author Zero
 * @date 2016/5/20 11:29
 */
public class SetAty extends BaseAty {


    @ViewInject(R.id.bset_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.bset_tv_time2)
    private TextView tv_time2;
    @ViewInject(R.id.bset_cbox_business)       //营业状态
    private CheckBox cbox_business;
    @ViewInject(R.id.bset_cbox_work)           //工作状态
    private CheckBox cbox_work;
    @ViewInject(R.id.bset_cbox_delivery)       //货到付款
    private CheckBox cbox_delivery;
    @ViewInject(R.id.bset_cbox_deduct)         //支持抵扣
    private CheckBox cbox_deduct;
    @ViewInject(R.id.set_imgv_head)         //支持抵扣
    private ImageView imgv_head;
    @ViewInject(R.id.bset_relay_zhi)
    private RelativeLayout relay_zhi;

    private ArrayList<String> list;
    private Site site;
    private String time, mStartTime, mEndTime;
    private String time_inc, mStartTime_inc, mEndTime_inc;
    private String site_id;
    private int type = 0;
    private ImageLoader imageLoader;

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    showToast("清理完毕！");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bset;
    }

    @Override
    protected void initialized() {
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        list = new ArrayList<>();
        site = new Site();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (application.getUserInfo().get("water").equals("2")) {
            relay_zhi.setVisibility(View.VISIBLE);
        } else {
            relay_zhi.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(application.getUserInfo().get("business_time_a"))) {
            time = "";
        } else {
            time = application.getUserInfo().get("business_time_a") + "-" + application.getUserInfo().get("business_time_b");
        }
        if (TextUtils.isEmpty(application.getUserInfo().get("inc_time_a"))) {
            time_inc = "";
        } else {
            time_inc = application.getUserInfo().get("inc_time_a") + "-" + application.getUserInfo().get("inc_time_b");
        }
        site_id = application.getUserInfo().get("site_id");
        if (TextUtils.isEmpty(application.getUserInfo().get("inc_time_a"))) {
            tv_time2.setText("暂不支持顺带");
        } else {
            tv_time2.setText(time_inc);
        }
        tv_time.setText(time);
        if (application.getUserInfo().get("is_business").equals("1")) {
            cbox_business.setChecked(true);
        } else {
            cbox_business.setChecked(false);
        }
        if (application.getUserInfo().get("is_work").equals("1")) {
            cbox_work.setChecked(true);
        } else {
            cbox_work.setChecked(false);
        }
        if (application.getUserInfo().get("is_delivery").equals("1")) {
            cbox_delivery.setChecked(true);
        } else {
            cbox_delivery.setChecked(false);
        }
        if (application.getUserInfo().get("is_score_deduct").equals("1")) {
            cbox_deduct.setChecked(true);
        } else {
            cbox_deduct.setChecked(false);
        }
        imageLoader.disPlay(imgv_head, application.getUserInfo().get("cover"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("设置");
    }

    @Event(value = {R.id.bset_relay_account, R.id.bset_relay_time02, R.id.bset_relay_time01, R.id.bset_relay_about, R.id.bset_relay_idea
            , R.id.bset_relay_mss, R.id.bset_fb_eixt, R.id.bset_relay_qin, R.id.bset_cbox_business, R.id.bset_cbox_work, R.id.bset_cbox_delivery
            , R.id.bset_cbox_deduct})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bset_relay_account:
                startActivity(AccountAty.class, null);
                break;
            case R.id.bset_relay_time01:
                //默认选中当前时间
                TimePicker picker = new TimePicker(this, TimePicker.HOUR_OF_DAY);
                picker.setSelectedItem(12, 12);
                picker.setSelectedItem2(12, 25);
                picker.setTopLineVisible(false);
                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute, String hour2, String minute2) {
                        String mhour, mhour2;
                        if (hour.substring(0, 1).equals("0")) {
                            mhour = hour.substring(1, 2);
                        } else {
                            mhour = hour;
                        }
                        if (hour2.substring(0, 1).equals("0")) {
                            mhour2 = hour2.substring(1, 2);
                        } else {
                            mhour2 = hour2;
                        }
                        LogUtil.e(hour + "," + minute + "," + hour2 + "," + minute2);
                        time = mhour + ":" + minute + "-" + mhour2 + ":" + minute2;
                        mStartTime = mhour + ":" + minute;
                        mEndTime = mhour2 + ":" + minute2;
                        LogUtil.e(time);
                        showProgressDialog();
                        site.setBusinessTime(site_id, mStartTime, mEndTime, SetAty.this);
                    }

                    @Override
                    public void bb() {
                    }
                });
                picker.show();
                break;
            case R.id.bset_relay_time02:
                //默认选中当前时间
                TimePicker picker1 = new TimePicker(this, TimePicker.HOUR_OF_DAY);
                picker1.setTitlelVisible(true);
                picker1.setSelectedItem(12, 12);
                picker1.setSelectedItem2(12, 25);
                picker1.setTopLineVisible(false);
                picker1.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute, String hour2, String minute2) {
                        String mhour, mhour2;
                        if (hour.substring(0, 1).equals("0")) {
                            mhour = hour.substring(1, 2);
                        } else {
                            mhour = hour;
                        }
                        if (hour2.substring(0, 1).equals("0")) {
                            mhour2 = hour2.substring(1, 2);
                        } else {
                            mhour2 = hour2;
                        }
                        time_inc = mhour + ":" + minute + "-" + mhour2 + ":" + minute2;
                        mStartTime_inc = mhour + ":" + minute;
                        mEndTime_inc = mhour2 + ":" + minute2;
                        showProgressDialog();
                        site.setIncTime(site_id, mStartTime_inc, mEndTime_inc, SetAty.this);
                    }

                    @Override
                    public void bb() {
                        time_inc = "";
                        showProgressContent();
                        site.setIncTime(site_id, "", "", SetAty.this);
                    }
                });
                picker1.show();
                break;
            case R.id.bset_relay_about:
                startActivity(AboutAty.class, null);
                break;
            case R.id.bset_relay_idea:
                startActivity(IdeaAty.class, null);
                break;
            case R.id.bset_relay_mss:
                startActivity(MessageAty.class, null);
                break;
            case R.id.bset_fb_eixt:
                showBuilder(2);
                break;
            case R.id.bset_relay_qin:
                showBuilder(3);
                break;
            case R.id.bset_cbox_business:
                type = 1;
                showProgressDialog();
                if (application.getUserInfo().get("is_business").equals("1")) {
                    site.setSite(site_id, "0", "is_business", this);
                } else {
                    site.setSite(site_id, "1", "is_business", this);
                }
                break;
            case R.id.bset_cbox_work:
                type = 2;
                showProgressDialog();
                if (application.getUserInfo().get("is_work").equals("1")) {
                    site.setSite(site_id, "0", "is_work", this);
                } else {
                    site.setSite(site_id, "1", "is_work", this);
                }
                break;
            case R.id.bset_cbox_delivery:
                type = 3;
                showProgressDialog();
                if (application.getUserInfo().get("is_delivery").equals("1")) {
                    site.setSite(site_id, "0", "is_delivery", this);
                } else {
                    site.setSite(site_id, "1", "is_delivery", this);
                }
                break;
            case R.id.bset_cbox_deduct:
                type = 4;
                showProgressDialog();
                if (application.getUserInfo().get("is_score_deduct").equals("1")) {
                    site.setSite(site_id, "0", "is_score_deduct", this);
                } else {
                    site.setSite(site_id, "1", "is_score_deduct", this);
                }
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("setBusinessTime")) {
            tv_time.setText(time);
            application.setUserInfoItem("business_time_a", mStartTime);
            application.setUserInfoItem("business_time_b", mEndTime);

        }
        if (params.getUri().contains("setIncTime")) {
            if (TextUtils.isEmpty(time_inc)) {
                tv_time2.setText("暂不支持顺带");
                application.setUserInfoItem("inc_time_a", "");
                application.setUserInfoItem("inc_time_b", "");
            } else {
                tv_time2.setText(time_inc);
                application.setUserInfoItem("inc_time_a", mStartTime_inc);
                application.setUserInfoItem("inc_time_b", mEndTime_inc);
            }
        }

        if (params.getUri().contains("setSite")) {
            switch (type) {
                case 1:
                    setSwich(cbox_business, "is_business", application.getUserInfo().get("is_business"));
                    break;
                case 2:
                    setSwich(cbox_work, "is_work", application.getUserInfo().get("is_work"));
                    break;
                case 3:
                    setSwich(cbox_delivery, "is_delivery", application.getUserInfo().get("is_delivery"));
                    break;
                case 4:
                    setSwich(cbox_deduct, "is_score_deduct", application.getUserInfo().get("is_score_deduct"));
                    break;
            }
        }
        super.onComplete(params, result);
    }

    public void setSwich(CheckBox cbox, String type, String data) {
        if (data.equals("1")) {
            cbox.setChecked(false);
            application.setUserInfoItem(type, "0");
        } else {
            cbox.setChecked(true);
            application.setUserInfoItem(type, "1");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case Constants.SELECT_IMAGE:
                if (data != null) {
                    ArrayList<String> list = getSelectImagePath(data);
                    LogUtil.e(list.toString());
                }
                break;
        }
    }

    public void showBuilder(final int index) {
        View view = View.inflate(SetAty.this, R.layout.dlg_exit, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(SetAty.this, R.style.dialog);
        if (index == 1) {
        } else if (index == 2) {
            tv_content.setText("你确定要退出账号吗？");
        } else if (index == 3) {
            tv_content.setText("你确定要清除缓存吗？");
        }
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
                if (index == 1) {
                } else if (index == 2) {
                    Config.setLoginState(false);
                    startActivity(LarAty.class, null);
                    finish();
                    AppManager.getInstance().killActivity(MainAty.class);
                } else if (index == 3) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileManager.clearCacheFiles();
                            myHandler.sendEmptyMessage(1);
                        }
                    }).start();
                }

            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


}
