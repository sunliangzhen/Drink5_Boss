package com.toocms.drink5.boss.ui.page;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Courier;
import com.toocms.drink5.boss.interfaces2.Order;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
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
 * @date 2016/5/21 11:41
 */
public class DistaskAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.distask_lv)
    private SwipeToLoadRecyclerView distask_lv;

    private MyAdapter myAdapter;
    private Courier courier;
    private int p = 1;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;
    private String order_id;
    private Order order;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_distask;
    }

    @Override
    protected void initialized() {
        courier = new Courier();
        myAdapter = new MyAdapter();
        maps = new ArrayList<>();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        order_id = getIntent().getStringExtra("order_id");
        order = new Order();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        courier.index(application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        distask_lv.stopRefreshing();
        distask_lv.stopLoadingMore();
        if (params.getUri().contains("Courier/index")) {
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            if (maps != null) {
                myAdapter.notifyDataSetChanged();
            }
        }
        if (params.getUri().contains("asCourier")) {
            finish();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        distask_lv.stopRefreshing();
        distask_lv.stopLoadingMore();
        removeProgressDialog();
        removeProgressContent();
    }

    @Override
    public void onRefresh() {
        p = 1;
        courier.index(application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    public void onLoadMore() {
        p++;
        courier.index(application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("分配任务");

        distask_lv.setOnRefreshListener(this);
        distask_lv.setOnLoadMoreListener(this);
        distask_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        distask_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                showProgressDialog();
                order.asCourier(application.getUserInfo().get("c_id"), order_id, maps.get(i).get("c_id"), DistaskAty.this);
            }
        });
        distask_lv.setAdapter(myAdapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DistaskAty.this).inflate(R.layout.item_distask_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            imageLoader.disPlay(holder.imgv_head, maps.get(position).get("head"));
            holder.tv_name.setText(maps.get(position).get("nickname"));
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_distasklv_imgv_head)
            ImageView imgv_head;
            @ViewInject(R.id.item_distasklv_tv)
            TextView tv_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
