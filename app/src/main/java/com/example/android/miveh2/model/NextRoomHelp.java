package com.example.android.miveh2.model;

public class NextRoomHelp {

    private String date;
    private String room_key;
    private int help_count_number;
    private String help_status="";
    private String uuid;

    public int getHelp_count_number() {
        return help_count_number;
    }

    public void setHelp_count_number(int help_count_number) {
        this.help_count_number = help_count_number;
    }

    public String getRoom_key() {
        return room_key;
    }

    public void setRoom_key(String room_key) {
        this.room_key = room_key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHelp_status() {
        return help_status;
    }

    public void setHelp_status(String help_status) {
        this.help_status = help_status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
