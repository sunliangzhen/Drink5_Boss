package com.toocms.drink5.boss.ui.mine.mines.yq;

import android.os.Bundle;
import android.view.View;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;

import org.xutils.view.annotation.Event;

/**
 * @author Zero
 * @date 2016/5/19 16:50
 */
public class YqAty extends BaseAty {
    @Override
    protected int getLayoutResId() {
        return R.layout.aty_yq;
    }

    private String type = "";

    @Override
    protected void initialized() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("我的邀请");
    }

    @Event(value = {R.id.yq_relay_hu, R.id.yq_relay_pai})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.yq_relay_hu:
                if (type.equals("shui")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    startActivity(YqhuAty.class, bundle);
                } else {
                    startActivity(YqhuAty.class, null);
                }
                break;
            case R.id.yq_relay_pai:
                if (type.equals("shui")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    startActivity(YqpaiAty.class, bundle);
                } else {
                    startActivity(YqpaiAty.class, null);
                }
                break;
        }
    }
}
