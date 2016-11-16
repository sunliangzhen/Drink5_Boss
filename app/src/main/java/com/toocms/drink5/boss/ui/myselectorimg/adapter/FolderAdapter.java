package com.toocms.drink5.boss.ui.myselectorimg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.myselectorimg.bean.Folder;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zero
 * @date 2016/4/24 14:08
 */
public class FolderAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Folder> mFolders = new ArrayList();
    int mImageSize;
    int lastSelected = 0;

    public FolderAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mImageSize = this.mContext.getResources().getDimensionPixelOffset(R.dimen.activity_horizontal_margin);
//        this.imageLoader = new ImageLoader();
//        ImageOptions options = (new ImageOptions.Builder()).setSize(this.mImageSize, this.mImageSize).setLoadingDrawableId(drawable.default_error).setImageScaleType(ImageView.ScaleType.CENTER_CROP).setFadeIn(true).setUseMemCache(true).build();
//        this.imageLoader.setImageOptions(options);
    }

    public void setData(List<Folder> folders) {
        if (folders != null && folders.size() > 0) {
            this.mFolders = folders;
        } else {
            this.mFolders.clear();
        }
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return this.mFolders.size() + 1;
    }

    public Folder getItem(int i) {
        return i == 0 ? null : (Folder) this.mFolders.get(i - 1);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        FolderAdapter.ViewHolder holder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.listitem_folder, viewGroup, false);
            holder = new FolderAdapter.ViewHolder(view);
        } else {
            holder = (FolderAdapter.ViewHolder) view.getTag();
        }
        if(holder != null) {
            if(i == 0) {
                holder.name.setText("所有图片");
                holder.path.setText("/sdcard");
                holder.size.setText(String.format("%d%s", new Object[]{Integer.valueOf(this.getTotalImageSize()),"张"}));
                if(this.mFolders.size() > 0) {
                    Folder f = (Folder)this.mFolders.get(0);
                    x.image().bind(holder.cover, f.cover.path);
                }
            } else {
                holder.bindData(this.getItem(i));
            }

            if(this.lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }

        return view;
    }
    private int getTotalImageSize() {
        int result = 0;
        Folder f;
        if(this.mFolders != null && this.mFolders.size() > 0) {
            for(Iterator var2 = this.mFolders.iterator(); var2.hasNext(); result += f.images.size()) {
                f = (Folder)var2.next();
            }
        }

        return result;
    }
    public void setSelectIndex(int i) {
        if(this.lastSelected != i) {
            this.lastSelected = i;
            this.notifyDataSetChanged();
        }
    }
    public int getSelectIndex() {
        return this.lastSelected;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView path;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            this.cover = (ImageView) view.findViewById(R.id.cover);
            this.name = (TextView) view.findViewById(R.id.name);
            this.path = (TextView) view.findViewById(R.id.path);
            this.size = (TextView) view.findViewById(R.id.size);
            this.indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }
        void bindData(Folder data) {
            if (data != null) {
                this.name.setText(data.name);
                this.path.setText(data.path);
                if (data.images != null) {
                    this.size.setText(String.format("%d%s", new Object[]{Integer.valueOf(data.images.size()),"张"}));
                } else {
                    this.size.setText("*" + "张");
                }
                if (data.cover != null) {
                    x.image().bind(this.cover, data.cover.path);
                } else {
                    this.cover.setImageResource(R.drawable.default_error);
                }

            }
        }
    }
}
