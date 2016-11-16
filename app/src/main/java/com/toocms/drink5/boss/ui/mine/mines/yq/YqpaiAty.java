package com.toocms.drink5.boss.ui.mine.mines.yq;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.interfaces2.Contact;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.config.Settings;
import com.toocms.frame.image.ImageLoader;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;

/**
 * @author Zero
 * @date 2016/5/19 17:00
 */
public class YqpaiAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.yqpai_v_line)
    private View sortFlag; // 排序标识
    @ViewInject(R.id.yqpai_lv)
    private SwipeToLoadRecyclerView yqpai_lv;
    @ViewInject(R.id.yqpai_tv_pai)
    private TextView tv_pai;
    @ViewInject(R.id.yqpai_tv_peo)
    private TextView tv_peo;
    @ViewInject(R.id.yqpai_tv_money)
    private TextView tv_money;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    private Site site;
    private int p = 1;
    private Map<String, String> map1;
    private ArrayList<Map<String, String>> list;
    private MyAdapter myAdapter;
    private String type = "1";
    private ImageLoader imageLoader;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition; // 排序标识位置

    private Contact contact;
    private String type_can = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_yqpai;
    }

    @Override
    protected void initialized() {
        sortFlagWidth = AutoUtils.getPercentWidthSize(120);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 1)) / 2);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
        site = new Site();
        list = new ArrayList<>();
        myAdapter = new MyAdapter();
        list = new ArrayList<>();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        contact = new Contact();
        if (getIntent().hasExtra("type")) {
            type_can = getIntent().getStringExtra("type");
        }
    }

    @Override
    protected void requestData() {
        showProgressContent();
        if (type_can.equals("shui")) {
            contact.offerRank(application.getUserInfo().get("c_id"), type, p, this);
        } else {
            site.rank(YqpaiAty.this, application.getUserInfo().get("site_id"), type, p, this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        imgv_empty.setVisibility(View.GONE);
        tv_empty.setVisibility(View.GONE);
        yqpai_lv.stopLoadingMore();
        yqpai_lv.stopRefreshing();
        if (params.getUri().contains("rank")) {
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            map1 = JSONUtils.parseKeyAndValueToMap(map.get("rank"));
            if (p == 1) {
                list = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
            } else {
                list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("list")));
            }
            if (p == 1) {
                if (list.size() < 1) {
                    imgv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.VISIBLE);
                } else {
                    imgv_empty.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.GONE);
                }
            }
            tv_pai.setText(map1.get("m"));
            tv_peo.setText(map1.get("invite"));
            tv_money.setText(map1.get("invite_money"));
        }
        if (params.getUri().contains("offerRank")) {
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            map1 = JSONUtils.parseKeyAndValueToMap(map.get("myRank"));
            if (p == 1) {
                list = JSONUtils.parseKeyAndValueToMapList(map.get("orders"));
            } else {
                list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("orders")));
            }

            if (p == 1) {
                if (list.size() < 1) {
                    imgv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.VISIBLE);
                } else {
                    imgv_empty.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.GONE);
                }
            }
            tv_pai.setText(map1.get("rank"));
            tv_peo.setText(map1.get("count"));
            tv_money.setText(map1.get("award"));
        }
        myAdapter.notifyDataSetChanged();
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
        yqpai_lv.stopLoadingMore();
        yqpai_lv.stopRefreshing();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            list = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
            startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("我的发展户");
        sortFlag.setBackgroundColor(Color.parseColor("#2c82df"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);

        yqpai_lv.setOnRefreshListener(this);
        yqpai_lv.setOnLoadMoreListener(this);
        yqpai_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        yqpai_lv.setAdapter(myAdapter);
    }

    @Override
    public void onRefresh() {
        p = 1;
        if (type_can.equals("shui")) {
            contact.offerRank(application.getUserInfo().get("c_id"), type, p, this);
        } else {
            site.rank(YqpaiAty.this, application.getUserInfo().get("site_id"), type, p, this);
        }
    }

    @Override
    public void onLoadMore() {
        p++;
        if (type_can.equals("shui")) {
            contact.offerRank(application.getUserInfo().get("c_id"), type, p, this);
        } else {
            site.rank(YqpaiAty.this, application.getUserInfo().get("site_id"), type, p, this);
        }

    }

    @Event(value = {R.id.yqpai_tv_jin, R.id.yqpai_tv_ming})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.yqpai_tv_jin:
                if (sortFlagPosition != 0) {
                    sortFlagPosition = 0;
                    type = "1";
                    yqpai_lv.startRefreshing();
                }
                break;
            case R.id.yqpai_tv_ming:
                if (sortFlagPosition != 1) {
                    sortFlagPosition = 1;
                    type = "2";
                    yqpai_lv.startRefreshing();
                }
                break;
        }
    }

    private void startTranslate(final View view, float endX) {
        float startx = view.getX();
        ValueAnimator animator = ValueAnimator.ofFloat(startx, endX);
        animator.setTarget(view);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(YqpaiAty.this).inflate(R.layout.item_yqpai_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (type_can.equals("shui")) {
                imageLoader.disPlay(holder.imgv_head, list.get(position).get("head"));
                holder.tv_name.setText(list.get(position).get("nickname"));
                holder.tv_time.setText(list.get(position).get("site_name"));
                holder.tv_peo.setText(list.get(position).get("count") + "人");
                holder.tv_peo2.setText(list.get(position).get("award"));
                holder.tv_shu.setText(position + 1 + "");
            } else {
                imageLoader.disPlay(holder.imgv_head, list.get(position).get("cover"));
                holder.tv_name.setText(list.get(position).get("real_name"));
                holder.tv_time.setText(list.get(position).get("site_name"));
                holder.tv_peo.setText(list.get(position).get("invite") + "人");
                holder.tv_peo2.setText(list.get(position).get("invite_money"));
                holder.tv_shu.setText(position + 1 + "");
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_yqpailv_shu)
            TextView tv_shu;
            @ViewInject(R.id.item_yqpai_imgv_head)
            ImageView imgv_head;
            @ViewInject(R.id.item_yqpailv_name)
            TextView tv_name;
            @ViewInject(R.id.item_yqpailv_time)
            TextView tv_time;
            @ViewInject(R.id.item_yqpailv_peo)
            TextView tv_peo;
            @ViewInject(R.id.item_yqpailv_peo2)
            TextView tv_peo2;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
