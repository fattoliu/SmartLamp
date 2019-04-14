package com.smart.lamp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * TODO
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 8/3/2019 10:23 PM
 */
public class VerifyInfo implements Parcelable {
    public String timeStamp;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.timeStamp);
    }

    public VerifyInfo(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private VerifyInfo(Parcel in) {
        this.timeStamp = in.readString();
    }

    public static final Creator<VerifyInfo> CREATOR = new Creator<VerifyInfo>() {
        @Override
        public VerifyInfo createFromParcel(Parcel source) {
            return new VerifyInfo(source);
        }

        @Override
        public VerifyInfo[] newArray(int size) {
            return new VerifyInfo[size];
        }
    };

    @Override
    public String toString() {
        return "VerifyInfo{" +
                "timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
