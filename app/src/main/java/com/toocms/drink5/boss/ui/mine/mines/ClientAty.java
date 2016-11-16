package com.toocms.drink5.boss.ui.mine.mines;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Contact;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.tool.Commonly;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
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
public class ClientAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, TextView.OnEditorActionListener {

    @ViewInject(R.id.client_cbox_time)
    private CheckBox cbox_time;
    @ViewInject(R.id.client_cbox_long)
    private CheckBox cbox_long;
    @ViewInject(R.id.client_cbox_num)
    private CheckBox cbox_num;
    @ViewInject(R.id.client_etxt)
    private EditText client_etxt;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    @ViewInject(R.id.client_lv)
    private SwipeToLoadRecyclerView client_lv;

    private Drawable rightDrawable;
    private Drawable rightDrawable2;
    private Contact contact;
    private String type = "";
    private String search = "";
    private int p = 1;
    private ArrayList<Map<String, String>> maps;
    private MyAdapter myAdapter;
    private ImageLoader imageLoader;
    private ImageOptions imageOptions;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_client;
    }

    @Override
    protected void initialized() {
        rightDrawable = getResources().getDrawable(R.drawable.selector_page_top);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        rightDrawable2 = getResources().getDrawable(R.drawable.ic_page_topn);
        rightDrawable2.setBounds(0, 0, rightDrawable2.getMinimumWidth(), rightDrawable2.getMinimumHeight());
        contact = new Contact();
        maps = new ArrayList<>();
        myAdapter = new MyAdapter();
        imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
    }

    @Override
    protected void requestData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        contact.isClient(application.getUserInfo().get("c_id"), type, search, p, this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        client_lv.stopLoadingMore();
        client_lv.stopRefreshing();
        if (params.getUri().contains("isClient")) {
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
        client_lv.stopLoadingMore();
        client_lv.stopRefreshing();
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
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        client_etxt.setOnEditorActionListener(this);
        client_lv.setOnRefreshListener(this);
        client_lv.setOnLoadMoreListener(this);
        client_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        client_lv.setAdapter(myAdapter);
    }


    @Event(value = {R.id.client_cbox_time, R.id.client_cbox_long, R.id.client_cbox_num, R.id.client_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.client_imgv_back:
                finish();
                break;
            case R.id.client_cbox_time:
                cbox_time.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setChecked(false);
                cbox_num.setChecked(false);
                if (cbox_time.isChecked()) {
                    type = "1";
                } else {
                    type = "2";
                }
                search = "";
                client_lv.startRefreshing();
                break;
            case R.id.client_cbox_long:
                cbox_long.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setChecked(false);
                cbox_time.setChecked(false);
                if (cbox_long.isChecked()) {
                    type = "3";
                } else {
                    type = "4";
                }
                search = "";
                client_lv.startRefreshing();
                break;
            case R.id.client_cbox_num:
                cbox_num.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setChecked(false);
                cbox_time.setChecked(false);
                cbox_time.setChecked(false);
                if (cbox_long.isChecked()) {
                    type = "5";
                } else {
                    type = "6";
                }
                search = "";
                client_lv.startRefreshing();
                break;
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        contact.isClient(application.getUserInfo().get("c_id"), type, search, p, this);

    }

    @Override
    public void onLoadMore() {
        p++;
        contact.isClient(application.getUserInfo().get("c_id"), type, search, p, this);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(Commonly.getViewText(client_etxt))) {
                showToast("请输入搜索内容");
                return false;
            }
            //隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    client_etxt.getWindowToken(), 0);
            p = 1;
            search = Commonly.getViewText(client_etxt);
            client_lv.startRefreshing();
        }
        return false;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ClientAty.this).inflate(R.layout.item_client_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv_beizhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("beizhu", maps.get(position).get("remarks"));
                    bundle.putString("m_id", maps.get(position).get("m_id"));
                    bundle.putString("type", "shui");
                    startActivity(ClientbeiAty.class, bundle);
                }
            });
//            imageLoader.disPlay(holder.imgv_head, maps.get(position).get("head"));
            x.image().bind(holder.imgv_head, maps.get(position).get("head"), imageOptions);
            holder.tv_name.setText(maps.get(position).get("nickname"));
            holder.tv_nickname.setText(maps.get(position).get("remarks"));
            holder.tv_address.setText(maps.get(position).get("ress"));
            holder.tv_tong.setText(maps.get(position).get("buy_num"));
            holder.tv_piao.setText(maps.get(position).get("ticket_number"));
            holder.tv_time.setText(maps.get(position).get("last_time"));
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_clientlv_tv_beizhu)
            private TextView tv_beizhu;
            @ViewInject(R.id.item_clientlv_imgv_head)
            private ImageView imgv_head;
            @ViewInject(R.id.item_clientlv_tv_nickname)
            private TextView tv_name;
            @ViewInject(R.id.item_clientlv_tv_nickname2)
            private TextView tv_nickname;
            @ViewInject(R.id.item_clientlv_tv_addresss)
            private TextView tv_address;
            @ViewInject(R.id.item_client_tv_tong)
            private TextView tv_tong;
            @ViewInject(R.id.item_client_tv_piao)
            private TextView tv_piao;
            @ViewInject(R.id.item_client_tv_time)
            private TextView tv_time;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
