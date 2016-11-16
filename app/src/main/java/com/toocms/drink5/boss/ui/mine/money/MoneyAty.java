package com.toocms.drink5.boss.ui.mine.money;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Pay2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.card.CardAty;
import com.toocms.drink5.boss.ui.mine.mines.date.util.LogUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/5/23 11:18
 */
public class MoneyAty extends BaseAty {

    @ViewInject(R.id.money_tv_ti)
    private TextView tv_ti;
    @ViewInject(R.id.money_tv_di)
    private TextView tv_di;
    @ViewInject(R.id.money_tv_total)
    private TextView tv_total;
    @ViewInject(R.id.money_tv_ok)
    private TextView tv_ok;

    private Pay2 pay;
    private double site_balance;
    private String diff;

    private double ooo; //京币抵扣差额
    private double total; //合并结算
    private double score_total; //京币抵扣
    private double award_total; //推荐费用
    private StringBuffer buffer;
    private StringBuffer buffer_applay;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_money;
    }

    @Override
    protected void initialized() {
        pay = new Pay2();
        buffer = new StringBuffer();
        buffer_applay = new StringBuffer();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        pay.index(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                PreferencesUtils.getString(this, "latitude"), 1, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("对账结算");
    }


    @Override
    public void onComplete(RequestParams params, String result) {

        if (params.getUri().contains("withdrawIndex")) {
            buffer_applay = new StringBuffer();
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            ArrayList<Map<String, String>> maps_order = JSONUtils.parseKeyAndValueToMapList(map.get("order_ids"));
            if (maps_order != null) {
                if (maps_order.size() > 0) {
                    int k = 1;
                    for (int i = 0; i < maps_order.size(); i++) {
                        if (k != 1) {
                            buffer_applay.append(",");
                        }
                        buffer_applay.append(maps_order.get(i).get("order_id"));
                        k++;
                    }
                    if (!TextUtils.isEmpty(buffer_applay.toString())) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "pay_applay");
                        bundle.putString("award_total", award_total + "");
                        bundle.putString("score_total", score_total + "");
                        startActivity(MonqinAty.class, bundle);
                    } else {
                        showToast("当前暂无可提现订单！");
                    }
                } else {
                    showToast("当前暂无可提现订单！");
                }
            } else {
                showToast("当前暂无可提现订单！");
            }
        }
        if (params.getUri().contains("Pay/pay")) {
            String order_sn = JSONUtils.parseDataToMap(result).get("order_id");
            Bundle bundle = new Bundle();
//            bundle.putString("type", "pay_jin");
            bundle.putString("order_sn", order_sn);
            startActivity(Pay2Aty.class, bundle);
        }
        if (params.getUri().contains("Pay/index")) {
            buffer = new StringBuffer();
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            ArrayList<Map<String, String>> maps = JSONUtils.parseKeyAndValueToMapList(map.get("order_ids"));
            if (maps != null) {
                if (maps.size() > 0) {
                    int k = 1;
                    for (int i = 0; i < maps.size(); i++) {
                        if (k != 1) {
                            buffer.append(",");
                        }
                        buffer.append(maps.get(i).get("order_id"));
                        k++;
                    }
                }
            }
            score_total = Double.parseDouble(map.get("score_total"));//京币抵扣
            award_total = Double.parseDouble(map.get("award_total"));//推荐费
            site_balance = Double.parseDouble(map.get("site_balance"));//余额
            double diff = Double.parseDouble(map.get("diff"));//余额
            if (score_total > award_total) {   //提现
                ooo = score_total - award_total;
                total = site_balance + ooo;
//                tv_di.setText(ooo + "");
            } else {                            //支付
                ooo = award_total - score_total;
                total = site_balance - ooo;
//                tv_di.setText("-" + ooo + "");
            }
            if (total < 0) {
                tv_ok.setText("结算支付");
            } else {
                tv_ok.setText("结算");
            }
            tv_ti.setText(map.get("site_balance") + "");
            tv_total.setText("￥" + total + "");
            tv_di.setText(map.get("diff") + "");
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        ooo = 0;
        site_balance = 0;
        total = 0;
        tv_di.setText("-" + ooo + "");
        tv_ti.setText(site_balance + "");
        tv_total.setText("￥" + total + "");
    }

    @Event(value = {R.id.money_fb_qin, R.id.money_fb_to, R.id.money_tv_ok})
    private void onTestBaidulClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.money_fb_qin:  //京币结算
                if (ooo > 0) {
                    if (!TextUtils.isEmpty(buffer.toString())) {
                        LogUtils.error(buffer.toString() + "55555555555555");
                        bundle = new Bundle();
                        bundle.putString("type", "pay_jiesuan");
                        bundle.putString("award_total", award_total + "");
                        bundle.putString("score_total", score_total + "");
                        startActivity(MonqinAty.class, bundle);
                    } else {
                        showToast("当前暂无可结算订单！");
                    }
                } else {
                    showToast("金额不足");
                }
                break;
            case R.id.money_fb_to:    //余额结算
                if (site_balance > 0) {
                    showProgressDialog();
                    pay.withdrawIndex(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                            PreferencesUtils.getString(this, "latitude"), 1, this);
//                    bundle = new Bundle();
//                    bundle.putString("type", "pay_applay");
//                    bundle.putString("award_total", award_total + "");
//                    bundle.putString("score_total", score_total + "");
//                    startActivity(MonqinAty.class, bundle);
                } else {
//                    bundle = new Bundle();
//                    bundle.putString("type", "pay_applay");
//                    bundle.putString("award_total", award_total + "");
//                    bundle.putString("score_total", score_total + "");
//                    startActivity(MonqinAty.class, bundle);
                    showToast("金额不足");
                }
                break;
            case R.id.money_tv_ok:
                if (total > 0) {
                    double abs = Math.abs(total);
                    bundle = new Bundle();
                    bundle.putString("type", "total_aplay");
                    bundle.putString("order_ids", buffer.toString());
                    bundle.putString("money", abs + "");
                    bundle.putString("money", abs + "");
                    bundle.putString("award_total", award_total + "");
                    bundle.putString("score_total", score_total + "");
                    startActivity(CardAty.class, bundle);
                } else if (total < 0) {
                    showProgressDialog();
                    pay.pay2(application.getUserInfo().get("site_id"), Math.abs(total) + "", Math.abs(total) + "",
                            "0", award_total + "", score_total + "", MoneyAty.this);
                } else {
                    showToast("金额不足");
                }
                break;
        }


    }
}
