package com.capstone.dolphindive.utility;

public class Message {

    private String senderid;
    private String receiverid;
    private String message;
    private String photoUrl;
    private String fileUrl;
    private boolean isseen;

    public Message(String sender, String receiver, String message, boolean isseen, String photoUrl, String fileUrl){
        this.senderid = sender;
        this.receiverid = receiver;
        this.message = message;
        this.isseen = isseen;
        this.photoUrl = photoUrl;
        this.fileUrl = fileUrl;
    }

    public Message() {
    }

    public String getSender() {
        return senderid;
    }

    public void setSender(String sender) {
        this.senderid = sender;
    }

    public String getReceiver() {
        return receiverid;
    }

    public void setReceiver(String receiver) {
        this.receiverid = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
