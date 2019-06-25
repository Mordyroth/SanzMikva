package com.example.android.miveh2.model;

public class Settings {

    private String room_key;
    private long last_time_millis;
    private String last_time_date;
    private String uuid;

    public String getRoom_key() {
        return room_key;
    }

    public void setRoom_key(String room_key) {
        this.room_key = room_key;
    }


    public String getLast_time_date() {
        return last_time_date;
    }

    public void setLast_time_date(String last_time_date) {
        this.last_time_date = last_time_date;
    }

    public long getLast_time_millis() {
        return last_time_millis;
    }

    public void setLast_time_millis(long last_time_millis) {
        this.last_time_millis = last_time_millis;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
