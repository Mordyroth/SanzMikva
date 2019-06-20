package com.example.android.miveh2.model;

public class Help {

    public static String READY_PRESS = "READY_PRESS";
    public static String READY_CANCEL = "READY_CANCEL";
    public static String HELP_PRESS = "HELP_PRESS";
    public static String HELP_CANCEL = "HELP_CANCEL";
    public static String DONE = "DONE";


    private String room_key;

    private Long help_press_time = 0l;
    private Long help_cancel_time = 0l;

    private Long ready_press_time = 0l;
    private Long ready_cancel_time = 0l;

    private Long done_press_time = 0l;

    private String uuid;

    private String status;


    private String help_to_ready_time="";
    private String ready_to_done_time="";


    public Long getHelp_press_time() {
        return help_press_time;
    }

    public void setHelp_press_time(Long help_press_time) {
        this.help_press_time = help_press_time;
    }

    public Long getHelp_cancel_time() {
        return help_cancel_time;
    }

    public void setHelp_cancel_time(Long help_cancel_time) {
        this.help_cancel_time = help_cancel_time;
    }

    public Long getReady_press_time() {
        return ready_press_time;
    }

    public void setReady_press_time(Long ready_press_time) {
        this.ready_press_time = ready_press_time;
    }

    public Long getReady_cancel_time() {
        return ready_cancel_time;
    }

    public void setReady_cancel_time(Long ready_cancel_time) {
        this.ready_cancel_time = ready_cancel_time;
    }

    public Long getDone_press_time() {
        return done_press_time;
    }

    public void setDone_press_time(Long done_press_time) {
        this.done_press_time = done_press_time;
    }


    public static String getReadyPress() {
        return READY_PRESS;
    }

    public static void setReadyPress(String readyPress) {
        READY_PRESS = readyPress;
    }

    public static String getReadyCancel() {
        return READY_CANCEL;
    }

    public static void setReadyCancel(String readyCancel) {
        READY_CANCEL = readyCancel;
    }

    public static String getHelpPress() {
        return HELP_PRESS;
    }

    public static void setHelpPress(String helpPress) {
        HELP_PRESS = helpPress;
    }

    public static String getHelpCancel() {
        return HELP_CANCEL;
    }

    public static void setHelpCancel(String helpCancel) {
        HELP_CANCEL = helpCancel;
    }

    public static String getDONE() {
        return DONE;
    }

    public static void setDONE(String DONE) {
        Help.DONE = DONE;
    }

    public String getRoom_key() {
        return room_key;
    }

    public void setRoom_key(String room_key) {
        this.room_key = room_key;
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


    public String getHelp_to_ready_time() {
        return help_to_ready_time;
    }

    public void setHelp_to_ready_time(String help_to_ready_time) {
        this.help_to_ready_time = help_to_ready_time;
    }

    public String getReady_to_done_time() {
        return ready_to_done_time;
    }

    public void setReady_to_done_time(String ready_to_done_time) {
        this.ready_to_done_time = ready_to_done_time;
    }
}
