package com.toocms.drink5.boss.ui.lar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.database.Account;
import com.toocms.drink5.boss.database.MyApplication;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.MainAty;
import com.toocms.frame.config.Config;
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

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/13 17:15
 */
public class LoginAty extends BaseAty {


    @ViewInject(R.id.login_etxt_account)
    private EditText etxt_account;
    @ViewInject(R.id.login_etxt_pass)
    private EditText etxt_pass;
    private RegisterLog registerLog;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_login;
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
        mActionBar.setTitle("登录");
    }

    @Event(value = {R.id.fb_login_ok, R.id.login_tv_findpass})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_login_ok:
                if (Commonly.getViewText(etxt_account).length() < 11) {
                    showToast("请输入11位手机号");
                    return;
                } else if (Commonly.getViewText(etxt_pass).length() < 6) {
                    showToast("请输入最少6位以上的密码");
                    return;
                }
                showProgressDialog();
                registerLog.login(Commonly.getViewText(etxt_account), Commonly.getViewText(etxt_pass), this);
                break;
            case R.id.login_tv_findpass:
                Bundle bundle = new Bundle();
                bundle.putString("type", "findPass");
                startActivity(RegisterAty.class, bundle);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("login")) {
            Intent intent = new Intent(this, staService.class);
            startService(intent);
            Config.setLoginState(true);
            application.setUserInfo(JSONUtils.parseDataToMap(result));
            startActivity(MainAty.class, null);
            AppManager.getInstance().killActivity(LarAty.class);
            finish();
            saveAccount();
        }
        super.onComplete(params, result);
    }

    public void saveAccount() {
        Boolean isSave = true;
        Map<String, String> userInfo = application.getUserInfo();
        DbManager db = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig());
        Account first = null;
        try {
            db.update(Account.class, WhereBuilder.b("site_id", "=", userInfo.get("site_id")), new KeyValue("head", userInfo.get("cover")));
            first = db.selector(Account.class).where("site_id", "=", userInfo.get("site_id")).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (first == null) {
            Account account = new Account();
            account.setPhone(Commonly.getViewText(etxt_account));
            account.setPass(Commonly.getViewText(etxt_pass));
            account.setHead(userInfo.get("cover"));
            account.setName(userInfo.get("site_name"));
            account.setSite_id(userInfo.get("site_id"));
            try {
                db.save(account);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
}
