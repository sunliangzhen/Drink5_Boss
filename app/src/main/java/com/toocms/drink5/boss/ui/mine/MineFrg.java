package com.toocms.drink5.boss.ui.mine;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.database.Account;
import com.toocms.drink5.boss.database.MyApplication;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.ui.mine.card.CardAty;
import com.toocms.drink5.boss.ui.mine.client.BclientAty;
import com.toocms.drink5.boss.ui.mine.mines.MinesAty;
import com.toocms.drink5.boss.ui.mine.mines.RecordAty;
import com.toocms.drink5.boss.ui.mine.mines.TotalAty;
import com.toocms.drink5.boss.ui.mine.mines.YhmAty;
import com.toocms.drink5.boss.ui.mine.mines.yq.YqAty;
import com.toocms.drink5.boss.ui.mine.money.MoneyAty;
import com.toocms.drink5.boss.ui.mine.pro.BproAty;
import com.toocms.drink5.boss.ui.mine.set.SetAty;
import com.toocms.drink5.boss.ui.myselectorimg.Myselectorimg;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.ui.BaseFragment;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * @author Zero
 * @date 2016/5/18 11:00
 */
public class MineFrg extends BaseFragment {

    @ViewInject(R.id.iv_bmine_head)
    private ImageView imgv_head;
    @ViewInject(R.id.bmine_tv_name)
    private TextView tv_name;

    private ImageLoader imageLoader;
    private Site site;

    @Override
    protected int getLayoutResId() {
        return R.layout.frg_mine;
    }

    @Override
    protected void initialized() {
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        site = new Site();
    }

    @Override
    protected void requestData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        imageLoader.disPlay(imgv_head, application.getUserInfo().get("cover"));
        tv_name.setText(application.getUserInfo().get("site_name"));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Event(value = {R.id.bmine_linlay_shui, R.id.bmine_relay_pro, R.id.bmine_relay_record, R.id.bmine_relay_total, R.id.bmine_relay_money
            , R.id.bmine_relay_card, R.id.bmine_relay_client, R.id.bmine_relay_mystu, R.id.bmine_relay_yhm, R.id.bmine_relay_yq,
            R.id.bmine_relay_ewm, R.id.bmine_relay_news, R.id.bmine_relay_set, R.id.iv_bmine_head})
    private void onTestBaidulClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.bmine_linlay_shui:
                startActivity(MinesAty.class, null);
                break;
            case R.id.bmine_relay_pro:
                startActivity(BproAty.class, null);
                break;
            case R.id.bmine_relay_record:
                bundle.putString("type", "boss");
                startActivity(RecordAty.class, bundle);
                break;
            case R.id.bmine_relay_total:
                bundle.putString("type", "boss");
                startActivity(TotalAty.class, bundle);
                break;
            case R.id.bmine_relay_money:
                startActivity(MoneyAty.class, null);
                break;
            case R.id.bmine_relay_card:
                startActivity(CardAty.class, null);
                break;
            case R.id.bmine_relay_client:
                startActivity(BclientAty.class, null);
                break;
            case R.id.bmine_relay_mystu:
                startActivity(MystuAty.class, null);
                break;
            case R.id.bmine_relay_yhm:
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "boss");
                startActivity(YhmAty.class, bundle1);
                break;
            case R.id.bmine_relay_yq:
                startActivity(YqAty.class, null);
                break;
            case R.id.bmine_relay_ewm:
                startActivity(EwmAty.class, null);
                break;
            case R.id.bmine_relay_news:
                startActivity(StationAty.class, null);
                break;
            case R.id.bmine_relay_set:
                startActivity(SetAty.class, null);
                break;
            case R.id.iv_bmine_head:
                bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent = new Intent(getActivity(), Myselectorimg.class);
                intent.putExtras(bundle);
                this.startActivityForResult(intent, 2083);
                break;
        }
    }

    private String path = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) return;
        switch (requestCode) {
            case 2083:
                if (data != null) {
                    ArrayList<String> list = data.getStringArrayListExtra("select_result");
                    path = list.get(0);
                    showProgressDialog();
                    site.setCover(application.getUserInfo().get("site_id"), path, MineFrg.this);
                }
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("setCover")) {
            application.setUserInfoItem("cover", path);
            imageLoader.disPlay(imgv_head, path);
            try {
                x.getDb(((MyApplication) getActivity()
                        .getApplicationContext())
                        .getDaoConfig())
                        .update(Account.class, WhereBuilder.b("site_id", "=", application.getUserInfo().get("site_id")), new KeyValue("head", path));
            } catch (DbException e) {
                e.printStackTrace();
            }

        }
        super.onComplete(params, result);
    }
}
