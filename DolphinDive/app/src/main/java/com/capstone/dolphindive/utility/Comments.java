package com.capstone.dolphindive.utility;

public class Comments {
    public String uid, content, date, fullname, profileimage, time;

    public Comments(){

    }
    public Comments(String uid, String content, String date, String fullname, String profileimage, String time) {
        this.uid = uid;
        this.content = content;
        this.date = date;
        this.fullname = fullname;
        this.profileimage = profileimage;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
