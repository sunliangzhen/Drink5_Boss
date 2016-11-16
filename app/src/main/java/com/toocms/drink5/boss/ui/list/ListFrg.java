package com.toocms.drink5.boss.ui.list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
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
public class ListFrg extends BaseFragment implements RadioGroup.OnCheckedChangeListener, OnLoadMoreListener, OnRefreshListener {

    @ViewInject(R.id.list_cbox_time)
    private CheckBox cbox_time;
    @ViewInject(R.id.list_cbox_long)
    private CheckBox cbox_long;
    @ViewInject(R.id.list_cbox_num)
    private CheckBox cbox_num;
    @ViewInject(R.id.list_tv_list)
    private CheckBox cbox_total;
    @ViewInject(R.id.list_lv)
    private SwipeToLoadRecyclerView list_lv;

    @ViewInject(R.id.list_rg)
    private RadioGroup list_rg;
    @ViewInject(R.id.list_rdobtn_all)
    private RadioButton rdobtn_all;
    @ViewInject(R.id.list_rdobtn_divide)
    private RadioButton rdobtn_divide;
    @ViewInject(R.id.list_tv_city)
    private TextView tv_city;
    @ViewInject(R.id.list_relay_bottom)
    private RelativeLayout relay_bottom;
    @ViewInject(R.id.list_relay_bttom2)
    private RelativeLayout relay_bottom2;
    @ViewInject(R.id.list_tv_list2)
    private TextView tv_list2;
    @ViewInject(R.id.list_tv_tong2)
    private TextView tv_totaltong2;

    @ViewInject(R.id.list_tv_xuan)
    private TextView list_tv_xuan;
    @ViewInject(R.id.list_tv_tong)
    private TextView tv_totaltong;
    @ViewInject(R.id.list_relay_02)
    private RelativeLayout list_relay_02;
    @ViewInject(R.id.list_lv_bttom)
    private ListView list_lv_bttom;
    @ViewInject(R.id.iv_main_head2)
    private ImageView iv_main_head2;

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
    private ArrayList<Map<String, String>> list;
    private ArrayList<Map<String, String>> list2;
    private ArrayList<Integer> list_state;
    private Map<String, String> map;
    private int p = 1;
    private String type = "1";
    private String data = "1";
    private String data2 = "";
    private String data3 = "";
    private String geared = "4";
    private int typee = 1;
    private int xuan_size = 0;
    private ImageLoader imageLoader;
    private ChatAdp chatAdp;


    @Override
    protected int getLayoutResId() {
        return R.layout.frg_list;
    }

    @Override
    protected void initialized() {
        rightDrawable = getResources().getDrawable(R.drawable.selector_page_top);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        rightDrawable2 = getResources().getDrawable(R.drawable.ic_page_topn);
        rightDrawable2.setBounds(0, 0, rightDrawable2.getMinimumWidth(), rightDrawable2.getMinimumHeight());
        order = new Order();
        myAdapter = new MyAdapter();
        list2 = new ArrayList<>();
        list = new ArrayList<>();
        list_state = new ArrayList<>();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        chatAdp = new ChatAdp();
    }

