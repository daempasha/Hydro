package com.example.hydro;

import com.example.hydro.models.Todo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class FirebaseHandler {
    private FirebaseDatabase database;

    public FirebaseHandler(){
        database = FirebaseDatabase.getInstance();
    }

    public void getTodos(OnCompleteListener<DataSnapshot> onCompleteListener){
        database.getReference("todos").get().addOnCompleteListener(onCompleteListener);
    }

    public boolean pushTodo(String description, Long timestamp){
       try {

           DatabaseReference databaseReference = database.getReference("todos").push();
           String key = databaseReference.getKey();
           Todo todo = new Todo(key, description, timestamp, new Date().getTime());
           databaseReference.setValue(todo);

           return true;
       } catch (Exception e){
           return false;
       }

    }

    public void deleteTodo(Todo todo, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){
        DatabaseReference taskReference = database.getReference("todos").child(todo.getId());
        boolean result = false;

        taskReference.setValue(null).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }
}
