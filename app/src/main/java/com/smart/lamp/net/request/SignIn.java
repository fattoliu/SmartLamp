package com.smart.lamp.net.request;

/**
 * Created by marco on 2017/8/21.
 * 登陆实体
 */

public class SignIn {
    private String account;
    private String password;

    public SignIn(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
