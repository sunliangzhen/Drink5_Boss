package com.toocms.drink5.boss.ui.mine.money;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Pay2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.AppManager;
import com.toocms.pay.Pay;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/5/23 14:04
 */
public class Pay2Aty extends BaseAty {


    @ViewInject(R.id.fb_pay2_ok)
    private Button fb_ok; // 排序标识
    @ViewInject(R.id.pay2_rg)
    private RadioGroup pay2_rg; // 排序标识

    private String type = "";
    private String order_sn = "";
    private String money = "";
    private String type_pay = "wei";

    private boolean isPayState; // 是否为支付状态的标识
    private Pay2 pay;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_pay2;
    }

    @Override
    protected void initialized() {
        pay = new Pay2();
        type = getIntent().getStringExtra("type");
        order_sn = getIntent().getStringExtra("order_sn");
        money = getIntent().getStringExtra("money");
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("结算支付");
        pay2_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pay2_rbtn_01) {
                    type_pay = "wei";
                } else {
                    type_pay = "bao";
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPayState) {
            removeProgressDialog();
            isPayState = false;
            showProgressDialog();
            pay.successPayStatus(order_sn, this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("successPayStatus")) {
            AppManager.getInstance().killActivity(MonqinAty.class);
            finish();
        }
        super.onComplete(params, result);
    }

    @Event(value = {R.id.fb_pay2_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_pay2_ok:
                LogUtil.e(order_sn);
                isPayState = true;
                showProgressDialog();
                if (type_pay.equals("wei")) {
                    Pay.pay(Pay2Aty.this, "http://drink-bossapi.toocms.com/index.php/Pay/wxpayParam", order_sn, Pay.WXPAY);
                } else {
                    pay.alipayParam(order_sn, this);
                    Pay.pay(Pay2Aty.this, "http://drink-bossapi.toocms.com/index.php/Pay/alipayParam", order_sn, Pay.ALIPAY);
                }
                break;
        }
    }
}
