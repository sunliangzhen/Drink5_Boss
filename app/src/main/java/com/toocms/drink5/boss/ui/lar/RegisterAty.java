package com.toocms.drink5.boss.ui.lar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.config.AppCountdown;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/13 17:39
 */
public class RegisterAty extends BaseAty implements TextWatcher {

    @ViewInject(R.id.register_etxt_account)
    private EditText etxt_account;
    @ViewInject(R.id.register_etxt_code)
    private EditText etxt_code;
    @ViewInject(R.id.num_imgv_cancel)
    private ImageView imgv_cancel;
    @ViewInject(R.id.register_tv_yzm)
    private TextView tv_yzm;
    private String type = "";
    private RegisterLog registerLog;
    private AppCountdown appCountdown;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_register;
    }

    @Override
    protected void initialized() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
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
        if (type.equals("findPass")) {
            mActionBar.setTitle("忘记密码");
        } else if (type.equals("resPass")) {
            mActionBar.setTitle("注册");
        }
        etxt_account.addTextChangedListener(this);
        appCountdown = AppCountdown.getInstance();
        appCountdown.play(tv_yzm);
    }

    @Event(value = {R.id.fb_register_ok, R.id.num_imgv_cancel, R.id.register_tv_yzm})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.num_imgv_cancel:
                etxt_account.setText("");
                imgv_cancel.setVisibility(View.GONE);
                break;
            case R.id.register_tv_yzm:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_account))) {
                    showToast("请先输入手机号");
                    return;
                } else if (Commonly.getViewText(etxt_account).length() < 11) {
                    showToast("请核对手机号位数");
                    return;
                }
                showProgressDialog();
                if (type.equals("resPass")) {
                    registerLog.sendVerify(1 + "", Commonly.getViewText(etxt_account), this);
                } else if (type.equals("findPass")) {
                    registerLog.sendVerify(2 + "", Commonly.getViewText(etxt_account), this);
                }
                break;
            case R.id.fb_register_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_account))) {
                    showToast("请先输入手机号");
                    return;
                } else if (Commonly.getViewText(etxt_account).length() < 11) {
                    showToast("请核对手机号位数");
                    return;
                } else if (TextUtils.isEmpty(Commonly.getViewText(etxt_code))) {
                    showToast("请输入验证码");
                    return;
                }
                showProgressDialog();
                if (type.equals("resPass")) {
                    registerLog.register(Commonly.getViewText(etxt_code), Commonly.getViewText(etxt_account), this);
                } else {
                    registerLog.retrieve(Commonly.getViewText(etxt_code), Commonly.getViewText(etxt_account), this);
                }
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        imgv_cancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("sendVerify")) {
            showToast(JSONUtils.parseKeyAndValueToMap(result).get("message"));
            String data = JSONUtils.parseKeyAndValueToMap(result).get("data");
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(data);
//            showToast(map.get("vc"));
            appCountdown.start();
            tv_yzm.setEnabled(false);
        }
        if (params.getUri().contains("register")) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("phone", Commonly.getViewText(etxt_account));
            bundle.putString("ver", Commonly.getViewText(etxt_code));
            startActivity(Register2Aty.class, bundle);
        }
        if (params.getUri().contains("retrieve")) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("phone", Commonly.getViewText(etxt_account));
            bundle.putString("ver", Commonly.getViewText(etxt_code));
            startActivity(Register2Aty.class, bundle);
        }
        super.onComplete(params, result);
    }
}
