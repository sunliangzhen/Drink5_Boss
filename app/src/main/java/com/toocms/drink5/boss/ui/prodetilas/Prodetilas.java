package com.toocms.drink5.boss.ui.prodetilas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Order;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.lar.RoutePlanDemo;
import com.toocms.drink5.boss.ui.page.RefuseAty;
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
import cn.zero.android.common.view.linearlistview.LinearListView;


/**
 * @author Zero
 * @date 2016/5/18 17:14
 */
public class Prodetilas extends BaseAty {

    @ViewInject(R.id.details_tv_bianhao)
    private TextView tv_bianhao;
    @ViewInject(R.id.details_tv_state)
    private TextView tv_state;
    @ViewInject(R.id.details_tv_peo)
    private TextView tv_peo;
    @ViewInject(R.id.details_tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.details_tv_address)
    private TextView tv_address;
    @ViewInject(R.id.details_imgv_pro)
    private ImageView imgv_pro;
    @ViewInject(R.id.details_tv_proname)
    private TextView tv_proname;
    @ViewInject(R.id.details_pagelv_tv_attr)
    private TextView tv_attr;
    @ViewInject(R.id.details_pagelv_tv_num)
    private TextView tv_num;
    @ViewInject(R.id.details_pagelv_tv_price)
    private TextView tv_price;
    @ViewInject(R.id.details_pagelv_tv_time)
    private TextView tv_time;
    @ViewInject(R.id.details_pagelv_tv_piao)
    private TextView tv_piao;
    @ViewInject(R.id.details_pagelv_tv_xiaofei)
    private TextView tv_xiaofei;
    @ViewInject(R.id.details_pagelv_tv_paystate)
    private TextView tv_paystate;
    @ViewInject(R.id.details_pagelv_tv_total)
    private TextView tv_total;
    @ViewInject(R.id.details_pagelv_tv_jinbi)
    private TextView tv_jinbi;
    @ViewInject(R.id.detail_tv_shifu)
    private TextView tv_shifu;
    @ViewInject(R.id.details_pagelv_tv_ordertime)
    private TextView tv_ordertime;
    @ViewInject(R.id.details_tv_type)
    private TextView tv_type;
    @ViewInject(R.id.details_linlay_t)
    private LinearLayout details_linlay_t;
    @ViewInject(R.id.details_pagelv_tv_beizhu)
    private TextView tv_bei;
    @ViewInject(R.id.details_pagelv_tv_paytype)
    private TextView tv_paytype;
    @ViewInject(R.id.details_pagelv_tv_tick)
    private TextView tv_tick;
    @ViewInject(R.id.details_fb_ok)
    private Button fb_ok;
    @ViewInject(R.id.details2_relay_cbox)
    private RelativeLayout relay_cbox;
    @ViewInject(R.id.details2_tv_barrel)
    private TextView tv_barrel;
    @ViewInject(R.id.details2_tv_add)
    private TextView tv_add;
    @ViewInject(R.id.details2_tv_add2)
    private TextView tv_add2;
    @ViewInject(R.id.details2_relay_add)
    private RelativeLayout details2_relay_add;
    @ViewInject(R.id.details2_relay_add2)
    private RelativeLayout details2_relay_add2;
    @ViewInject(R.id.details2_cbox)
    private CheckBox details2_cbox;
    @ViewInject(R.id.details_linlay_g)
    private LinearLayout linlay_g;
    @ViewInject(R.id.details_linlay_ba)
    private LinearLayout details_linlay_ba;
    @ViewInject(R.id.details_lv)
    private LinearListView details_lv;
    @ViewInject(R.id.details_v_line)
    private View v_line;
    @ViewInject(R.id.details_relay_botoom)
    private RelativeLayout details_relay_botoom;
    @ViewInject(R.id.details2_relay_piao)
    private RelativeLayout details2_relay_piao;
    @ViewInject(R.id.details2_relay_jin)
    private RelativeLayout details2_relay_jin;
    @ViewInject(R.id.details2_relay_ti)
    private RelativeLayout details2_relay_ti;
    @ViewInject(R.id.details_pagelv_tv_piao2)
    private TextView details_pagelv_tv_piao;
    @ViewInject(R.id.details_tv_wwwww)
    private RelativeLayout details_tv_wwwww;

