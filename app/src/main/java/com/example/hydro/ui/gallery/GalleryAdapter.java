package com.example.hydro.ui.gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.R;
import com.example.hydro.models.Todo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Map;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    List<Todo>  data;
    Context context;
    private FirebaseDatabase database;

    public GalleryAdapter(Context ct, List<Todo>  data){
        this.context = ct;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.todo_row, parent, false);
        database = FirebaseDatabase.getInstance();



        return new MyViewHolder(view);
    }

    public void removeTask(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Todo todo = data.get(position);
        holder.todoDescription.setText(todo.message);
        holder.todoCheck.setSelected(false);
        holder.todoCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.todoCheck.isChecked()){
                    Toast toast = Toast.makeText(context, R.string.task_deleted_successfully, Toast.LENGTH_SHORT);

                    DatabaseReference taskReference = database.getReference("tasks").child(todo.getId());
                    taskReference.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            toast.setText(R.string.task_deleted_successfully);
                            toast.show();

                            removeTask(holder.getAdapterPosition());

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            toast.setText(R.string.task_deleted_failure);
                            toast.show();

                        }
                    });

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox todoCheck;
        TextView todoDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            todoCheck = itemView.findViewById(R.id.todoCheckbox);
            todoDescription = itemView.findViewById(R.id.todoDescription);
        }
    }
}
