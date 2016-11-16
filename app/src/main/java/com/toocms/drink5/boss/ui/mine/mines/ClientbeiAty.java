package com.toocms.drink5.boss.ui.mine.mines;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.interfaces2.Contact;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/5/19 14:47
 */
public class ClientbeiAty extends BaseAty {


    @ViewInject(R.id.clientbei_etxt_name)
    private EditText etxt_name;
    private String remarks;
    private String m_id;
    private Site site;
    private String type = "";
    private Courier2 courier2;
    private Contact contact;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_clientbei;
    }

    @Override
    protected void initialized() {
        courier2 = new Courier2();
        contact = new Contact();
        type = getIntent().getStringExtra("type");
        if (type.equals("boss") || type.equals("shui")) {
            remarks = getIntent().getStringExtra("beizhu");
            m_id = getIntent().getStringExtra("m_id");
        } else {
            remarks = PreferencesUtils.getString(this, "nickname2");
        }
        site = new Site();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (type.equals("boss")) {
            mActionBar.setTitle("备注");
        } else if (type.equals("shui_01")) {
            mActionBar.setTitle("修改昵称");
        } else if (type.equals("shui")) {
            mActionBar.setTitle("备注");
        }
        etxt_name.setText(remarks);
    }

    @Event(value = {R.id.fb_clientbei_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_clientbei_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_name))) {
                    showToast("备注不能为空");
                    return;
                }
                showProgressDialog();
                if (type.equals("boss")) {
                    site.setRemark(application.getUserInfo().get("site_id"), m_id, Commonly.getViewText(etxt_name), this);
                } else if (type.equals("shui")) {
                    contact.onRemarks(application.getUserInfo().get("c_id"), m_id, Commonly.getViewText(etxt_name), this);
                } else {
                    courier2.onNickname(application.getUserInfo().get("c_id"), Commonly.getViewText(etxt_name), this);
                }
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("setRemark")) {
            finish();
        }
        if (params.getUri().contains("onNickname")) {
            application.setUserInfoItem("nickname2", Commonly.getViewText(etxt_name));
            finish();
        }
        if(params.getUri().contains("onRemarks")){
            finish();
        }
        super.onComplete(params, result);
    }
}
