package com.toocms.drink5.boss.ui.prodetilas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Order;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.tool.AppManager;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/5/21 13:32
 */
public class PhyAty extends BaseAty {


    @ViewInject(R.id.phy_tv_nickname)
    private TextView tv_nickname;
    @ViewInject(R.id.phy_tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.phy_tv_address)
    private TextView tv_address;
    @ViewInject(R.id.phy_tv_company)
    private TextView tv_company;
    @ViewInject(R.id.phy_tv_code)
    private EditText tv_code;

    private String nickname;
    private String contact;
    private String address;
    private String order_id;
    private Order order;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_phy;
    }

    @Override
    protected void initialized() {
        nickname = getIntent().getStringExtra("nickname");
        contact = getIntent().getStringExtra("contact");
        address = getIntent().getStringExtra("address");
        order_id = getIntent().getStringExtra("order_id");
        order = new Order();
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("填写物流");
        tv_nickname.setText(nickname);
        tv_phone.setText(contact);
        tv_address.setText(address);
    }

    @Event(value = {R.id.phy_relay_wu, R.id.phy_fb_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.phy_relay_wu:
                Intent intent = new Intent(this, Phy2Aty.class);
                startActivityForResult(intent, 12);
                break;
            case R.id.phy_fb_ok:
                if (TextUtils.isEmpty(tv_company.getText().toString())) {
                    showToast("请选择物流公司");
                    return;
                }
                if (TextUtils.isEmpty(tv_code.getText().toString())) {
                    showToast("请填写物流单号");
                    return;
                }
                showProgressDialog();
                order.setLogistical(application.getUserInfo().get("c_id"), order_id, tv_company.getText().toString()
                        , tv_code.getText().toString(), this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 12) {
            String name = data.getStringExtra("name");
            tv_company.setText(name);
            tv_company.setHint("");
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        AppManager.getInstance().killActivity(Prodetilas.class);
        finish();
        super.onComplete(params, result);
    }
}
