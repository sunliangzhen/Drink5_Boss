package com.toocms.drink5.boss.ui.mine.set;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.About;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;

import java.util.Map;

import cn.zero.android.common.util.JSONUtils;


/**
 * @author Zero
 * @date 2016/5/20 11:52
 */
public class AboutAty extends BaseAty {

    @ViewInject(R.id.about_imgv)
    private ImageView about_imgv;
    @ViewInject(R.id.about_content)
    private TextView tv_content;

    private About about;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_about;
    }

    @Override
    protected void initialized() {
        about = new About();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        about.index(this);

    }

    @Override
    public void onComplete(RequestParams params, String result) {
        Map<String, String> map = JSONUtils.parseDataToMap(result);
        tv_content.setText(map.get("about_c"));
        Picasso.with(AboutAty.this).load(map.get("web_logo")).into(about_imgv);
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("关于我们");

    }
}
