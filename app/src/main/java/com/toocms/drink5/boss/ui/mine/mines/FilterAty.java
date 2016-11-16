package com.toocms.drink5.boss.ui.mine.mines;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.drink5.boss.ui.mine.mines.date.DatePicker;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Zero
 * @date 2016/5/19 8:50
 */
public class FilterAty extends BaseAty {

    @ViewInject(R.id.filter_tv_end)
    private TextView tv_end;
    @ViewInject(R.id.filter_tv_start)
    private TextView tv_start;

    @ViewInject(R.id.filter_cbox_01)
    private CheckBox cbox_01;
    @ViewInject(R.id.filter_cbox_02)
    private CheckBox cbox_02;
    @ViewInject(R.id.filter_cbox_03)
    private CheckBox cbox_03;
    @ViewInject(R.id.filter_cbox_04)
    private CheckBox cbox_04;
    @ViewInject(R.id.filter_cbox_05)
    private CheckBox cbox_05;
    @ViewInject(R.id.filter_cbox_06)
    private CheckBox cbox_06;
    @ViewInject(R.id.filter_cbox_07)
    private CheckBox cbox_07;

    private DatePicker picker;
    private CheckBox checkBox[];
    private String type = "";
    private String startTime = "";
    private String endTime = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.aty_filter;
    }

    @Override
    protected void initialized() {
        picker = new DatePicker(this);
        picker.setRange(2000, 2016);
        picker.setSelectedItem(2016, 7, 1);
    }

    @Override
    protected void requestData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.setTitle("筛选");
        checkBox = new CheckBox[]{cbox_01, cbox_02, cbox_03, cbox_04, cbox_05, cbox_06, cbox_07};

    }

    public Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @Event(value = {R.id.filter_relay_end, R.id.filter_relay_start, R.id.filter_cbox_01, R.id.filter_cbox_02, R.id.filter_cbox_03
            , R.id.filter_cbox_04, R.id.filter_cbox_05, R.id.filter_cbox_06,R.id.filter_cbox_07, R.id.fb_filter_ok})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.filter_relay_end:
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        tv_end.setText(year + "-" + month + "-" + day);
                        endTime = year + "-" + month + "-" + day;
                    }
                });
                picker.show();
                break;
            case R.id.filter_relay_start:
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        tv_start.setText(year + "-" + month + "-" + day);
                        startTime = year + "-" + month + "-" + day;
                    }
                });
                picker.show();
                break;
            case R.id.filter_cbox_01:
                setCbox1(0);
                type = "0";
                break;
            case R.id.filter_cbox_02:
                setCbox1(1);
                type = "5";
                break;
            case R.id.filter_cbox_03:
                setCbox1(2);
                type = "4";
                break;
            case R.id.filter_cbox_04:
                setCbox1(3);
                type = "1";
                break;
            case R.id.filter_cbox_05:
                setCbox1(4);
                type = "2";
                break;
            case R.id.filter_cbox_06:
                setCbox1(5);
                type = "6";
                break;
            case R.id.filter_cbox_07:
                setCbox1(6);
                type = "3";
                break;
            case R.id.fb_filter_ok:
                if (!TextUtils.isEmpty(endTime) && !TextUtils.isEmpty(startTime)) {
                    Date date_endTime = StrToDate(endTime);
                    Date date_startTime = StrToDate(startTime);
                    if (date_startTime.getTime() > date_endTime.getTime()) {
                        showToast("开始日期不能大于结束日期");
                        return;
                    }
                }
                Intent intent = getIntent();
                intent.putExtra("start_time", startTime);
                intent.putExtra("end_time", endTime);
                intent.putExtra("type", type);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void setCbox1(int index) {
        for (int i = 0; i < checkBox.length; i++) {
            if (i == index) {
                checkBox[i].setChecked(true);
            } else {
                checkBox[i].setChecked(false);
            }
        }
    }
}
