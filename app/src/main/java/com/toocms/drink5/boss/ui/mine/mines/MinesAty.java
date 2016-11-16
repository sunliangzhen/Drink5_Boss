package com.toocms.drink5.boss.ui.mine.mines;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.mines.news.NewsAty;
import com.toocms.drink5.boss.ui.mine.mines.setnews.SetnewsAty;
import com.toocms.drink5.boss.ui.mine.mines.yq.YqAty;
import com.toocms.frame.image.ImageLoader;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/5/21 14:55
 */
public class MinesAty extends BaseAty {

    @ViewInject(R.id.iv_mine_head)
    private ImageView imgv_head;
    @ViewInject(R.id.smine_tv_name)
    private TextView tv_name;
    private Courier2 courier;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_smine;
    }

    @Override
    protected void initialized() {
        courier = new Courier2();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        courier.isInfo(application.getUserInfo().get("c_id"), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (PreferencesUtils.getString(this, "c_head").contains("http")) {
//            Picasso.with(MinesAty.this).load(PreferencesUtils.getString(this, "head2")).into(imgv_head);
//        } else {
//            Picasso.with(MinesAty.this).load(new File(PreferencesUtils.getString(this, "c_head"))).into(imgv_head);
//        }
        imageLoader.disPlay(imgv_head, PreferencesUtils.getString(this, "c_head"));
        tv_name.setText(application.getUserInfo().get("nickname2"));

    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("isInfo")) {
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            imageLoader.disPlay(imgv_head, map.get("head"));
            application.setUserInfoItem("c_head", map.get("head"));
            application.setUserInfoItem("nickname2", map.get("nickname"));
            application.setUserInfoItem("cer_healthy2", map.get("cer_healthy"));
            application.setUserInfoItem("address2", map.get("address"));
            application.setUserInfoItem("code_water", map.get("my_code"));
            tv_name.setText(map.get("nickname"));
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();

    }

    @Event(value = {R.id.smin_imgv_back, R.id.mine_relay_record, R.id.mine_relay_total, R.id.mine_relay_client, R.id.mine_relay_student,
            R.id.mine_relay_yhm, R.id.mine_relay_yq, R.id.mine_relay_news, R.id.mine_relay_setnews})
    private void onTestBaidulClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.smin_imgv_back:
                finish();
                break;
            case R.id.mine_relay_record:
                bundle.putString("type", "shui");
                startActivity(RecordAty.class, bundle);
                break;
            case R.id.mine_relay_total:
                bundle.putString("type", "shui");
                startActivity(TotalAty.class, bundle);
                break;
            case R.id.mine_relay_client:
                startActivity(ClientAty.class, null);
                break;
            case R.id.mine_relay_student:
                startActivity(StudentAty.class, null);
                break;
            case R.id.mine_relay_yhm:
                startActivity(YhmAty.class, null);
                break;
            case R.id.mine_relay_yq:
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "shui");
                startActivity(YqAty.class, bundle1);
                break;
            case R.id.mine_relay_news:
                startActivity(NewsAty.class, null);
                break;
            case R.id.mine_relay_setnews:
                startActivity(SetnewsAty.class, null);
                break;
        }
    }
}
