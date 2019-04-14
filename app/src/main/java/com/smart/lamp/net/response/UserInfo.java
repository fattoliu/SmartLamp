package com.smart.lamp.net.response;

import java.io.Serializable;

/**
 * Created by marco on 2017/8/21.
 * 用户信息
 */

public class UserInfo implements Serializable {
    private static final long serialVersionUID = -2699260829173776740L;
    public String UserID;
    public String UserName;
    public String AccessToken;
    public String DataToken;

}
