package com.smart.lamp.net.request;



/**
 * TODO  login bean
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 9/3/2019 9:30 PM
 */
public class SignIn {
    /**
     * 账户
     */
    private String account;
    /**
     * 密码
     */
    private String password;

    public SignIn(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
