package com.example.hydro.models;

import java.util.Date;

public class Todo {
    public String message;
    public Date dateCreated;


    public Todo(){
        this.message = "";
        this.dateCreated = new Date();
    }

    public Todo(String message){
        this.message = message;
        this.dateCreated = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
