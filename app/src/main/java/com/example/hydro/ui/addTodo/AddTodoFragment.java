package com.example.hydro.ui.addTodo;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hydro.R;
import com.example.hydro.models.Todo;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTodoFragment extends Fragment {

    private TextInputLayout  descriptionText;
    private TextInputLayout dateView;
    private Button submitButton;
    private MaterialDatePicker datePicker;
    private Long timestamp;
    private FirebaseDatabase database;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_todo, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        database = FirebaseDatabase.getInstance();

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
        Log.e("TEST", description);
        Log.e("TEST", String.valueOf(timestamp));

        DatabaseReference databaseReference = database.getReference("todos").push();
        String key = databaseReference.getKey();
        Todo todo = new Todo(key, description, timestamp);

        databaseReference.setValue(todo);
    }


}