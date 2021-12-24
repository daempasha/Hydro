package com.example.hydro.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.databinding.FragmentTodoBinding;
import com.example.hydro.models.Todo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoFragment extends Fragment {

    private FragmentTodoBinding binding;
    private FirebaseDatabase database;
    private FloatingActionButton actionButton;
    private RecyclerView recyclerView;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.recyclerView = binding.recyclerView;
        database = FirebaseDatabase.getInstance();


        Task<DataSnapshot> data = database.getReference("tasks").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> tasks) {
                if (!tasks.isSuccessful()) {
                    Log.e("firebase", "Error getting data", tasks.getException());
                }
                else {
                    GenericTypeIndicator<HashMap<String, Todo>> tasksGTypeInd = new GenericTypeIndicator<HashMap<String, Todo>>() {};

                    Map<String, Todo> objectHashMap = tasks.getResult().getValue(tasksGTypeInd);

                    if(objectHashMap != null) {
                        List<Todo> todoList = new ArrayList<Todo>(objectHashMap.values());
                        TodoAdapter adapter = new TodoAdapter(getActivity(), todoList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }

                }
            }
        });

        this.actionButton = binding.addTodoButton;
        actionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}