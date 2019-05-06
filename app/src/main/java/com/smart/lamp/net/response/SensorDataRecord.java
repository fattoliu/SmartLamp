package com.smart.lamp.net.response;

import java.util.ArrayList;
import java.util.List;

public class SensorDataRecord {
    public int DeviceId;
    public List<DataPoint> DataPoints = new ArrayList<>();

    public class DataPoint {
        public String ApiTag;
        public List<VR> PointDTO;
    }

    public static class VR {
        public float Value;
        public String RecordTime;

        public VR(float value, String recordTime) {
            Value = value;
            RecordTime = recordTime;
        }
    }
}
