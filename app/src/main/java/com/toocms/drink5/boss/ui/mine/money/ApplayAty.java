package com.toocms.drink5.boss.ui.mine.money;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;

import org.xutils.view.annotation.Event;

/**
 * @author Zero
 * @date 2016/5/23 14:45
 */
public class ApplayAty extends BaseAty {
    @Override
    protected int getLayoutResId() {
        return R.layout.aty_applay;
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
        mActionBar.setTitle("提现");
    }

    @Event(value = {R.id.fb_applay_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.fb_applay_ok:
                showBuilderok();
                break;
        }
    }

    public void showBuilderok() {
        View view = View.inflate(ApplayAty.this, R.layout.dlg_pay, null);
        final Dialog dialog = new Dialog(ApplayAty.this, R.style.dialog);
        TextView tv_no = (TextView) view.findViewById(R.id.builderpay_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderpay_tv_ok);
        final EditText et_pass = (EditText) view.findViewById(R.id.modify_pwd_pwdview);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
