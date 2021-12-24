package com.example.hydro.ui.models;

import java.util.Date;

public class Todo {

    public String id;
    public String message;
    public Date dateCreated;


    public Todo(){
        this.id = "";
        this.message = "";
        this.dateCreated = new Date();
    }

    public Todo(String id, String message){
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
