package com.toocms.drink5.boss.ui.mine.pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Goods;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.myselectorimg.Myselectorimg;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.tool.Commonly;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.linearlistview.LinearListView;

/**
 * @author Zero
 * @date 2016/5/23 9:06
 */
public class AddproAty extends BaseAty {
    @ViewInject(R.id.addpro_imgv_pro)
    private ImageView imgv_pro;
    @ViewInject(R.id.addpro_imgv_cancel)
    private ImageView imgv_cancel;
    @ViewInject(R.id.addpro_gv)
    private GridView addpro_gv;
    @ViewInject(R.id.addpro_cbox_yh)
    private CheckBox cbox_yh;
    @ViewInject(R.id.addpro_edtext_yh)
    private EditText edtext_yh;
    @ViewInject(R.id.addpro_edxt_name)     //商品名
    private EditText edxt_name;
    @ViewInject(R.id.addpro_tv_pai)       //品牌
    private TextView tv_pai;
    @ViewInject(R.id.addpro_tv_shop)      //商品分类
    private TextView tv_shop;
    @ViewInject(R.id.addpro_etxt_attr)    //规格
    private EditText etxt_attr;
    @ViewInject(R.id.addpro_etxt_price)   //价格
    private EditText etxt_price;
    @ViewInject(R.id.addpro_etxt_intro)   // 商品介绍
    private EditText etxt_intro;
    @ViewInject(R.id.addpro_lv)
    private LinearListView addpro_lv;

    private ImageLoader imageLoader;
    private boolean isProType;
    private Myadapter myadapter;
    private ArrayList<String> list;
    private ArrayList<String> list_id;
    private ArrayList<String> mList1;
    private ArrayList<String> mList2;
    private ArrayList<String> mList3;
    private ArrayList<Integer> list2;
    private ArrayList<String> tick;
    private Myadapter2 myadapter2;

    //    private File file;
//    private String path = "";
    String path = "";
    String path2;
    private Goods goods;
    private String idShop = "";
    private String is_promotion = "0";
    private String brand_id = "";
    private String cate_id = "";
    private String goods_id = "";
    private Map<String, String> map;
    private ArrayList<Map<String, String>> ticket_list;
    private ArrayList<Map<String, String>> gallery;
    private int yy = 0;
    private int zz = 0;
    private String pathBit;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_addpro;
    }

    @Override
    protected void initialized() {
        pathBit = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + x.app().getPackageName() + "/cache/sun";
        LogUtil.e(Environment.getExternalStorageDirectory().toString());
        myadapter2 = new Myadapter2();
        myadapter = new Myadapter();
        list = new ArrayList<>();
        list_id = new ArrayList<>();
        list2 = new ArrayList<>();
        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();
        mList3 = new ArrayList<>();
        tick = new ArrayList<>();
//        list.add("default");
        goods = new Goods();
        if (getIntent().hasExtra("goods_id")) {
            goods_id = getIntent().getStringExtra("goods_id");
        }
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        if (!TextUtils.isEmpty(goods_id)) {
            showProgressContent();
            goods.goodsDetail(goods_id, this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("添加水品");
//        imgv_pro.setImageResource(R.drawable.ic_default_pro);
        imgv_pro.setClickable(false);
        imgv_cancel.setVisibility(View.VISIBLE);
        cbox_yh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtext_yh.setVisibility(View.VISIBLE);
                    is_promotion = "1";
                } else {
                    edtext_yh.setVisibility(View.GONE);
                    is_promotion = "0";
                }
            }
        });
        if (zz == 0) {
            addpro_gv.setAdapter(myadapter);
            zz = 1;
        } else {
            myadapter.notifyDataSetChanged();
        }
//        path = "file://android_asset/ic_default_pro";
//        imageLoader.disPlay(imgv_pro, path);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default_pro);
        saveBitmapFile(bitmap);
        path = pathBit + "/a.jpg";
        imageLoader.disPlay(imgv_pro, path);
        imgv_pro.setClickable(true);

        if (TextUtils.isEmpty(goods_id)) {
            etxt_attr.setText("19");
            list.add(path);
            list.add("default");
            myadapter.notifyDataSetChanged();
        }
    }


    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }

    @Event(value = {R.id.addpro_relay_ptype, R.id.addpro_relay_stype, R.id.addpro_imgv_cancel, R.id.addpro_imgv_pro, R.id.addpro_tv_add
            , R.id.addpro_fb_ok})
    private void onTestBaidulClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.addpro_relay_ptype:
                bundle.putString("type", "ping");
                startActivityForResult(ProtypeAty.class, bundle, 1222);
                break;
            case R.id.addpro_relay_stype:
                bundle.putString("type", "shop");
                startActivityForResult(ProtypeAty.class, bundle, 1223);
                break;
            case R.id.addpro_imgv_cancel:
                imgv_cancel.setVisibility(View.GONE);
                imgv_pro.setImageResource(R.drawable.ic_addpro_jia);
                path = "";
                imgv_pro.setClickable(true);
                break;
            case R.id.addpro_imgv_pro:
