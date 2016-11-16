package com.toocms.drink5.boss.ui.mine.mines;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.ui.BaseAty;
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
import cn.zero.android.common.util.PreferencesUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;

/**
 * @author Zero
 * @date 2016/5/19 7:29
 */
public class RecordAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.record_lv)
    private SwipeToLoadRecyclerView record_lv;
    @ViewInject(R.id.record_tv_title)
    private TextView tv_title;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;


    private String type = "";
    private Courier2 courier2;
    private int p = 1;
    private ArrayList<Map<String, String>> list;
    private MyAdapter myAdapter;
    private String start_time = "";
    private String end_time = "";
    private String type_t = "";
    private Intent intent;
    private Site site;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_record;
    }

    @Override
    protected void initialized() {
        courier2 = new Courier2();
        site = new Site();
        list = new ArrayList<>();
        myAdapter = new MyAdapter();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        if (type.equals("shui")) {
            courier2.orderLog(application.getUserInfo().get("c_id"), start_time, end_time, type_t, p, this);
        } else {
            site.orderLog(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                    PreferencesUtils.getString(this, "latitude"), type_t, start_time, end_time, p, this);
        }
    }


    @Override
    public void onComplete(RequestParams params, String result) {
        record_lv.stopLoadingMore();
        record_lv.stopRefreshing();
        if (params.getUri().contains("Courier/orderLog")) {
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            if (map != null) {
                if (p == 1) {
                    list = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
                } else {
                    list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("list")));
                }
                myAdapter.notifyDataSetChanged();
            }
        }
        if (params.getUri().contains("Site/orderLog")) {
            if (p == 1) {
                list = JSONUtils.parseDataToMapList(result);
            } else {
                list.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapter.notifyDataSetChanged();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        record_lv.stopLoadingMore();
        record_lv.stopRefreshing();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            list = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        if (type.equals("boss")) {
            tv_title.setText("水站记录");
        } else if (type.equals("shui")) {
            tv_title.setText("配送记录");
        }
        record_lv.setOnRefreshListener(this);
        record_lv.setOnLoadMoreListener(this);
        record_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        record_lv.setAdapter(myAdapter);
    }

    @Event(value = {R.id.record_imgv_back, R.id.record_tv_xuan})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.record_imgv_back:
                finish();
                break;
            case R.id.record_tv_xuan:
                Intent intent = new Intent(this, FilterAty.class);
                startActivityForResult(intent, 2088);
                break;
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        if (type.equals("shui")) {
            courier2.orderLog(application.getUserInfo().get("c_id"), start_time, end_time, type_t, p, this);

        } else {
            site.orderLog(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                    PreferencesUtils.getString(this, "latitude"), type_t, start_time, end_time, p, this);
        }
    }

    @Override
    public void onLoadMore() {
        p++;
        if (type.equals("shui")) {
            courier2.orderLog(application.getUserInfo().get("c_id"), start_time, end_time, type_t, p, this);

        } else {
            site.orderLog(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                    PreferencesUtils.getString(this, "latitude"), type_t, start_time, end_time, p, this);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RecordAty.this).inflate(R.layout.item_page_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv_ok2.setVisibility(View.GONE);
            holder.tv_ok.setVisibility(View.GONE);
            holder.fb_ok2.setVisibility(View.GONE);
            holder.fb_ok.setVisibility(View.GONE);

            holder.imgv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecordAty.this.callPhone(list.get(position).get("contact"));
                }
            });
            if (type.equals("shui")) {
                float distance = Float.parseFloat(list.get(position).get("duration_distance")) / 1000;
                holder.tv_distance.setText(distance + "km");
                if (list.get(position).get("isFirst").equals("1")) {
                    holder.imgv_xin.setVisibility(View.VISIBLE);
                } else {
                    holder.imgv_xin.setVisibility(View.GONE);
                }
                switch (list.get(position).get("pay_type")) {
                    case "0":
                        holder.tv_huodao.setText("(待支付)");
                        break;
                    case "1":
                        holder.tv_huodao.setText("(微信支付)");
                        break;
                    case "2":
                        holder.tv_huodao.setText("(支付宝支付)");
                        break;
                    case "3":
                        holder.tv_huodao.setText("(货到付款)");
                        break;
                    case "4":
                        holder.tv_huodao.setText("(余额支付)");
                        break;
                    case "5":
                        holder.tv_huodao.setText("(水票支付)");
                        break;
                }
                switch (list.get(position).get("order_type")) {
                    case "1":
                        holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state4);
                        break;
                    case "2":
                        break;
                    case "3":
                        holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state2);
                        break;
                    case "4":
                        holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state4);
                        break;
                    case "5":
                        break;
                }
            } else {
                holder.tv_huodao.setText("(" + list.get(position).get("pay_name") + ")");
                float distance = Float.parseFloat(list.get(position).get("distance")) / 1000;
                holder.tv_distance.setText(distance + "km");
                if (list.get(position).get("is_first").equals("1")) {
                    holder.imgv_xin.setVisibility(View.VISIBLE);
                } else {
                    holder.imgv_xin.setVisibility(View.GONE);
                }
                switch (list.get(position).get("source")) {
                    case "1":
                        holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state4);
                        break;
                    case "2":
                        holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state3);
                        break;
                }
            }
            holder.tv_time.setText(list.get(position).get("create_time"));
            holder.tv_xiaofei.setText("￥" + list.get(position).get("reward"));
            holder.tv_proname.setText(list.get(position).get("goods_name"));
            holder.tv_attr.setText("(" + list.get(position).get("attr") + "L)");
            holder.tv_num.setText("x" + list.get(position).get("buy_num"));
            holder.tv_name.setText(list.get(position).get("consignee"));
            holder.tv_tel.setText(list.get(position).get("contact"));
            holder.tv_address.setText(list.get(position).get("address"));
            holder.tv_toallprice.setText("￥" + list.get(position).get("pay_fee"));
            holder.tv_state.setVisibility(View.VISIBLE);
            imageLoader.disPlay(holder.imgv_pro, list.get(position).get("cover"));
