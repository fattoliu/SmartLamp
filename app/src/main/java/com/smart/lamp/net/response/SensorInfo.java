package com.smart.lamp.net.response;

/**
 * Created by marco on 2017/8/22.
 * 网关对应的传感器列表item
 */

public class SensorInfo {
    public String ApiTag;
    public int Groups;
    public int Protocol;
    public String Name;
    public String CreateDate;
    public int TransType;
    public int DataType;
    public String TypeAttrs;
    public int DeviceID;
    public String SensorType;
    public String Value;
    public String RecordTime;
//传感器字段
    public String Unit;
    //执行器字段
    public long OperType;
    public String OperTypeAttrs;

    //摄像头字段
    public String HttpIp;
    public long HttpPort;
    public String UserName;
    public String Password;
    public String VideoStreamUrl;
    public String VideoStreamProtocol;
    public String VideoStreamPort;
    public String CtrlUrl;
}