    @Override
    protected void requestData() {
    }
    @Override
    public void onException(Throwable ex) {
        removeProgressDialog();
        removeProgressContent();
        list_lv.stopRefreshing();
        list_lv.stopLoadingMore();
        tv_net.setVisibility(View.VISIBLE);
        imgv_empty.setVisibility(View.GONE);
        tv_empty.setVisibility(View.GONE);
        relay_bottom.setVisibility(View.GONE);
        relay_bottom2.setVisibility(View.GONE);
//        list_lv.setVisibility(View.GONE);
    }
    @Override
    public void onComplete(RequestParams params, String result) {
        list_lv.stopRefreshing();
        list_lv.stopLoadingMore();
        if (params.getUri().contains("orderFocus")) {
            list_state = new ArrayList<>();
            xuan_size = 0;
            list_tv_xuan.setText("已选： " + xuan_size + "单");
            map = JSONUtils.parseDataToMap(result);
            ArrayList<Map<String, String>> list_num = JSONUtils.parseKeyAndValueToMapList(map.get("list"));
            if (p == 1) {
                list = new ArrayList<>();
            }
            if (list_num.size() > 0) {
                list_lv.setVisibility(View.VISIBLE);
                imgv_empty.setVisibility(View.GONE);
                tv_empty.setVisibility(View.GONE);
                tv_net.setVisibility(View.GONE);
                tv_totaltong.setText(map.get("num"));
                tv_totaltong2.setText(map.get("num"));
                tv_list2.setText("订单:  " + map.get("count") + "单");
                if (index == 0) {
                    relay_bottom.setVisibility(View.VISIBLE);
                    relay_bottom2.setVisibility(View.GONE);
                } else {
                    relay_bottom.setVisibility(View.GONE);
                    relay_bottom2.setVisibility(View.VISIBLE);
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
            } else {
//                if (p == 1) {
//                    relay_bottom.setVisibility(View.GONE);
//                    relay_bottom2.setVisibility(View.GONE);
//                }
            }
            for (int i = 0; i < this.list.size(); i++) {
                list_state.add(0);
            }
            myAdapter.notifyDataSetChanged();
            chatAdp.notifyDataSetChanged();
        }

        if (params.getUri().contains("backOrder")) {
            showToast("退单成功");
            list_lv.startRefreshing();
        }
        if (params.getUri().contains("toDistribute")) {
            showToast("分单完成");
            list_lv.startRefreshing();
        }
        if (params.getUri().contains("reOrder")) {
            list_lv.startRefreshing();
        }
        super.onComplete(params, result);
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
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        list_lv.stopRefreshing();
        list_lv.stopLoadingMore();
        if (error.get("message").equals("" +
                "没有更多的数据了")) {
            if (p == 1) {
                list = new ArrayList<>();
//                list_lv.setVisibility(View.GONE);
                myAdapter.notifyDataSetChanged();
                relay_bottom.setVisibility(View.GONE);
                tv_net.setVisibility(View.GONE);
                relay_bottom2.setVisibility(View.GONE);
                imgv_empty.setVisibility(View.VISIBLE);
                tv_empty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_rg.setOnCheckedChangeListener(this);
        list_lv.setOnRefreshListener(this);
        list_lv.setOnLoadMoreListener(this);
        list_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 1));
        list_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id", list.get(i).get("order_id"));
                bundle.putString("type", list.get(i).get("order_type"));
                bundle.putString("type_2", "list");
                startActivity(Prodetilas.class, bundle);
            }
        });
        list_lv.setAdapter(myAdapter);
        list_lv_bttom.setAdapter(chatAdp);
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
        cbox_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbox_total.isChecked()) {
                    for (int i = 0; i < list_state.size(); i++) {
                        list_state.set(i, 1);
                    }
                    xuan_size = list_state.size();
                } else {
                    for (int i = 0; i < list_state.size(); i++) {
                        list_state.set(i, 0);
                    }
                    xuan_size = 0;
                }
                list_tv_xuan.setText("已选： " + xuan_size + "单");
                myAdapter.notifyDataSetChanged();
            }
        });

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
        if (application.getUserInfo().get("is_work").equals("1")) {
            imageLoader.disPlay(iv_main_head2, application.getUserInfo().get("c_head"));
        } else {
            iv_main_head2.setImageResource(R.drawable.ic_page_state);
        }
        list_relay_02.setVisibility(View.GONE);
        showProgressContent();
        order.orderFocus(application.getUserInfo().get("c_id"), type, data, geared, p, this);
    }

    @Event(value = {R.id.list_relay_time, R.id.list_relay_where, R.id.list_relay_num, R.id.list_tv_fenpei, R.id.list_tv_tong,
            R.id.list_relay_02})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.list_relay_time:
                cbox_time.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_time.setChecked(!cbox_time.isChecked());
                cbox_long.setChecked(false);
                cbox_num.setChecked(false);
                type = "1";
                typee = 1;
                list_lv.startRefreshing();
                break;
            case R.id.list_relay_where:
                cbox_long.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setChecked(!cbox_long.isChecked());
                cbox_num.setChecked(false);
                cbox_time.setChecked(false);
                type = "2";
                typee = 2;
                list_lv.startRefreshing();
                break;
            case R.id.list_relay_num:
                cbox_num.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setChecked(!cbox_num.isChecked());
                cbox_long.setChecked(false);
                cbox_time.setChecked(false);
                type = "3";
                typee = 3;
                list_lv.startRefreshing();
                break;
            case R.id.list_tv_fenpei:
                if (xuan_size == 0) {
                    showToast("你还未选择");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                int k = 0;
                for (int i = 0; i < list_state.size(); i++) {
                    if (list_state.get(i) == 1) {
                        k++;
                        if (k != 1) {
                            buffer.append(",");
                        }
                        buffer.append(list.get(i).get("order_id"));
                    }
                }
                showProgressDialog();
                order.toDistribute(application.getUserInfo().get("c_id"), buffer.toString(), this);
                break;
            case R.id.list_tv_tong:
                if (list_relay_02.getVisibility() == View.VISIBLE) {
                    list_relay_02.setVisibility(View.GONE);
                } else {
                    list_relay_02.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.list_relay_02:
                list_relay_02.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.list_rdobtn_all) {
            if (index == 1) {
                geared = "4";
                index = 0;
                rdobtn_all.setTextColor(0xff2c82df);
                rdobtn_divide.setTextColor(0xffffffff);
                list_lv.startRefreshing();
            }
        } else if (checkedId == R.id.list_rdobtn_divide) {
            if (index == 0) {
                geared = "5";
                index = 1;
                rdobtn_divide.setTextColor(0xff2c82df);
                rdobtn_all.setTextColor(0xffffffff);
                list_lv.startRefreshing();
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
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if (index == 0) {
                holder.cbox.setVisibility(View.VISIBLE);
                holder.tv_ok2.setVisibility(View.VISIBLE);
                holder.tv_ok.setVisibility(View.GONE);
                holder.fb_ok.setVisibility(View.GONE);
                holder.fb_ok2.setVisibility(View.GONE);
            } else if (index == 1) {
                holder.cbox.setVisibility(View.GONE);
                holder.tv_ok2.setVisibility(View.GONE);
                holder.tv_ok.setVisibility(View.GONE);
                holder.fb_ok2.setVisibility(View.VISIBLE);
                holder.fb_ok.setVisibility(View.GONE);
            }
            holder.tv_state.setVisibility(View.VISIBLE);

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

            if (list.get(position).get("logistical_status").equals("1")) {
                holder.fb_ok2.setVisibility(View.GONE);
            }
            holder.imgv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainAty parentActivity = (MainAty) getActivity();
                    parentActivity.callPhone(list.get(position).get("contact"));
                }
            });
            holder.tv_ok2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    order.backOrder(application.getUserInfo().get("c_id"), list.get(position).get("order_id"), ListFrg.this);
                }
            });
            holder.fb_ok2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    order.reOrder(application.getUserInfo().get("c_id"), list.get(position).get("order_id"), ListFrg.this);
                }
            });
            if (list_state.get(position) == 1) {
                holder.cbox.setChecked(true);
            } else {
                holder.cbox.setChecked(false);
            }
            holder.cbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.cbox.isChecked()) {
                        list_state.set(position, 1);
                        xuan_size++;
                    } else {
                        list_state.set(position, 0);
                        xuan_size--;
                    }
                    if (xuan_size < list_state.size()) {
                        cbox_total.setChecked(false);
                    } else {
                        cbox_total.setChecked(true);
                    }
                    list_tv_xuan.setText("已选： " + xuan_size + "单");
                }
            });
            if (xuan_size < list_state.size()) {
                cbox_total.setChecked(false);
            } else {
                cbox_total.setChecked(true);
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
            @ViewInject(R.id.item_pagelv_cbox)
            CheckBox cbox;

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


    public class ChatAdp extends BaseAdapter {

        @Override
        public int getCount() {
            return list2.size();
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
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_listwindows, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_name.setText(list2.get(position).get("goods_name") + list2.get(position).get("attr") + "L");
            viewHolder.tv_arrt.setText("X" + list2.get(position).get("num"));
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_listwin_tvname)
            public TextView tv_name;
            @ViewInject(R.id.item_listwin_tvattr)
            public TextView tv_arrt;
        }
    }
}
