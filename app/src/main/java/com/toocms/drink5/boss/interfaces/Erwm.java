package com.toocms.drink5.boss.interfaces;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

/**
 * @author Zero
 * @date 2016/6/27 16:36
 */
public class Erwm {
    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 二维码
     *
     * @param apiListener
     */
    public void index(ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/index");
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }
}
