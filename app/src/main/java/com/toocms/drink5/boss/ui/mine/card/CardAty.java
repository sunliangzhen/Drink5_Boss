package com.toocms.drink5.boss.ui.mine.card;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Pay2;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.money.MonqinAty;
import com.toocms.frame.image.ImageLoader;
import com.toocms.frame.tool.AppManager;
import com.toocms.frame.tool.Commonly;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.zero.android.common.util.JSONUtils;

/**
 * @author Zero
 * @date 2016/5/23 15:21
 */
public class CardAty extends BaseAty {

    @ViewInject(R.id.card_lv)
    private ListView card_lv;
    @ViewInject(R.id.card_relay_setpass)
    private RelativeLayout relay_00;
    @ViewInject(R.id.card_rbtn_01)
    private CheckBox cbox_01;
    @ViewInject(R.id.card_rbtn_02)
    private CheckBox cbox_02;
    @ViewInject(R.id.card_relay_cancel)
    private RelativeLayout relay_cancle;
    @ViewInject(R.id.card_tv_cancel)
    private TextView tv_cancle;
    @ViewInject(R.id.card_tv_jiechu)
    private TextView tv_jiechu;
    @ViewInject(R.id.card_tv_hui)
    private TextView tv_hui;
    @ViewInject(R.id.card_tv_state)
    private TextView tv_state;
    @ViewInject(R.id.fb_card_ok)
    private Button btn_ok;
    @ViewInject(R.id.card_tv_01)
    private TextView card_tv_01;
    private Site site;
    private Myadapter myadapter;
    private ArrayList<Map<String, String>> maps;
    private ArrayList<Integer> list;
    private ImageLoader imageLoader;
    private String bank_id = "";
    private String type = "";
    private int index = 0;
    private String order_ids;
    private String money;
    private String award_total;
    private String score_total;
    private String pay_fee_b;  //可提
    private String pay_fee_d;  //未提
    private String pay_type = "微信";
    private Pay2 pay;

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_card;
    }

    @Override
    protected void initialized() {
        site = new Site();
        myadapter = new Myadapter();
        maps = new ArrayList<>();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
        list = new ArrayList<>();
        if (getIntent().hasExtra("type")) {
            type = getIntent().getStringExtra("type");
            if (type.equals("total_aplay")) {
                order_ids = getIntent().getStringExtra("order_ids");
                money = getIntent().getStringExtra("money");
                score_total = getIntent().getStringExtra("score_total");
                award_total = getIntent().getStringExtra("award_total");
                money = getIntent().getStringExtra("money");
            } else if (type.equals("applay") || type.equals("applay_jin")) {
                order_ids = getIntent().getStringExtra("order_ids");
                money = getIntent().getStringExtra("money");
                pay_fee_b = getIntent().getStringExtra("pay_fee_b");
                pay_fee_d = getIntent().getStringExtra("pay_fee_d");
                award_total = getIntent().getStringExtra("award_total");
                score_total = getIntent().getStringExtra("score_total");
                LogUtil.e("order_ids" + order_ids + "money" + money + "pay_fee_b" + pay_fee_b + "pay_fee_d" + pay_fee_d);
            }
        }
        pay = new Pay2();

    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressContent();
        site.bank(application.getUserInfo().get("site_id"), this);
        if (!TextUtils.isEmpty(application.getUserInfo().get("pay_password"))) {
            tv_hui.setVisibility(View.GONE);
            tv_state.setVisibility(View.VISIBLE);
            card_tv_01.setText("修改支付密码");
        } else {
            tv_hui.setVisibility(View.VISIBLE);
            tv_state.setVisibility(View.GONE);
            card_tv_01.setText("添加支付密码");
        }
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("bank")) {
            maps = JSONUtils.parseDataToMapList(result);
            for (int i = 0; i < maps.size(); i++) {
                list.add(0);
            }
            myadapter.notifyDataSetChanged();
        }
        if (params.getUri().contains("delBank")) {
            showProgressContent();
            site.bank(application.getUserInfo().get("site_id"), this);
        }

        if (params.getUri().contains("withdraw")) {
//            showToast(JSONUtils.parseKeyAndValueToMap(result).get("message"));
            showToast("提现成功，审核中");
            finish();
            AppManager.getInstance().killActivity(MonqinAty.class);
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressDialog();
        removeProgressContent();
        if (!error.get("message").contains("支付密码错误")) {
            maps = new ArrayList<>();
            myadapter.notifyDataSetChanged();
        } else {
            showToast("支付密码错误！");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        if (type.equals("applay") || type.equals("total_aplay") || type.equals("applay_jin")) {
            mActionBar.setTitle("提现");
        } else {
            mActionBar.setTitle("我的银行卡");
        }
        card_lv.setAdapter(myadapter);
        cbox_01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (list != null) {
                        if (list.size() > 0) {
                            list.set(index, 0);
                        }
                    }
                    myadapter.notifyDataSetChanged();
                    cbox_02.setChecked(false);
                    pay_type = "微信";
                }
            }
        });
        cbox_02.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (list != null) {
                        if (list.size() > 0) {
                            list.set(index, 0);
                        }
                    }
                    myadapter.notifyDataSetChanged();
                    cbox_01.setChecked(false);
                    pay_type = "支付宝";
                }
            }
        });
        if (type.equals("applay") || type.equals("total_aplay") || type.equals("applay_jin")) {
            relay_00.setVisibility(View.GONE);
            btn_ok.setVisibility(View.VISIBLE);
        } else {
            cbox_02.setVisibility(View.GONE);
            cbox_01.setVisibility(View.GONE);
            btn_ok.setVisibility(View.GONE);
        }


    }


    @Event(value = R.id.card_lv, type = AdapterView.OnItemClickListener.class)
    private void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        bank_id = maps.get(position).get("bank_id");
        if (type.equals("applay") || type.equals("total_aplay") || type.equals("applay_jin")) {
            cbox_01.setChecked(false);
            cbox_02.setChecked(false);
            list.set(index, 0);
            list.set(position, 1);
            index = position;
            myadapter.notifyDataSetChanged();
            pay_type = "";
        } else {
            relay_cancle.setVisibility(View.VISIBLE);
        }
    }

    @Event(value = {R.id.card_tv_cancel, R.id.card_tv_jiechu, R.id.card_relay_cancel, R.id.card_tv_add, R.id.card_relay_setpass,
            R.id.fb_card_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.card_tv_cancel:
                relay_cancle.setVisibility(View.GONE);
                break;
            case R.id.card_tv_jiechu:
                relay_cancle.setVisibility(View.GONE);
                showBuilderok();
                break;
            case R.id.card_relay_cancel:
                relay_cancle.setVisibility(View.GONE);
                break;
            case R.id.card_tv_add:
                startActivity(AddcardAty.class, null);
                break;
            case R.id.card_relay_setpass:
                startActivity(SetpassAty.class, null);
                break;
            case R.id.fb_card_ok:
                if (TextUtils.isEmpty(application.getUserInfo().get("pay_password"))) {
                    showToast("请先设置支付密码");
                    return;
                }
                if (type.equals("total_aplay")) {
                    if (pay_type.equals("")) {
                        showBuilderok2("");
                    } else {
                        showBuilderok3();
                    }
                } else if (type.equals("applay") || type.equals("applay_jin")) {
                    if (pay_type.equals("")) {
                        showBuilderok2("");
                    } else {
                        showBuilderok3();
                    }
                }
                break;
        }
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
                convertView = LayoutInflater.from(CardAty.this).inflate(R.layout.item_card_lv, parent, false);
                x.view().inject(viewHolder, convertView);
                convertView.setTag(viewHolder);
                AutoUtils.autoSize(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            imageLoader.disPlay(viewHolder.imgv_icon, maps.get(position).get("icon"));
            viewHolder.tv_name.setText(maps.get(position).get("card_name"));
            String card_no = maps.get(position).get("card_no");
//            String strsub = card_no.substring(card_no.length() - 4);
//            viewHolder.tv_number.setText("*" + strsub + "储蓄卡");
            if (list.get(position) == 1) {
                viewHolder.imgv_c.setImageResource(R.drawable.ic_pay2_checked);
            } else {
                viewHolder.imgv_c.setImageResource(R.drawable.ic_pay2_ncheck);
            }
            return convertView;
        }

        private class ViewHolder {
            @ViewInject(R.id.item_cardlv)
            public ImageView imgv_icon;
            @ViewInject(R.id.item_cardlv_imgv_c)
            public ImageView imgv_c;
            @ViewInject(R.id.item_cardlv_name)
            public TextView tv_name;
            @ViewInject(R.id.item_cardlv_number)
            public TextView tv_number;
        }

    }

    public void showBuilderok() {
        View view = View.inflate(CardAty.this, R.layout.dlg_jie, null);
        final Dialog dialog = new Dialog(CardAty.this, R.style.dialog);
        TextView tv_no = (TextView) view.findViewById(R.id.builderpay_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderpay_tv_ok);
        final EditText et_pass = (EditText) view.findViewById(R.id.modify_pwd_pwdview);
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
                showProgressDialog();
                site.delBank(application.getUserInfo().get("site_id"), bank_id, CardAty.this);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void showBuilderok3() {
        View view = View.inflate(CardAty.this, R.layout.dlg_applay, null);
        final Dialog dialog = new Dialog(CardAty.this, R.style.dialog);
        TextView tv_no = (TextView) view.findViewById(R.id.dlg_applay_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.dlg_applay_tv_ok);
        final EditText et_account = (EditText) view.findViewById(R.id.dlg_applay_tv_content);
        final EditText et_account2 = (EditText) view.findViewById(R.id.dlg_applay_tv_content2);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) et_account.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(et_account, 0);
                           }
                       },
                998);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_account.getText().toString())) {
                    Toast toast = Toast.makeText(CardAty.this, "账户不能为空",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                if (!et_account.getText().toString().equals(et_account2.getText().toString())) {
                    Toast toast = Toast.makeText(CardAty.this, "两次输入账号不一致",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                dialog.cancel();
                showBuilderok2(et_account.getText().toString());
            }
        });
        dialog.setContentView(view);
        dialog.show();

    }

    public void showBuilderok2(final String account) {
        View view = View.inflate(CardAty.this, R.layout.dlg_pay, null);
        final Dialog dialog = new Dialog(CardAty.this, R.style.dialog);
        TextView tv_no = (TextView) view.findViewById(R.id.builderpay_tv_no);
        TextView tv_ok = (TextView) view.findViewById(R.id.builderpay_tv_ok);
        final EditText et_pass = (EditText) view.findViewById(R.id.modify_pwd_pwdview);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) et_pass.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(et_pass, 0);
                           }
                       },
                998);
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Commonly.getViewText(et_pass))) {
                    showToast("支付密码不能为空");
                    return;
                }
                if (et_pass.getText().length() != 6) {
                    showToast("请输入6位支付密码");
                    return;
                }
                dialog.cancel();
                showProgressDialog();
                if (TextUtils.isEmpty(account)) {
                    if (type.equals("total_aplay")) {
                        pay.withdraw2(application.getUserInfo().get("site_id"), money, money, award_total, score_total, et_pass.getText().toString(),
                                maps.get(index).get("bank_id"), CardAty.this);
                        LogUtil.e("order_ids" + order_ids + "money" + money);
                    } else if (type.equals("applay")) {
                        pay.withdraw(application.getUserInfo().get("site_id"),
                                order_ids, pay_fee_b, pay_fee_d, money, et_pass.getText().toString(), maps.get(index).get("bank_id"),
                                "", "", award_total, score_total, CardAty.this);
                        LogUtil.e("order_ids" + order_ids + "money" + money);
                    }
                } else {
                    if (type.equals("total_aplay")) {
                        pay.withdraw22(application.getUserInfo().get("site_id"), money, money, award_total, score_total, et_pass.getText().toString(),
                                account, pay_type, CardAty.this);
                        LogUtil.e("order_ids" + order_ids + "money" + money);
                    } else if (type.equals("applay")) {
                        pay.withdraw(application.getUserInfo().get("site_id"),
                                order_ids, pay_fee_b, pay_fee_d, money, et_pass.getText().toString(), "", account, pay_type, award_total,
                                score_total, CardAty.this);
                        LogUtil.e("order_ids" + order_ids + "money" + money);
                    }
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }
}