//            holder.imgv_xin.setVisibility(View.GONE);
            switch (list.get(position).get("status")) {
                case "0":
                    holder.tv_state.setText("待接单");
                    break;
                case "1":
                    holder.tv_state.setText("已完成");
                    break;
                case "2":
                    holder.tv_state.setText("已接单");
                    break;
                case "3":
                    holder.tv_state.setText("配送中");
                    break;
                case "4":
                    holder.tv_state.setText("已取消");
                    break;
                case "5":
                    holder.tv_state.setText("已拒绝");
                    break;
                case "6":
                    holder.tv_state.setText("已关闭");
                    break;
                case "7":
                    holder.tv_state.setText("已送达");
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_pagelv_fb_list)
            Button fb_ok;
            @ViewInject(R.id.item_pagelv_fb_list2)
            Button fb_ok2;
            @ViewInject(R.id.item_pagelv_tv_list)
            TextView tv_ok;
            @ViewInject(R.id.item_pagelv_tv_list2)
            TextView tv_ok2;
            @ViewInject(R.id.item_page_tv_state)
            TextView tv_state;

            @ViewInject(R.id.item_page_imgv_xin)
            public ImageView imgv_xin;
            @ViewInject(R.id.item_page_imgv_state)
            public ImageView imgv_state;
            @ViewInject(R.id.item_pagelv_imgv_pro)
            public ImageView imgv_pro;
            @ViewInject(R.id.item_pagelv_imgv_phone)
            public ImageView imgv_phone;
            @ViewInject(R.id.item_page_tv_time)
            public TextView tv_time;
            @ViewInject(R.id.item_page_tv_money2)
            public TextView tv_xiaofei;
            @ViewInject(R.id.item_pagelv_tv_proname)
            public TextView tv_proname;
            @ViewInject(R.id.item_pagelv_tv_pronum)
            public TextView tv_attr;
            @ViewInject(R.id.item_pagelv_tv_pronum2)
            public TextView tv_num;
            @ViewInject(R.id.item_pagelv_tv_name)
            public TextView tv_name;
            @ViewInject(R.id.item_pagelv_tv_tel)
            public TextView tv_tel;
            @ViewInject(R.id.item_pagelv_tv_address)
            public TextView tv_address;
            @ViewInject(R.id.item_pagelv_tv_distance)
            public TextView tv_distance;
            @ViewInject(R.id.item_pagelv_tv_toallprice)
            public TextView tv_toallprice;
            @ViewInject(R.id.item_pagelv_tv_huodao)
            public TextView tv_huodao;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 2088) {
            start_time = data.getStringExtra("start_time");
            end_time = data.getStringExtra("end_time");
            if (type.equals("shui")) {
                type_t = data.getStringExtra("type");
            } else {
                switch (data.getStringExtra("type")) {
                    case "1":
                        type_t = "pay_type_b";
                        break;
                    case "2":
                        type_t = "pay_type_a";
                        break;
                    case "3":
                        type_t = "status";
                        break;
                    case "4":
                        type_t = "order_type";
                        break;
                    case "5":
                        type_t = "barrel_num";
                        break;
                    case "0":
                        type_t = "";
                        break;
                    case "6":
                        type_t = "status_a";
                        break;
                }
            }
            record_lv.startRefreshing();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
