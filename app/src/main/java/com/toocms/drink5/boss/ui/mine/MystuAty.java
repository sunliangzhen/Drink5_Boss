package com.toocms.drink5.boss.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Courier;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.mines.news.NewsAty;
import com.toocms.frame.image.ImageLoader;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.linearlistview.LinearListView;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;

/**
 * @author Zero
 * @date 2016/5/24 13:53
 */
public class MystuAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener {

    @ViewInject(R.id.mystu_lv)
    private SwipeToLoadRecyclerView mystu_lv;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    private Courier courier;
    private int p = 1;
    private MyAdapter myAdapter;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_mystu;
    }

    @Override
    protected void initialized() {
        maps = new ArrayList<>();
        courier = new Courier();
        myAdapter = new MyAdapter();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        courier.index(application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mystu_lv.startRefreshing();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("我的水工");
        mystu_lv.setOnRefreshListener(this);
        mystu_lv.setOnLoadMoreListener(this);
        mystu_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        mystu_lv.setAdapter(myAdapter);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("Courier/index")) {
            mystu_lv.stopLoadingMore();
            mystu_lv.stopRefreshing();
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapter.notifyDataSetChanged();
        }
        if (params.getUri().contains("unBind")) {
            mystu_lv.startRefreshing();
        }
        super.onComplete(params, result);

    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        mystu_lv.stopLoadingMore();
        mystu_lv.stopRefreshing();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            maps = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_student);
        menuItem.setIcon(R.drawable.ic_student_gps);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_student:
                startActivity(Mystu2Aty.class, null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MystuAty.this).inflate(R.layout.item_mystu_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final ArrayList<Map<String, String>> member_list = JSONUtils.parseKeyAndValueToMapList(maps.get(position).get("member_list"));
            holder.mystu_lv.setVisibility(View.GONE);
            holder.mystu_cbox.setChecked(false);
            holder.mystu_cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (member_list.size() > 0) {
                            holder.mystu_lv.setAdapter(new MyAdapterLv(member_list));
                            holder.mystu_lv.setVisibility(View.VISIBLE);
                        } else {
                            holder.mystu_cbox.setChecked(false);
                            showToast("该水工暂无用户");
                        }
                    } else {
                        holder.mystu_lv.setVisibility(View.GONE);
                    }
                }
            });
            holder.imgv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "boss");
                    bundle.putString("c_id", maps.get(position).get("c_id"));
                    startActivity(NewsAty.class, bundle);
                }
            });
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "boss");
                    bundle.putString("c_id", maps.get(position).get("c_id"));
                    startActivity(NewsAty.class, bundle);
                }
            });
            imageLoader.disPlay(holder.imgv_head, maps.get(position).get("head"));
            holder.tv_name.setText(maps.get(position).get("nickname"));
            holder.tv_tong.setText(maps.get(position).get("curr_order"));
            holder.tv_piao.setText(maps.get(position).get("curr_ticket"));
            holder.tv_stu.setText(maps.get(position).get("curr_member"));
            holder.imgv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MystuAty.this.callPhone(maps.get(position).get("account"));
                }
            });

        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_mystulv_lv)
            private LinearListView mystu_lv;
            @ViewInject(R.id.item_mystulv_cbbox)
            private CheckBox mystu_cbox;
            @ViewInject(R.id.item_mystulv_imgv_head)
            private ImageView imgv_head;
            @ViewInject(R.id.item_mystulv_tv_nickname)
            private TextView tv_name;
            @ViewInject(R.id.item_mystulv_tv_tong)
            private TextView tv_tong;
            @ViewInject(R.id.item_mystulv_tv_piao)
            private TextView tv_piao;
            @ViewInject(R.id.item_mystulv_tv_stu)
            private TextView tv_stu;
            @ViewInject(R.id.item_mystu_imgv_phone)
            private ImageView imgv_phone;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

    public class MyAdapterLv extends BaseAdapter {
        private ArrayList<Map<String, String>> member_list;

        public MyAdapterLv(ArrayList<Map<String, String>> member_list) {
            this.member_list = member_list;
        }

        @Override
        public int getCount() {
            return member_list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MystuAty.this).inflate(R.layout.item_mystu_lv_item, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_name.setText(member_list.get(position).get("nickname"));
            viewHolder.tv_phone.setText(member_list.get(position).get("account"));
            viewHolder.tv_address.setText(member_list.get(position).get("address"));
            imageLoader.disPlay(viewHolder.Imgv_head, member_list.get(position).get("head"));
            viewHolder.tv_mystu_jiubang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressDialog();
                    courier.unBind(member_list.get(position).get("m_id"), MystuAty.this);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_mystulv_item_imgv)
            public ImageView Imgv_head;
            @ViewInject(R.id.item_mystulv_item_tv_name)
            public TextView tv_name;
            @ViewInject(R.id.item_mystulv_item_tv_phone)
            public TextView tv_phone;
            @ViewInject(R.id.item_mystulv_item_tv_address)
            public TextView tv_address;
            @ViewInject(R.id.mystu_jiubang)
            public TextView tv_mystu_jiubang;
        }
    }
}
