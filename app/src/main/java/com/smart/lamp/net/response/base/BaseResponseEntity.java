package com.smart.lamp.net.response.base;

import java.io.Serializable;


/**
 * TODO  基础相应实体
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 7/3/2019 7:30 AM
 */
public class BaseResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = -6726645970755330812L;
    private Object ErrorObj;
    private int Status;
    private int StatusCode;
    private String Msg;
    private T ResultObj;

    public Object getErrorObj() {
        return ErrorObj;
    }

    public void setErrorObj(Object errorObj) {
        ErrorObj = errorObj;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public void setErrorObj(String errorObj) {
        ErrorObj = errorObj;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }


    public void setResultObj(T resultObj) {
        ResultObj = resultObj;
    }


    public int getStatus() {
        return Status;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public String getMsg() {
        return Msg;
    }

    public T getResultObj() {
        return ResultObj;
    }

}
