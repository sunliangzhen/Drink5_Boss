package com.toocms.drink5.boss.ui.mine;

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
import com.toocms.drink5.boss.interfaces.Courier;
import com.toocms.drink5.boss.interfaces.Goods;
import com.toocms.drink5.boss.interfaces.Site;
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
import java.util.HashMap;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;


/**
 * @author Zero
 * @date 2016/5/24 15:33
 */
public class StationAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {
    @ViewInject(R.id.sta_lv)
    private SwipeToLoadRecyclerView sta_lv;

    @ViewInject(R.id.sta_v_line)
    private View sortFlag; // 排序标识

    @ViewInject(R.id.sta_tv_jin)
    private TextView tv_jin; // 排序标识
    @ViewInject(R.id.sta_tv_ming)
    private TextView tv_ming; // 排序标识
    @ViewInject(R.id.sta_tv_all)
    private TextView tv_all; // 排序标识
    @ViewInject(R.id.sta_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.sta_tv_address)
    private TextView tv_address;
    @ViewInject(R.id.sta_tv_type)
    private TextView tv_type;
    @ViewInject(R.id.sta_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.sta_imgv_head)
    private ImageView imgv_head;
    @ViewInject(R.id.sta_tv_s)
    private TextView tv_s;
    @ViewInject(R.id.sta_tv_content)
    private TextView tv_content;
    @ViewInject(R.id.sta_tv_03)
    private TextView tv_red;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition = 0; // 排序标识位置
    private TextView[] ttvv;

