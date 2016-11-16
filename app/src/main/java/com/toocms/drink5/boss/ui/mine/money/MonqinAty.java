package com.toocms.drink5.boss.ui.mine.money;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Pay2;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.card.CardAty;
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
import cn.zero.android.common.util.PreferencesUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/23 12:24
 */
public class MonqinAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.monqin_lv)
    private SwipeToLoadRecyclerView monqin_lv;


    @ViewInject(R.id.monqin_v_line)
    private View sortFlag; // 排序标识

    @ViewInject(R.id.monqin_tv_jin)
    private TextView tv_jin;
    @ViewInject(R.id.money_tv_01)
    private TextView tv_01;
    @ViewInject(R.id.monqin_tv_all)
    private TextView tv_all;
    @ViewInject(R.id.monqi_tv_di)
    private TextView tv_di;
    @ViewInject(R.id.monqi_tv_tui)
    private TextView tv_tui;
    @ViewInject(R.id.monqi_tv_cha)
    private TextView tv_cha;
    @ViewInject(R.id.monqi_etxt_cha)
    private EditText monqi_etxt_cha;
    @ViewInject(R.id.monqi_tv_ok)
    private TextView tv_ok;
    @ViewInject(R.id.monqi_tv_di_01)
    private TextView tv_di_01;
    @ViewInject(R.id.monqi_tv_tui_01)
    private TextView tv_tui_01;
    @ViewInject(R.id.monqi_tv_cha_01)
    private TextView tv_cha_01;
    @ViewInject(R.id.monqin_relay_bottom)
    private RelativeLayout relay_bottom;
    @ViewInject(R.id.monqi_linlay_bottom_01)
    private LinearLayout inlay_bottom_01;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition = 0; // 排序标识位置
    private TextView[] ttvv;
    private Pay2 pay;
    private int p = 1;
    private ArrayList<Map<String, String>> maps1;
    private MyAdapter myAdapter;
    private int balance;
    private ImageLoader imageLoader;
    private double ooo;
    private double score_total; //京币抵扣
    private double award_total;
    private String pay_type;
    private ArrayList<Map<String, String>> all_list;
    private StringBuffer buffer_applay;
    private StringBuffer buffer_jinbi_applay;
    private double applay_total;
    private String price_b; //对账结算本期可提
    private String is_check = "";
    private String pay_id = "";
    private String award_total_go;
    private String score_total_go;
    private ArrayList<Map<String, String>> maps_order;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_monqi;
    }

    @Override
    protected void initialized() {
        sortFlagWidth = AutoUtils.getPercentWidthSize(100);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 1)) / 2);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
        pay = new Pay2();
        maps1 = new ArrayList<>();
        myAdapter = new MyAdapter();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        pay_type = getIntent().getStringExtra("type");
        award_total_go = getIntent().getStringExtra("award_total");
        score_total_go = getIntent().getStringExtra("score_total");
        all_list = new ArrayList<>();
        buffer_applay = new StringBuffer();
        buffer_jinbi_applay = new StringBuffer();
        maps_order = new ArrayList<>();
    }

    @Override
    protected void requestData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        if (pay_type.equals("pay_applay")) {   //余额未结算
            pay.withdrawIndex(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                    PreferencesUtils.getString(this, "latitude"), p, this);
        } else {
            pay.index(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                    PreferencesUtils.getString(this, "latitude"), p, this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        monqin_lv.stopRefreshing();
        monqin_lv.stopLoadingMore();
        if (!params.getUri().contains("payS")) {
            if (params.getUri().contains("Pay/pay")) {
                String order_sn = JSONUtils.parseDataToMap(result).get("order_id");
                Bundle bundle = new Bundle();
                bundle.putString("type", "pay_jin");
//                bundle.putString("money", ooo + "");
                bundle.putString("order_sn", order_sn);
                startActivity(Pay2Aty.class, bundle);
            }
        }
        if (params.getUri().contains("payS")) {
            monqin_lv.startRefreshing();
        }
        if (params.getUri().contains("ok")) {
            monqin_lv.startRefreshing();
        }
        if (!params.getUri().contains("Pay/indexS")) {    //京币未结算
            if (params.getUri().contains("Pay/index")) {
                imgv_empty.setVisibility(View.GONE);
                tv_empty.setVisibility(View.GONE);
                inlay_bottom_01.setVisibility(View.VISIBLE);
                relay_bottom.setVisibility(View.VISIBLE);
                Map<String, String> map = JSONUtils.parseDataToMap(result);
                if (map == null) {
                    inlay_bottom_01.setVisibility(View.GONE);
                    relay_bottom.setVisibility(View.GONE);
                }
                maps_order = JSONUtils.parseKeyAndValueToMapList(map.get("order_ids"));
                if (maps_order != null) {
                    if (maps_order.size() > 0) {
                        int k = 1;
                        for (int i = 0; i < maps_order.size(); i++) {
                            if (k != 1) {
                                buffer_jinbi_applay.append(",");
                            }
                            buffer_jinbi_applay.append(maps_order.get(i).get("order_id"));
                            k++;
                        }
                    }
                }
                Map<String, String> map1_ischeck = JSONUtils.parseKeyAndValueToMap(map.get("is_check"));
                if (map1_ischeck != null) {
                    is_check = map1_ischeck.get("is_check");
                    pay_id = map1_ischeck.get("pay_id");
                }
                tv_di.setText(map.get("score_total"));
                tv_tui.setText(map.get("award_total"));
                tv_cha.setText(map.get("diff"));
                balance = Integer.parseInt(map.get("balance"));
                score_total = Double.parseDouble(map.get("score_total"));//京币抵扣
                award_total = Double.parseDouble(map.get("award_total"));
                if (score_total > award_total) {   //提现
                    switch (is_check) {
                        case "0":
                            tv_ok.setText("申请结算");
                            break;
                        case "1":
                            tv_ok.setText("申请结算");
                            break;
                        case "":
                            tv_ok.setText("申请结算");
                            break;
                        case "3":
                            tv_ok.setText("待审核");
                            break;
                        case "4":
                            tv_ok.setText("确认收款");
                            break;
                    }
                    ooo = score_total - award_total;
                } else {                            //支付
                    ooo = award_total - score_total;
                    tv_ok.setText("结算支付");
                }
                monqi_etxt_cha.setText(ooo + "");
                if (p == 1) {
                    maps1 = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
                } else {
                    maps1.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("list")));
                }
                myAdapter.notifyDataSetChanged();
            }
        }
        if (params.getUri().contains("withdrawIndex")) {  //余额未结算
            imgv_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            inlay_bottom_01.setVisibility(View.VISIBLE);
            relay_bottom.setVisibility(View.VISIBLE);
            tv_ok.setText("确定");
            Map<String, String> map = JSONUtils.parseDataToMap(result);
            maps_order = JSONUtils.parseKeyAndValueToMapList(map.get("order_ids"));
            if (maps_order != null) {
                if (maps_order.size() > 0) {
                    int k = 1;
                    for (int i = 0; i < maps_order.size(); i++) {
                        if (k != 1) {
                            buffer_applay.append(",");
                        }
                        buffer_applay.append(maps_order.get(i).get("order_id"));
                        k++;
                    }
                }
            }
            tv_di.setText(map.get("price_a"));
            tv_tui.setText(map.get("price_b"));
            tv_cha.setText(map.get("total"));
            applay_total = Double.parseDouble(map.get("total"));
            monqi_etxt_cha.setText(applay_total + "");
            if (p == 1) {
                maps1 = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
            } else {
                maps1.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("list")));
            }
            myAdapter.notifyDataSetChanged();
        }
        if (params.getUri().contains("withdrawB") || params.getUri().contains("Pay/indexS")) {  //已结算
            imgv_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            inlay_bottom_01.setVisibility(View.GONE);
            relay_bottom.setVisibility(View.GONE);
            ArrayList<Map<String, String>> maps = JSONUtils.parseDataToMapList(result);
            ArrayList<Map<String, String>> total = new ArrayList<>();
            for (int i = 0; i < maps.size(); i++) {
                Map<String, String> mm = new HashMap<>();
                mm.put("type", "1");
                mm.put("pay_fee_a", maps.get(i).get("pay_fee_a"));
                mm.put("pay_fee_b", maps.get(i).get("pay_fee_b"));
                mm.put("pay_fee_c", maps.get(i).get("pay_fee_c"));
                mm.put("pay_fee_d", maps.get(i).get("pay_fee_d"));
                total.add(mm);
                ArrayList<Map<String, String>> order_list = JSONUtils.parseKeyAndValueToMapList(maps.get(i).get("order_list"));
                for (int k = 0; k < order_list.size(); k++) {
                    order_list.get(k).put("type", "2");
                }
                total.addAll(order_list);
                if (p == 1) {
                    all_list = new ArrayList<>();
                    all_list.addAll(total);
                } else {
                    all_list.addAll(total);
                }
            }
            myAdapter.notifyDataSetChanged();
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        monqin_lv.stopRefreshing();
        monqin_lv.stopLoadingMore();
        removeProgressContent();
        removeProgressDialog();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            inlay_bottom_01.setVisibility(View.GONE);
            relay_bottom.setVisibility(View.GONE);
            if (sortFlagPosition == 0) {
                maps1 = new ArrayList<>();
            } else {
                all_list = new ArrayList<>();
            }
            myAdapter.notifyDataSetChanged();
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (pay_type.equals("pay_applay")) {
            mActionBar.setTitle("对账结算");
        } else {
            mActionBar.setTitle("京币对账结算");
        }
        if (pay_type.equals("pay_applay")) {
            tv_di_01.setText("上期未提");
            tv_tui_01.setText("本期可提");
            tv_cha_01.setText("合计");
            tv_01.setText("可提(元)");
        }
        ttvv = new TextView[]{tv_jin, tv_all};
        sortFlag.setBackgroundColor(Color.parseColor("#2c82df"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        setTextviewColor(sortFlagPosition);

        monqin_lv.setOnRefreshListener(this);
        monqin_lv.setOnLoadMoreListener(this);
        monqin_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        monqin_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
//                startActivity(monqindetailsAty.class, null);
            }
        });
        monqin_lv.setAdapter(myAdapter);
