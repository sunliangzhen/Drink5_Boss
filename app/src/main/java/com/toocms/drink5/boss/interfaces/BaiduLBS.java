package com.toocms.drink5.boss.interfaces;

import com.toocms.frame.web.ApiListener;
import com.toocms.frame.web.ApiTool;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import java.net.URLEncoder;

/**
 * 搜索地址
 *
 * @author Zero
 * @date 2016/2/26 14:19
 */
public class BaiduLBS {

    /**
     * Place区域检索POI服务
     *
     * @param query
     * @param apiListener
     */
    public void search(String query, String city, ApiListener apiListener) {
        String http = "http://api.map.baidu.com/place/v2/search?";
        String utl = http + "q=" + URLEncoder.encode(query) + "&region=" + URLEncoder.encode(city) + "&output=json&ak=Hq1TKva6enLPVRTODBDnPlX2OYrXWZZI&mcode=46:9B:91:B2:90:21:3B:3D:B9:B3:C7:AB:D1:99:88:D5:97:23:2F:67;com.toocms.drink5.boss";
        RequestParams params = new RequestParams(utl);
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }

    public void search2(String address, ApiListener apiListener) {
        LogUtil.e("address" + address);
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=Hq1TKva6enLPVRTODBDnPlX2OYrXWZZI&mcode=46:9B:91:B2:90:21:3B:3D:B9:B3:C7:AB:D1:99:88:D5:97:23:2F:67;com.toocms.drink5.boss=" + address + "&output=json";
//        String url = "http://api.map.baidu.com/geocoder/v2/?ak=ggLPgdLDaLpmQFKa0cfX5SIDc1qIV78N&mcode=9C:76:7A:02:C8:26:46:A1:48:85:C2:0D:80:29:AA:DE:90:19:9D:51;com.toocms.drink5.boss&location=39.905375,116.405121&output=json";
//        RequestParams params2 = new RequestParams("http://api.map.baidu.com/place/v2/search?q=饭店&region=北京&output=json&ak=ggLPgdLDaLpmQFKa0cfX5SIDc1qIV78N&mcode=9C:76:7A:02:C8:26:46:A1:48:85:C2:0D:80:29:AA:DE:90:19:9D:51;com.toocms.drink5.boss");
        RequestParams params = new RequestParams(url);
//        params.addQueryStringParameter("ak", "ggLPgdLDaLpmQFKa0cfX5SIDc1qIV78N");
//        params.addQueryStringParameter("output", "json");
//        params.addQueryStringParameter("query", URLEncoder.encode(query));
//        params.addQueryStringParameter("page_size", "10");
//        params.addQueryStringParameter("page_num", "0");
//        params.addQueryStringParameter("scope", "1");
//        params.addQueryStringParameter("region", URLEncoder.encode("中国"));
//        params.addQueryStringParameter("mcode", "9C:76:7A:02:C8:26:46:A1:48:85:C2:0D:80:29:AA:DE:90:19:9D:51;com.toocms.drink5.boss");
        ApiTool apiTool = new ApiTool();
        apiTool.getApi(params, apiListener);
    }
}
