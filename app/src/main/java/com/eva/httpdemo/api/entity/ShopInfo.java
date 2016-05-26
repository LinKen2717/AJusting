package com.eva.httpdemo.api.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 店铺信息
 *
 * @author test
 * @date 2015-11-17 05:24
 */
public class ShopInfo {

    /** 店铺ID */
    @JSONField(name = "shopid")
    private String id;

    /** 店铺名称 */
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
