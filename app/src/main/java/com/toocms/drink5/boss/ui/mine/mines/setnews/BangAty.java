package com.toocms.drink5.boss.ui.mine.mines.setnews;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces2.Contact;
import com.toocms.drink5.boss.ui.BaseAty;
import com.toocms.frame.config.Settings;
import com.toocms.frame.image.ImageLoader;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.util.PreferencesUtils;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;

/**
 * @author Zero
 * @date 2016/5/20 15:16
 */
public class BangAty extends BaseAty {

    @ViewInject(R.id.bang_v_line)
    private View sortFlag; // 排序标识
    @ViewInject(R.id.bang_tv_jin)
    private TextView tv_jin; // 排序标识
    @ViewInject(R.id.bang_tv_ming)
    private TextView tv_ming; // 排序标识
    @ViewInject(R.id.bang_tv_all)
    private TextView tv_all; // 排序标识
    @ViewInject(R.id.bang_lv)
    private SwipeToLoadRecyclerView bang_lv;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition; // 排序标识位置
    private TextView[] ttvv;
    private Contact contact;
    private ArrayList<Map<String, String>> maps;
    private ImageLoader imageLoader;


    @Override
    protected int getLayoutResId() {
        return R.layout.aty_bang;
    }

    @Override
    protected void initialized() {
        sortFlagWidth = AutoUtils.getPercentWidthSize(100);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 2)) / 3);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
        contact = new Contact();
        ImageOptions imageOptions = new ImageOptions.Builder().setImageScaleType(ImageView.ScaleType.FIT_XY).setUseMemCache(true).build();
        imageLoader = new ImageLoader();
        imageLoader.setImageOptions(imageOptions);
    }

    @Override
    protected void requestData() {
        showProgressContent();
        contact.siteList(application.getUserInfo().get("account_id"), PreferencesUtils.getString(BangAty.this, "longitude"), PreferencesUtils.getString(BangAty.this, "latitude"), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isBule = 1;
        super.onCreate(savedInstanceState);
        mActionBar.hide();
        ttvv = new TextView[]{tv_jin, tv_ming, tv_all};
        sortFlag.setBackgroundColor(Color.parseColor("#2c82df"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        setTextviewColor(sortFlagPosition);

//        record_lv.setOnRefreshListener(this);
//        record_lv.setOnLoadMoreListener(this);
        bang_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(this, 1));
    }

    @Event(value = {R.id.bang_tv_jin, R.id.bang_tv_ming, R.id.bang_tv_all, R.id.bang_imgv_back})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.bang_tv_jin:
                sortFlagPosition = 0;
                break;
            case R.id.bang_tv_ming:
                sortFlagPosition = 1;
                break;
            case R.id.bang_tv_all:
                sortFlagPosition = 2;
                break;
            case R.id.bang_imgv_back:
                finish();
                break;
        }
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("siteList")) {
            maps = JSONUtils.parseDataToMapList(result);
            bang_lv.setAdapter(new MyAdapter());
        }
        super.onComplete(params, result);
    }

    private void startTranslate(final View view, float endX) {
        float startx = view.getX();
        ValueAnimator animator = ValueAnimator.ofFloat(startx, endX);
        animator.setTarget(view);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationX((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    private void setTextviewColor(int index) {
        for (int i = 0; i < ttvv.length; i++) {
            if (index == i) {
                ttvv[i].setTextColor(0xff2c82df);
            } else {
                ttvv[i].setTextColor(0xff282828);
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BangAty.this).inflate(R.layout.item_bang_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.cbox_bb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.tv_content.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_content.setVisibility(View.GONE);
                    }
                }
            });
            if (TextUtils.isEmpty((maps.get(position).get("invite_info")))) {
                holder.relay_content.setVisibility(View.GONE);
                holder.tv_content.setVisibility(View.GONE);
            } else {
                holder.tv_content.setText(maps.get(position).get("invite_info"));
            }
            holder.tv_name.setText(maps.get(position).get("site_name"));
            holder.tv_name2.setText(maps.get(position).get("brand"));
            holder.tv_address.setText(maps.get(position).get("address"));
            holder.tv_distance.setText(maps.get(position).get("distance") + "m");
            if (!maps.get(position).get("cover").equals("false")) {
                imageLoader.disPlay(holder.imgv_cover, maps.get(position).get("cover"));
            }
            if (maps.get(position).get("ensure").equals("1")) {
                holder.imgv_bao.setVisibility(View.VISIBLE);
            } else {
                holder.imgv_bao.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_banglv_cbox_bb)
            CheckBox cbox_bb;
            @ViewInject(R.id.item_banglv_tv_content)
            TextView tv_content;
            @ViewInject(R.id.item_banglv_imgv_head)
            ImageView imgv_cover;
            @ViewInject(R.id.item_banglv_imgv_bao)
            ImageView imgv_bao;
            @ViewInject(R.id.item_banglv_imgv_name)
            TextView tv_name;
            @ViewInject(R.id.item_banglv_imgv_name2)
            TextView tv_name2;
            @ViewInject(R.id.item_banglv_tv_address)
            TextView tv_address;
            @ViewInject(R.id.item_banglv_tv_distance)
            TextView tv_distance;
            @ViewInject(R.id.item_banglv_relay_content)
            RelativeLayout relay_content;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
