package com.example.android.miveh2;

import android.content.Context;


import java.util.ArrayList;

public class User {

    //declare private data instead of public to ensure the privacy of data field of each class
    private String name;
    private String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    //retrieve user's name
    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }

    public static ArrayList<User> getUsers(Context context) {
        Context mContext;
        mContext = context;
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(mContext.getResources().getString(R.string.question1), "1"));
        users.add(new User(mContext.getResources().getString(R.string.question2), "2"));
        users.add(new User(mContext.getResources().getString(R.string.question3), "3"));
        users.add(new User(mContext.getResources().getString(R.string.question4), "4"));
        users.add(new User(mContext.getResources().getString(R.string.question5), "5"));
        users.add(new User(mContext.getResources().getString(R.string.question6), "6"));
        users.add(new User(mContext.getResources().getString(R.string.question7), "7"));
        users.add(new User(mContext.getResources().getString(R.string.question8), "8"));
        users.add(new User(mContext.getResources().getString(R.string.question9), "9"));
        users.add(new User(mContext.getResources().getString(R.string.question10), "10"));
        users.add(new User(mContext.getResources().getString(R.string.question11), "11"));
        users.add(new User(mContext.getResources().getString(R.string.question12), "12"));
        users.add(new User(mContext.getResources().getString(R.string.question13), "13"));
        users.add(new User(mContext.getResources().getString(R.string.question14), "14"));
        return users;
    }
}