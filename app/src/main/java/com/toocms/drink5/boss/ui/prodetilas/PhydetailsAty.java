package com.toocms.drink5.boss.ui.prodetilas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Order;
import com.toocms.drink5.boss.ui.BaseAty;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.linearlistview.LinearListView;

/**
 * @author Zero
 * @date 2016/7/21 17:24
 */
public class PhydetailsAty extends BaseAty {


    @ViewInject(R.id.yhm_lv)
    private LinearListView yhm_lv;
    @ViewInject(R.id.phydetails_tv)
    private TextView tv_content;

    private Order order;
    private String order_id;
    private String logistical;
    private String logistical_sn;
    private ArrayList<Map<String, String>> maps;
    private MyAdapter myAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_phydetails;
    }

    @Override
    protected void initialized() {
        order = new Order();
        order_id = getIntent().getStringExtra("order_id");
        logistical = getIntent().getStringExtra("logistical");
        logistical_sn = getIntent().getStringExtra("logistical_sn");
        maps = new ArrayList<>();
        myAdapter = new MyAdapter();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        order.logistical(order_id, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("物流信息");
        yhm_lv.setAdapter(myAdapter);
        tv_content.setText("快递公司:   " + logistical + "   快递编号:  " + logistical_sn);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("logistical")) {
            maps = JSONUtils.parseDataToMapList(result);
            myAdapter.notifyDataSetChanged();
        }
        super.onComplete(params, result);
    }

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
                convertView = LayoutInflater.from(PhydetailsAty.this).inflate(R.layout.item_phyde, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_time.setText(maps.get(position).get("time"));
            viewHolder.tv_content.setText(maps.get(position).get("context"));
            if(position==0){
                viewHolder.tv_point.setBackgroundResource(R.drawable.shape_yell);
                viewHolder.v_line_01.setBackgroundColor(0xffF39800);
                viewHolder.v_line_02.setBackgroundColor(0xffF39800);
                viewHolder.tv_content.setTextColor(0xffF39800);
                viewHolder.tv_time.setTextColor(0xffF39800);
            }
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.phyde_v_line1)
            public View v_line_01;
            @ViewInject(R.id.phyde_v_line2)
            public View v_line_02;
            @ViewInject(R.id.item_pd_v_03)
            public View v_03;
            @ViewInject(R.id.item_pd_tv_point)
            public TextView tv_point;
            @ViewInject(R.id.item_phyde_tv_time)
            public TextView tv_time;
            @ViewInject(R.id.item_phyde_tv_content)
            public TextView tv_content;
        }

    }

}
