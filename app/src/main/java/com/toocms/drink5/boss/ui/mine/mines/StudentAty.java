package com.toocms.drink5.boss.ui.mine.mines;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Contact;
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
 * @date 2016/5/19 13:06
 */
public class StudentAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {


    @ViewInject(R.id.student_lv)
    private SwipeToLoadRecyclerView student_lv;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;
    private Contact contact;
    private ArrayList<Map<String, String>> maps;
    private MyAdapter myAdapter;
    private ImageLoader imageLoader;
    private int p = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_student;
    }

    @Override
    protected void initialized() {
        contact = new Contact();
        maps = new ArrayList<>();
        myAdapter = new MyAdapter();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        contact.isColleague(application.getUserInfo().get("c_id"), p, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("我的同事");
        student_lv.setOnRefreshListener(this);
        student_lv.setOnLoadMoreListener(this);
        student_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        student_lv.setAdapter(myAdapter);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        student_lv.stopLoadingMore();
        student_lv.stopRefreshing();
        if (params.getUri().contains("isColleague")) {
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
        student_lv.stopLoadingMore();
        student_lv.stopRefreshing();
        removeProgressContent();
        removeProgressDialog();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            maps = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_student) {
            startActivity(Student2Aty.class, null);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        p = 1;
        contact.isColleague(application.getUserInfo().get("c_id"), p, this);
    }

    @Override
    public void onLoadMore() {
        p++;
        contact.isColleague(application.getUserInfo().get("c_id"), p, this);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(StudentAty.this).inflate(R.layout.item_student_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            imageLoader.disPlay(holder.imgv_head, maps.get(position).get("head"));
            holder.tv_name.setText(maps.get(position).get("nickname"));
            holder.tv_tong.setText(maps.get(position).get("num"));
            holder.tv_hu.setText(maps.get(position).get("mem"));
            holder.tv_piao.setText(maps.get(position).get("ticket"));
            holder.imgv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StudentAty.this.callPhone(maps.get(position).get("account"));
                }
            });
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_stulv_imgv_head)
            private ImageView imgv_head;
            @ViewInject(R.id.item_studentlv_imgv_phone)
            private ImageView imgv_phone;
            @ViewInject(R.id.item_stulv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_stulv_tong)
            private TextView tv_tong;
            @ViewInject(R.id.item_stulv_tv_piao)
            private TextView tv_piao;
            @ViewInject(R.id.item_stulv_tv_hu)
            private TextView tv_hu;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
