package com.toocms.drink5.boss.ui.mine.mines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.interfaces2.Rules;
import com.toocms.drink5.boss.ui.BaseAty;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.linearlistview.LinearListView;

/**
 * @author Zero
 * @date 2016/5/19 15:30
 */
public class YhmAty extends BaseAty {

    @ViewInject(R.id.yhm_lv)
    private LinearListView yhm_lv;
    @ViewInject(R.id.share_relay)
    private RelativeLayout relay_share;
    @ViewInject(R.id.yhm_tv_code)
    private TextView tv_code;
    private String type = "";
    private Courier2 courier2;
    private OnekeyShare share;
    private Animation animShow;
    private Animation animHide;

    private Rules rules;
    private MyAdapter myAdapter;
    private ArrayList<Map<String, String>> maps;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_yhm;
    }

    @Override
    protected void initialized() {
        rules = new Rules();
        maps = new ArrayList<>();
        share = new OnekeyShare();
        myAdapter = new MyAdapter();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
    }

    @Override
    protected void requestData() {
        showProgressContent();
        rules.codeRules(this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        maps = JSONUtils.parseDataToMapList(result);
        myAdapter.notifyDataSetChanged();
        super.onComplete(params, result);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        ShareSDK.initSDK(this);
        yhm_lv.setAdapter(myAdapter);
        if (type.equals("boss")) {
            tv_code.setText(application.getUserInfo().get("my_code"));
        } else {
            tv_code.setText(application.getUserInfo().get("code_water"));
        }

        share.setText("下载俊客到家");
        share.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg ");
        share.setUrl("http://www.wecomeshow.com/");

    }


    @Event(value = {R.id.yhm_imgv_back, R.id.yhm_tv_share, R.id.share_qq, R.id.share_wei, R.id.share_weip,
            R.id.yhm_share_tv_cacel, R.id.share_relay})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.yhm_imgv_back:
                finish();
                break;
            case R.id.yhm_tv_share:
                share.show(this);
                break;
            case R.id.share_qq:
                QQ.ShareParams sp = new QQ.ShareParams();
                sp.setTitle("点击立即下载");
                sp.setTitleUrl("http://www.wecomeshow.com/"); // 标题的超链接
                sp.setText("下载");
                sp.setImageUrl("http://www.wecomeshow.com/images/01.png");
                Platform qzone = ShareSDK.getPlatform(QQ.NAME);
                qzone.setPlatformActionListener(actionListener);
                Platform qzone2 = ShareSDK.getPlatform(QQ.NAME);
                qzone.share(sp);
                break;
            case R.id.share_wei:
                Platform.ShareParams wechat = new Platform.ShareParams();
                wechat.setTitle("点击立即下载");
                wechat.setText("点击立即下载");
                wechat.setImageUrl("http://www.wecomeshow.com/images/01.png");
                wechat.setUrl("http://www.wecomeshow.com/");
                wechat.setShareType(Platform.SHARE_WEBPAGE);
                Platform weixin = ShareSDK.getPlatform(this,
                        Wechat.NAME);
                weixin.share(wechat);
                weixin.setPlatformActionListener(actionListener);
                break;
            case R.id.share_weip:
                Platform.ShareParams wechatMoments = new Platform.ShareParams();
                wechatMoments.setTitle("点击立即下载");
                wechatMoments.setText("点击立即下载");
                wechatMoments.setUrl("http://www.wecomeshow.com/");
                wechatMoments.setImageUrl("http://www.wecomeshow.com/images/01.png");
                wechatMoments.setShareType(Platform.SHARE_MUSIC);
                weixin = ShareSDK.getPlatform(this, WechatMoments.NAME);
                weixin.setPlatformActionListener(actionListener);
                weixin.share(wechatMoments);
                break;
            case R.id.yhm_share_tv_cacel:
                relay_share.setVisibility(View.GONE);
                break;
            case R.id.share_relay:
                relay_share.setVisibility(View.GONE);
                break;
        }
    }

    PlatformActionListener actionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            showToast("分享成功");
            relay_share.setVisibility(View.GONE);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            relay_share.setVisibility(View.GONE);
        }

        @Override
        public void onCancel(Platform platform, int i) {
            relay_share.setVisibility(View.GONE);
        }
    };


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return maps.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(YhmAty.this).inflate(R.layout.item_yhm_lv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_name.setText(maps.get(position).get("title"));
            viewHolder.tv_content.setText(maps.get(position).get("content"));
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.rules_tv_title)
            public TextView tv_name;
            @ViewInject(R.id.rules_tv_content)
            public TextView tv_content;
        }

    }

}
