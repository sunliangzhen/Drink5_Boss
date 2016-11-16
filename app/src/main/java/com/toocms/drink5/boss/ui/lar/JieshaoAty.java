package com.toocms.drink5.boss.ui.lar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.Commonly;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/5/19 14:47
 */
public class JieshaoAty extends BaseAty {
    @ViewInject(R.id.jieshao_etxt_name)
    private EditText etxt_name;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_jieshao;
    }

    @Override
    protected void initialized() {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("水站介绍");
    }

    @Event(value = {R.id.jieshao_fb})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.jieshao_fb:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_name))) {
                    showToast("请输入介绍内容");
                    return;
                }
                setResult(-1, new Intent().putExtra("jieshao", Commonly.getViewText(etxt_name)));
                finish();
                break;
        }
    }
}
