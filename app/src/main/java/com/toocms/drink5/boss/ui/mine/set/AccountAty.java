package com.toocms.drink5.boss.ui.mine.set;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.database.Account;
import com.toocms.drink5.boss.database.MyApplication;
import com.toocms.drink5.boss.interfaces.RegisterLog;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.image.ImageLoader;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/25 11:30
 */
public class AccountAty extends BaseAty {
    @ViewInject(R.id.account_lv)
    private ListView account_lv;
    private MyAdapter myAdapter;
    private RegisterLog registerLog;
    private List<Account> list;
    private ImageLoader imageLoader;
    private ArrayList<Integer> list_check;
    private int index;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_account;
    }

    @Override
    protected void initialized() {
        registerLog = new RegisterLog();
        list = new ArrayList<>();
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
        mActionBar.setTitle("账号管理");
        myAdapter = new MyAdapter();
        account_lv.setAdapter(myAdapter);
        account_lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!list.get(position).getSite_id().equals(application.getUserInfo().get("site_id"))) {
                    showBuilder(position);
                }
                return true;
            }
        });
    }

    public void showBuilder(final int index) {
        View view = View.inflate(AccountAty.this, R.layout.dlg_exit, null);
        TextView tv_content = (TextView) view.findViewById(R.id.buildeexti_tv_content);
        TextView tv_no = (TextView) view.findViewById(R.id.buildeexti_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderexit_tv_ok);
        final Dialog dialog = new Dialog(AccountAty.this, R.style.dialog);
        tv_content.setText("你确定要移除此水站吗？");
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
                DbManager db = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig());
                try {
                    db.delete(Account.class, WhereBuilder.b("site_id", "=", list.get(index).getSite_id()));
                } catch (DbException e) {
                    e.printStackTrace();
                }
                selectData();
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        selectData();
    }

    public void selectData() {

        DbManager db = x.getDb(((MyApplication) getApplicationContext()).getDaoConfig());
//        try {
//            db.update(Account.class, WhereBuilder.b("site_id", "=", 1), new KeyValue("name", "君临大厦水站"));
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
        try {
            list = db.selector(Account.class).orderBy("id", true).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (list != null) {
            if (list.size() > 0) {
                list_check = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getSite_id().equals(application.getUserInfo().get("site_id"))) {
                        list_check.add(1);
                        index = i;
                    } else {
                        list_check.add(0);
                    }
                }
            }
        }else{
            list = new ArrayList<>();
        }
        myAdapter.notifyDataSetChanged();
    }

    @Event(value = R.id.account_lv, type = AdapterView.OnItemClickListener.class)
    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!list.get(position).getSite_id().equals(application.getUserInfo().get("site_id"))) {
            showProgressDialog();
            registerLog.login(list.get(position).getPhone(), list.get(position).getPass(), this);
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("login")) {
            application.setUserInfo(JSONUtils.parseDataToMap(result));
            selectData();
        }
        super.onComplete(params, result);
    }

    @Event(value = {R.id.account_tv})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.account_tv:
                startActivity(AddaccountAty.class, null);
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(AccountAty.this).inflate(R.layout.item_account_lv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (list_check.get(position) == 1) {
                viewHolder.accountlv_cbox.setChecked(true);
            } else {
                viewHolder.accountlv_cbox.setChecked(false);
            }
            viewHolder.tv_name.setText(list.get(position).getName());
            imageLoader.disPlay(viewHolder.imgv_head, list.get(position).getHead());
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_accountlv_cbox)
            public CheckBox accountlv_cbox;
            @ViewInject(R.id.item_accountlv_imgv_head)
            public ImageView imgv_head;
            @ViewInject(R.id.item_accountlv_tv_name)
            public TextView tv_name;
        }
    }
}
