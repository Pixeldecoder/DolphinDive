package com.capstone.dolphindive.utility;

import java.util.HashMap;

public class myPosts {
    public String time, date, postimage, description;
    public myPosts(){

    }

    public myPosts(String time, String date, String description, String postimage) {

        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
