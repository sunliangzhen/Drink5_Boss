package com.toocms.drink5.boss.interfaces2;

import android.text.TextUtils;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.http.RequestParams;

import java.io.File;

/**
 * @author Zero
 * @date 2016/6/20 15:26
 */
public class Courier2 {

    private String module = "Courier"; // 模块名

    /**
     * 水工信息
     *
     * @param apiListener
     */
    public void isInfo(String c_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/isInfo");
        params.addQueryStringParameter("c_id", c_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 水工优惠码
     *
     * @param apiListener
     */
    public void isCode(String c_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/isCode");
        params.addQueryStringParameter("c_id", c_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 上传经纬度
     *
     * @param apiListener
     */
    public void changePos(String c_id, String lon, String lat, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/changePos");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 修改头像
     *
     * @param apiListener
     */
    public void onHead(String c_id, String head, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onHead");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("head", new File(head));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
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
     * 健康证明
     *
     * @param apiListener
     */
    public void onHealthy(String c_id, String healthy, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onHealthy");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("healthy", new File(healthy));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 我的评价
     *
     * @param apiListener
     */
    public void isEvaluate(String c_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/isEvaluate");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }
    /**
     * 我的统计
     *
     * @param apiListener
     */
    public void recode(String c_id,String geared, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/recode");
        params.addBodyParameter("c_id", c_id);
        if(TextUtils.isEmpty(geared)){
            params.addBodyParameter("geared", "3");
        }else{
            params.addBodyParameter("geared", geared);
        }
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 修改水工的地址
     *
     * @param apiListener
     */
    public void onRess(String c_id, String province, String city, String area, String ress, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/onRess");
        province = province.replace("市", "");
        province = province.replace("省", "");
        city = city.replace("市", "");
        city = city.replace("省", "");
        params.addQueryStringParameter("c_id", c_id);
        params.addQueryStringParameter("province", province);
        params.addQueryStringParameter("city", city);
        params.addQueryStringParameter("area", area);
        params.addQueryStringParameter("ress", ress);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 配送记录
     *
     * @param apiListener
     */
    public void orderLog(String c_id, String start_time, String end_time, String type, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL2 + module + "/orderLog");
        params.addQueryStringParameter("c_id", c_id);
        if (!TextUtils.isEmpty(start_time)) {
            params.addQueryStringParameter("start_time", start_time);
        }
        if(!TextUtils.isEmpty(end_time)){
            params.addQueryStringParameter("end_time", end_time);
        }
        if (!TextUtils.isEmpty(type)) {
            params.addQueryStringParameter("type", type);
        }
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }
}
