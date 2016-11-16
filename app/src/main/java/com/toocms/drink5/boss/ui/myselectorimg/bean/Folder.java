package com.toocms.drink5.boss.ui.myselectorimg.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * @author Zero
 * @date 2016/4/24 8:10
 */

public class Folder {
    public String name;
    public String path;
    public Image cover;
    public List<Image> images;

    public Folder() {
    }

    public boolean equals(Object o) {
        try {
            Folder e = (Folder)o;
            return TextUtils.equals(e.path, this.path);
        } catch (ClassCastException var3) {
            var3.printStackTrace();
            return super.equals(o);
        }
    }
}
