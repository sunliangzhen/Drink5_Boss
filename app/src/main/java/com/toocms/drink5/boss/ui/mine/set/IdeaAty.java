package com.toocms.drink5.boss.ui.mine.set;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Feedback;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * @author Zero
 * @date 2016/5/20 12:02
 */
public class IdeaAty extends BaseAty {

    @ViewInject(R.id.idea_etxt_content)
    private EditText idea_etxt_content;
    @ViewInject(R.id.idea_etxt_phone)
    private EditText idea_etxt_phone;

    private Feedback feedback;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_idea;
    }

    @Override
    protected void initialized() {
        feedback = new Feedback();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("意见反馈");
    }

    @Event(value = {R.id.fb_idea_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_idea_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(idea_etxt_content))) {
                    showToast("内容不能为空");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(idea_etxt_phone))) {
                    showToast("手机号不能为空");
                    return;
                }
                feedback.send(Commonly.getViewText(idea_etxt_content), Commonly.getViewText(idea_etxt_phone), this);
                showProgressDialog();
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("send")) {
            showToast("您的意见已提交!");
            finish();
        }
        super.onComplete(params, result);
    }
}
