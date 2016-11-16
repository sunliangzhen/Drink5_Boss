package com.toocms.drink5.boss.interfaces;

import android.content.Context;
import android.text.TextUtils;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.io.File;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * @author Zero
 * @date 2016/6/17 17:48
 */
public class Site {

    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 设置营业时间
     *
     * @param apiListener
     */
    public void setBusinessTime(String site_id, String business_time_a, String business_time_b, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setBusinessTime");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("business_time_a", business_time_a);
        params.addBodyParameter("business_time_b", business_time_b);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 设置顺带时间
     *
     * @param apiListener
     */
    public void setIncTime(String site_id, String inc_time_a, String inc_time_b, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setIncTime");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("inc_time_a", inc_time_a);
        params.addBodyParameter("inc_time_b", inc_time_b);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 设置水站开关
     *
     * @param apiListener
     */
    public void setSite(String site_id, String data, String field, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setSite");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("data", data);
        params.addBodyParameter("field", field);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 我的消息
     *
     * @param apiListener
     */
    public void message(String type, String site_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/message");
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 消息详情
     *
     * @param apiListener
     */
    public void messageDetail(String message_id, String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/messageDetail");
        params.addQueryStringParameter("message_id", message_id);
        params.addQueryStringParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 绑定水工
     *
     * @param apiListener
     */
    public void bind(String message_id, String c_id, String site_id, String reply, ApiListener apiListener) {
        LogUtil.e(message_id + "," + c_id + "," + site_id + "," + reply);
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/bind");
        params.addBodyParameter("message_id", message_id);
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("reply", reply);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 绑定客户
     *
     * @param apiListener
     */
    public void bindM(Context context, String c_id, String m_id, String message_id, String reply, String site_id, String nickname,
                      ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/bindM");
        if (TextUtils.isEmpty(c_id) || c_id.equals("O")) {
            params.addBodyParameter("c_id", PreferencesUtils.getString(context, "site_id"));
        } else {
            params.addBodyParameter("c_id", c_id);
        }
        params.addBodyParameter("m_id", m_id);
        params.addBodyParameter("message_id", message_id);
        params.addBodyParameter("reply", reply);
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("nickname", nickname);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 冻结水工
     *
     * @param apiListener
     */
    public void freeze(String c_id, String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/freeze");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 解除水工
     *
     * @param apiListener
     */
    public void cancelBind(String c_id, String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/cancelBind");
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 群发消息
     *
     * @param apiListener
     */
    public void sendMessage(String site_id, String content, String type, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/sendMessage");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("content", content);
        params.addBodyParameter("type", type);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 我的银行卡
     *
     * @param apiListener
     */
    public void bank(String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/bank");
        params.addQueryStringParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 银行卡列表
     *
     * @param apiListener
     */
    public void bankList(String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/bankList");
        params.addQueryStringParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 解除银行卡
     *
     * @param apiListener
     */
    public void delBank(String site_id, String bank_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/delBank");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("bank_id", bank_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 添加银行卡
     *
     * @param apiListener
     */
    public void bindBank(String site_id, String card_holder, String card_no, String card_mobile, String icon_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/bindBank");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("card_holder", card_holder);
        params.addBodyParameter("card_no", card_no);
        params.addBodyParameter("card_mobile", card_mobile);
        params.addBodyParameter("icon_id", icon_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 提现
     *
     * @param apiListener
     */
    public void withdraw(String site_id, String money, String bank_id, String pay_password, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/withdraw");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("money", money);
        params.addBodyParameter("bank_id", bank_id);
        params.addBodyParameter("pay_password", pay_password);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 水站发展虎
     *
     * @param apiListener
     */
    public void memberA(String site_id, String type, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/memberA");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 水站发展虎排行
     *
     * @param apiListener
     */
    public void rank(Context context, String site_id, String type, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/rank");
        params.addQueryStringParameter("site_id", site_id);
        if (!type.equals("2")) {
            String city = PreferencesUtils.getString(context, "city");
            city = city.replace("市", "");
            params.addQueryStringParameter("city", city);
        }
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 证件列表
     *
     * @param apiListener
     */
    public void cerList(String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/cerList");
        params.addQueryStringParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 水战记录
     *
     * @param apiListener
     */
    public void orderLog(String site_id, String lon, String lat, String where, String start_time,
                         String end_time, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/orderLog");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("lon", lon);
        params.addQueryStringParameter("lat", lat);
        if (!TextUtils.isEmpty(where)) {
            params.addQueryStringParameter("where", where);
        }
        if (!TextUtils.isEmpty(start_time)) {
            params.addQueryStringParameter("start_time", start_time);
        }
        if (!TextUtils.isEmpty(end_time)) {
            params.addQueryStringParameter("end_time", end_time);
        }
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 备注
     *
     * @param apiListener
     */
    public void setRemark(String site_id, String m_id, String remarks, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setRemark");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("m_id", m_id);
        params.addBodyParameter("remarks", remarks);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 修改封面
     *
     * @param apiListener
     */
    public void setCover(String site_id, String img, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/setCover");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("img", new File(img));
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 绑定客户给水工
     *
     * @param apiListener
     */
    public void bindMember(String site_id, String c_id, String m_id, ApiListener apiListener) {
        LogUtil.e("site_id" + site_id + ",c_id" + c_id + ",m_id" + m_id);
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/bindMember");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("c_id", c_id);
        params.addBodyParameter("m_id", m_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 修改水站地址
     *
     * @param apiListener
     */
    public void updateAddress(String site_id, String address, String address_code,
                              String lon, String lar, String province, String city, String area, ApiListener apiListener) {
        LogUtil.e("site_id" + site_id + ",address" + address + ",lar" + lar
                + ",lon" + lon + ",province" + province
                + ",city" + city + ",area" + area + ",address_code" + address_code);
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/updateAddress");
        params.addBodyParameter("site_id", site_id);
        params.addBodyParameter("address", address);
        params.addBodyParameter("address_code", address_code);
        params.addBodyParameter("lon", lon);
        params.addBodyParameter("lar", lar);
        params.addBodyParameter("province", province);
        params.addBodyParameter("city", city);
        params.addBodyParameter("area", area);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }

    /**
     * 水站的统计
     *
     * @param apiListener
     */
    public void count(String site_id, String type, int p, ApiListener apiListener) {
        LogUtil.e("site_id" + site_id);
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/count");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 水站客户
     *
     * @param apiListener
     */
    public void member(String site_id, String order, String order_type, String where, int p, String search, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/member");
        params.addQueryStringParameter("site_id", site_id);
        if (TextUtils.isEmpty(where)) {
            if (!TextUtils.isEmpty(search)) {
                params.addQueryStringParameter("search", search);
            } else {
                if (!TextUtils.isEmpty(order)) {
                    params.addQueryStringParameter("order", order);
                    if (order_type.equals("1")) {
                        params.addQueryStringParameter("order_type", "desc");
                    } else {
                        params.addQueryStringParameter("order_type", "asc");
                    }
                }
            }
        } else {
            if (!where.equals("1")) {
                params.addQueryStringParameter("where", where);
            }
        }
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }
}
