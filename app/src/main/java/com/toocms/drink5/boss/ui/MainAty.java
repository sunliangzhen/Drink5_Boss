package com.toocms.drink5.boss.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.RadioGroup;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.config.AppCountdown;
import com.toocms.drink5.boss.ui.list.ListFrg;
import com.toocms.drink5.boss.ui.mine.MineFrg;
import com.toocms.drink5.boss.ui.news.NewsFrg;
import com.toocms.drink5.boss.ui.page.PageFrg;
import com.toocms.frame.config.Constants;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.update.UpdateManager;

import org.xutils.view.annotation.ViewInject;

/**
 * @author Zero
 * @date 2016/5/18 10:57
 */
public class MainAty extends BaseAty implements RadioGroup.OnCheckedChangeListener {
    @ViewInject(R.id.rg_tab)
    private RadioGroup rg_tab;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_main;
    }

    @Override
    protected void initialized() {

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.main_content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        UpdateManager.checkUpdate("http://drink-bossapi.toocms.com/index.php/About/apk", false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 122);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 128);
        }
        addFragment(PageFrg.class, null);
        rg_tab.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_tab_page:
                addFragment(PageFrg.class, null);
                break;
            case R.id.rbtn_tab_list:
                addFragment(ListFrg.class, null);
                break;
            case R.id.rbtn_tab_news:
                addFragment(NewsFrg.class, null);
                break;
            case R.id.rbtn_tab_mine:
                addFragment(MineFrg.class, null);
                break;
        }
    }
}
