package com.toocms.drink5.boss.ui.mine.mines;

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
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.interfaces2.Courier2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.config.Settings;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
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
 * @date 2016/5/19 11:35
 */
public class TotalAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.total_v_line)
    private View sortFlag; // 排序标识

    @ViewInject(R.id.total_tv_jin)
    private TextView tv_jin; // 排序标识
    @ViewInject(R.id.total_tv_ming)
    private TextView tv_ming; // 排序标识
    @ViewInject(R.id.total_tv_all)
    private TextView tv_all; // 排序标识
    @ViewInject(R.id.totla_lv)
    private SwipeToLoadRecyclerView total_lv;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition = 0; // 排序标识位置
    private TextView[] ttvv;
    private String type = "";
    private String type_to = "1";
    private Site site;
    private Courier2 courier2;
    private ArrayList<Map<String, String>> list;
    private MyAdapter myAdapter;
    private int p = 1;
    private TextView tv_totalprice, tv_totaltong, tv_totaltongprice, tv_ticknum, tv_tickprice,
            tv_uprice, tv_tong, tv_tongprice, tv_piao, tv_piaoprice, tv_di, tv_uprice2, tv_tong2,
            tv_tongprice2, tv_piao2, tv_piaoprice2, tv_di2;

    @Override
    protected void initialized() {
        sortFlagWidth = AutoUtils.getPercentWidthSize(100);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 2)) / 3);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);

        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        site = new Site();
        courier2 = new Courier2();
        list = new ArrayList<>();
        myAdapter = new MyAdapter();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_total;
    }

    @Override
    protected void requestData() {
        showProgressContent();
        if (type.equals("boss")) {
            site.count(application.getUserInfo().get("site_id"), type_to, p, this);
        } else {
            courier2.recode(application.getUserInfo().get("c_id"), type_to, this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        total_lv.stopLoadingMore();
        total_lv.stopRefreshing();
        if (params.getUri().contains("count")) {
            Map<String, String> maps = JSONUtils.parseDataToMap(result);
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(maps.get("header"));
            tv_totalprice.setText(map.get("pay_fee"));
            tv_totaltong.setText(map.get("buy_num"));
            tv_totaltongprice.setText("￥" + map.get("goods_amount"));
            tv_ticknum.setText(map.get("buy_ticket"));
            tv_tickprice.setText("￥" + map.get("ticket_price"));
            tv_uprice.setText(map.get("line_a_pay_fee"));
            tv_uprice2.setText(map.get("line_b_pay_fee"));
            tv_tong.setText(map.get("line_a_buy_num"));
            tv_tong2.setText(map.get("line_b_buy_num"));
            tv_tongprice.setText("￥" + map.get("line_a_goods_amount"));
            tv_tongprice2.setText("￥" + map.get("line_b_goods_amount"));
            tv_piao.setText(map.get("line_a_buy_ticket"));
            tv_piao2.setText(map.get("line_b_buy_ticket"));
            tv_piaoprice.setText("￥" + map.get("line_a_ticket_price"));
            tv_piaoprice2.setText("￥" + map.get("line_b_ticket_price"));
            tv_di.setText(map.get("line_a_score"));
            tv_di2.setText(map.get("ticket_num"));
            if (p == 1) {
                list = JSONUtils.parseKeyAndValueToMapList(maps.get("list"));
            } else {
                list.addAll(JSONUtils.parseKeyAndValueToMapList(maps.get("list")));
            }
            myAdapter.notifyDataSetChanged();
        }

        if (params.getUri().contains("recode")) {
            total_lv.stopLoadingMore();
            total_lv.stopRefreshing();
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            Map<String, String> map1 = JSONUtils.parseKeyAndValueToMap(map.get("total"));
            list = JSONUtils.parseKeyAndValueToMapList(map.get("sub"));
            myAdapter.notifyDataSetChanged();
            tv_totalprice.setText(map1.get("income"));
            tv_totaltong.setText(map1.get("num"));
            tv_totaltongprice.setText("￥" + map1.get("amount"));
            tv_ticknum.setText(map1.get("ticket"));
            tv_tickprice.setText("￥" + map1.get("ticketamount"));
            tv_uprice.setText(map1.get("onlineincome"));
            tv_uprice2.setText(map1.get("lineincome"));
            tv_tong.setText(map1.get("onlinenum"));
            tv_tong2.setText(map1.get("linenum"));
            tv_tongprice.setText("￥" + map1.get("onlineamount"));
            tv_tongprice2.setText("￥" + map1.get("lineamount"));
            tv_piao.setText(map1.get("onlinenum"));
            tv_piao2.setText(map1.get("lineticket"));
            tv_piaoprice.setText("￥" + map1.get("onlineticketamount"));
            tv_piaoprice2.setText("￥" + map1.get("lineticketamount"));
            tv_di.setText(map1.get("score"));
            tv_di2.setText(map1.get("use_ticket"));
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        total_lv.stopLoadingMore();
        total_lv.stopRefreshing();
        if (p == 1) {
            list = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        ttvv = new TextView[]{tv_jin, tv_ming, tv_all};
        mActionBar.setTitle("我的统计");
        if (type.equals("boss")) {
            mActionBar.setTitle("水站统计");
        } else {
            mActionBar.setTitle("我的统计");
        }
        sortFlag.setBackgroundColor(Color.parseColor("#2c82df"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        setTextviewColor(sortFlagPosition);

        total_lv.setOnRefreshListener(this);
        total_lv.setOnLoadMoreListener(this);
        total_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        View include_total = View.inflate(this, R.layout.include_total, null);
        tv_totalprice = (TextView) include_total.findViewById(R.id.include_total_totalprice);
        tv_totaltong = (TextView) include_total.findViewById(R.id.include_total_totaltong);
        tv_totaltongprice = (TextView) include_total.findViewById(R.id.include_total_totaltongprice);
        tv_ticknum = (TextView) include_total.findViewById(R.id.include_total_totalticknum);
        tv_tickprice = (TextView) include_total.findViewById(R.id.include_total_totaltickprice);
        tv_uprice = (TextView) include_total.findViewById(R.id.include_total_upprice);
        tv_tong = (TextView) include_total.findViewById(R.id.include_total_tong);
        tv_tongprice = (TextView) include_total.findViewById(R.id.include_total_tongprice);
        tv_piao = (TextView) include_total.findViewById(R.id.include_total_piao);
        tv_piaoprice = (TextView) include_total.findViewById(R.id.include_total_piaoprice);
        tv_di = (TextView) include_total.findViewById(R.id.include_total_di);
        tv_uprice2 = (TextView) include_total.findViewById(R.id.include_total_uppriceb);
        tv_tong2 = (TextView) include_total.findViewById(R.id.include_total_tong2);
        tv_tongprice2 = (TextView) include_total.findViewById(R.id.include_total_tongprice2);
        tv_piao2 = (TextView) include_total.findViewById(R.id.include_total_piao2);
        tv_piaoprice2 = (TextView) include_total.findViewById(R.id.include_total_piaoprice2);
        tv_di2 = (TextView) include_total.findViewById(R.id.include_total_di2);
        total_lv.addHeaderView(include_total);
        total_lv.setAdapter(myAdapter);
    }


    @Event(value = {R.id.total_tv_jin, R.id.total_tv_ming, R.id.total_tv_all})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.total_tv_jin:
                if (sortFlagPosition != 0) {
                    sortFlagPosition = 0;
                    type_to = "1";
                    total_lv.startRefreshing();
                }
                break;
            case R.id.total_tv_ming:
                if (sortFlagPosition != 1) {
                    sortFlagPosition = 1;
                    type_to = "2";
                    total_lv.startRefreshing();
                }
                break;
            case R.id.total_tv_all:
                if (sortFlagPosition != 2) {
                    sortFlagPosition = 2;
                    type_to = "";
                    total_lv.startRefreshing();
                }
                break;
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
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

    @Override
    public void onRefresh() {
        p = 1;
        if (type.equals("boss")) {
            site.count(application.getUserInfo().get("site_id"), type_to, p, this);
        } else {
            courier2.recode(application.getUserInfo().get("c_id"), type_to, this);
        }
    }

    @Override
    public void onLoadMore() {
        if (type.equals("boss")) {
            p++;
            site.count(application.getUserInfo().get("site_id"), type_to, p, this);
        } else {
            total_lv.stopLoadingMore();
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(TotalAty.this).inflate(R.layout.item_total_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (type.equals("boss")) {
                holder.tv_proname.setText(list.get(position).get("goods_name") + "   " + list.get(position).get("attr") + "L");
                holder.tv_price.setText(list.get(position).get("pay_fee"));
                holder.tv_totaltong.setText(list.get(position).get("buy_num"));
                holder.tv_totaltongprice.setText("￥" + list.get(position).get("goods_amount"));
                holder.tv_totaltick.setText(list.get(position).get("buy_ticket"));
                holder.tv_totaltickprice.setText("￥" + list.get(position).get("ticket_price"));
                holder.tv_upprice.setText(list.get(position).get("line_a_pay_fee"));
                holder.tv_upprice2.setText(list.get(position).get("line_b_pay_fee"));
                holder.tv_tong.setText(list.get(position).get("line_a_buy_num"));
                holder.tv_tong2.setText(list.get(position).get("line_b_buy_num"));
                holder.tv_tongprice.setText("￥" + list.get(position).get("line_a_goods_amount"));
                holder.tv_tongprice2.setText("￥" + list.get(position).get("line_b_goods_amount"));
                holder.tv_piao.setText(list.get(position).get("line_a_buy_ticket"));
                holder.tv_piao2.setText(list.get(position).get("line_b_buy_ticket"));
                holder.tv_piaoprice.setText("￥" + list.get(position).get("line_a_ticket_price"));
                holder.tv_piaoprice2.setText("￥" + list.get(position).get("line_b_ticket_price"));
                holder.tv_di.setText(list.get(position).get("line_a_score"));
                holder.tv_di2.setText(list.get(position).get("ticket_num"));
            } else {
                holder.tv_proname.setText(list.get(position).get("name") + "   " + list.get(position).get("attr") + "L");
                holder.tv_price.setText(list.get(position).get("income"));
                holder.tv_totaltong.setText(list.get(position).get("num"));
                holder.tv_totaltongprice.setText("￥" + list.get(position).get("amount"));
                holder.tv_totaltick.setText(list.get(position).get("ticket"));
                holder.tv_totaltickprice.setText("￥" + list.get(position).get("ticketamount"));
                holder.tv_upprice.setText(list.get(position).get("onlineincome"));
                holder.tv_upprice2.setText(list.get(position).get("lineincome"));
                holder.tv_tong.setText(list.get(position).get("onlinenum"));
                holder.tv_tong2.setText(list.get(position).get("linenum"));
                holder.tv_tongprice.setText("￥" + list.get(position).get("onlineamount"));
                holder.tv_tongprice2.setText("￥" + list.get(position).get("lineamount"));
                holder.tv_piao.setText(list.get(position).get("onlineticket"));
                holder.tv_piao2.setText(list.get(position).get("lineticket"));
                holder.tv_piaoprice.setText("￥" + list.get(position).get("onlineticketamount"));
                holder.tv_piaoprice2.setText("￥" + list.get(position).get("lineticketamount"));
                holder.tv_di.setText(list.get(position).get("score"));
                holder.tv_di2.setText(list.get(position).get("use_ticket"));
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_total_proname)
            public TextView tv_proname;
            @ViewInject(R.id.item_total_totalprice)
            public TextView tv_price;
            @ViewInject(R.id.item_total_totaltong)
            public TextView tv_totaltong;
            @ViewInject(R.id.item_total_totaltongprice)
            public TextView tv_totaltongprice;
            @ViewInject(R.id.item_total_totalticknum)
            public TextView tv_totaltick;
            @ViewInject(R.id.item_total_totaltickprice)
            public TextView tv_totaltickprice;
            @ViewInject(R.id.item_total_upprice)
            public TextView tv_upprice;
            @ViewInject(R.id.item_total_uppriceb)
            public TextView tv_upprice2;
            @ViewInject(R.id.item_total_tong)
            public TextView tv_tong;
            @ViewInject(R.id.item_total_tong2)
            public TextView tv_tong2;
            @ViewInject(R.id.item_total_tongprice)
            public TextView tv_tongprice;
            @ViewInject(R.id.item_total_tongprice2)
            public TextView tv_tongprice2;
            @ViewInject(R.id.item_total_piao)
            public TextView tv_piao;
            @ViewInject(R.id.item_total_piao2)
            public TextView tv_piao2;
            @ViewInject(R.id.item_total_piaoprice)
            public TextView tv_piaoprice;
            @ViewInject(R.id.item_total_piaoprice2)
            public TextView tv_piaoprice2;
            @ViewInject(R.id.item_total_di)
            public TextView tv_di;
            @ViewInject(R.id.item_total_di2)
            public TextView tv_di2;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

}
