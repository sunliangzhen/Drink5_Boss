package com.toocms.drink5.boss.ui.mine.card;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
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


/**
 * @author Zero
 * @date 2016/5/23 17:10
 */
public class AddcardAty extends BaseAty {


    @ViewInject(R.id.addcard_tv_type)
    private TextView tv_type;
    @ViewInject(R.id.addcard_etxt_peo)
    private EditText etxt_peo;
    @ViewInject(R.id.addcard_etxt_num)
    private EditText etxt_num;
    @ViewInject(R.id.addcard_etxt_phone)
    private EditText etxt_phone;

    private String type = "";
    private String icon_id = "";
    private ArrayList<Map<String, String>> maps;
    private Site site;
    private ImageLoader imageLoader;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_addcard;
    }

    @Override
    protected void initialized() {
        maps = new ArrayList<>();
        site = new Site();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("添加银行卡");
    }


    @Event(value = {R.id.addcard_tv_type, R.id.fb_addcard_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.addcard_tv_type:
                showProgressDialog();
                site.bankList(application.getUserInfo().get("site_id"), AddcardAty.this);
                break;
            case R.id.fb_addcard_ok:
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_peo))) {
                    showToast("持卡人不能为空");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_num))) {
                    showToast("卡号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_phone))) {
                    showToast("手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(tv_type.getText().toString())) {
                    showToast("卡类型不能为空");
                    return;
                }
                showProgressDialog();
                site.bindBank(application.getUserInfo().get("site_id"), Commonly.getViewText(etxt_peo), Commonly.getViewText(etxt_num),
                        Commonly.getViewText(etxt_phone), icon_id, AddcardAty.this);
                break;
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("bankList")) {
            maps = JSONUtils.parseDataToMapList(result);
            showBuilderok();
        }
        if (params.getUri().contains("bindBank")) {
            finish();
        }

        super.onComplete(params, result);
    }

    public void showBuilderok() {
        View view = View.inflate(AddcardAty.this, R.layout.dlg_card, null);
        final Dialog dialog = new Dialog(AddcardAty.this, R.style.dialog);
        ListView lv = (ListView) view.findViewById(R.id.dlg_card_lv);
        lv.setAdapter(new Myadapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                type = maps.get(position).get("name");
                icon_id = maps.get(position).get("icon_id");
                tv_type.setText(type);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return maps.size();
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
                convertView = LayoutInflater.from(AddcardAty.this).inflate(R.layout.item_dlgcard, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            imageLoader.disPlay(viewHolder.imgv_icon, maps.get(position).get("icon"));
            viewHolder.tv_name.setText(maps.get(position).get("name"));
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_dlgcard_imgv_head)
            public ImageView imgv_icon;
            @ViewInject(R.id.item_dlgcard_tv_name)
            public TextView tv_name;
        }

    }
}
