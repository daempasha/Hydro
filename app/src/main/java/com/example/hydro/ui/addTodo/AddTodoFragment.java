package com.example.hydro.ui.addTodo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hydro.FirebaseHandler;
import com.example.hydro.R;
import com.example.hydro.ui.todo.TodoFragment;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddTodoFragment extends Fragment {

    private TextInputLayout  descriptionText;
    private TextInputLayout dateView;
    private Button submitButton;
    private MaterialDatePicker datePicker;
    private Long timestamp;
    private FirebaseHandler firebaseHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_todo, container, false);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseHandler = new FirebaseHandler();
        descriptionText = (TextInputLayout ) view.findViewById(R.id.todoDescriptionView);
        dateView = (TextInputLayout ) view.findViewById(R.id.todoDateView);
        submitButton = (Button) view.findViewById(R.id.addTodoItemButton);

        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());
        CalendarConstraints calendarConstraints = calendarConstraintsBuilder.build();


        datePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(calendarConstraints)
                .build();

        dateView.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateView.getEditText().setText(null);
                timestamp = null;

            }
        });
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                timestamp = (Long) selection;
                setDateText();
            }
        });


        dateView.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker();

                }
            }
        });

        dateView.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushTodoToDatabase();
            }
        });
    }

    private void setDateText() {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm");
        dateView.getEditText().setText(simpleDateFormat.format(calendar.getTime()));

    }

    private void showDatePicker() {
        if(!datePicker.isVisible()){
            datePicker.show(getParentFragmentManager(), "tag");

        }
    }

    private void pushTodoToDatabase(){
        String description = descriptionText.getEditText().getText().toString();


        if(description.length() > 0) {
            boolean pushTodo = firebaseHandler.pushTodo(description, timestamp);

            if(pushTodo){
                Toast.makeText(getContext(), "Created todo", Toast.LENGTH_SHORT).show();
                openTodoFragment();
            }


        } else {
            Toast.makeText(getContext(), "There are some errors in the form. Please fix them before continuing", Toast.LENGTH_SHORT).show();
            descriptionText.setError("Please fill out the description");
        }
    }

    private void openTodoFragment() {
        TodoFragment todoFragment = new TodoFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_activity_main, todoFragment).addToBackStack(null);
        transaction.commit();
    }


}