package com.toocms.drink5.boss.interfaces;

import android.text.TextUtils;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

/**
 * @author Zero
 * @date 2016/6/29 9:21
 */
public class Pay2 {
    private String module = "Pay"; // 模块名

    /**
     * 京币未结算
     *
     * @param apiListener
     */
    public void index(String site_id, String lon, String lat, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/index");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 京币已结算
     *
     * @param apiListener
     */
    public void indexS(String site_id, String lon, String lat, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/indexS");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 余额未结算
     *
     * @param apiListener
     */
    public void withdrawIndex(String site_id, String lon, String lat, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/withdrawIndex");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 余额已结算
     *
     * @param apiListener
     */
    public void withdrawB(String site_id, String lon, String lat, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/withdrawB");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 余额提现
     *
     * @param apiListener
     */
    public void withdraw(String site_id, String order_ids, String pay_fee_b, String pay_fee_d,
                         String pay_fee, String pay_password, String bank_id, String account, String account_type,
                         String award_total, String score, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/withdraw");
        params.addBodyParameter("site_id", site_id);
        if (!TextUtils.isEmpty(order_ids)) {
            params.addBodyParameter("order_ids", order_ids);
        }
        if (!pay_fee_b.equals("0")) {
            params.addBodyParameter("pay_fee_b", pay_fee_b);
        }
        if (!pay_fee_d.equals("0")) {
            params.addBodyParameter("pay_fee_d", pay_fee_d);
        }
        if (!TextUtils.isEmpty(account)) {
            params.addBodyParameter("account", account);
            params.addBodyParameter("type", "2");
        } else {
            params.addBodyParameter("type", "1");
        }
        if (!TextUtils.isEmpty(account_type)) {
            params.addBodyParameter("account_type", account_type);
        }
        params.addBodyParameter("pay_fee", pay_fee);
        params.addBodyParameter("balance_s", award_total);
        params.addBodyParameter("score", score);
        params.addBodyParameter("type", "2");
        params.addBodyParameter("pay_password", pay_password);
        if (!TextUtils.isEmpty(bank_id)) {
            params.addBodyParameter("bank_id", bank_id);
        }
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 余额提现
     *
     * @param apiListener
     */
    public void withdraw2(String site_id, String pay_fee_b, String pay_fee, String balance_s, String score,
                          String pay_password, String bank_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/withdraw");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("pay_fee_b", pay_fee_b);
        params.addBodyParameter("pay_fee_d", "0");
        params.addBodyParameter("pay_fee", pay_fee);
        params.addBodyParameter("balance_s", balance_s);
        params.addBodyParameter("score", score);
        params.addBodyParameter("pay_password", pay_password);
        params.addBodyParameter("bank_id", bank_id);
        params.addBodyParameter("type", "1");
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 余额提现
     *
     * @param apiListener
     */
    public void withdraw22(String site_id, String pay_fee_b, String pay_fee, String balance_s, String score,
                           String pay_password, String account, String account_type, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/withdraw");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("pay_fee_b", pay_fee_b);
        params.addBodyParameter("pay_fee_d", "0");
        params.addBodyParameter("pay_fee", pay_fee);
        params.addBodyParameter("balance_s", balance_s);
        params.addBodyParameter("score", score);
        params.addBodyParameter("pay_password", pay_password);
        params.addBodyParameter("account", account);
        params.addBodyParameter("type", "2");
        params.addBodyParameter("account_type", account_type);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 京币提现
     *
     * @param apiListener
     */
    public void payS(String site_id, String order_ids, String pay_fee_b, String pay_fee_d,
                     String pay_fee, ApiListener apiListener) {
        LogUtil.e("site_id=" + site_id + ",order_ids=" + order_ids + ",pay_fee=" + pay_fee + ",pay_fee_b=" + pay_fee_b + ",pay_fee_d=" + pay_fee_d);
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/payS");
        params.addBodyParameter("site_id", site_id);
        if (!TextUtils.isEmpty(order_ids)) {
            params.addBodyParameter("order_ids", order_ids);
        }
        params.addBodyParameter("pay_fee_b", pay_fee_b);
        params.addBodyParameter("pay_fee_d", pay_fee_d);
        params.addBodyParameter("pay_fee", pay_fee);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    public void ok(String pay_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/ok");
        LogUtil.e("pay_id=" + pay_id);
        params.addQueryStringParameter("pay_id", pay_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 支付记录
     *
     * @param apiListener
     */
    public void pay(String site_id, String pay_fee, String order_ids, String pay_fee_b, String pay_fee_d,
                    ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/pay");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("pay_fee", pay_fee);     //实付
        params.addBodyParameter("order_ids", order_ids);
        params.addBodyParameter("pay_fee_b", pay_fee_b);  //应结
        params.addBodyParameter("pay_fee_d", pay_fee_d);  //未结
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 支付记录2
     *
     * @param apiListener
     */
    public void pay2(String site_id, String pay_fee, String pay_fee_b, String pay_fee_d, String balance_s, String score,
                     ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/pay");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("pay_fee", pay_fee);
        params.addBodyParameter("pay_fee_b", pay_fee_b);//应结
        params.addBodyParameter("pay_fee_d", pay_fee_d);//未结
        params.addBodyParameter("balance_s", balance_s);
        params.addBodyParameter("score", score);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 支付回调
     */
    public void successPayStatus(String order_sn, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/successPayStatus");
        LogUtil.e(order_sn + "ppppppppppppppppp");
        params.addBodyParameter("order_sn", order_sn);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 支付宝参数
     */
    public void alipayParam(String order_sn, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/alipayParam");
        params.addBodyParameter("order_sn", order_sn);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 微信参数
     */
    public void wxpayParam(String order_sn, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/wxpayParam");
        params.addBodyParameter("order_sn", order_sn);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

}
