package com.eva.httpdemo;

import android.app.Activity;
import android.os.Bundle;

import com.eva.httpdemo.api.HttpRequestCallback;
import com.eva.httpdemo.api.entity.UserInfo;

/**
 * 登陆页
 *
 * @author test
 * @date 2015-09-23 14:13
 */
public class LoginActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取用户信息请求
     *
     * @param token
     *
     */
    void getUser(String token) {
        // api可初始化在Application中
        DemoApi api = new DemoApi(this);
        api.getProfile(token, new HttpRequestCallback<UserInfo>() {
            @Override
            public void onSuccess(int code, String msg, UserInfo data) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

}
