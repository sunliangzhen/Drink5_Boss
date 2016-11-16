package com.toocms.drink5.boss.interfaces;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.io.File;

/**
 * @author Zero
 * @date 2016/6/12 13:53
 */
public class RegisterLog {

    private String module = this.getClass().getSimpleName(); // 模块名


    /**
     * 注册发送验证码
     *
     * @param apiListener
     */
    public void sendVerify(String type, String mobile, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/sendVerify");
        params.addBodyParameter("type", type);
        params.addBodyParameter("mobile", mobile);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 登录
     *
     * @param apiListener
     */
    public void login(String mobile, String password, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/login");
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("password", password);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 水站注册第一步
     *
     * @param apiListener
     */
    public void register(String verify, String mobile, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/register");
        params.addQueryStringParameter("verify", verify);
        params.addQueryStringParameter("mobile", mobile);
        params.addQueryStringParameter("type", 1 + "");
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 水站注册第2步
     *
     * @param apiListener
     */
    public void register(String verify, String mobile, String password, String code, String site_name, String address,
                         String lon, String lat, String tel, String brand, String intro, String water, String cer_yyzz,
                         String cer_wszm, String province, String city, String area, String address_code
            , ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/register");
        params.addBodyParameter("type", 2 + "");
        params.addBodyParameter("verify", verify);
        params.addBodyParameter("password", password);
        params.addBodyParameter("re_password", password);
        params.addBodyParameter("mobile", mobile);
        if (!code.equals("0")) {
            params.addBodyParameter("code", code);
        } else {
        }
        params.addBodyParameter("site_name", site_name);
        params.addBodyParameter("address", address);
        params.addBodyParameter("lon", lon);
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("tel", tel);
        params.addBodyParameter("brand", brand);
        params.addBodyParameter("intro", intro);
        params.addBodyParameter("water", water);
        params.addBodyParameter("img_1", new File(cer_yyzz));
        params.addBodyParameter("img_2", new File(cer_wszm));
        city = city.replace("市", "");
        province = province.replace("市", "");
        city = city.replace("省", "");
        province = province.replace("省", "");
        params.addBodyParameter("province", province);
        params.addBodyParameter("city", city);
        params.addBodyParameter("area", area);
        params.addBodyParameter("address_code", address_code);
        LogUtil.e(lon + "lon");
        LogUtil.e(lat + "lat");
        LogUtil.e(province);
        LogUtil.e(city);
        LogUtil.e(area);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 找回密码第一步
     *
     * @param apiListener
     */
    public void retrieve(String verify, String mobile, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/retrieve");
        params.addBodyParameter("type", "1");
        params.addBodyParameter("verify", verify);
        params.addBodyParameter("mobile", mobile);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 找回密码第二步
     *
     * @param apiListener
     */
    public void retrieve2(String mobile, String password, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/retrieve");
        params.addBodyParameter("type", "2");
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("password", password);
        params.addBodyParameter("re_password", password);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 设置支付密码第一步
     *
     * @param apiListener
     */
    public void setPayPassword(String type, String verify, String mobile, String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setPayPassword");
        params.addBodyParameter("verify", verify);
        params.addBodyParameter("type", type);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 设置支付密码第2步
     *
     * @param apiListener
     */
    public void setPayPassword2(String type, String pay_password, String mobile, String site_id, ApiListener apiListener) {
        LogUtil.e(type + "," + pay_password + "," + mobile + "," + site_id);
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setPayPassword");
        params.addBodyParameter("pay_password", pay_password);
        params.addBodyParameter("re_pay_password", pay_password);
        params.addBodyParameter("type", type);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 添加账号
     *
     * @param apiListener
     */
    public void addAccount(String site_id, String mobile, String password, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/addAccount");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("password", password);
        params.addBodyParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }
}
