package com.eva.httpdemo.api.entity;

/**
 * 用户实体
 *
 * @author test
 * @date 2015-09-23 16:08
 */
public class UserInfo{

    /** 用户UUID */
    private String uuid;

    /** 用户token */
    private String token;

    /** 昵称 */
    private String nickname;

    /** 真实姓名 */
    private String realname;

    /** 头像 */
    private String avatar;

    /** 手机号 */
    private String phone = "";

    /** 用户邮箱 */
    private String email = "";

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User: uuid = " + uuid + " , token = " + token;
    }
}
