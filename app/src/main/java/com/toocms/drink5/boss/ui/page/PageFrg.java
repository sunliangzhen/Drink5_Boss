package com.toocms.drink5.boss.ui.page;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Order;
import com.toocms.drink5.boss.ui.MainAty;
import com.toocms.drink5.boss.ui.prodetilas.Prodetilas;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.ui.BaseFragment;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.common.util.LogUtil;
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
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/18 11:00
 */
public class PageFrg extends BaseFragment implements RadioGroup.OnCheckedChangeListener, OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.page_cbox_time)
    private CheckBox cbox_time;
    @ViewInject(R.id.page_cbox_long)
    private CheckBox cbox_long;
    @ViewInject(R.id.page_cbox_num)
    private CheckBox cbox_num;
    @ViewInject(R.id.page_relay_bttom)
    private RelativeLayout relay_bttom;

    @ViewInject(R.id.page_lv)
    private SwipeToLoadRecyclerView page_lv;

    @ViewInject(R.id.page_rg)
    private RadioGroup page_rg;
    @ViewInject(R.id.page_rdobtn_all)
    private RadioButton rdobtn_all;
    @ViewInject(R.id.page_rdobtn_divide)
    private RadioButton rdobtn_divide;
    @ViewInject(R.id.page_tv_city)
    private TextView tv_city;
    @ViewInject(R.id.page_tv_list)
    private TextView tv_totallist;
    @ViewInject(R.id.page_tv_tong)
    private TextView tv_totaltong;
    @ViewInject(R.id.page_tv_allok)
    private TextView page_tv_allok;
    @ViewInject(R.id.iv_main_head)
    private ImageView iv_main_head;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;
    @ViewInject(R.id.tv_net)
    private TextView tv_net;

    private Drawable rightDrawable;
    private Drawable rightDrawable2;
    private MyAdapter myAdapter;
    private int index = 0;
    private String city;
    private Intent intent;
    private Order order;
    private ImageLoader imageLoader;
    private String type = "1";
    private String data = "1";
    private String data2 = "";
    private String data3 = "";
    private int typee = 1;
    private int p = 1;
    private ArrayList<Map<String, String>> list;
    private ArrayList<Map<String, String>> list2;
    private Map<String, String> map;
    private String geared = "1";

    @Override
    protected int getLayoutResId() {
        return R.layout.frg_page;
    }

    @Override
    protected void initialized() {
        rightDrawable = getResources().getDrawable(R.drawable.selector_page_top);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        rightDrawable2 = getResources().getDrawable(R.drawable.ic_page_topn);
        rightDrawable2.setBounds(0, 0, rightDrawable2.getMinimumWidth(), rightDrawable2.getMinimumHeight());
        order = new Order();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        myAdapter = new MyAdapter();
        list = new ArrayList<>();
        list2 = new ArrayList<>();
    }

    @Override
    protected void requestData() {
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        page_lv.stopRefreshing();
        page_lv.stopLoadingMore();
        if (params.getUri().contains("orderFocus")) {
            map = JSONUtils.parseDataToMap(result);
            ArrayList<Map<String, String>> list_num = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
            if (p == 1) {
                list = new ArrayList<>();
            }
            if (list_num.size() > 0) {
                page_lv.setVisibility(View.VISIBLE);
                imgv_empty.setVisibility(View.GONE);
                tv_empty.setVisibility(View.GONE);
                tv_net.setVisibility(View.GONE);
                relay_bttom.setVisibility(View.VISIBLE);
                if (index == 1) {
                    relay_bttom.setVisibility(View.GONE);
                } else {
                    relay_bttom.setVisibility(View.VISIBLE);
                }
                if (p == 1) {
                    this.list = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
                    if (this.list.size() > 0) {
                        list2 = JSONUtils.parseKeyAndValueToMapList(map.get("classify"));
                    }
                } else {
                    this.list.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("list")));
                    if (JSONUtils.parseKeyAndValueToMapList(map.get("list")).size() > 0) {
                        list2.addAll(JSONUtils.parseKeyAndValueToMapList(map.get("classify")));
                    }
                }
                int num2 = 0;
                tv_totallist.setText("订单： " + list.size() + "单");
                for (int i = 0; i < list.size(); i++) {
                    num2 = num2 + Integer.parseInt(list.get(i).get("buy_num"));
                }
                tv_totaltong.setText(num2 + "");
            } else {
//                if (index == 1) {
//                    relay_bttom.setVisibility(View.GONE);
//                } else {
//                    if (p == 1) {
//                        relay_bttom.setVisibility(View.GONE);
//                    }
//                }
            }
            myAdapter.notifyDataSetChanged();
        }
        if (params.getUri().contains("toOrder")) {
            page_lv.startRefreshing();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        page_lv.stopRefreshing();
        page_lv.stopLoadingMore();
        if (error.get("message").equals("" +
                "没有更多的数据了")) {
            if (p == 1) {
                list = new ArrayList<>();
                myAdapter.notifyDataSetChanged();
                relay_bttom.setVisibility(View.GONE);
//                page_lv.setVisibility(View.GONE);
                tv_net.setVisibility(View.GONE);
                imgv_empty.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onException(Throwable ex) {
        removeProgressDialog();
        removeProgressContent();
        page_lv.stopRefreshing();
        page_lv.stopLoadingMore();
        tv_net.setVisibility(View.VISIBLE);
        tv_empty.setVisibility(View.GONE);
        imgv_empty.setVisibility(View.GONE);
        relay_bttom.setVisibility(View.GONE);
//        page_lv.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        page_rg.setOnCheckedChangeListener(this);

        page_lv.setOnRefreshListener(this);
        page_lv.setOnLoadMoreListener(this);
        page_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 1));
        page_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id", list.get(i).get("order_id"));
                bundle.putString("type_2", "page");
                bundle.putString("type", list.get(i).get("order_type"));
                startActivity(Prodetilas.class, bundle);
            }
        });
        page_lv.setAdapter(myAdapter);
        cbox_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data = "1";
                } else {
                    data = "2";
                }
            }
        });
        cbox_long.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data2 = "1";
                } else {
                    data2 = "2";
                }
            }
        });
        cbox_num.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data3 = "1";
                } else {
                    data3 = "2";
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        p = 1;
        switch (typee) {
            case 1:
                order.orderFocus(application.getUserInfo().get("c_id"), type, data, geared, p, this);
                break;
            case 2:
                order.orderFocus(application.getUserInfo().get("c_id"), type, data2, geared, p, this);
                break;
            case 3:
                order.orderFocus(application.getUserInfo().get("c_id"), type, data3, geared, p, this);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        p++;
        switch (typee) {
            case 1:
                order.orderFocus(application.getUserInfo().get("c_id"), type, data, geared, p, this);
                break;
            case 2:
                order.orderFocus(application.getUserInfo().get("c_id"), type, data2, geared, p, this);
                break;
            case 3:
                order.orderFocus(application.getUserInfo().get("c_id"), type, data3, geared, p, this);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        city = PreferencesUtils.getString(getActivity(), "city");
        if (!TextUtils.isEmpty(city)) {
            city = city.replace("市", "");
            if (!TextUtils.isEmpty(city)) {
                tv_city.setText(city);
            }
        }
        LogUtil.e(application.getUserInfo().get("is_work") + "mmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        LogUtil.e(application.getUserInfo().get("c_head") + "mmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        if (application.getUserInfo().get("is_work").equals("1")) {
            imageLoader.disPlay(iv_main_head, application.getUserInfo().get("c_head"));
        } else {
            iv_main_head.setImageResource(R.drawable.ic_page_state);
        }
        showProgressContent();
        p = 1;
        order.orderFocus(application.getUserInfo().get("c_id"), type, data, geared, p, this);
    }

    @Event(value = {R.id.page_relay_time, R.id.page_relay_where, R.id.page_relay_num, R.id.page_tv_allok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.page_relay_time:
                cbox_time.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_time.setChecked(!cbox_time.isChecked());
                cbox_long.setChecked(false);
                cbox_num.setChecked(false);
                type = "1";
                typee = 1;
                page_lv.startRefreshing();
                break;
            case R.id.page_relay_where:
                cbox_long.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setChecked(!cbox_long.isChecked());
                cbox_time.setChecked(false);
                cbox_num.setChecked(false);
                type = "2";
                typee = 2;
                page_lv.startRefreshing();
                break;
            case R.id.page_relay_num:
                cbox_num.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setChecked(!cbox_num.isChecked());
                cbox_time.setChecked(false);
                cbox_long.setChecked(false);
                type = "3";
                typee = 3;
                page_lv.startRefreshing();
                break;
            case R.id.page_tv_allok:
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        stringBuffer.append(",");
                    }
                    stringBuffer.append(list.get(i).get("order_id"));
                }
                showProgressDialog();
                order.toOrder(getContext(), application.getUserInfo().get("c_id"), stringBuffer.toString(), this);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.page_rdobtn_all) {
            if (index == 1) {
                geared = "1";
                index = 0;
                rdobtn_all.setTextColor(0xff2c82df);
                rdobtn_divide.setTextColor(0xffffffff);
                page_lv.startRefreshing();
            }
        } else if (checkedId == R.id.page_rdobtn_divide) {
            if (index == 0) {
                geared = "3";
                index = 1;
                relay_bttom.setVisibility(View.GONE);
                rdobtn_divide.setTextColor(0xff2c82df);
                rdobtn_all.setTextColor(0xffffffff);
                page_lv.startRefreshing();
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_page_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (index == 0) {
                holder.fb_ok.setText("抢单");
                holder.tv_ok.setVisibility(View.VISIBLE);
            } else {
                holder.fb_ok.setText("重新分配");
                holder.tv_ok.setVisibility(View.GONE);
            }
//            holder.tv_ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(DistaskAty.class, null);
//                }
//            });
            holder.imgv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainAty parentActivity = (MainAty) getActivity();
                    parentActivity.callPhone(list.get(position).get("contact"));
                }
            });

            if (list.get(position).get("isFirst").equals("1")) {
                holder.imgv_xin.setVisibility(View.VISIBLE);
            } else {
                holder.imgv_xin.setVisibility(View.GONE);
            }
            holder.tv_time.setText(list.get(position).get("create_time"));
            holder.tv_xiaofei.setText("￥" + list.get(position).get("reward"));
            imageLoader.disPlay(holder.imgv_pro, list.get(position).get("cover"));
            holder.tv_proname.setText(list.get(position).get("goods_name"));
            holder.tv_attr.setText("(" + list.get(position).get("attr") + "L)");
            holder.tv_num.setText("x" + list.get(position).get("buy_num"));
            holder.tv_name.setText(list.get(position).get("consignee"));
            holder.tv_tel.setText(list.get(position).get("contact"));
            holder.tv_address.setText(list.get(position).get("address"));
            float distance = Float.parseFloat(list.get(position).get("distance")) / 1000;
            holder.tv_distance.setText(distance + "km");
            holder.tv_toallprice.setText("￥" + list.get(position).get("pay_fee"));
            holder.tv_huodao.setText(list.get(position).get("pay_fee"));
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
            if (list.get(position).get("order_type").equals("3")) {
                holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state2);
            } else if (list.get(position).get("source").equals("2")) {
                holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state3);
            } else {
                holder.imgv_state.setImageResource(R.drawable.ic_page_lv_state4);
            }
            holder.fb_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (application.getUserInfo().get("is_work").equals("1")) {
                        if (index == 0) {
                            showProgressDialog();
                            order.toOrder(getContext(), application.getUserInfo().get("c_id"), list.get(position).get("order_id"), PageFrg.this);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("order_id", list.get(position).get("order_id"));
                            startActivity(DistaskAty.class, bundle);
                        }
                    } else {
                        showToast("不是工作状态");
                    }
                }
            });
            holder.tv_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (application.getUserInfo().get("is_work").equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", list.get(position).get("order_id"));
                        startActivity(DistaskAty.class, bundle);
                    } else {
                        showToast("不是工作状态");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_pagelv_fb_list)
            Button fb_ok;
            @ViewInject(R.id.item_pagelv_tv_list)
            TextView tv_ok;
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
