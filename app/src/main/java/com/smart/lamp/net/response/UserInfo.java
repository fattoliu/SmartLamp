package com.smart.lamp.net.response;

import java.io.Serializable;

/**
 * TODO user info
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 6/3/2019 4:00 PM
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -2699260829173776740L;
    public String UserID;
    public String UserName;
    public String AccessToken;
    public String DataToken;

}
