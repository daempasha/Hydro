package com.example.hydro.ui.todo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydro.DatabaseHandler;
import com.example.hydro.FirebaseHandler;
import com.example.hydro.R;
import com.example.hydro.databinding.FragmentTodoBinding;
import com.example.hydro.models.Todo;
import com.example.hydro.ui.addTodo.AddTodoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoFragment extends Fragment {

    private FragmentTodoBinding binding;
    private FirebaseDatabase database;
    private FloatingActionButton actionButton;
    private RecyclerView recyclerView;
    private DatabaseHandler localDatabaseHandler;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        FirebaseHandler firebaseHandler = new FirebaseHandler();
        Task<DataSnapshot> data = firebaseHandler.getTodos(new OnCompleteListener<DataSnapshot>() {
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
                        Collections.sort(todoList, new Comparator<Todo>() {
                            @Override
                            public int compare(Todo o1, Todo o2) {
                                if(o1.dateCreated > o2.dateCreated){
                                    return 1;
                                } else if(o1.dateCreated < o2.dateCreated){
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        });
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
                openAddTodoFragment();

            }
        });

        return root;
    }

    private void openAddTodoFragment() {
        AddTodoFragment addTodoFragment = new AddTodoFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, addTodoFragment).addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}