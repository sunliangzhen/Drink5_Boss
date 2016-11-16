package com.toocms.drink5.boss.ui.prodetilas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/21 14:02
 */
public class Phy2Aty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {


    @ViewInject(R.id.phy2_lv)
    private SwipeToLoadRecyclerView phy2_lv;
    private MyAdapter myAdapter;
    private Order order;
    private ArrayList<Map<String, String>> maps;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_phy2;
    }

    @Override
    protected void initialized() {
        order = new Order();
        myAdapter = new MyAdapter();
        maps = new ArrayList<>();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        order.getCompany(this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        maps = JSONUtils.parseDataToMapList(result);
        myAdapter.notifyDataSetChanged();
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("物流公司");
        phy2_lv.setOnRefreshListener(this);
        phy2_lv.setOnLoadMoreListener(this);
        phy2_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        phy2_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                setResult(-1, new Intent().putExtra("name", maps.get(i).get("name")));
                finish();
            }
        });
        phy2_lv.setAdapter(myAdapter);
    }

    @Override
    public void onRefresh() {
        phy2_lv.stopRefreshing();
    }

    @Override
    public void onLoadMore() {
        phy2_lv.stopLoadingMore();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Phy2Aty.this).inflate(R.layout.item_phy2_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_ok2.setText(maps.get(position).get("name"));
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_phy2_tv)
            TextView tv_ok2;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