    private ArrayList<Map<String, String>> barreling;

    private ImageLoader imageLoader;
    private Order order;
    private String order_id;
    private String type = "";
    private String type2 = "";
    private Map<String, String> map;
    private int barrel;
    private int jie = 0;
    private int jie_totla = 0;
    private int huan = 0;
    private MyAdapter myAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_details;
    }

    @Override
    protected void initialized() {
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        order = new Order();
        order_id = getIntent().getStringExtra("order_id");
        type = getIntent().getStringExtra("type");
        type2 = getIntent().getStringExtra("type_2");
        barreling = new ArrayList<>();
        myAdapter = new MyAdapter();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        order.orderDetails(application.getUserInfo().get("c_id"), order_id, this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("onCompleted")) {
            finish();
        }
        if (params.getUri().contains("orderDetails")) {
            map = JSONUtils.parseDataToMap(result);
            Map<String, String> map1 = JSONUtils.parseKeyAndValueToMap(map.get("orderGoods"));
            if (type.equals("3")) {
                barreling = JSONUtils.parseKeyAndValueToMapList(map.get("barreling"));
                myAdapter.notifyDataSetChanged();
            }
            tv_bianhao.setText(map.get("order_sn"));
            tv_peo.setText(map.get("consignee"));
            tv_phone.setText(map.get("contact"));
            tv_address.setText(map.get("address"));
            imageLoader.disPlay(imgv_pro, map1.get("cover"));
            tv_proname.setText(map1.get("goods_name"));
            tv_attr.setText("规格：  " + map1.get("attr") + "L");
            tv_price.setText("￥" + map1.get("goods_price"));
            tv_num.setText("X" + map1.get("buy_num"));
            jie = Integer.parseInt(map1.get("buy_num"));
            jie_totla = Integer.parseInt(map.get("barrel"));
            tv_add.setText(jie + "");
            tv_time.setText(map.get("shipping_time"));
            tv_piao.setText(map.get("invoice_type"));
            tv_bei.setText(map.get("remarks"));
            tv_xiaofei.setText("￥" + map.get("reward"));
            tv_total.setText("￥" + map1.get("goods_amount"));
            if (map.get("score").equals("0")) {
                details2_relay_jin.setVisibility(View.GONE);
            } else {
                tv_jinbi.setText("-￥" + map.get("score"));
            }
            if (map.get("ticket_num").equals("0")) {
                details2_relay_piao.setVisibility(View.GONE);
            } else {
                tv_tick.setText("-" + map.get("ticket_num") + "张");
            }
            tv_shifu.setText(map.get("pay_fee"));
            tv_barrel.setText(map.get("barrel"));
            if ((int) Double.parseDouble(map.get("ticket_price")) == 0) {
                details2_relay_ti.setVisibility(View.GONE);
            } else {
                details_pagelv_tv_piao.setText("￥" + map.get("ticket_price"));
            }
            tv_ordertime.setText("下单时间： " + map.get("create_time"));
            if (map.get("logistical_status").equals("1")) {
                fb_ok.setText("重新填写物流");
            }
            switch (map.get("pay_type")) {
                case "0":
                    tv_paystate.setText("(待支付)");
                    break;
                case "1":
                    tv_paystate.setText("(微信支付)");
                    break;
                case "2":
                    tv_paystate.setText("(支付宝支付)");
                    break;
                case "3":
                    tv_paystate.setText("(货到付款)");
                    break;
                case "4":
                    tv_paystate.setText("(余额支付)");
                    break;
                case "5":
                    tv_paystate.setText("(水票支付)");
                    break;
            }
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        if (type2.equals("page")) {
            tv_type.setText("拒绝订单");
            v_line.setVisibility(View.GONE);
            details_linlay_t.setVisibility(View.GONE);
            details_relay_botoom.setVisibility(View.GONE);
            relay_cbox.setVisibility(View.GONE);
            details2_relay_add.setVisibility(View.GONE);
            details2_relay_add2.setVisibility(View.GONE);

            if (type.equals("3")) {
                linlay_g.setVisibility(View.GONE);
                details_linlay_ba.setVisibility(View.VISIBLE);
                tv_state.setText("待取桶");
            }
        } else {
            if (type.equals("4")) {
                tv_type.setText("关闭订单");
                relay_cbox.setVisibility(View.GONE);
                details2_relay_add.setVisibility(View.GONE);
                details2_relay_add2.setVisibility(View.GONE);
                details_tv_wwwww.setVisibility(View.VISIBLE);
                fb_ok.setText("填写物流");
            } else if (type.equals("3")) {
                fb_ok.setText("确认收桶");
                tv_type.setText("关闭订单");
                linlay_g.setVisibility(View.GONE);
                details_linlay_ba.setVisibility(View.VISIBLE);
                tv_state.setText("待取桶");
            } else {
                tv_type.setText("关闭订单");
                fb_ok.setText("确认送达");
            }
        }
        details2_cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    details2_relay_add.setVisibility(View.VISIBLE);
                    details2_relay_add2.setVisibility(View.VISIBLE);
                } else {
                    details2_relay_add.setVisibility(View.GONE);
                    details2_relay_add2.setVisibility(View.GONE);
                }
            }
        });
        details_lv.setAdapter(myAdapter);
    }

    @Event(value = {R.id.details_fb_ok, R.id.details_imgv_back, R.id.details_tv_type, R.id.details2_tv_jian,
            R.id.details2_imgv_add, R.id.details2_tv_jian2, R.id.details2_imgv_add2, R.id.details_imgv_address,
            R.id.details_tv_wwwww})
    private void onTestBaidulClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.details_fb_ok:
                if (type.equals("3")) {   //还桶单
                    showProgressDialog();
                    order.onCompleted(application.getUserInfo().get("c_id"), map.get("order_id"), map.get("barrel"), this);
                } else if (type.equals("4")) {   //高端水单
                    bundle = new Bundle();
                    bundle.putString("nickname", map.get("consignee"));
                    bundle.putString("contact", map.get("contact"));
                    bundle.putString("address", map.get("address"));
                    bundle.putString("order_id", map.get("order_id"));
                    startActivity(PhyAty.class, bundle);
                } else {    //普通水单
                    showProgressDialog();
                    order.onCompleted(application.getUserInfo().get("c_id"), map.get("order_id"), huan + "", this);
                }
                break;
            case R.id.details_imgv_back:
                finish();
                break;
            case R.id.details_tv_type:
                bundle = new Bundle();
                bundle.putString("type", type2);
                bundle.putString("order_id", map.get("order_id"));
                startActivity(RefuseAty.class, bundle);
                break;
            case R.id.details2_tv_jian:
                if (jie > 0) {
                    jie--;
                }
                tv_add.setText(jie + "");
                break;
            case R.id.details2_imgv_add:
                jie++;
                tv_add.setText(jie + "");
                break;
            case R.id.details2_tv_jian2:
                if (huan > 0) {
                    huan--;
                }
                tv_add2.setText(huan + "");
                break;
            case R.id.details2_imgv_add2:
                if (huan < jie_totla) {
                    huan++;
                }
                tv_add2.setText(huan + "");
                break;
            case R.id.details_imgv_address:
                bundle = new Bundle();
                bundle.putString("lat", map.get("lat"));
                bundle.putString("lon", map.get("lon"));
                bundle.putString("c_lat", map.get("c_lat"));
                bundle.putString("c_lon", map.get("c_lon"));
                startActivity(RoutePlanDemo.class, bundle);
                break;
            case R.id.details_tv_wwwww:
                bundle = new Bundle();
                bundle.putString("order_id", map.get("order_id"));
                bundle.putString("logistical", map.get("logistical"));
                bundle.putString("logistical_sn", map.get("logistical_sn"));
                startActivity(PhydetailsAty.class, bundle);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return barreling.size();
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
                convertView = LayoutInflater.from(Prodetilas.this).inflate(R.layout.item_details_lv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_name.setText(barreling.get(position).get("goods_name")
                    + barreling.get(position).get("attr") + "L");
            viewHolder.tv_num.setText(barreling.get(position).get("buy_num") + "只");
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_details_tv_proname)
            public TextView tv_name;
            @ViewInject(R.id.item_details_tv_num)
            public TextView tv_num;
        }
    }

}