//                isProType = true;
//                Intent intent = new Intent(this, Myselectorimg.class);
//                bundle.putInt("max_select_count", 1);
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 2083);

                isProType = true;
                bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent = new Intent(this, Myselectorimg.class);
                intent.putExtras(bundle);
                this.startActivityForResult(intent, 2083);
                break;
            case R.id.addpro_tv_add:
                list2.add(1);
                mList1.add("0");
                mList2.add("0");
                mList3.add("0");
                if (yy == 0) {
                    addpro_lv.setAdapter(myadapter2);
                    yy = 1;
                } else {
                    myadapter2.notifyDataSetChanged();
                }
                tick.add("");
                break;
            case R.id.addpro_fb_ok:
                if (TextUtils.isEmpty(path)) {
                    showToast("请选择商品图");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(edxt_name))) {
                    showToast("请输入商品名称");
                    return;
                }
                if (TextUtils.isEmpty(tv_pai.getText().toString())) {
                    showToast("请选择品牌类型");
                    return;
                }
                if (TextUtils.isEmpty(tv_shop.getText().toString())) {
                    showToast("请选择商品类型");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_attr))) {
                    showToast("请输入商品规格");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_price))) {
                    showToast("请输入商品价格");
                    return;
                }
                if (TextUtils.isEmpty(Commonly.getViewText(etxt_intro))) {
                    showToast("请输入商品介绍");
                    return;
                }
                if (list.size() <= 1) {
                    showToast("请选择商品图");
                    return;
                }
                if (is_promotion.equals("1")) {
                    if (TextUtils.isEmpty(Commonly.getViewText(edtext_yh))) {
                        showToast("请填写优惠信息");
                        return;
                    }
                } else {
                    edtext_yh.setText("");
                }
                showProgressDialog();
                goods.update(application.getUserInfo().get("water"), goods_id, path, Commonly.getViewText(edxt_name), list_id, cate_id, brand_id, Commonly.getViewText(etxt_attr),
                        Commonly.getViewText(etxt_price), Commonly.getViewText(etxt_intro),
                        list, mList1, mList2, mList3, tick, is_promotion, Commonly.getViewText(edtext_yh), application.getUserInfo().get("site_id"), this);
                break;
        }
    }

    public void saveBitmapFile(Bitmap bitmap) {
        File f = new File(pathBit);//将要保存图片的路径
        if (!f.exists()) {
            if (!f.exists()) {
                f.mkdirs();
            }
        }
        File file = new File(pathBit + "/a.jpg");//将要保存图片的路径
        if (!file.exists()) {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("update")) {
            finish();
        }
        if (params.getUri().contains("goodsDetail")) {
            map = JSONUtils.parseDataToMap(result);
            ticket_list = JSONUtils.parseKeyAndValueToMapList(map.get("ticket_list"));
            gallery = JSONUtils.parseKeyAndValueToMapList(map.get("gallery"));
            imageLoader.disPlay(imgv_pro, map.get("cover"));
            path = map.get("cover");
            edxt_name.setText(map.get("goods_name"));
            etxt_attr.setText(map.get("attr"));
            etxt_price.setText(map.get("goods_price"));
            if (map.get("is_promotion").equals("1")) {
                cbox_yh.setChecked(true);
                is_promotion = "1";
                edtext_yh.setText(map.get("prompt"));
            } else {
                is_promotion = "01";
                cbox_yh.setChecked(false);
            }
            cate_id = map.get("cate_id");
            brand_id = map.get("brand_id");
            etxt_intro.setText(map.get("intro"));
            tv_shop.setText(map.get("cate_name"));
            tv_pai.setText(map.get("brand_name"));
            for (int i = 0; i < ticket_list.size(); i++) {
                list2.add(1);
                mList1.add(ticket_list.get(i).get("buy_num"));
                mList2.add(ticket_list.get(i).get("give_num"));
                mList3.add(ticket_list.get(i).get("price"));
                tick.add(ticket_list.get(i).get("t_id"));
            }
            for (int i = 0; i < gallery.size(); i++) {
                list_id.add(gallery.get(i).get("gallery_id"));
                list = new ArrayList<>();
                list.add(gallery.get(i).get("img"));
                list.add("default");
            }
            myadapter.notifyDataSetChanged();
            if (yy == 0) {
                addpro_lv.setAdapter(myadapter2);
                yy = 1;
            } else {
                myadapter2.notifyDataSetChanged();
            }
            if (zz == 0) {
                addpro_gv.setAdapter(myadapter);
                zz = 1;
            } else {
                myadapter.notifyDataSetChanged();
            }
        }
        super.onComplete(params, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case 2083:
                if (data != null) {
                    if (isProType) {
                        path = data.getStringArrayListExtra("select_result").get(0);
                        imageLoader.disPlay(imgv_pro, data.getStringArrayListExtra("select_result").get(0));
                        imgv_cancel.setVisibility(View.VISIBLE);
                        isProType = false;
                        imgv_pro.setClickable(false);
                    } else {
                        list.remove(list.size() - 1);
                        list.add(data.getStringArrayListExtra("select_result").get(0));
                        list.add("default");
                        myadapter.notifyDataSetChanged();
                    }
                }
                break;
            case 1222:
                String name = data.getStringExtra("name");
                tv_pai.setText(name);
                brand_id = data.getStringExtra("brand_id");
                break;
            case 1223:
                String name2 = data.getStringExtra("name");
                tv_shop.setText(name2);
                cate_id = data.getStringExtra("cate_id");
                break;
        }
    }

    @Event(value = R.id.addpro_gv, type = AdapterView.OnItemClickListener.class)
    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (list.size() < 10) {
            if (position == list.size() - 1) {
//                Bundle bundle = new Bundle();
//                Intent intent = new Intent(this, Myselectorimg.class);
//                bundle.putInt("max_select_count", 9 - (list.size() - 1));
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 2083);
                Bundle bundle = new Bundle();
                bundle.putInt("select_count_mode", 0);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioX", 1.0f);
                bundle.putFloat("com.toocms.frame.ui.AspectRatioY", 1.0f);
                Intent intent = new Intent(this, Myselectorimg.class);
                intent.putExtras(bundle);
                this.startActivityForResult(intent, 2083);
            }
        } else {
            showToast("已达最高数量");
        }

    }


    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
                convertView = LayoutInflater.from(AddproAty.this).inflate(R.layout.item_addpro_gv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (list.size() == 1) {
                viewHolder.imgv_cancel.setVisibility(View.GONE);
                viewHolder.imgv_cover.setImageResource(R.drawable.ic_addpro_jia);
            } else {
                if (position != list.size() - 1) {
                    imageLoader.disPlay(viewHolder.imgv_cover, list.get(position));
                    viewHolder.imgv_cancel.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.imgv_cancel.setVisibility(View.GONE);
                    viewHolder.imgv_cover.setImageResource(R.drawable.ic_addpro_jia);
                }
            }
            viewHolder.imgv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).contains("http")) {
                        for (int i = 0; i < gallery.size(); i++) {
                            if (gallery.get(i).get("img").equals(list.get(position))) {
                                gallery.remove(i);
                                list_id.remove(i);
                            }
                        }
                    }
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_addpro_imgv_pro)
            public ImageView imgv_cover;
            @ViewInject(R.id.item_addpro_imgv_cancel)
            public ImageView imgv_cancel;
        }
    }

    private class Myadapter2 extends BaseAdapter {

        @Override
        public int getCount() {
            return mList2.size();
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
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(AddproAty.this).inflate(R.layout.item_addpro_lv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imgv_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list2.remove(position);
                    mList1.remove(position);
                    mList2.remove(position);
                    mList3.remove(position);
                    tick.remove(position);
                    notifyDataSetChanged();
                }
            });
            viewHolder.item_etxt_mai.setText(mList1.get(position));
            viewHolder.item_etxt_song.setText(mList2.get(position));
            viewHolder.item_etxt_price.setText(mList3.get(position));

            viewHolder.item_etxt_mai.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mList1.set(position, Commonly.getViewText(viewHolder.item_etxt_mai));
                }
            });
            viewHolder.item_etxt_song.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mList2.set(position, Commonly.getViewText(viewHolder.item_etxt_song));
                }
            });
            viewHolder.item_etxt_price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mList3.set(position, Commonly.getViewText(viewHolder.item_etxt_price));
                }
            });
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_addprolv_imgv_jian)
            public ImageView imgv_jian;
            @ViewInject(R.id.item_addprolv_etxt_price)
            public EditText item_etxt_price;
            @ViewInject(R.id.item_addprolv_etxt_mai)
            public EditText item_etxt_mai;
            @ViewInject(R.id.item_addprolv_etxt_song)
            public EditText item_etxt_song;
        }
    }

}
