package com.toocms.drink5.boss.config;

import android.content.Context;

public class DensityUtils {

    /**
     * dp转px
     * <p>
     * nox_adb.exe connect 127.0.0.1:62001
     * cd\Users\Administrator\AppData\Roaming\Nox\bin
     */
    public static int dp2px(Context ctx, float dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);// 4.9->5 4.4->4

        return px;
    }

    public static float px2dp(Context ctx, int px) {
        float density = ctx.getResources().getDisplayMetrics().density;
        float dp = px / density;
        return dp;
    }
}
