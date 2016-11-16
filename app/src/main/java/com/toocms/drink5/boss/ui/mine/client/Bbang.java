package com.toocms.drink5.boss.ui.mine.client;

import android.app.Dialog;
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
import com.toocms.drink5.boss.interfaces.Site;
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
 * @date 2016/5/24 13:23
 */
public class Bbang extends BaseAty implements OnRefreshListener, OnLoadMoreListener {


    @ViewInject(R.id.bbang_lv)
    private SwipeToLoadRecyclerView bbang_lv;
    private Courier courier;
    private int p = 1;
    private MyAdapter myAdapter;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;
    private String m_id;
    private Site site;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bbang;
    }

    @Override
    protected void initialized() {
        courier = new Courier();
        myAdapter = new MyAdapter();
        maps = new ArrayList<>();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        m_id = getIntent().getStringExtra("m_id");
        site = new Site();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        courier.index(application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("Courier/index")) {
            bbang_lv.stopLoadingMore();
            bbang_lv.stopRefreshing();
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapter.notifyDataSetChanged();
        }
        if (params.getUri().contains("bindMember")) {
            showToast("绑定成功");
            finish();
        }
        super.onComplete(params, result);
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
    public void onError(Map<String, String> error) {
        bbang_lv.stopLoadingMore();
        bbang_lv.stopRefreshing();
        removeProgressDialog();
        removeProgressContent();
        String message = error.get("message");
        this.showToast(message);
        if (message.contains("绑定")) {
            this.showToast(message);
        } else {
            if (p == 1) {
                maps = new ArrayList<>();
                myAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("绑定给水工");
        bbang_lv.setOnRefreshListener(this);
        bbang_lv.setOnLoadMoreListener(this);
        bbang_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        bbang_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                showBuilder(i);
            }
        });
        bbang_lv.setAdapter(myAdapter);
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Bbang.this).inflate(R.layout.item_bbang_lv, parent, false);
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
            @ViewInject(R.id.item_bbanglv_imgv)
            ImageView imgv_head;
            @ViewInject(R.id.item_bbang_tv_name)
            TextView tv_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

    public void showBuilder(final int index) {
        View view = View.inflate(Bbang.this, R.layout.dlg_exit, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(Bbang.this, R.style.dialog);
        tv_content.setText("你确定要绑定给该水工吗？");
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                showProgressDialog();
                site.bindMember(application.getUserInfo().get("site_id"), maps.get(index).get("c_id"), m_id, Bbang.this);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
