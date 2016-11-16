package com.toocms.drink5.boss.ui.mine.set;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/5/25 14:09
 */
public class MessageAty extends BaseAty {

    @ViewInject(R.id.mss_etxt_content)
    private EditText etxt_content;
    @ViewInject(R.id.mss__rg)
    private RadioGroup mss__rg;
    private String type = "1";
    private Site site;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_mss;
    }

    @Override
    protected void initialized() {
        site = new Site();
    }

    @Override
    protected void requestData() {

    }

    @Event(value = {R.id.fb_pay_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_pay_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_content))) {
                    showToast("请输入消息内容");
                    return;
                }
                showProgressDialog();
                site.sendMessage(application.getUserInfo().get("site_id"), Commonly.getViewText(etxt_content), type, this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("sendMessage")) {
            finish();
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("群发消息");

        mss__rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.mss_rbtn_01) {
                    type = "1";
                } else if (checkedId == R.id.mss_rbtn_02) {
                    type = "2";
                }
            }
        });
    }
}
