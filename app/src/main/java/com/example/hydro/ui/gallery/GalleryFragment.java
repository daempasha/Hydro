package com.example.hydro.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hydro.R;
import com.example.hydro.databinding.FragmentGalleryBinding;
import com.example.hydro.databinding.FragmentHomeBinding;
import com.example.hydro.models.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    private FirebaseDatabase database;
    private FloatingActionButton actionButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Database
        database = FirebaseDatabase.getInstance();
        //DatabaseReference tasksRef = database.getReference("tasks");
        //Todo todo = new Todo("TestingFF");
        //tasksRef.push().setValue(todo);

        this.actionButton = binding.addTodoButton;
        actionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService("S");

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