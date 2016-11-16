package com.toocms.drink5.boss.interfaces2;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

/**
 * @author Zero
 * @date 2016/6/29 13:47
 */
public class Rules {

    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 意见反馈
     *
     * @param apiListener
     */
    public void codeRules(ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL4 + module + "/codeRules");
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }
}
