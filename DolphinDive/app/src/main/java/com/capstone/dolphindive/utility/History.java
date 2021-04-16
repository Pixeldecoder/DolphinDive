package com.capstone.dolphindive.utility;

public class History {

    private String id;
    private String message;

    public History(String id, String message){
        this.id = id;
        this.message = message;
    }

    public History(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
