package com.toocms.drink5.boss.ui.mine.card;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/5/23 17:48
 */
public class Setpass2Aty extends BaseAty {

    @ViewInject(R.id.setpass2_etxt_pass)
    private EditText etxt_pass;
    @ViewInject(R.id.setpass2_etxt_pass2)
    private EditText etxt_pass2;

    private String phone = "";
    private RegisterLog registerLog;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_setpass2;
    }

    @Override
    protected void initialized() {
        phone = getIntent().getStringExtra("phone");
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
    }

    @Event(value = {R.id.fb_setpass2_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_setpass2_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_pass))) {
                    showToast("请填写密码");
                    return;
                }
                if (Commonly.getViewText(etxt_pass).length() != 6) {
                    showToast("请填写6位密码");
                    return;
                }
                if (!Commonly.getViewText(etxt_pass).equals(Commonly.getViewText(etxt_pass2))) {
                    showToast("两次密码输入不一致");
                    return;
                }
                showProgressDialog();
                registerLog.setPayPassword2("2", Commonly.getViewText(etxt_pass), phone, application.getUserInfo().get("site_id"),
                        Setpass2Aty.this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("setPayPassword")) {
            application.setUserInfoItem("pay_password", Commonly.getViewText(etxt_pass2));
            AppManager.getInstance().killActivity(SetpassAty.class);
            finish();
        }
        super.onComplete(params, result);
    }
}
