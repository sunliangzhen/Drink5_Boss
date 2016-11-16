package com.toocms.drink5.boss.interfaces2;

import android.text.TextUtils;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

/**
 * @author Zero
 * @date 2016/6/13 16:32
 */
public class Contact {

    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 水站列表
     *
     * @param apiListener
     */
    public void siteList(String c_id, String lon, String lat, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/siteList");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 我的同事
     *
     * @param apiListener
     */
    public void isColleague(String c_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/isColleague");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }


    /**
     * 我的客户
     *
     * @param apiListener
     */
    public void isClient(String c_id, String sort, String serach, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/isClient");
        params.addBodyParameter("c_id", c_id);
        if (TextUtils.isEmpty(serach)) {
            if (!TextUtils.isEmpty(sort)) {
                params.addBodyParameter("sort", sort);
            }
        } else {
            params.addBodyParameter("serach", serach);
        }
        params.addBodyParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 水工发展客户
     *
     * @param apiListener
     */
    public void offerRank(String c_id, String type, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/offerRank");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 我的发展客户
     *
     * @param apiListener
     */
    public void isCustomer(String c_id, String type, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/isCustomer");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 修改客户的备注
     *
     * @param apiListener
     */
    public void onRemarks(String c_id, String m_id, String remarks, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onRemarks");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("m_id", m_id);
        params.addBodyParameter("remarks", remarks);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

}
