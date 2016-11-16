package com.toocms.drink5.boss.interfaces;

import android.text.TextUtils;

import com.toocms.drink5.boss.config.AppConfig;
import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.List;

/**
 * @author Zero
 * @date 2016/6/16 21:10
 */
public class Goods {
    private String module = this.getClass().getSimpleName(); // 模块名

    /**
     * 添加水品
     *
     * @param apiListener
     */
    public void update(String water, String goods_id, String cover, String goods_name, List<String> old_id, String cate_id, String brand_id, String attr,
                       String goods_price,
                       String intro, List<String> gallery, List<String> buy_num, List<String> give_num, List<String> price, List<String> tick,
                       String is_promotion, String prompt, String site_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/update");
        params.addBodyParameter("water", water);
        if (!TextUtils.isEmpty(goods_id)) {
            LogUtil.e(goods_id + "goods_id");
            params.addBodyParameter("goods_id", goods_id);
        }
        if (!cover.contains("http")) {
            params.addBodyParameter("cover", new File(cover));
            if (!TextUtils.isEmpty(goods_id)) {
                params.addBodyParameter("edit_img", "1");
            }
        }
        params.addBodyParameter("goods_name", goods_name);

        StringBuffer oldPath = new StringBuffer();
        for (int i = 0; i < old_id.size(); i++) {
            oldPath.append(old_id.get(i));
            if (i != old_id.size() - 1) {
                oldPath.append(",");
            }
        }
        LogUtil.e(oldPath.toString());
        LogUtil.e(gallery.toString());
        if (old_id.size() > 0) {
            params.addBodyParameter("old_gallery", oldPath.toString());
        } else {
            params.addBodyParameter("old_gallery", "");
        }
        params.addBodyParameter("cate_id", cate_id);
        params.addBodyParameter("brand_id", brand_id);
        params.addBodyParameter("attr", attr);
        params.addBodyParameter("goods_price", goods_price);
        params.addBodyParameter("intro", intro);
        int k = 1;
        if (gallery.size() > 0) {
            for (int i = 0; i < gallery.size() - 1; i++) {
                if (!gallery.get(i).contains("http")) {
                    params.addBodyParameter("gallery_" + k, new File(gallery.get(i)));
                    LogUtil.e("kk" + k);
                    LogUtil.e(gallery.get(i));
                    k++;
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = new StringBuffer();
        StringBuffer stringBuffer4 = new StringBuffer();
        for (int i = 0; i < buy_num.size(); i++) {
            stringBuffer.append(buy_num.get(i));
            if (i != buy_num.size() - 1) {
                stringBuffer.append(",");
            }
            stringBuffer2.append(give_num.get(i));
            if (i != buy_num.size() - 1) {
                stringBuffer2.append(",");
            }
            stringBuffer3.append(price.get(i));
            if (i != buy_num.size() - 1) {
                stringBuffer3.append(",");
            }
            stringBuffer4.append(tick.get(i));
            if (i != buy_num.size() - 1) {
                stringBuffer4.append(",");
            }
        }
        if (buy_num.size() > 0) {
            params.addBodyParameter("buy_num", stringBuffer.toString());
            params.addBodyParameter("give_num", stringBuffer2.toString());
            params.addBodyParameter("price", stringBuffer3.toString());
            params.addBodyParameter("t_id", stringBuffer4.toString());
        }
//        LogUtil.e(stringBuffer.toString());
//        LogUtil.e(stringBuffer2.toString());
//        LogUtil.e(stringBuffer3.toString());
//        LogUtil.e(stringBuffer4.toString());

        params.addBodyParameter("is_promotion", is_promotion);
        if (!TextUtils.isEmpty(prompt)) {
            params.addBodyParameter("prompt", prompt);
        }
        params.addBodyParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }
//    /**
//     * 修改头像
//     *
//     * @param apiListener
//     */
//    public void onHead(String c_id, String head, ApiListener apiListener) {
//        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/onHead");
//        params.addBodyParameter("c_id", c_id);
//        params.addBodyParameter("head", new File(head));
//        ApiTool apiTool = new ApiTool();
//        apiTool.postApi(params, apiListener);
//    }

    /**
     * 获取商品分类
     *
     * @param apiListener
     */
    public void goodsCate(String site_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/goodsCate");
        params.addQueryStringParameter("site_id", site_id);
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 获取品牌分类
     *
     * @param apiListener
     */
    public void goodsBrand(int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/goodsBrand");
        params.addQueryStringParameter("p", String.valueOf(p));
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 商品列表
     *
     * @param apiListener
     */
    public void goodsList(String site_id, int p, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/goodsList");
        params.addQueryStringParameter("p", String.valueOf(p));
        params.addQueryStringParameter("site_id", site_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 商品详情
     *
     * @param apiListener
     */
    public void goodsDetail(String goods_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/goodsDetail");
        params.addQueryStringParameter("goods_id", goods_id);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    /**
     * 删除商品
     *
     * @param apiListener
     */
    public void del(String goods_id, ApiListener apiListener) {
        RequestParams params = new RequestParams(AppConfig.BASE_URL + module + "/del");
        params.addBodyParameter("goods_id", goods_id);
        ApiTool apiTool = new ApiTool();
        apiTool.postApi(params, apiListener);
    }


}
