package com.toocms.drink5.boss.ui.myselectorimg.bean;

/**
 * @author Zero
 * @date 2016/4/24 8:10
 */
public class Image {
    public String path;
    public String name;
    public long time;

    public Image(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    public boolean equals(Object o) {
        try {
            Image e = (Image)o;
            return this.path.equalsIgnoreCase(e.path);
        } catch (ClassCastException var3) {
            var3.printStackTrace();
            return super.equals(o);
        }
    }
}
