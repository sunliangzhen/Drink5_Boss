package com.toocms.drink5.boss.ui.mine.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * @author Zero
 * @date 2016/5/24 12:48
 */
public class Bfilterty extends BaseAty {

    @ViewInject(R.id.bfilter_cbox_01)
    private CheckBox cbox_01;
    @ViewInject(R.id.bfilter_cbox_03)
    private CheckBox cbox_02;
    @ViewInject(R.id.bfilter_cbox_04)
    private CheckBox cbox_03;
    @ViewInject(R.id.bfilter_cbox_05)
    private CheckBox cbox_04;

    private CheckBox checkBox[];
    private String type = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bfilter;
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
        mActionBar.setTitle("筛选");
        checkBox = new CheckBox[]{cbox_01, cbox_02, cbox_03, cbox_04};
    }

    @Event(value = {R.id.bfilter_cbox_01, R.id.bfilter_cbox_03, R.id.bfilter_cbox_04, R.id.bfilter_cbox_05, R.id.bfilter_btn_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bfilter_cbox_01:
                setCbox1(0);
                type = "1";
                break;
            case R.id.bfilter_cbox_03:
                setCbox1(1);
                type = "barrel";
                break;
            case R.id.bfilter_cbox_04:
                setCbox1(2);
                type = "bind_a";
                break;
            case R.id.bfilter_cbox_05:
                setCbox1(3);
                type = "bind_b";
                break;
            case R.id.bfilter_btn_ok:
                Intent intent = new Intent();
                intent.putExtra("where", type);
                setResult(-1,intent);
                finish();
                break;
        }
    }

    private void setCbox1(int index) {
        for (int i = 0; i < checkBox.length; i++) {
            if (i == index) {
                checkBox[i].setChecked(true);
            } else {
                checkBox[i].setChecked(false);
            }
        }
    }
}
