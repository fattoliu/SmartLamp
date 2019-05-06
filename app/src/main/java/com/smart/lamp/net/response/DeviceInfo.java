package com.smart.lamp.net.response;

import java.util.List;

/**
 * TODO Device info
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 6/3/2019 5:03 PM
 */
public class DeviceInfo {
    public String Name;
    public String Tag;
    public String Protocol;
    public String LastOnlineIP;
    public String LastOnlineTime;
    public String Coordinate;
    public String CreateDate;
    public String IsShare;
    public String IsTrans;
    public String DeviceID;
    public String ProjectID;
    public String IsOnline;
    public List<SensorInfo> Sensors;
    public String ProjectIdOrTag;
    public String DeviceImg;
}
