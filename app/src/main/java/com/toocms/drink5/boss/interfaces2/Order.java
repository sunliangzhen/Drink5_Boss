package com.toocms.drink5.boss.interfaces2;

import android.content.Context;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/6/23 20:12
 */
public class Order {

    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 我的订单
     *
     * @param apiListener
     */
    public void courierGetOrder(String c_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/courierGetOrder");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 任务大厅
     *
     * @param apiListener
     */
    public void orderFocus(String c_id, String type, String sort, String geared, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/orderFocus");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("type", type);
        params.addBodyParameter("sort", sort);
        params.addBodyParameter("geared", geared);
        params.addBodyParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 抢单
     *
     * @param apiListener
     */
    public void toOrder(Context context, String c_id, String order_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/toOrder");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("order_id", order_id);
        params.addQueryStringParameter("lat", PreferencesUtils.getString(context, "latitude"));
        params.addQueryStringParameter("lon", PreferencesUtils.getString(context, "longitude"));
        params.addQueryStringParameter("order_id", order_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 分单配送
     *
     * @param apiListener
     */
    public void toDistribute(String c_id, String order_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/toDistribute");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("order_id", order_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 退回统一配送
     *
     * @param apiListener
     */
    public void reOrder(String c_id, String order_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/reOrder");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("order_id", order_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 分配任务
     *
     * @param apiListener
     */
    public void asCourier(String c_id, String order_id, String to_c_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/asCourier");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("order_id", order_id);
        params.addQueryStringParameter("to_c_id", to_c_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 退单回厅
     *
     * @param apiListener
     */
    public void backOrder(String c_id, String order_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/backOrder");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("order_id", order_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 拒绝订单
     *
     * @param apiListener
     */
    public void onRefuse(String c_id, String order_id, String refuse, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onRefuse");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("refuse", refuse);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 关闭订单
     *
     * @param apiListener
     */
    public void onClose(String c_id, String order_id, String refuse, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onClose");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("refuse", refuse);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 确认订单
     *
     * @param apiListener
     */
    public void onCompleted(String c_id, String order_id, String barrel, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onCompleted");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("order_id", order_id);
        params.addQueryStringParameter("barrel", barrel);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 订单详情
     *
     * @param apiListener
     */
    public void orderDetails(String c_id, String order_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/orderDetails");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("order_id", order_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 物流列表
     *
     * @param apiListener
     */
    public void logistical(String order_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL4 + module + "/logistical");
        params.addBodyParameter("order_id", order_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 物流信息
     *
     * @param apiListener
     */
    public void getCompany(ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/getCompany");
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 设置物流
     *
     * @param apiListener
     */
    public void setLogistical(String c_id, String order_id, String company, String sn, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/setLogistical");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("order_id", order_id);
        params.addBodyParameter("company", company);
        params.addBodyParameter("sn", sn);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

}
