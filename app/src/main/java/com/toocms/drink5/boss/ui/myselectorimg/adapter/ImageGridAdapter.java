package com.toocms.drink5.boss.ui.myselectorimg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.toocms.drink5.boss.R;
import com.toocms.drink5.boss.ui.myselectorimg.bean.Image;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zero
 * @date 2016/4/24 8:48
 */
public class ImageGridAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private boolean showCamera = true;
//    final int itemWidth;
    private boolean showSelectIndicator = true;
    private List<Image> selectedImages = new ArrayList();
    private List<Image> images = new ArrayList();
    private Context context;

    public ImageGridAdapter(Context context, boolean showCamera) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        boolean width = false;
//        int width1;
//        if (Build.VERSION.SDK_INT >= 13) {
//            Point options = new Point();
//            wm.getDefaultDisplay().getSize(options);
//            width1 = options.x;
//        } else {
//            width1 = wm.getDefaultDisplay().getWidth();
//        }
//        this.itemWidth = width1 / column;

    }

    public void showSelectIndicator(boolean b) {
        this.showSelectIndicator = b;
    }

    public void setShowCamera(boolean b) {
        if (this.showCamera != b) {
            this.showCamera = b;
            this.notifyDataSetChanged();
        }
    }

    public boolean isShowCamera() {
        return this.showCamera;
    }

    public void select(Image image) {
        if (this.selectedImages.contains(image)) {
            this.selectedImages.remove(image);
        } else {
            this.selectedImages.add(image);
        }

        this.notifyDataSetChanged();
    }

    public void setDefaultSelected(ArrayList<String> resultList) {
        Iterator var2 = resultList.iterator();
        while (var2.hasNext()) {
            String path = (String) var2.next();
            Image image = this.getImageByPath(path);
            if (image != null) {
                this.selectedImages.add(image);
            }
        }
        if (this.selectedImages.size() > 0) {
            this.notifyDataSetChanged();
        }
    }

    private Image getImageByPath(String path) {
        if (this.images != null && this.images.size() > 0) {
            Iterator var2 = this.images.iterator();
            while (var2.hasNext()) {
                Image image = (Image) var2.next();
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    public void setData(List<Image> images) {
        this.selectedImages.clear();
        if (images != null && images.size() > 0) {
            this.images = images;
        } else {
            this.images.clear();
        }

        this.notifyDataSetChanged();
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        return this.showCamera ? (position == 0 ? 0 : 1) : 1;
    }

    public int getCount() {
        return this.showCamera ? this.images.size() + 1 : this.images.size();
    }

    public Image getItem(int i) {
        return this.showCamera ? (i == 0 ? null : (Image) this.images.get(i - 1)) : (Image) this.images.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.isShowCamera() && i == 0) {
            view = this.inflater.inflate(R.layout.item_imgadapter2, viewGroup, false);
            return view;
        } else {
            ImageGridAdapter.ViewHolder holder;
            if (view == null) {
                view = this.inflater.inflate(R.layout.item_imgadapter, viewGroup, false);
                holder = new ImageGridAdapter.ViewHolder(view);
            } else {
                holder = (ImageGridAdapter.ViewHolder) view.getTag();
            }
            if (holder != null) {
                holder.bindData(this.getItem(i));
            }
            return view;
        }
    }

    class ViewHolder {
        ImageView image;
        CheckBox indicator;
//        View mask;

        ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.item_imgvada_imgv);
            this.indicator = (CheckBox) view.findViewById(R.id.item_imgvada_cbox);
            view.setTag(this);
        }

        void bindData(Image data) {
            if (data != null) {
                if (ImageGridAdapter.this.showSelectIndicator) {
                    this.indicator.setVisibility(View.VISIBLE);
                    if (ImageGridAdapter.this.selectedImages.contains(data)) {
                        this.indicator.setChecked(true);
//                        this.mask.setVisibility(View.VISIBLE);
                    } else {
                        this.indicator.setChecked(false);
//                        this.mask.setVisibility(View.GONE);
                    }
                } else {
                    this.indicator.setVisibility(View.GONE);
                }
                File imageFile = new File(data.path);
                if (imageFile.exists()) {
                    x.image().bind(this.image, imageFile.getAbsolutePath());
                } else {
                    this.image.setImageResource(R.drawable.default_error);
                }

            }
        }
    }
}
