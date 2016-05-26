package com.eva.httpdemo.api;

/**
 * 网络请求回调
 *
 * @author test
 * @date 2015-11-05 17:35
 */
public interface HttpRequestCallback<T> {

    void onSuccess(int code, String msg, T data);

    void onFailure(int code, String msg);
}