    private MyAdapterPro myAdapterPro;
    private MyAdapterPeo myAdapterPeo;
    private MyAdapterRes myAdapterRes;
    private ImageLoader imageLoader;
    private Goods goods;
    private Courier courier;
    private Site site;
    private int p = 1;
    private ArrayList<Map<String, String>> maps;
    private ArrayList<Map<String, String>> maps2;
    private ArrayList<Map<String, String>> maps3;
    private ArrayList<Map<String, String>> maps4;
    private int isRequest = 0;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_station;
    }

    @Override
    protected void initialized() {
        sortFlagWidth = AutoUtils.getPercentWidthSize(100);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 2)) / 3);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);

        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        goods = new Goods();
        courier = new Courier();
        site = new Site();
        maps = new ArrayList<>();
        maps2 = new ArrayList<>();
        maps3 = new ArrayList<>();
        maps4 = new ArrayList<>();
        myAdapterPro = new MyAdapterPro();
        myAdapterPeo = new MyAdapterPeo();
        myAdapterRes = new MyAdapterRes();
        Map<String, String> mm = new HashMap<>();
        mm.put("name", "营业执照");
        mm.put("cer", application.getUserInfo().get("cer_yyzz"));
        maps4.add(mm);
        Map<String, String> mm2 = new HashMap<>();
        mm2.put("name", "卫生证明");
        mm2.put("cer", application.getUserInfo().get("cer_wszm"));
        maps4.add(mm2);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        goods.goodsList(application.getUserInfo().get("site_id"), p, this);
        isRequest = 1;
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        sta_lv.stopLoadingMore();
        sta_lv.stopRefreshing();
        if (params.getUri().contains("Goods/goodsList")) {
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapterPro.notifyDataSetChanged();
        }
        if (params.getUri().contains("Courier/index")) {
            if (p == 1) {
                maps2 = JSONUtils.parseDataToMapList(result);
            } else {
                maps2.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapterPeo.notifyDataSetChanged();
        }
        if (params.getUri().contains("cerList")) {
            if (p == 1) {
                maps3 = JSONUtils.parseDataToMapList(result);
                maps3.addAll(maps4);
            } else {
                maps3.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapterRes.notifyDataSetChanged();
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        isRequest = 0;
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
        sta_lv.stopLoadingMore();
        sta_lv.stopRefreshing();
        if (p == 1) {
            switch (sortFlagPosition) {
                case 0:
                    maps = new ArrayList<>();
                    sta_lv.setAdapter(myAdapterPro);
                    break;
                case 1:
                    maps2 = new ArrayList<>();
                    sta_lv.setAdapter(myAdapterPeo);
                    break;
                case 2:
                    maps3 = new ArrayList<>();
                    maps3.addAll(maps4);
                    sta_lv.setAdapter(myAdapterRes);
                    break;
            }
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        isRequest = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_address.setText(application.getUserInfo().get("address"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("水站详情");
        ttvv = new TextView[]{tv_jin, tv_ming, tv_all};
        sortFlag.setBackgroundColor(Color.parseColor("#2c82df"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        setTextviewColor(sortFlagPosition);

        tv_name.setText(application.getUserInfo().get("site_name"));
        tv_address.setText(application.getUserInfo().get("address"));
        tv_type.setText(application.getUserInfo().get("brand"));
        tv_time.setText(application.getUserInfo().get("business_time_a") + "-" + application.getUserInfo().get("business_time_b"));
        tv_s.setText(application.getUserInfo().get("radius") + "m");
        tv_content.setText(application.getUserInfo().get("intro"));
        tv_red.setText(application.getUserInfo().get("prompt"));
        imageLoader.disPlay(imgv_head, application.getUserInfo().get("cover"));

        sta_lv.setOnRefreshListener(this);
        sta_lv.setOnLoadMoreListener(this);
        sta_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        sta_lv.setAdapter(myAdapterPro);
    }

    @Override
    public void onRefresh() {
        p = 1;
        switch (sortFlagPosition) {
            case 0:
                if (isRequest == 0) {
                    goods.goodsList(application.getUserInfo().get("site_id"), p, this);
                    isRequest = 1;
                }
                break;
            case 1:
                if (isRequest == 0) {
                    courier.index(application.getUserInfo().get("site_id"), p, this);
                    isRequest = 1;
                }
                break;
            case 2:
                if (isRequest == 0) {
                    site.cerList(application.getUserInfo().get("site_id"), this);
                    isRequest = 1;
                }
                break;
        }

    }

    @Override
    public void onLoadMore() {
        p++;
        switch (sortFlagPosition) {
            case 0:
                if (isRequest == 0) {
                    goods.goodsList(application.getUserInfo().get("site_id"), p, this);
                    isRequest = 1;
                }
                break;
            case 1:
                if (isRequest == 0) {
                    courier.index(application.getUserInfo().get("site_id"), p, this);
                    isRequest = 1;
                }
                break;
            case 2:
                if (isRequest == 0) {
                    sta_lv.stopLoadingMore();
                }
                break;
        }
    }

    @Event(value = {R.id.sta_tv_jin, R.id.sta_tv_ming, R.id.sta_tv_all, R.id.sta_tv_address})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.sta_tv_jin:
                if (sortFlagPosition != 0) {
                    sortFlagPosition = 0;
                    p = 1;
                    if (isRequest == 0) {
                        sta_lv.setAdapter(myAdapterPro);
                        goods.goodsList(application.getUserInfo().get("site_id"), p, this);
                        isRequest = 1;
                    }
                }
                break;
            case R.id.sta_tv_ming:
                if (sortFlagPosition != 1) {
                    sortFlagPosition = 1;
                    p = 1;
                    if (isRequest == 0) {
                        sta_lv.setAdapter(myAdapterPeo);
                        courier.index(application.getUserInfo().get("site_id"), p, this);
                        isRequest = 1;
                    }
                }
                break;
            case R.id.sta_tv_all:
                if (sortFlagPosition != 2) {
                    sortFlagPosition = 2;
                    p = 1;
                    if (isRequest == 0) {
                        sta_lv.setAdapter(myAdapterRes);
                        site.cerList(application.getUserInfo().get("site_id"), this);
                        isRequest = 1;
                    }
                }
                break;
            case R.id.sta_tv_address:
                startActivity(Updapeaddress.class, null);
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

    private void setTextviewColor(int index) {
        for (int i = 0; i < ttvv.length; i++) {
            if (index == i) {
                ttvv[i].setTextColor(0xff2c82df);
            } else {
                ttvv[i].setTextColor(0xff282828);
            }
        }
    }


    private class MyAdapterPro extends RecyclerView.Adapter<MyAdapterPro.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(StationAty.this).inflate(R.layout.item_station_pro, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            imageLoader.disPlay(holder.imgv, maps.get(position).get("cover"));
            holder.tv_name.setText(maps.get(position).get("goods_name"));
            holder.tv_attr.setText("规格:" + maps.get(position).get("attr") + "L");
            holder.tv_price.setText("￥" + maps.get(position).get("goods_price"));
            holder.tv_num.setText(maps.get(position).get("volume"));
            if (maps.get(position).get("is_promotion").equals("1")) {
                holder.imgv_pro.setVisibility(View.VISIBLE);
            } else {
                holder.imgv_pro.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_stapro_imgv)
            private ImageView imgv;
            @ViewInject(R.id.item_stapro_imgv_promotion)
            private ImageView imgv_pro;
            @ViewInject(R.id.item_sta_pro_tv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_stapro_tv_attr)
            private TextView tv_attr;
            @ViewInject(R.id.item_stapro_tv_price)
            private TextView tv_price;
            @ViewInject(R.id.item_sta_pro_tv_num)
            private TextView tv_num;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

    private class MyAdapterPeo extends RecyclerView.Adapter<MyAdapterPeo.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(StationAty.this).inflate(R.layout.item_station_peo, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            imageLoader.disPlay(holder.imgv, maps2.get(position).get("head"));
            holder.tv_name.setText(maps2.get(position).get("nickname"));
            holder.tv_time.setText(maps2.get(position).get("avg_time"));
            holder.tv_order.setText(maps2.get(position).get("total_order") + "单");
            holder.imgvy = new ImageView[]{holder.imgv_01, holder.imgv_02, holder.imgv_03, holder.imgv_04, holder.imgv_05};
//            LogUtil.e(maps2.get(position).get("average"));
            int average = Integer.parseInt(maps2.get(position).get("average"));
            holder.setXin(average);
        }

        @Override
        public int getItemCount() {
            return maps2.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView[] imgvy;

            @ViewInject(R.id.item_sta_peo_imgv_head)
            private ImageView imgv;
            @ViewInject(R.id.item_sta_peo_01)
            private ImageView imgv_01;
            @ViewInject(R.id.item_sta_peo_02)
            private ImageView imgv_02;
            @ViewInject(R.id.item_sta_peo_03)
            private ImageView imgv_03;
            @ViewInject(R.id.item_sta_peo_04)
            private ImageView imgv_04;
            @ViewInject(R.id.item_sta_peo_05)
            private ImageView imgv_05;
            @ViewInject(R.id.item_sta_peo_tv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_sta_peo_tv_time)
            private TextView tv_time;
            @ViewInject(R.id.item_sta_peo_tv_order)
            private TextView tv_order;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }

            public void setXin(int index) {
                for (int i = 0; i < imgvy.length; i++) {
                    if (i < index) {
                        imgvy[i].setVisibility(View.VISIBLE);
                    } else {
                        imgvy[i].setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private class MyAdapterRes extends RecyclerView.Adapter<MyAdapterRes.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(StationAty.this).inflate(R.layout.item_station_res, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            imageLoader.disPlay(holder.sta_imgv, maps3.get(position).get("cer"));
            holder.tv_name.setText(maps3.get(position).get("name"));
        }

        @Override
        public int getItemCount() {
            return maps3.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_sta_tv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_sta_imgv)
            private ImageView sta_imgv;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }


}
