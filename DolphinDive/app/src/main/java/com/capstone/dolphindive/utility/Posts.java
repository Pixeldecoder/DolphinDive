package com.capstone.dolphindive.utility;

import java.util.HashMap;

public class Posts {
    public String uid, time, date, postimage, profileimage, description, fullname, likes, newLikes, liker, newLiker, commentCounter;

    public Posts(){

    }


    public Posts(String uid, String time, String date, String postimage, String profileimage, String description, String fullname, String likes, String newLikes, String liker, String newLiker) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.profileimage = profileimage;
        this.description = description;
        this.fullname = fullname;
        this.likes = likes;
        this.newLikes = newLikes;
        this.liker = liker;
        this.newLiker = newLiker;
        this.commentCounter = commentCounter;

    }


    public String getNewLikes() { return newLikes; }

    public String getLiker() { return liker; }

    public void setLiker(String liker) { this.liker = liker; }

    public String getNewLiker() { return newLiker; }

    public void setNewLiker(String newLiker) { this.newLiker = newLiker; }

    public void setNewLikes(String newLikes) { this.newLikes = newLikes; }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCommentCounter() { return commentCounter; }

    public void setCommentCounter(String commentCounter) { this.commentCounter = commentCounter; }
}
