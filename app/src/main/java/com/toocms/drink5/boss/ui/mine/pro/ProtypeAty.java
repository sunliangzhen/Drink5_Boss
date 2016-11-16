package com.toocms.drink5.boss.ui.mine.pro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Goods;
import com.toocms.drink5.boss.ui.BaseAty;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/23 10:17
 */
public class ProtypeAty extends BaseAty {

    @ViewInject(R.id.protype_lv)
    private SwipeToLoadRecyclerView protype_lv;
    private String type = "";
    private List<String> list;
    private Goods goods;
    private int p = 1;
    private ArrayList<Map<String, String>> maps;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_protype;
    }

    @Override
    protected void initialized() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
        }
        goods = new Goods();
    }

    @Override
    protected void requestData() {
        showProgressContent();
        if (type.equals("ping")) {
            goods.goodsBrand(p, this);
        } else {
            goods.goodsCate(application.getUserInfo().get("site_id"), p, this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        maps = JSONUtils.parseDataToMapList(result);
        protype_lv.setAdapter(new MyAdapter());
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (type.equals("ping")) {
            mActionBar.setTitle("品牌分类");
        } else if (type.equals("shop")) {
            mActionBar.setTitle("商品分类");
        }
//        protype_lv.setOnRefreshListener(this);
//        protype_lv.setOnLoadMoreListener(this);
        protype_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        protype_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent();
                if (type.equals("ping")) {
                    intent.putExtra("brand_id", maps.get(i).get("brand_id"));
                } else {
                    intent.putExtra("cate_id", maps.get(i).get("cate_id"));
                }
                intent.putExtra("name", maps.get(i).get("name"));
                setResult(-1, intent);
                finish();
            }
        });
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ProtypeAty.this).inflate(R.layout.item_protype_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.item_tv_name.setText(maps.get(position).get("name"));
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_protypelv)
            TextView item_tv_name;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

}
