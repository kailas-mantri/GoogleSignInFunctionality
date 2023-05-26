package com.leadsoft.smart_campus.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.leadsoft.smart_campus.R;

public class ProfileFragment extends Fragment {

    TextInputEditText textInputEditText, cgpa_average_semester, EditTextCgpa;
    RadioGroup radioGroup;
    RadioButton btnMale, btnFemale;

    Spinner spinner;

    String[] semesters = {"First Semester", "Second Semester", "Third Semester", "Fourth Semester", "Fifth Semester",
            "Sixth Semester", "Seventh Semester", "Eighth Semester"};

    public ProfileFragment(){ }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile,container,false);

        findIdFragments(rootView);

        cgpa_average_semester = rootView.findViewById(R.id.user_average_cgpa);

        textInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDailogBox(rootView);
            }
        });

        btnMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnMale.setBackground(getContext().getResources().getDrawable(R.drawable.border_gender));
                }
                else {
                    btnMale.setBackground(null);
                }
            }
        });

        btnFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnFemale.setBackground(getContext().getResources().getDrawable(R.drawable.border_gender));
                }
                else {
                    btnFemale.setBackground(null);
                }
            }
        });

        cgpa_average_semester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                average_semesterDialogBox(rootView);
            }
        });

        return rootView;
    }

    private void findIdFragments(View rootView) {
        textInputEditText = rootView.findViewById(R.id.user_profile_user_date);
        radioGroup = rootView.findViewById(R.id.radio_gender_selector);
        btnMale = rootView.findViewById(R.id.gender_male);
        btnFemale = rootView.findViewById(R.id.gender_female);

    }

    private void average_semesterDialogBox(View rootView) {
        ViewGroup view = rootView.findViewById(android.R.id.content);
        View dailogAverage = LayoutInflater.from(getContext()).inflate(R.layout.dailog_average,view,false);
        spinner = dailogAverage.findViewById(R.id.spinner_semester);

//        TextView txtTotal = dailogAverage.findViewById(R.id.txt_average_cgpa_title);
//        Button btnAdd = dailogAverage.findViewById(R.id.add_details);
        EditTextCgpa = dailogAverage.findViewById(R.id.dialog_semester_marks);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,semesters);
        spinner.setAdapter(spinnerAdapter);

        builder.setView(dailogAverage);
        builder.setTitle("ADD CGPA Details");

//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditTextCgpa
//            }
//        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                cgpa_average_semester.setText();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDailogBox(View rootView) {
        ViewGroup viewGroup = rootView.findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_picker, viewGroup,false);
        DatePicker datePicker = dialogView.findViewById(R.id.b_date_date_spinner);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(dialogView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = datePicker.getMonth()+1;
                String dob = datePicker.getDayOfMonth()+"/"+month+"/"+datePicker.getYear();
                textInputEditText.setText(dob);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
