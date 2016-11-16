package com.toocms.drink5.boss.ui.mine.set;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.database.Account;
import com.toocms.drink5.boss.database.MyApplication;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.config.Config;
import com.toocms.frame.tool.Commonly;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/25 13:32
 */
public class AddaccountAty extends BaseAty {

    @ViewInject(R.id.addacco_etxt_phone)
    private EditText etxt_phone;
    @ViewInject(R.id.addacco_etxt_pass)
    private EditText etxt_pass;

    private RegisterLog registerLog;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_addaccou;
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
        mActionBar.setTitle("添加账号");
    }


    @Event(value = {R.id.fb_addacco_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_addacco_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_phone))) {
                    showToast("手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_pass))) {
                    showToast("密码不能为空");
                    return;
                }
                showProgressDialog();
                registerLog.login(Commonly.getViewText(etxt_phone)
                        , Commonly.getViewText(etxt_pass), this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("login")) {
            Config.setLoginState(true);
            application.setUserInfo(JSONUtils.parseDataToMap(result));
            saveAccount();
        }
        super.onComplete(params, result);
    }

    public void saveAccount() {
        Boolean isSave = true;
        Map<String, String> userInfo = application.getUserInfo();
        DbManager db = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig());
        List<Account> list = new ArrayList<>();
        try {
            list = db.selector(Account.class).orderBy("id", true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (userInfo.get("site_id").equals(list.get(i).getSite_id())) {
                    isSave = false;
                }
            }
            if (isSave) {
                Account account = new Account();
                account.setPhone(Commonly.getViewText(etxt_phone));
                account.setPass(Commonly.getViewText(etxt_pass));
                account.setHead(userInfo.get("cover"));
                account.setName(userInfo.get("site_name"));
                account.setSite_id(userInfo.get("site_id"));
                try {
                    db.save(account);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                finish();
            } else {
                showToast("该水站已经添加");
            }
        } else {
            Account account = new Account();
            account.setPhone(Commonly.getViewText(etxt_phone));
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
