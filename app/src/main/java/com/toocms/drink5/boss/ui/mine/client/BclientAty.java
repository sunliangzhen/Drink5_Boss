package com.toocms.drink5.boss.ui.mine.client;

import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.mines.ClientbeiAty;
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
import cn.zero.android.common.view.linearlistview.LinearListView;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;

/**
 * @author Zero
 * @date 2016/5/24 9:06
 */
public class BclientAty extends BaseAty implements OnRefreshListener, OnLoadMoreListener, TextView.OnEditorActionListener {

    @ViewInject(R.id.bclien_etxt_search)
    private EditText etxt_search;
    @ViewInject(R.id.bclient_cbox_time)
    private CheckBox cbox_time;
    @ViewInject(R.id.bclient_cbox_long)
    private CheckBox cbox_long;
    @ViewInject(R.id.bclient_cbox_num)
    private CheckBox cbox_num;
    @ViewInject(R.id.bclient_cbox)
    private CheckBox cbox;
    @ViewInject(R.id.bclient_tv_xuan)
    private TextView tv_xuan;
    @ViewInject(R.id.bclient_relay_bottom)
    private RelativeLayout relay_bottom;

    @ViewInject(R.id.bclient_lv)
    private SwipeToLoadRecyclerView bclient_lv;

    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    private Drawable rightDrawable;
    private Drawable rightDrawable2;
    private Site site;
    private String type = "";
    private String where = "";
    private int data1 = 0, data2 = 0, data3 = 0;
    private String typeData = "";
    private MyAdapter myAdapter;
    private ArrayList<Map<String, String>> maps;
    private int p = 1;
    private ImageLoader imageLoader;
    private ArrayList<Integer> list;
    private int xuan = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bclient;
    }

    @Override
    protected void initialized() {
        rightDrawable = getResources().getDrawable(R.drawable.selector_page_top);
        rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());
        rightDrawable2 = getResources().getDrawable(R.drawable.ic_page_topn);
        rightDrawable2.setBounds(0, 0, rightDrawable2.getMinimumWidth(), rightDrawable2.getMinimumHeight());
        site = new Site();
        myAdapter = new MyAdapter();
        maps = new ArrayList<>();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        list = new ArrayList<>();
    }

    @Override
    protected void requestData() {
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        bclient_lv.stopRefreshing();
        bclient_lv.stopLoadingMore();
        list = new ArrayList<>();
        if (params.getUri().contains("member")) {
            cbox.setChecked(false);
            xuan = 0;
            tv_xuan.setText("已选: " + xuan + "人");
            relay_bottom.setVisibility(View.VISIBLE);
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            for (int i = 0; i < maps.size(); i++) {
                list.add(0);
            }
            myAdapter.notifyDataSetChanged();
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
        bclient_lv.stopRefreshing();
        bclient_lv.stopLoadingMore();
        if (p == 1) {
            imgv_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            maps = new ArrayList<>();
            list = new ArrayList<>();
            myAdapter.notifyDataSetChanged();
            relay_bottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        p = 1;
        site.member(application.getUserInfo().get("site_id"), type, typeData, where, p, "", this);
    }

    @Override
    public void onLoadMore() {
        p++;
        site.member(application.getUserInfo().get("site_id"), type, typeData, where, p, "", this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        etxt_search.setOnEditorActionListener(this);
        bclient_lv.setOnRefreshListener(this);
        bclient_lv.setOnLoadMoreListener(this);
        bclient_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
        bclient_lv.setAdapter(myAdapter);
        cbox_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data1 = 1;
                } else {
                    data1 = 0;
                }
            }
        });
        cbox_long.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data2 = 1;
                } else {
                    data2 = 0;
                }
            }
        });
        cbox_num.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    data3 = 1;
                } else {
                    data3 = 0;
                }
            }
        });
        cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbox.isChecked()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, 1);
                    }
                    myAdapter.notifyDataSetChanged();
                    xuan = list.size();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, 0);
                    }
                    myAdapter.notifyDataSetChanged();
                    xuan = 0;
                }
                tv_xuan.setText("已选: " + xuan + "人");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        p = 1;
        showProgressContent();
        site.member(application.getUserInfo().get("site_id"), type, typeData, where, p, "", this);
        xuan = 0;
        tv_xuan.setText("已选: " + xuan + "人");
        cbox.setChecked(false);
    }

    @Event(value = {R.id.bclient_relay_piao, R.id.bclient_relay_time, R.id.bclient_relay_num, R.id.bclient_imgv_back, R.id.bclien_ttv_xuan
            , R.id.bclient_tv_bbang})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bclient_imgv_back:
                finish();
                break;
            case R.id.bclient_relay_piao:
                cbox_time.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_time.setChecked(!cbox_time.isChecked());
                cbox_long.setChecked(false);
                cbox_num.setChecked(false);
                type = "ticket_number";
                typeData = data1 + "";
                where = "";
                bclient_lv.startRefreshing();
                break;
            case R.id.bclient_relay_time:
                cbox_long.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setChecked(!cbox_long.isChecked());
                cbox_num.setChecked(false);
                cbox_time.setChecked(false);
                type = "last_time";
                typeData = data2 + "";
                where = "";
                bclient_lv.startRefreshing();
                break;
            case R.id.bclient_relay_num:
                cbox_num.setCompoundDrawables(null, null, rightDrawable, null);
                cbox_time.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_long.setCompoundDrawables(null, null, rightDrawable2, null);
                cbox_num.setChecked(!cbox_num.isChecked());
                cbox_long.setChecked(false);
                cbox_time.setChecked(false);
                type = "buy_num";
                typeData = data2 + "";
                where = "";
                bclient_lv.startRefreshing();
                break;
            case R.id.bclien_ttv_xuan:
                Intent intent = new Intent(this, Bfilterty.class);
                this.startActivityForResult(intent, 111);
                break;
            case R.id.bclient_tv_bbang:
                StringBuffer stringBuffer = new StringBuffer();
                int k = 1;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == 1) {
                        if (k != 1) {
                            stringBuffer.append(",");
                        }
                        stringBuffer.append(maps.get(i).get("m_id"));
                        k++;
                    }
                }
                if (TextUtils.isEmpty(stringBuffer.toString())) {
                    showToast("请选择用户");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("m_id", stringBuffer.toString());
                startActivity(Bbang.class, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == 111) {
            where = data.getStringExtra("where");
            showProgressContent();
            p = 1;
            site.member(application.getUserInfo().get("site_id"), type, typeData, where, p, "", this);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (TextUtils.isEmpty(Commonly.getViewText(etxt_search))) {
                showToast("请输入搜索内容");
                return false;
            }
            //隐藏软键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    etxt_search.getWindowToken(), 0);
            p = 1;
            type = "";
            where = "";
            showProgressDialog();
            site.member(application.getUserInfo().get("site_id"), type, typeData, where, p, Commonly.getViewText(etxt_search), this);
        }
        return false;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BclientAty.this).inflate(R.layout.item_bclient_lv, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.bclient_lv.setVisibility(View.GONE);
            holder.bclient_cbox.setChecked(false);
            holder.bclient_cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ArrayList<Map<String, String>> barrel_list = JSONUtils.parseKeyAndValueToMapList(maps.get(position).get("barrel_list"));
                        if (barrel_list.size() > 0) {
                            holder.bclient_lv.setAdapter(new MyAdapterLv(barrel_list));
                            holder.bclient_lv.setVisibility(View.VISIBLE);
                        } else {
                            showToast("暂无欠捅记录");
                            holder.bclient_cbox.setChecked(false);
                        }
                    } else {
                        holder.bclient_lv.setVisibility(View.GONE);
                    }
                }
            });
            holder.tv_beizhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("beizhu", maps.get(position).get("remarks"));
                    bundle.putString("m_id", maps.get(position).get("m_id"));
                    bundle.putString("type", "boss");
                    startActivity(ClientbeiAty.class, bundle);
                }
            });

            holder.tv_name.setText(maps.get(position).get("c_name"));
            holder.tv_name2.setText(maps.get(position).get("nickname"));
            if (TextUtils.isEmpty(maps.get(position).get("remarks"))) {
                holder.tv_nickname.setText("");
            } else {
                holder.tv_nickname.setText("(昵称:  " + maps.get(position).get("remarks") + ")");
            }
            holder.tv_address.setText(maps.get(position).get("address"));
            holder.tv_tong.setText(maps.get(position).get("buy_num"));
            holder.tv_time.setText(maps.get(position).get("last_time"));
            holder.tv_piao.setText(maps.get(position).get("ticket_number"));
            imageLoader.disPlay(holder.imgv_head, maps.get(position).get("head"));
            holder.bclient_cbox2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.bclient_cbox2.isChecked()) {
                        if (xuan < list.size()) {
                            xuan++;
                        }
                        list.set(position, 1);
                    } else {
                        if (xuan > 0) {
                            xuan--;
                        }
                        list.set(position, 0);
                    }
                    if (xuan == list.size()) {
                        cbox.setChecked(true);
                    } else if (xuan < list.size()) {
                        cbox.setChecked(false);
                    }
                    tv_xuan.setText("已选: " + xuan + "人");
                }
            });
            if (list.get(position) == 1) {
                holder.bclient_cbox2.setChecked(true);
            } else {
                holder.bclient_cbox2.setChecked(false);
            }
            holder.imgv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BclientAty.this.callPhone(maps.get(position).get("account"));
                }
            });

        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            @ViewInject(R.id.item_bclientlv_lv)
            private LinearListView bclient_lv;
            @ViewInject(R.id.item_bclientlv_cbbox)
            private CheckBox bclient_cbox;
            @ViewInject(R.id.item_bclientlv_cbox)
            private CheckBox bclient_cbox2;
            @ViewInject(R.id.item_bclientlv_imgv_head)
            private ImageView imgv_head;
            @ViewInject(R.id.item_bclientlv_tv_beizhu)
            private TextView tv_beizhu;
            @ViewInject(R.id.item_bclient_tv_name)
            private TextView tv_name;
            @ViewInject(R.id.item_bclientlv_tv_name2)
            private TextView tv_name2;
            @ViewInject(R.id.item_bclientlv_tv_nickname)
            private TextView tv_nickname;
            @ViewInject(R.id.item_bclient_tv_address)
            private TextView tv_address;
            @ViewInject(R.id.item_bclient_tv_tong)
            private TextView tv_tong;
            @ViewInject(R.id.item_bclient_tv_piao)
            private TextView tv_piao;
            @ViewInject(R.id.item_bclient_tv_time)
            private TextView tv_time;
            @ViewInject(R.id.item_bclientlv_imgv_phone)
            private ImageView imgv_phone;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }

    public class MyAdapterLv extends BaseAdapter {
        private ArrayList<Map<String, String>> barrel_list;

        public MyAdapterLv(ArrayList<Map<String, String>> barrel_list) {
            this.barrel_list = barrel_list;
        }

        @Override
        public int getCount() {
            return barrel_list.size();
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
                convertView = LayoutInflater.from(BclientAty.this).inflate(R.layout.item_bclient_lv_item, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_name.setText(barrel_list.get(position).get("goods_name") + "    " + barrel_list.get(position).get("attr"));
            viewHolder.tv_number.setText(barrel_list.get(position).get("barrel_num") + "只");
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_qbclient_tv_name)
            public TextView tv_name;
            @ViewInject(R.id.item_qbclient_tv_num)
            public TextView tv_number;
        }
    }
}
