package com.toocms.drink5.boss.interfaces;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

/**
 * @author Zero
 * @date 2016/6/29 13:47
 */
public class Feedback {

    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 意见反馈
     *
     * @param apiListener
     */
    public void send(String content, String mobile, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/send");
        params.addBodyParameter("content", content);
        params.addBodyParameter("mobile", mobile);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }
}
