package com.toocms.drink5.boss.ui.mine.pro;

import android.app.Dialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.zero.android.common.view.linearlistview.LinearListView;

/**
 * @author Zero
 * @date 2016/6/4 10:17
 */
public class Bprodetails extends BaseAty {

    @ViewInject(R.id.bprod_lv)
    private LinearListView bprod_lv;
    @ViewInject(R.id.bprode_imgv_cover)
    private ImageView imgv_cover;
    @ViewInject(R.id.bprode_tv_name)
    private TextView tv_name;
    @ViewInject(R.id.bprode_tv_attr)
    private TextView tv_attr;
    @ViewInject(R.id.bprode_tv_price)
    private TextView tv_price;
    @ViewInject(R.id.bprod_tv_num)
    private TextView tv_num;
    @ViewInject(R.id.bprode_tv_prompt)
    private TextView tv_prompt;
    @ViewInject(R.id.bprode_tv_content)
    private TextView tv_content;
    @ViewInject(R.id.bprode_linlay_prompt)
    private LinearLayout linlay_prompt;
    @ViewInject(R.id.bprode_linlay_gallery)
    private LinearLayout linlay_gallery;
    @ViewInject(R.id.bprode_v_line)
    private View v_line;


    private int yy = 0;
    private String goods_id;
    private Goods goods;
    private ImageLoader imageLoader;
    private Map<String, String> map;
    private ArrayList<Map<String, String>> ticket_list;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bprodetails;
    }

    @Override
    protected void initialized() {
        if (getIntent().hasExtra("goods_id")) {
            goods_id = getIntent().getStringExtra("goods_id");
        }
        goods = new Goods();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        goods.goodsDetail(goods_id, this);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("goodsDetail")) {
            map = JSONUtils.parseDataToMap(result);
            ArrayList<Map<String, String>> maps = JSONUtils.parseKeyAndValueToMapList(map.get("gallery"));
            ticket_list = JSONUtils.parseKeyAndValueToMapList(map.get("ticket_list"));
            imageLoader.disPlay(imgv_cover, map.get("cover"));
            for (int i = 0; i < maps.size(); i++) {
                ImageView imgv = new ImageView(Bprodetails.this);
                imgv.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout.LayoutParams paramsView2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(600), TypedValue.COMPLEX_UNIT_PX);
                imageLoader.disPlay(imgv, maps.get(i).get("img"));
                linlay_gallery.addView(imgv, paramsView2);

                View view2 = new View(Bprodetails.this);
                view2.setBackgroundColor(0xffffffff);
                LinearLayout.LayoutParams paramsView3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(30), TypedValue.COMPLEX_UNIT_PX);
                linlay_gallery.addView(view2, paramsView3);
            }
            tv_name.setText(map.get("goods_name"));
            tv_attr.setText("规格" + map.get("attr") + "L");
            tv_price.setText("￥" + map.get("goods_price"));
            tv_num.setText(map.get("volume"));
            tv_content.setText(map.get("intro"));
            if (map.get("is_promotion").equals("1")) {
                linlay_prompt.setVisibility(View.VISIBLE);
                tv_prompt.setText(map.get("prompt"));
            } else {
                linlay_prompt.setVisibility(View.GONE);
                v_line.setVisibility(View.GONE);
            }
            bprod_lv.setAdapter(new Myadapter());
        }

        if (params.getUri().contains("del")) {
            finish();
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("商品详情");
    }

    @Event(value = {R.id.bprode_tv_edit, R.id.bprode_tv_del})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bprode_tv_edit:
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goods_id);
                startActivity(AddproAty.class, bundle);
                break;
            case R.id.bprode_tv_del:
                showBuilder(1, map.get("goods_id"));
                break;
        }
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ticket_list.size();
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
                convertView = LayoutInflater.from(Bprodetails.this).inflate(R.layout.item_bprod_lv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_content.setText(map.get("goods_name") + "买" + ticket_list.get(position).get("buy_num") +
                    "送" + ticket_list.get(position).get("give_num"));
            viewHolder.tv_price.setText("￥" + ticket_list.get(position).get("price"));
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_bprodlv_content)
            public TextView tv_content;
            @ViewInject(R.id.item_bprodlv_price)
            public TextView tv_price;
        }
    }

    public void showBuilder(final int index, final String goods_id) {
        View view = View.inflate(Bprodetails.this, R.layout.dlg_exit, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(Bprodetails.this, R.style.dialog);
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
                    goods.del(goods_id, Bprodetails.this);
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
