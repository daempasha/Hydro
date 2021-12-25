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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddTodoFragment extends Fragment {

    private TextView descriptionText;
    private TextView dateView;
    private Button submitButton;
    private MaterialDatePicker datePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_todo, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descriptionText = (TextView) view.findViewById(R.id.todoDescription);
        dateView = (TextView) view.findViewById(R.id.todoDateView);
        submitButton = (Button) view.findViewById(R.id.addTodoItemButton);

        CalendarConstraints.Builder calendarConstraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now());
        CalendarConstraints calendarConstraints = calendarConstraintsBuilder.build();


        datePicker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setCalendarConstraints(calendarConstraints)
                .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Long timestamp = (Long) selection;
                setDateText(timestamp);
            }
        });

        dateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePicker();

                }
            }
        });

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });
    }

    private void setDateText(Long timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy h:mm");
        dateView.setText(simpleDateFormat.format(calendar.getTime()));

    }

    private void showDatePicker() {
        if(!datePicker.isVisible()){
            datePicker.show(getParentFragmentManager(), "tag");

        }
    }


}