package com.toocms.drink5.boss.ui.lar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.database.Account;
import com.toocms.drink5.boss.database.MyApplication;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.tool.Commonly;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;

/**
 * @author Zero
 * @date 2016/5/13 17:39
 */
public class Register2Aty extends BaseAty {

    @ViewInject(R.id.register2_etxt_yhm)
    private EditText etxt_yhm;
    @ViewInject(R.id.fb_register2_ok)
    private Button fb_register2_ok;
    @ViewInject(R.id.register2_etxt_pass)
    private EditText etxt_pass;
    @ViewInject(R.id.register2_etxt_pass2)
    private EditText etxt_pass2;
    @ViewInject(R.id.view_line)
    private View v_line;
    private String type = "";
    private RegisterLog registerLog;
    private String phone = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_register2;
    }

    @Override
    protected void initialized() {
        type = getIntent().getStringExtra("type");
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
        if (type.equals("findPass")) {
            mActionBar.setTitle("忘记密码");
            etxt_yhm.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);
            fb_register2_ok.setText("确认修改");
        } else if (type.equals("resPass")) {
            mActionBar.setTitle("注册");
        }
    }

    @Event(value = {R.id.fb_register2_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_register2_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_pass))) {
                    showToast("请填写密码");
                    return;
                }
                if (Commonly.getViewText(etxt_pass).length() < 6) {
                    showToast("请填写至少6位以上的密码");
                    return;
                }
                if (!Commonly.getViewText(etxt_pass).equals(Commonly.getViewText(etxt_pass2))) {
                    showToast("两次密码输入不一致");
                    return;
                }
                if (type.equals("resPass")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pass", Commonly.getViewText(etxt_pass));
                    bundle.putString("phone", getIntent().getStringExtra("phone"));
                    bundle.putString("ver", getIntent().getStringExtra("ver"));
                    if (TextUtils.isEmpty(Commonly.getViewText(etxt_yhm))) {
                        bundle.putString("yhm", "0");
                    } else {
                        bundle.putString("yhm", Commonly.getViewText(etxt_yhm));
                    }
                    startActivity(Register3Aty.class, bundle);

                } else if (type.equals("findPass")) {
                    showProgressDialog();
                    registerLog.retrieve2(phone, Commonly.getViewText(etxt_pass), this);
                }
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("retrieve")) {
            AppManager.getInstance().killActivity(RegisterAty.class);
            AppManager.getInstance().killActivity(LarAty.class);
            startActivity(LoginAty.class, null);
            finish();
        }
        super.onComplete(params, result);
    }
}
