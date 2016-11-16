package com.toocms.drink5.boss.interfaces;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

/**
 * @author Zero
 * @date 2016/6/18 9:34
 */
public class Courier {

    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 水工列表
     *
     * @param apiListener
     */
    public void index(String site_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/index");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 获取商品分类
     *
     * @param apiListener
     */
    public void find(String c_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/find");
        params.addQueryStringParameter("c_id", c_id);
        ApiTool apiTool = new ApiTool();

        apiTool.getApi(params, apiListener);
    }

    /**
     * 修改昵称
     *
     * @param apiListener
     */
    public void onNickname(String c_id, String nickname, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onNickname");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("nickname", nickname);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 解除绑定
     *
     * @param apiListener
     */
    public void unBind(String m_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/unBind");
        params.addBodyParameter("m_id", m_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

}
