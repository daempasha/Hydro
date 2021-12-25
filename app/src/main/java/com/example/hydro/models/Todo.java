package com.example.hydro.models;

import java.util.Date;

public class Todo {

    public String id;
    public String message;
    public Long dateDue;


    public Todo(){
        this.id = "";
        this.message = "";
        this.dateDue = null;
    }

    public Todo(String id, String message, Long timestamp){
        this.id = id;
        this.message = message;
        this.dateDue = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDateDue() {
        return dateDue;
    }

    public void setDateDue(Long dateCreated) {
        this.dateDue = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
