package com.eva.httpdemo.api;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.eva.httpdemo.JsonParser;
import com.eva.httpdemo.api.entity.BaseListBean;
import com.eva.httpdemo.api.entity.ResultData;
import com.eva.httpdemo.http.HttpRequestEngine;
import com.eva.httpdemo.http.HttpResponseHandle;

import java.util.List;


/**
 * Class description
 *
 * @author test
 * @date 2015-09-22 21:40
 */
public abstract class BaseApi extends HttpRequestEngine {

    public BaseApi(Context context) {
        super(context);
    }

    @Override
    protected ArrayMap<?, ?> getHeaders() {
        // TODO: 16/5/19 添加header
        return null;
    }

    protected <T> void get(final Class<T> entityClass, String hostUrl, String url, ArrayMap<?, ?> params, final HttpRequestCallback<T> callback) {
        get(null, entityClass, hostUrl, url, params, callback);
    }

    protected <T> void get(Object tag, final Class<T> entityClass, String hostUrl, String url, ArrayMap<?, ?> params, final HttpRequestCallback<T> callback) {
        get(tag, hostUrl, url, params, new HttpResponseHandle() {
            @Override
            public void onSuccess(int code, String responseBody) {
                parseResponse(entityClass, responseBody, callback);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (callback != null) {
                    callback.onFailure(code, errorMsg);
                }
            }

            @Override
            public void onTimeout() {
            }
        });
    }

    protected <T> void post(final Class<T> entityClass, String hostUrl, String url, ArrayMap<?, ?> params, final HttpRequestCallback<T> callback) {
        post(null, entityClass, hostUrl, url, params, callback);
    }

    protected <T> void post(Object tag, final Class<T> entityClass, String hostUrl, String url, ArrayMap<?, ?> params, final HttpRequestCallback<T> callback) {
        post(tag, hostUrl, url, params, new HttpResponseHandle() {
            @Override
            public void onSuccess(int code, String responseBody) {
                parseResponse(entityClass, responseBody, callback);
            }

            @Override
            public void onFailure(int code, String errorMsg) {
                if (callback != null) {
                    callback.onFailure(code, errorMsg);
                }
            }

            @Override
            public void onTimeout() {

            }
        });
    }

    private <T> void parseResponse(Class<T> entityClass, String responseBody, HttpRequestCallback<T> callback) {
        int code = -1;
        String msg = "返回的实体为空";
        T data = null;

        Log.v("Eva", "response result = " + responseBody);
        if (!TextUtils.isEmpty(responseBody)) {
            ResultData result = JsonParser.parseObject(responseBody, ResultData.class);
            code = result.getCode();
            msg = result.getMsg();
            if (result.isOk() && result.getData() !=null && !result.getData().isEmpty()) {
                String jsonStr = result.getData().toJSONString().trim();
                if (!TextUtils.isEmpty(jsonStr)) {
                    data = JsonParser.parseObject(jsonStr, entityClass);
                }
            }

            if (data instanceof BaseListBean) {
                List list = ((BaseListBean) data).getList();
                if (list != null && list.size() > 0) {
                    for(int i = 0; i < list.size(); i++) {
                        JSONObject item = (JSONObject) list.get(i);
                        list.set(i, JsonParser.parseObject(item.toJSONString(), ((BaseListBean) data).getType()));
                    }
                }
            }
        }

        if (callback != null) {
            if (code == 0) {
                callback.onSuccess(code, msg, data);
            } else {
                callback.onFailure(code, msg);
            }
        }

    }
}
