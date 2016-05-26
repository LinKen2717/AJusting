package com.eva.httpdemo.http;

/**
 *
 * @author test
 * @date 2015-09-22 16:37
 */
public interface HttpResponseHandle {

    void onSuccess(int code, String responseBody);

    void onFailure(int code, String errorMsg);

    void onTimeout();
}
