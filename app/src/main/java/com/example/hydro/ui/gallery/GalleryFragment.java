package com.example.hydro.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.R;
import com.example.hydro.databinding.FragmentGalleryBinding;
import com.example.hydro.databinding.FragmentHomeBinding;
import com.example.hydro.models.Todo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private FirebaseDatabase database;
    private FloatingActionButton actionButton;
    private RecyclerView recyclerView;

    public void updateRecyclerView(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.recyclerView = binding.recyclerView;
        //Database
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
                        GalleryAdapter adapter = new GalleryAdapter(getActivity(), todoList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }

                }
            }
        });

//        DatabaseReference tasksRef = database.getReference("tasks").push();
//        String id = tasksRef.getKey();
//        Todo todo = new Todo(id, "Put the washing out");
//        tasksRef.setValue(todo);

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