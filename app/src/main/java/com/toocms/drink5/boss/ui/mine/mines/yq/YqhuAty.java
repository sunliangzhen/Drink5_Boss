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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.interfaces2.Contact;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.config.Settings;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
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
 * @date 2016/5/19 17:00
 */
public class YqhuAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.yqhu_v_line)
    private View sortFlag; // 排序标识
    @ViewInject(R.id.yqhu_lv)
    private SwipeToLoadRecyclerView yqhu_lv;
    @ViewInject(R.id.yqhu_tv_today)
    private TextView tv_today;
    @ViewInject(R.id.yqhu_tv_total)
    private TextView tv_total;
    @ViewInject(R.id.qyhu_v_line)
    private View qyhu_v_line;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    private Site site;
    private String type = "1";
    private MyAdapter myAdapter;
    private Map<String, String> map;
    private ArrayList<Map<String, String>> list;
    private Map<String, String> map_m;
    private int p = 1;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition; // 排序标识位置
    private String type_can = "";
    private Contact contact;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_yqhu;
    }

    @Override
    protected void initialized() {
        site = new Site();
        sortFlagWidth = AutoUtils.getPercentWidthSize(120);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 1)) / 2);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
        myAdapter = new MyAdapter();
        list = new ArrayList<>();
        if (getIntent().hasExtra("type")) {
            type_can = getIntent().getStringExtra("type");
        }
        contact = new Contact();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        if (type_can.equals("shui")) {
            contact.isCustomer(application.getUserInfo().get("c_id"), type, p, this);
        } else {
            site.memberA(application.getUserInfo().get("site_id"), type, p, this);
        }
    }

    private ArrayList<Map<String, String>> total = new ArrayList<>();

    @Override
    public void onComplete(RequestParams params, String result) {
        imgv_empty.setVisibility(View.GONE);
        tv_empty.setVisibility(View.GONE);
        yqhu_lv.stopRefreshing();
        yqhu_lv.stopLoadingMore();
        if (type.equals("2")) {
            qyhu_v_line.setVisibility(View.GONE);
        } else {
            qyhu_v_line.setVisibility(View.VISIBLE);
        }
        if (params.getUri().contains("memberA")) {
            map = JSONUtils.parseDataToMap(result);
            list = JSONUtils.parseKeyAndValueToMapList(map.get("list"));

            if (p == 1) {
                if (list.size() < 1) {
                    imgv_empty.setVisibility(View.VISIBLE);
                    tv_empty.setVisibility(View.VISIBLE);
                } else {
                    imgv_empty.setVisibility(View.GONE);
                    tv_empty.setVisibility(View.GONE);
                }
            }
            Map<String, String> map_1 = JSONUtils.parseKeyAndValueToMap(this.map.get("m"));
            if (type.equals("2")) {
                ArrayList<Map<String, String>> total2 = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = JSONUtils.parseKeyAndValueToMap(list.get(i).get("time_list"));
                    Map<String, String> mm = new HashMap<>();
                    mm.put("type", "1");
                    mm.put("dTime", map.get("time"));
                    mm.put("today_count", map.get("member"));
                    mm.put("today_award", map.get("price"));
                    total2.add(mm);
                    ArrayList<Map<String, String>> content = JSONUtils.parseKeyAndValueToMapList(list.get(i).get("time_line_list"));
                    for (int k = 0; k < content.size(); k++) {
                        content.get(k).put("type", "2");
                    }
                    total2.addAll(content);
                }
                if (p == 1) {
                    total = new ArrayList<>();
                    total.addAll(total2);
                } else {
                    total.addAll(total2);
                }
                tv_today.setText(map_1.get("total_member"));
                tv_total.setText(map_1.get("curr_award"));
            } else {
                ArrayList<Map<String, String>> content = new ArrayList<>();
                ArrayList<Map<String, String>> content2 = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    content = JSONUtils.parseKeyAndValueToMapList(list.get(i).get("time_line_list"));
                    content2.addAll(content);
                }
                if (p == 1) {
                    total = new ArrayList<>();
                    total.addAll(content2);
                } else {
                    total.addAll(content2);
                }
                tv_today.setText(map_1.get("total_member"));
                tv_total.setText(map_1.get("curr_award"));
            }
        }
        if (params.getUri().contains("isCustomer")) {
            map = JSONUtils.parseDataToMap(result);
            if (type.equals("2")) {
                list = JSONUtils.parseKeyAndValueToMapList(map.get("orders"));
                if (p == 1) {
                    if (list.size() < 1) {
                        imgv_empty.setVisibility(View.VISIBLE);
                        tv_empty.setVisibility(View.VISIBLE);
                    } else {
                        imgv_empty.setVisibility(View.GONE);
                        tv_empty.setVisibility(View.GONE);
                    }
                }
                ArrayList<Map<String, String>> total2 = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> mm = new HashMap<>();
                    mm.put("type", "1");
                    mm.put("dTime", list.get(i).get("dTime"));
                    mm.put("today_count", list.get(i).get("today_count"));
                    mm.put("today_award", list.get(i).get("today_award"));
                    total2.add(mm);
                    ArrayList<Map<String, String>> content = JSONUtils.parseKeyAndValueToMapList(list.get(i).get("content"));
                    for (int k = 0; k < content.size(); k++) {
                        content.get(k).put("type", "2");
                    }
                    total2.addAll(content);
                }
                if (p == 1) {
                    total = new ArrayList<>();
                    total.addAll(total2);
                } else {
                    total.addAll(total2);
                }
                tv_today.setText(map.get("count"));
                tv_total.setText(map.get("award"));
            } else {
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
                tv_today.setText(map.get("count"));
                tv_total.setText(map.get("award"));
            }
        }
        myAdapter.notifyDataSetChanged();
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        yqhu_lv.stopRefreshing();
        yqhu_lv.stopLoadingMore();
        removeProgressDialog();
        removeProgressContent();
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

        yqhu_lv.setOnRefreshListener(this);
        yqhu_lv.setOnLoadMoreListener(this);
        yqhu_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        yqhu_lv.setAdapter(myAdapter);
    }

    @Override
    public void onRefresh() {
        p = 1;
        if (type_can.equals("shui")) {
            contact.isCustomer(application.getUserInfo().get("c_id"), type, p, this);
        } else {
            site.memberA(application.getUserInfo().get("site_id"), type, p, this);
        }
    }

    @Override
    public void onLoadMore() {
        p++;
        if (type_can.equals("shui")) {
            contact.isCustomer(application.getUserInfo().get("c_id"), type, p, this);
        } else {
            site.memberA(application.getUserInfo().get("site_id"), type, p, this);
        }
    }

    @Event(value = {R.id.yqhu_tv_jin, R.id.yqhu_tv_ming})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.yqhu_tv_jin:
                if (sortFlagPosition != 0) {
                    sortFlagPosition = 0;
                    type = "1";
                    yqhu_lv.startRefreshing();
                }
                break;
            case R.id.yqhu_tv_ming:
                if (sortFlagPosition != 1) {
                    sortFlagPosition = 1;
                    type = "2";
                    yqhu_lv.startRefreshing();
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
        public int getItemViewType(int position) {
            if (type.equals("2")) {
                return Integer.parseInt(total.get(position).get("type"));
            } else {
                return super.getItemViewType(position);
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(YqhuAty.this).inflate(R.layout.item_yqhu_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (type_can.equals("shui")) {
                if (type.equals("1")) {
                    holder.relay_01.setVisibility(View.VISIBLE);
                    holder.relay_02.setVisibility(View.GONE);
                    holder.tv_money.setText("+" + list.get(position).get("award") + "元");
                    holder.tv_name.setText(list.get(position).get("nickname"));
                    holder.tv_time.setText(list.get(position).get("create_time"));
                } else {
                    if (getItemViewType(position) == 1) {
                        holder.relay_01.setVisibility(View.GONE);
                        holder.relay_02.setVisibility(View.VISIBLE);
                        holder.tv_time2.setText(total.get(position).get("dTime"));
                        holder.tv_peo2.setText("发展" + total.get(position).get("today_count") + "人");
                        holder.tv_money2.setText("奖励" + total.get(position).get("today_award") + "元");
                    } else {
                        holder.relay_01.setVisibility(View.VISIBLE);
                        holder.relay_02.setVisibility(View.GONE);
                        holder.tv_money.setText("+" + total.get(position).get("award") + "元");
                        holder.tv_name.setText(total.get(position).get("nickname"));
                        holder.tv_time.setText(total.get(position).get("create_time"));
                    }
                }

            } else {
                if (type.equals("1")) {
                    holder.relay_01.setVisibility(View.VISIBLE);
                    holder.relay_02.setVisibility(View.GONE);
                    holder.tv_money.setText("+" + total.get(position).get("award") + "元");
                    holder.tv_name.setText(total.get(position).get("consignee"));
                    holder.tv_time.setText(total.get(position).get("create_time"));
                } else {
                    if (getItemViewType(position) == 1) {
                        holder.relay_01.setVisibility(View.GONE);
                        holder.relay_02.setVisibility(View.VISIBLE);
                        holder.tv_time2.setText(total.get(position).get("dTime"));
                        holder.tv_peo2.setText("发展" + total.get(position).get("today_count") + "人");
                        holder.tv_money2.setText("奖励" + total.get(position).get("today_award") + "元");
                    } else {
                        holder.relay_01.setVisibility(View.VISIBLE);
                        holder.relay_02.setVisibility(View.GONE);
                        holder.tv_money.setText("+" + total.get(position).get("award") + "元");
                        holder.tv_name.setText(total.get(position).get("consignee"));
                        holder.tv_time.setText(total.get(position).get("create_time"));
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (type_can.equals("shui")) {
                if (type.equals("1")) {
                    return list.size();
                } else {
                    return total.size();
                }
            } else {
                return total.size();
            }
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_yqhulv_name)
            TextView tv_name;
            @ViewInject(R.id.item_yqhulv_time)
            TextView tv_time;
            @ViewInject(R.id.item_yqhulv_money)
            TextView tv_money;
            @ViewInject(R.id.item_yhmlv_relay_01)
            RelativeLayout relay_01;
            @ViewInject(R.id.item_yhmlv_relay_02)
            RelativeLayout relay_02;
            @ViewInject(R.id.item_yqhulv_time2)
            TextView tv_time2;
            @ViewInject(R.id.item_yqhulv_peo2)
            TextView tv_peo2;
            @ViewInject(R.id.item_yqhulv_money2)
            TextView tv_money2;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
