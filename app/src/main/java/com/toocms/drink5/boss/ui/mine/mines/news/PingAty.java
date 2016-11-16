package com.toocms.drink5.boss.ui.mine.mines.news;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Courier2;
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

/**
 * @author Zero
 * @date 2016/5/20 11:06
 */
public class PingAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.ping_lv)
    private SwipeToLoadRecyclerView ping_lv;
    private Courier2 courier2;
    private int p = 1;
    private MyAdapter myAdapter;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;
    private String c_id = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_ping;
    }

    @Override
    protected void initialized() {
        maps = new ArrayList<>();
        courier2 = new Courier2();
        myAdapter = new MyAdapter();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        if (getIntent().hasExtra("type")) {
            c_id = getIntent().getStringExtra("c_id");
        } else {
            c_id = application.getUserInfo().get("c_id");
        }
    }

    @Override
    protected void requestData() {
        showProgressContent();
        courier2.isEvaluate(c_id, p, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("评价");
        ping_lv.setOnRefreshListener(this);
        ping_lv.setOnLoadMoreListener(this);
        ping_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        ping_lv.setAdapter(myAdapter);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("isEvaluate")) {
            ping_lv.stopLoadingMore();
            ping_lv.stopRefreshing();
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapter.notifyDataSetChanged();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
        ping_lv.stopLoadingMore();
        ping_lv.stopRefreshing();
        if (p == 1) {
            maps = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        courier2.isEvaluate(application.getUserInfo().get("c_id"), p, this);
    }

    @Override
    public void onLoadMore() {
        p++;
        courier2.isEvaluate(application.getUserInfo().get("c_id"), p, this);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PingAty.this).inflate(R.layout.item_ping_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            imageLoader.disPlay(holder.imgv_head, maps.get(position).get("head"));
            holder.tv_name.setText(maps.get(position).get("nickname"));
            holder.tv_time.setText(maps.get(position).get("create_time"));
            holder.tv_content.setText(maps.get(position).get("content"));
            ImageView imgv[] = new ImageView[]{holder.imgv_01, holder.imgv_02, holder.imgv_03, holder.imgv_04, holder.imgv_05};
            String avg_evaluate = maps.get(position).get("avg_evaluate");
            for (int i = 0; i < imgv.length; i++) {
                if (i < Integer.parseInt(avg_evaluate)) {
                    imgv[i].setVisibility(View.VISIBLE);
                } else {
                    imgv[i].setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_pinglv_imgv_head)
            private ImageView imgv_head;
            @ViewInject(R.id.item_pinglv_imgv_01)
            private ImageView imgv_01;
            @ViewInject(R.id.item_pinglv_imgv_02)
            private ImageView imgv_02;
            @ViewInject(R.id.item_pinglv_imgv_03)
            private ImageView imgv_03;
            @ViewInject(R.id.item_pinglv_imgv_04)
            private ImageView imgv_04;
            @ViewInject(R.id.item_pinglv_imgv_05)
            private ImageView imgv_05;
            @ViewInject(R.id.item_pinglv_tv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_pinglv_tv_time)
            private TextView tv_time;
            @ViewInject(R.id.item_pinglv_tv_content)
            private TextView tv_content;
//            private ImageView imgv[];

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }

        }
    }
}
