package com.example.android.miveh2.model;

public class Feedback {

    private String experience_status;
    private String feedback;
    private String room_key;
    private long currantDate;

    public String getExperience_status() {
        return experience_status;
    }

    public void setExperience_status(String experience_status) {
        this.experience_status = experience_status;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRoom_key() {
        return room_key;
    }

    public void setRoom_key(String room_key) {
        this.room_key = room_key;
    }


    public long getCurrantDate() {
        return currantDate;
    }

    public void setCurrantDate(long currantDate) {
        this.currantDate = currantDate;
    }
}
