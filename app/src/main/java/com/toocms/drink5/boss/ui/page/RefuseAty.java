package com.toocms.drink5.boss.ui.page;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Order;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.prodetilas.Prodetilas;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.tool.Commonly;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/6/28 11:17
 */
public class RefuseAty extends BaseAty {

    @ViewInject(R.id.refuse_etxt_content)
    private EditText etxt_content;
    @ViewInject(R.id.refuse_rbtn_01)
    private CheckBox cbox_01;
    @ViewInject(R.id.refuse_rbtn_02)
    private CheckBox cbox_02;
    @ViewInject(R.id.refuse_rbtn_03)
    private CheckBox cbox_03;
    @ViewInject(R.id.refuse_rbtn_04)
    private CheckBox cbox_04;

    private Order order;
    private String order_id;
    private String type;
    private CheckBox[] checkBoxes;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_refuse;
    }

    @Override
    protected void initialized() {
        order = new Order();
        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (type.equals("page")) {
            mActionBar.setTitle("拒绝订单");
        } else   {
            mActionBar.setTitle("关闭订单");
        }
        checkBoxes = new CheckBox[]{cbox_01, cbox_02, cbox_03, cbox_04};
    }

    @Event(value = {R.id.fb_refuse_ok, R.id.refuse_rbtn_01, R.id.refuse_rbtn_02, R.id.refuse_rbtn_03, R.id.refuse_rbtn_04})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_refuse_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_content))) {
                    showToast("内容不能为空");
                    return;
                }
                if (type.equals("page")) {
                    showProgressDialog();
                    order.onRefuse(application.getUserInfo().get("c_id"), order_id, Commonly.getViewText(etxt_content), this);
                } else {
                    showProgressDialog();
                    order.onClose(application.getUserInfo().get("c_id"), order_id, Commonly.getViewText(etxt_content), this);
                }
                break;
            case R.id.refuse_rbtn_01:
                seSelect(0);
                break;
            case R.id.refuse_rbtn_02:
                seSelect(1);
                break;
            case R.id.refuse_rbtn_03:
                seSelect(2);
                break;
            case R.id.refuse_rbtn_04:
                seSelect(3);
                break;
        }
    }

    public void seSelect(int index) {
        for (int i = 0; i < checkBoxes.length; i++) {
            if (i == index) {
                checkBoxes[i].setChecked(true);
            } else {
                checkBoxes[i].setChecked(false);
            }
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("onRefuse")) {
            showToast("提交成功");
            finish();
            AppManager.getInstance().killActivity(Prodetilas.class);
        }
        if (params.getUri().contains("onClose")) {
            showToast("提交成功");
            finish();
            AppManager.getInstance().killActivity(Prodetilas.class);
        }
        super.onComplete(params, result);
    }
}
