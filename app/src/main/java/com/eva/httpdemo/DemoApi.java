package com.eva.httpdemo;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.eva.httpdemo.api.BaseApi;
import com.eva.httpdemo.api.HttpRequestCallback;
import com.eva.httpdemo.api.entity.ShopListBean;
import com.eva.httpdemo.api.entity.UserInfo;


/**
 * Class description
 *
 * @author test
 * @date 2015-11-04 14:09
 */
public class DemoApi extends BaseApi {

    private static DemoApi sInstance = null;

    // 公司IP
    private  String hostUrl = "http://10.59.90.54:8080";

    // 家里IP
//    private  String hostUrl = "http://192.168.0.101:8080";

    public DemoApi(Context context) {
        super(context);
    }

    /**
     * 初次使用时调用此方法获取实例
     *
     * @return
     */
    public synchronized static DemoApi getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DemoApi(context);
        }
        return sInstance;
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @param callback
     */
    public void getProfile(String token, HttpRequestCallback<UserInfo> callback) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("token", token);
        post(UserInfo.class, hostUrl, "/1.0/my/profile", params, callback);
    }

    /**
     * 获取店铺列表
     *
     * @param token
     * @param page
     * @param perpage
     * @param callback
     */
    public void getShopList(String token, int page, int perpage, HttpRequestCallback<ShopListBean> callback) {
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("token", token);
        params.put("page", Integer.toString(page));
        params.put("perpage", Integer.toString(perpage));
        post(ShopListBean.class, hostUrl, "/1.0/shop_myshop/list", params, callback);
    }


}
