package com.toocms.drink5.boss.ui.lar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.BaiduLBS;
import com.toocms.drink5.boss.ui.BaseAty;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.util.ListUtils;

/**
 * 搜索地址
 *
 * @author Zero
 * @date 2016/1/20 18:58
 */
public class SearchAddressAty extends BaseAty implements TextWatcher {

    @ViewInject(R.id.search_address_keyword)
    private EditText etxtKeyword;
    @ViewInject(android.R.id.list)
    private ListView listView;
    private MyAdapter adapter;
    private String city;

    private BaiduLBS baiduLBS;
    private Map<String, String> map;

    private List<Map<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        etxtKeyword.addTextChangedListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_search_address;
    }

    @Override
    protected int getTitlebarResId() {
        return R.id.search_address_titlebar;
    }

    @Override
    protected void initialized() {
        baiduLBS = new BaiduLBS();
        if (getIntent().hasExtra("city")) {
            city = getIntent().getStringExtra("city");
        }
    }

    @Override
    protected void requestData() {
    }

    @Event(value = {R.id.search_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.search_imgv_back:
                finish();
                break;
        }
    }

    private int index;

    @Event(value = android.R.id.list, type = AdapterView.OnItemClickListener.class)
    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> map1 = JSONUtils.parseKeyAndValueToMap(list.get(index).get("location"));
        String s = map1.get("lat") + "," + map1.get("lng");
        index = position;

        showProgressDialog();
        baiduLBS.search2(s, SearchAddressAty.this);

//        Intent intent = getIntent();
//        intent.putExtra("address", adapter.getItem(position).get("address"));
//        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(adapter.getItem(position).get("location"));
//        intent.putExtra("latitude", map.get("lat"));
//        intent.putExtra("longitude", map.get("lng"));
//        setResult(RESULT_OK, intent);
//        finish();
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("search")) {
            map = JSONUtils.parseKeyAndValueToMap(result);
            list = JSONUtils.parseKeyAndValueToMapList(map.get("results"));
            if (adapter == null) {
                adapter = new MyAdapter();
                listView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
        if (params.getUri().contains("geocoder")) {
            Map<String, String> map = JSONUtils.parseKeyAndValueToMap(result);
            Map<String, String> map1 = JSONUtils.parseKeyAndValueToMap(map.get("result"));
            Map<String, String> map2 = JSONUtils.parseKeyAndValueToMap(map1.get("addressComponent"));
            String province = map2.get("province");
            String city = map2.get("city");
            String district = map2.get("district");

            Intent intent = getIntent();
            intent.putExtra("address", adapter.getItem(index).get("name"));
            Map<String, String> map3 = JSONUtils.parseKeyAndValueToMap(adapter.getItem(index).get("location"));
            intent.putExtra("latitude", map3.get("lat"));
            intent.putExtra("longitude", map3.get("lng"));
            intent.putExtra("province", province);
            intent.putExtra("city", city);
            intent.putExtra("district", district);
            setResult(RESULT_OK, intent);
            finish();

        }
        super.onComplete(params, result);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s)) {
            if (!TextUtils.isEmpty(city)) {
                baiduLBS.search(s.toString(), city, SearchAddressAty.this);
//                baiduLBS.search2("", SearchAddressAty.this);
            }
        }
    }


    //
//    @Override
//    public void onComplete(String requestUrl, ResponseInfo<String> responseInfo) {
//        LogUtils.e(requestUrl);
//        Map<String, String> map = JSONUtils.parseKeyAndValueToMap(responseInfo.result);
//        list = JSONUtils.parseKeyAndValueToMapList(map.get("results"));
//        if (adapter == null) {
//            adapter = new MyAdapter();
//            listView.setAdapter(adapter);
//        } else {
//            adapter.notifyDataSetChanged();
//        }
//        super.onComplete(requestUrl, responseInfo);
//    }

    private class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return ListUtils.getSize(list);
        }

        @Override
        public Map<String, String> getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, String> map = getItem(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(SearchAddressAty.this).inflate(R.layout.listitem_location, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvAddress.setText(map.get("name"));
            viewHolder.tvDetail.setText(map.get("address"));
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.listitem_location_address)
            public TextView tvAddress;
            @ViewInject(R.id.listitem_location_detail)
            public TextView tvDetail;
        }
    }
}
