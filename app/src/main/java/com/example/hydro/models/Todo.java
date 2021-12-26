package com.example.hydro.models;

import java.util.Date;

public class Todo {

    public String id;
    public String message;
    public Long dateDue;



    public Long dateCreated;

    public Todo(){
        this.id = "";
        this.message = "";
        this.dateDue = null;
        this.dateCreated = new Date().getTime();
    }

    public Todo(String id, String message, Long timestamp, Long dateCreated){
        this.id = id;
        this.message = message;
        this.dateDue = timestamp;
        this.dateCreated = dateCreated;
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

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

}
