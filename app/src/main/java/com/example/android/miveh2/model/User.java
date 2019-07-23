package com.example.android.miveh2.model;

import android.content.Context;

import com.example.android.miveh2.R;

import java.util.ArrayList;

public class User {

    //declare private data instead of public to ensure the privacy of data field of each class
    private String name;
    private String id;
    private boolean isChecked=false;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    //retrieve user's name
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public static ArrayList<User> getUsers(Context context) {
        Context mContext;
        mContext = context;
        ArrayList<User> users = new ArrayList<User>();


        users.add(new User(mContext.getResources().getString(R.string.q10), "10"));
        users.add(new User(mContext.getResources().getString(R.string.q11), "11"));
        users.add(new User(mContext.getResources().getString(R.string.q12), "12"));
        users.add(new User(mContext.getResources().getString(R.string.q13), "13"));
        users.add(new User(mContext.getResources().getString(R.string.q14), "14"));
        users.add(new User(mContext.getResources().getString(R.string.q15), "15"));
        users.add(new User(mContext.getResources().getString(R.string.q16), "16"));
        users.add(new User(mContext.getResources().getString(R.string.q17), "17"));
        users.add(new User(mContext.getResources().getString(R.string.q18), "18"));
        users.add(new User(mContext.getResources().getString(R.string.q19), "19"));
        users.add(new User(mContext.getResources().getString(R.string.q20), "20"));
        users.add(new User(mContext.getResources().getString(R.string.q21), "21"));
        users.add(new User(mContext.getResources().getString(R.string.q22), "22"));
        users.add(new User(mContext.getResources().getString(R.string.q23), "23"));
        users.add(new User(mContext.getResources().getString(R.string.q24), "24"));
        users.add(new User(mContext.getResources().getString(R.string.q25), "25"));
        users.add(new User(mContext.getResources().getString(R.string.q26), "26"));
        users.add(new User(mContext.getResources().getString(R.string.q27), "27"));
        users.add(new User(mContext.getResources().getString(R.string.q28), "27"));
        users.add(new User(mContext.getResources().getString(R.string.q29), "29"));
        users.add(new User(mContext.getResources().getString(R.string.q30), "30"));
        users.add(new User(mContext.getResources().getString(R.string.q31), "31"));
        users.add(new User(mContext.getResources().getString(R.string.q32), "32"));
        users.add(new User(mContext.getResources().getString(R.string.q33), "33"));
        users.add(new User(mContext.getResources().getString(R.string.q34), "34"));
        users.add(new User(mContext.getResources().getString(R.string.q35), "35"));
        users.add(new User(mContext.getResources().getString(R.string.q36), "36"));
        users.add(new User(mContext.getResources().getString(R.string.q37), "37"));
        users.add(new User(mContext.getResources().getString(R.string.q38), "38"));
        users.add(new User(mContext.getResources().getString(R.string.q39), "39"));
        users.add(new User(mContext.getResources().getString(R.string.q40), "40"));
        users.add(new User(mContext.getResources().getString(R.string.q41), "42"));
        users.add(new User(mContext.getResources().getString(R.string.q42), "42"));
        users.add(new User(mContext.getResources().getString(R.string.q43), "43"));
        users.add(new User(mContext.getResources().getString(R.string.q44), "44"));
        users.add(new User(mContext.getResources().getString(R.string.q45), "45"));
        users.add(new User(mContext.getResources().getString(R.string.q46), "46"));


        return users;
    }

    public static ArrayList<User> getUsersOther(Context context) {
        Context mContext;
        mContext = context;
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(mContext.getResources().getString(R.string.q1), "1"));
        users.add(new User(mContext.getResources().getString(R.string.q2), "2"));
        users.add(new User(mContext.getResources().getString(R.string.q3), "3"));
        users.add(new User(mContext.getResources().getString(R.string.q4), "4"));
        users.add(new User(mContext.getResources().getString(R.string.q5), "5"));
        users.add(new User(mContext.getResources().getString(R.string.q6), "6"));
        users.add(new User(mContext.getResources().getString(R.string.q7), "7"));
        users.add(new User(mContext.getResources().getString(R.string.q8), "8"));
        users.add(new User(mContext.getResources().getString(R.string.q9), "9"));




        return users;
    }

    public static ArrayList<User> getUsersOther1(Context context) {
        Context mContext;
        mContext = context;
        ArrayList<User> users = new ArrayList<User>();

        users.add(new User(mContext.getResources().getString(R.string.q47), "47"));
        users.add(new User(mContext.getResources().getString(R.string.q48), "48"));
        users.add(new User(mContext.getResources().getString(R.string.q49), "49"));
        users.add(new User(mContext.getResources().getString(R.string.q50), "50"));
        users.add(new User(mContext.getResources().getString(R.string.q51), "51"));
        users.add(new User(mContext.getResources().getString(R.string.q52), "52"));
        users.add(new User(mContext.getResources().getString(R.string.q53), "53"));
        users.add(new User(mContext.getResources().getString(R.string.q54), "54"));
        users.add(new User(mContext.getResources().getString(R.string.q55), "55"));
        users.add(new User(mContext.getResources().getString(R.string.q56), "56"));
        users.add(new User(mContext.getResources().getString(R.string.q57), "57"));


        return users;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}