package com.tuktuk.dmth.tuktuk.Bussiness;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nrv on 9/16/16.
 */
public class JSONMessage implements Parcelable{
    protected JSONMessage(Parcel in) {
    }

    public static final Creator<JSONMessage> CREATOR = new Creator<JSONMessage>() {
        @Override
        public JSONMessage createFromParcel(Parcel in) {
            return new JSONMessage(in);
        }

        @Override
        public JSONMessage[] newArray(int size) {
            return new JSONMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
