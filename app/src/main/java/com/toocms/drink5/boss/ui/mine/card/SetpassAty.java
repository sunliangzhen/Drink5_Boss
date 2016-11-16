package com.toocms.drink5.boss.ui.mine.card;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.config.AppCountdown;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/23 17:30
 */
public class SetpassAty extends BaseAty {

    @ViewInject(R.id.setpass_etxt_phone)
    private EditText etxt_phone;
    @ViewInject(R.id.setpass_etxt_ver)
    private EditText etxt_ver;
    @ViewInject(R.id.setpass_tv_send)
    private TextView tv_code;

    private RegisterLog registerLog;
    private AppCountdown appCountdown;

    @Override
    protected int getLayoutResId() {
        return R.layout.setpass;
    }

    @Override
    protected void initialized() {
        registerLog = new RegisterLog();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (TextUtils.isEmpty(application.getUserInfo().get("pay_password"))) {
            mActionBar.setTitle("设置支付密码");
        } else {
            mActionBar.setTitle("修改支付密码");
        }
        appCountdown = AppCountdown.getInstance();
        appCountdown.play(tv_code);
    }

    @Event(value = {R.id.fb_setpass_ok, R.id.setpass_tv_send})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_setpass_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_phone))) {
                    showToast("手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_ver))) {
                    showToast("验证码不能为空");
                    return;
                }
                showProgressDialog();
                registerLog.setPayPassword("1", Commonly.getViewText(etxt_ver), Commonly.getViewText(etxt_phone),
                        application.getUserInfo().get("site_id"), SetpassAty.this);
                break;
            case R.id.setpass_tv_send:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_phone))) {
                    showToast("手机号不能为空");
                    return;
                }
                showProgressDialog();
                registerLog.sendVerify("4", Commonly.getViewText(etxt_phone), SetpassAty.this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("sendVerify")) {
            showToast(JSONUtils.parseKeyAndValueToMap(result).get("message"));
            tv_code.setEnabled(false);
            appCountdown.start();
        }
        if (params.getUri().contains("setPayPassword")) {
            Bundle bundle = new Bundle();
            bundle.putString("phone", Commonly.getViewText(etxt_phone));
            startActivity(Setpass2Aty.class, bundle);
            appCountdown.reSet();
        }
        super.onComplete(params, result);
    }
}
