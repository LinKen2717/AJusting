package com.eva.httpdemo.api.entity;


/**
 * @author test
 * @date 16/3/10
 */
public class ShopListBean extends BaseListBean<ShopInfo> {

    @Override
    public Class getType() {
        return ShopInfo.class;
    }
}
