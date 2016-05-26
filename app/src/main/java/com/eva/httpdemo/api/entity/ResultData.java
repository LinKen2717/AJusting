package com.eva.httpdemo.api.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 网络请求JSON实体类
 *
 * @author test
 * @date 2015-11-05 11:41
 */
public class ResultData {

    private static final int RESULT_OK = 0;

    protected int code = -1; // 代码
    protected String msg; // 信息
    private JSONObject data;
    @JSONField(name = "elapsed_time")
    protected float elapsedTime;

    public ResultData() {}

    /**
     * 错误代码 网络错误或超时等未请求到api或其他错误。 errorCode = -1
     */
    public boolean isOk() {
        return code == RESULT_OK;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
