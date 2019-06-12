package com.example.android.miveh2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DevicesEvent implements Parcelable {
    //private String room_id;
    private String room_key;
    private String token;
    private String uuid;
    private String status;

    public DevicesEvent()
    {

    }

    protected DevicesEvent(Parcel in) {
        //room_id = in.readString();
        room_key = in.readString();
        token = in.readString();
        uuid = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeString(room_id);
        dest.writeString(room_key);
        dest.writeString(token);
        dest.writeString(uuid);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DevicesEvent> CREATOR = new Creator<DevicesEvent>() {
        @Override
        public DevicesEvent createFromParcel(Parcel in) {
            return new DevicesEvent(in);
        }

        @Override
        public DevicesEvent[] newArray(int size) {
            return new DevicesEvent[size];
        }
    };


    //public String getRoomId() { return room_id; }
    //public void setRoomId(String room_id) {this.room_id = room_id; }

    public String getToken() {return token; }
    public void setToken(String token) {this.token = token; }

    public String getUuid() {return uuid; }
    public void setUuid(String uuid) {this.uuid = uuid; }

    public String getStatus() {return status; }
    public void setStatus(String status) {this.status = status; }

    public String getRoom_key() {
        return room_key;
    }

    public void setRoom_key(String room_key) {
        this.room_key = room_key;
    }
}
