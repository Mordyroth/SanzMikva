package com.example.android.miveh2.model;

import java.io.Serializable;

public class Room implements Serializable {
    //private String room_id;
    private String room_key;
    private String uuid;
    private String status;

    public Room() {

    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoom_key() {
        return room_key;
    }

    public void setRoom_key(String room_key) {
        this.room_key = room_key;
    }
}
