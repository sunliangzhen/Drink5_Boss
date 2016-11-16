package com.toocms.drink5.boss.ui.news;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.interfaces.Site;
import com.toocms.frame.config.Settings;
import com.toocms.frame.ui.BaseFragment;
import com.zero.autolayout.utils.AutoUtils;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Map;

import cn.zero.android.common.util.JSONUtils;
import cn.zero.android.common.view.swipetoloadlayout.OnLoadMoreListener;
import cn.zero.android.common.view.swipetoloadlayout.OnRefreshListener;
import cn.zero.android.common.view.swipetoloadlayout.view.SwipeToLoadRecyclerView;
import cn.zero.android.common.view.swipetoloadlayout.view.listener.OnItemClickListener;

/**
 * @author Zero
 * @date 2016/5/18 11:00
 */
public class NewsFrg extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @ViewInject(R.id.news_lv)
    private SwipeToLoadRecyclerView news_lv;

    @ViewInject(R.id.news_v_line)
    private View sortFlag; // 排序标识

    @ViewInject(R.id.news_tv_jin)
    private TextView tv_jin;
    @ViewInject(R.id.news_tv_all)
    private TextView tv_all;
    @ViewInject(R.id.imgv_empty)
    private ImageView imgv_empty;
    @ViewInject(R.id.tv_empty)
    private TextView tv_empty;

    private float sortFlagWidth; // 排序标识的长度
    private int sortItemWidth; // 一个排序标签的宽度
    private int sortItemPadding; // 每个item的左右边距
    private int sortFlagPosition = 0; // 排序标识位置
    private TextView[] ttvv;
    private Site site;
    private int p = 1;
    private String type = "1";
    private MyAdapter myAdapter;
    private ArrayList<Map<String, String>> maps;
    private int isRequest = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.frg_news;
    }

    @Override
    protected void initialized() {
        sortFlagWidth = AutoUtils.getPercentWidthSize(100);
        sortItemWidth = (int) ((Settings.displayWidth - (AutoUtils.getPercentWidthSize(1) * 1)) / 2);
        sortItemPadding = (int) ((sortItemWidth - sortFlagWidth) / 2);
        site = new Site();
        maps = new ArrayList<>();
        myAdapter = new MyAdapter();
    }

    @Override
    protected void requestData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressContent();
        site.message(type, application.getUserInfo().get("site_id"), p, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ttvv = new TextView[]{tv_jin, tv_all};
        sortFlag.setBackgroundColor(Color.parseColor("#2c82df"));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) sortFlagWidth, AutoUtils.getPercentHeightSize(2));
        params.gravity = Gravity.BOTTOM;
        sortFlag.setLayoutParams(params);
        sortFlag.setX(sortItemPadding);
        setTextviewColor(sortFlagPosition);
        news_lv.setOnRefreshListener(this);
        news_lv.setOnLoadMoreListener(this);
        news_lv.getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(), 1));
        news_lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Bundle bundle = new Bundle();
                bundle.putString("message_id", maps.get(i).get("message_id"));
                startActivity(NewsdetailsAty.class, bundle);
            }
        });
        news_lv.setAdapter(myAdapter);
    }

    @Override
    public void onComplete(RequestParams params, String result) {
        if (params.getUri().contains("message")) {
            imgv_empty.setVisibility(View.GONE);
            tv_empty.setVisibility(View.GONE);
            news_lv.stopRefreshing();
            news_lv.stopLoadingMore();
            if (p == 1) {
                maps = JSONUtils.parseDataToMapList(result);
            } else {
                maps.addAll(JSONUtils.parseDataToMapList(result));
            }
            myAdapter.notifyDataSetChanged();
            setTextviewColor(sortFlagPosition);
            startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
            isRequest = 0;
        }
        super.onComplete(params, result);
    }

    @Override
    public void onError(Map<String, String> error) {
        removeProgressContent();
        removeProgressDialog();
        news_lv.stopRefreshing();
        news_lv.stopLoadingMore();
        if (p == 1) {
            imgv_empty.setVisibility(View.VISIBLE);
            tv_empty.setVisibility(View.VISIBLE);
            maps = new ArrayList<>();
        }
        myAdapter.notifyDataSetChanged();
        setTextviewColor(sortFlagPosition);
        startTranslate(sortFlag, sortItemPadding + (sortItemWidth * sortFlagPosition));
        isRequest = 0;
    }

    @Override
    public void onRefresh() {
        p = 1;
        site.message(type, application.getUserInfo().get("site_id"), p, this);

    }

    @Override
    public void onLoadMore() {
        p++;
        site.message(type, application.getUserInfo().get("site_id"), p, this);
    }

    @Event(value = {R.id.news_tv_jin, R.id.news_tv_all})
    private void onTestBaidulClick(View view) {
        switch (view.getId()) {
            case R.id.news_tv_jin:
                if (type.equals("2")) {
                    if (isRequest == 0) {
                        type = "1";
                        p = 1;
                        sortFlagPosition = 0;
                        site.message(type, application.getUserInfo().get("site_id"), p, this);
                        isRequest = 1;
                    }
                }
                break;
            case R.id.news_tv_all:
                if (type.equals("1")) {
                    if (isRequest == 0) {
                        type = "2";
                        p = 1;
                        sortFlagPosition = 1;
                        site.message(type, application.getUserInfo().get("site_id"), p, this);
                        isRequest = 1;
                    }
                }
                break;
        }
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_lv, parent, false);
            return new MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_time.setText(maps.get(position).get("create_time"));
            holder.tv_content.setText(maps.get(position).get("content"));
            holder.tv_title.setText("[" + maps.get(position).get("title") + "]");
        }

        @Override
        public int getItemCount() {
            return maps.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.item_newslv_content)
            TextView tv_content;
            @ViewInject(R.id.item_newslv_time)
            TextView tv_time;
            @ViewInject(R.id.item_news_tv_01)
            TextView tv_title;

            public MyViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
                AutoUtils.autoSize(itemView);
            }
        }
    }
}