//        if (maps_order.size() == 0) {
//            LogUtil.e("8888888888888888888");
//            monqi_etxt_cha.setClickable(false);
//            monqi_etxt_cha.setFocusable(false);
//        } else {
//            LogUtil.e("111111111111111111111");
//            monqi_etxt_cha.setClickable(true);
//        }
    }

    @Event(value = {R.id.monqin_tv_jin, R.id.monqin_tv_all, R.id.monqi_tv_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.monqin_tv_jin:
                if (sortFlagPosition != 0) {
                    sortFlagPosition = 0;
                    monqin_lv.startRefreshing();
                }
                break;
            case R.id.monqin_tv_all:
                if (sortFlagPosition != 1) {
                    sortFlagPosition = 1;
                    monqin_lv.startRefreshing();
                }
                break;
            case R.id.monqi_tv_ok:
                if (TextUtils.isEmpty(monqi_etxt_cha.getText().toString())) {
                    showToast("金额不能为空");
                    return;
                }
                Bundle bundle = new Bundle();
                double vv = Double.parseDouble(monqi_etxt_cha.getText().toString());
                if (pay_type.equals("pay_applay")) { //余额未结算
                    if (vv > applay_total) {
                        showToast("余额不足");
                    } else if (vv != 0) {
                        bundle.putString("type", "applay");
                        bundle.putString("award_total", award_total_go);
                        bundle.putString("score_total", score_total_go);
                        bundle.putString("order_ids", buffer_applay.toString());
                        bundle.putString("money", vv + "");
                        bundle.putString("pay_fee_b", applay_total + ""); //可提
                        bundle.putString("pay_fee_d", applay_total - vv + "");//未提
                        startActivity(CardAty.class, bundle);
                    } else {
                        showToast("非法操作");
                    }
                } else {
                    if (score_total > award_total) {
                        switch (is_check) {
                            case "0":
                                if (vv > ooo) {
                                    showToast("余额不足");
                                } else {
                                    showProgressDialog();
                                    pay.payS(application.getUserInfo().get("site_id"), buffer_jinbi_applay.toString(),
                                            ooo + "", ooo - vv + "", vv + "",
                                            MonqinAty.this);
                                }
                                break;
                            case "1":
                                if (vv > ooo) {
                                    showToast("余额不足");
                                } else {
                                    showProgressDialog();
                                    pay.payS(application.getUserInfo().get("site_id"), buffer_jinbi_applay.toString(),
                                            ooo + "", ooo - vv + "", vv + "",
                                            MonqinAty.this);
                                }
                                break;
                            case "":
                                if (vv > ooo) {
                                    showToast("余额不足");
                                } else {
                                    showProgressDialog();
                                    pay.payS(application.getUserInfo().get("site_id"), buffer_jinbi_applay.toString(),
                                            ooo + "", ooo - vv + "", vv + "",
                                            MonqinAty.this);
                                }
                                break;
                            case "3":
                                showToast("审核中");
                                break;
                            case "4":
                                showProgressDialog();
                                pay.ok(pay_id, this);
                                tv_ok.setText("确认收款");
                                break;
                        }
                    } else if (score_total < award_total) {
                        showProgressDialog();
                        pay.pay(application.getUserInfo().get("site_id"), vv + "", buffer_jinbi_applay.toString(),
                                ooo + "", ooo - vv + "", MonqinAty.this);
                    } else {
                        showToast("非法操作");
                    }
                }
                break;
        }
    }

    private void startTranslate(final View view, float endX) {
        float startx = view.getX();
        ValueAnimator animator = ValueAnimator.ofFloat(startx, endX);
        animator.setTarget(view);
        animator.setDuration(100).start();
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
        if (pay_type.equals("pay_applay")) {
            if (sortFlagPosition == 0) {
                pay.withdrawIndex(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            } else {
                pay.withdrawB(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            }
        } else {
            if (sortFlagPosition == 0) {
                pay.index(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            } else {
                pay.indexS(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            }
        }
    }

    @Override
    public void onLoadMore() {
        p++;
        if (pay_type.equals("pay_applay")) {
            if (sortFlagPosition == 0) {
                pay.withdrawIndex(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            } else {
                pay.withdrawB(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            }
        } else {
            if (sortFlagPosition == 0) {
                pay.index(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            } else {
                pay.indexS(application.getUserInfo().get("site_id"), PreferencesUtils.getString(this, "longitude"),
                        PreferencesUtils.getString(this, "latitude"), p, this);
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MonqinAty.this).inflate(R.layout.item_monqin_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (sortFlagPosition == 0) {
                holder.linlay_top.setVisibility(View.VISIBLE);
                holder.linlay_bottom.setVisibility(View.GONE);
                imageLoader.disPlay(holder.imgv_pro, maps1.get(position).get("cover"));
                holder.tv_time.setText(maps1.get(position).get("create_time"));
                holder.tv_proname.setText(maps1.get(position).get("goods_name"));
                holder.tv_attr.setText("(" + maps1.get(position).get("attr") + "L)");
                holder.tv_num.setText("X" + maps1.get(position).get("buy_num"));
                holder.tv_price.setText("￥" + maps1.get(position).get("pay_fee"));
                holder.tv_nickname.setText(maps1.get(position).get("consignee"));
                holder.tv_phone.setText(maps1.get(position).get("contact"));
                holder.tv_address.setText(maps1.get(position).get("address"));
                holder.tv_dimoney.setText(maps1.get(position).get("score"));
                holder.tv_tuimoney.setText(maps1.get(position).get("award"));
                if (pay_type.equals("pay_applay")) {
                    holder.tv_tuijian.setVisibility(View.GONE);
                } else {
                    holder.tv_tuijian.setText(maps1.get(position).get("re_name"));
                }
                if (maps1.get(position).get("award").equals("0")) {
                    holder.imgv_xin.setVisibility(View.VISIBLE);
                } else {
                    holder.imgv_xin.setVisibility(View.GONE);
                }
            } else {
                if (all_list.get(position).get("type").equals("1")) {
                    holder.linlay_top.setVisibility(View.GONE);
                    holder.v_line.setVisibility(View.GONE);
                    holder.linlay_bottom.setVisibility(View.VISIBLE);
                    holder.tv_1.setText(all_list.get(position).get("pay_fee_a"));
                    holder.tv_2.setText(all_list.get(position).get("pay_fee_b"));
                    holder.tv_3.setText(all_list.get(position).get("pay_fee_c"));
                    holder.tv_4.setText(all_list.get(position).get("pay_fee_d"));
                } else {
                    if (position > 0) {
                        if (all_list.get(position - 1).get("type").equals("1")) {
                            holder.v_line.setVisibility(View.GONE);
                        } else {
                            holder.v_line.setVisibility(View.VISIBLE);
                        }
                    }
                    holder.linlay_top.setVisibility(View.VISIBLE);
                    holder.linlay_bottom.setVisibility(View.GONE);
                    imageLoader.disPlay(holder.imgv_pro, all_list.get(position).get("cover"));
                    holder.tv_time.setText(all_list.get(position).get("create_time"));
                    holder.tv_proname.setText(all_list.get(position).get("goods_name"));
                    holder.tv_attr.setText("(" + all_list.get(position).get("attr") + "L)");
                    holder.tv_num.setText("X" + all_list.get(position).get("buy_num"));
                    holder.tv_price.setText("￥" + all_list.get(position).get("pay_fee"));
                    holder.tv_nickname.setText(all_list.get(position).get("consignee"));
                    holder.tv_phone.setText(all_list.get(position).get("contact"));
                    holder.tv_address.setText(all_list.get(position).get("address"));
                    holder.tv_dimoney.setText(all_list.get(position).get("score"));
                    holder.tv_tuimoney.setText(all_list.get(position).get("award"));
                    if (pay_type.equals("pay_applay")) {
                        holder.tv_tuijian.setVisibility(View.GONE);
                    } else {
                        holder.tv_tuijian.setText(all_list.get(position).get("re_name"));
                    }
                    if (all_list.get(position).get("award").equals("0")) {
                        holder.imgv_xin.setVisibility(View.VISIBLE);
                    } else {
                        holder.imgv_xin.setVisibility(View.GONE);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (sortFlagPosition == 0) {
                return maps1.size();
            } else {
                return all_list.size();
            }

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_monqin_imgv_xin)
            ImageView imgv_xin;
            @ViewInject(R.id.item_monqinlv_imgv_pro)
            ImageView imgv_pro;
            @ViewInject(R.id.item_monqin_tv_time)
            TextView tv_time;
            @ViewInject(R.id.item_monqin_tv_tuijian)
            TextView tv_tuijian;
            @ViewInject(R.id.item_monqinlv_tv_proname)
            TextView tv_proname;
            @ViewInject(R.id.item_monqinlv_tv_attr)
            TextView tv_attr;
            @ViewInject(R.id.item_monqinlv_tv_num)
            TextView tv_num;
            @ViewInject(R.id.item_monqinlv_tv_price)
            TextView tv_price;
            @ViewInject(R.id.item_monqinlv_tv_name)
            TextView tv_nickname;
            @ViewInject(R.id.item_monqinlv_tv_phone)
            TextView tv_phone;
            @ViewInject(R.id.item_monqinlv_tv_address)
            TextView tv_address;
            @ViewInject(R.id.item_monqinlv_tv_dimoney)
            TextView tv_dimoney;
            @ViewInject(R.id.item_monqinlv_tv_tuimoney)
            TextView tv_tuimoney;
            @ViewInject(R.id.item_monqi_linlay_top)
            LinearLayout linlay_top;
            @ViewInject(R.id.item_monqi_linlay_bottom)
            LinearLayout linlay_bottom;
            @ViewInject(R.id.item_monqinlv_tv_1)
            TextView tv_1;
            @ViewInject(R.id.item_monqinlv_tv_2)
            TextView tv_2;
            @ViewInject(R.id.item_monqinlv_tv_3)
            TextView tv_3;
            @ViewInject(R.id.item_monqinlv_tv_4)
            TextView tv_4;
            @ViewInject(R.id.item_monqi_v)
            View v_line;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

}
