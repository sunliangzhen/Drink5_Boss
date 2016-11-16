package com.toocms.drink5.boss.ui.mine.pro;

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
import com.toocms.drink5.boss.interfaces.Goods;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.MainAty;
import com.toocms.drink5.boss.ui.lar.LarAty;
import com.toocms.frame.config.Config;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.tool.AppManager;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.FileManager;
import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/21 17:17
 */
public class BproAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.bpro_lv)
    private SwipeToLoadRecyclerView bpro_lv;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;


    private int p = 1;
    private Goods goods;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;
    private MyAdapter myAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bpro;
    }

    @Override
    protected void initialized() {
        goods = new Goods();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        myAdapter = new MyAdapter();
        maps = new ArrayList<>();
    }

    @Override
    protected void requestData() {
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("goodsList")) {
            bpro_lv.stopLoadingMore();
            bpro_lv.stopRefreshing();
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapter.notifyDataSetChanged();
        }
        if (params.getUri().contains("del")) {
            bpro_lv.startRefreshing();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
        bpro_lv.stopLoadingMore();
        bpro_lv.stopRefreshing();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            maps = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        goods.goodsList(application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("水站水品");
        bpro_lv.setOnRefreshListener(this);
        bpro_lv.setOnLoadMoreListener(this);
        bpro_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", maps.get(i).get("goods_id"));
                startActivity(Bprodetails.class, bundle);
            }
        });
        imgv_empty.setVisibility(View.GONE);
        tv_empty.setVisibility(View.GONE);
        bpro_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        bpro_lv.setAdapter(myAdapter);
    }

    @Event(value = {R.id.bpro_linlay_addpro})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bpro_linlay_addpro:
                startActivity(AddproAty.class, null);
                break;
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        goods.goodsList(application.getUserInfo().get("site_id"), p, this);

    }

    @Override
    public void onLoadMore() {
        p++;
        goods.goodsList(application.getUserInfo().get("site_id"), p, this);

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BproAty.this).inflate(R.layout.item_bpro_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            imageLoader.disPlay(holder.imgv_por, maps.get(position).get("cover"));
            holder.tv_name.setText(maps.get(position).get("goods_name"));
            holder.tv_type.setText("规格:" + maps.get(position).get("attr") + "L");
            holder.tv_price.setText("￥" + maps.get(position).get("goods_price"));
            holder.tv_num.setText(maps.get(position).get("volume"));
            holder.imgv_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("goods_id", maps.get(position).get("goods_id"));
                    startActivity(AddproAty.class, bundle);
                }
            });
            holder.imgv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBuilder(1, maps.get(position).get("goods_id"));
                }
            });
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_bprolv_imgv_pro)
            ImageView imgv_por;
            @ViewInject(R.id.item_bprolv_tv_name)
            TextView tv_name;
            @ViewInject(R.id.item_bprolv_tv_type)
            TextView tv_type;
            @ViewInject(R.id.item_bprolv_tv_price)
            TextView tv_price;
            @ViewInject(R.id.item_bprolv_tv_num)
            TextView tv_num;
            @ViewInject(R.id.item_bprolv_imgv_edit)
            ImageView imgv_edit;
            @ViewInject(R.id.item_bprolv_del)
            ImageView imgv_del;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

    public void showBuilder(final int index, final String goods_id) {
        View view = View.inflate(BproAty.this, R.layout.dlg_exit, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(BproAty.this, R.style.dialog);
        if (index == 1) {
            tv_content.setText("你确定要删除该商品吗？");
        } else if (index == 2) {
            tv_content.setText("你确定要退出账号吗？");
        } else if (index == 3) {
            tv_content.setText("你确定要清除缓存吗？");
        }
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
                if (index == 1) {
                    showProgressDialog();
                    goods.del(goods_id, BproAty.this);
                } else if (index == 2) {
                    Config.setLoginState(false);
                    startActivity(LarAty.class, null);
                    finish();
                    AppManager.getInstance().killActivity(MainAty.class);
                } else if (index == 3) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileManager.clearCacheFiles();
                        }
                    }).start();
                }

            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
