package com.example.xpensemobileapp.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.example.xpensemobileapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class BudgetFormActivity extends AppCompatActivity {
    //DB Reference
    private DatabaseReference dbRef;
    //Edit Text and Buttons
    private EditText budgetDateFrom, budgetDateTo, budgetAmount;
    private FloatingActionButton btnAddBudget;
    //Budget Object
    private Budget budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_form);

        setTitle("Budget");




        //Edit Text Fields
        budgetDateFrom = findViewById(R.id.editTextDateFrom);
        budgetDateTo = findViewById(R.id.editTextDateTo);
        budgetAmount = findViewById(R.id.editTextAmount);

        //Buttons
        btnAddBudget = findViewById(R.id.btnAddBudget);

        //Instantiate budget object
        budget = new Budget();

        budgetDateFrom.setOnClickListener(this::openMaterialDatePicker);
        budgetDateTo.setOnClickListener(this::openMaterialDatePicker);



        //Add a new budget
        btnAddBudget.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(budgetDateFrom.getText().toString()))
                    Snackbar.make(view, "Please Enter a date", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(budgetDateTo.getText().toString()))
                    Snackbar.make(view, "Please Enter a date", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(budgetAmount.getText().toString()))
                    Snackbar.make(view, "Please Enter a amount", Snackbar.LENGTH_SHORT).show();
                else{

                    if(compareDate(budgetDateFrom.getText().toString(), budgetDateTo.getText().toString())) {
                        //Get user inputs
                        budget.setDate_from(budgetDateFrom.getText().toString());
                        budget.setDate_to(budgetDateTo.getText().toString());
                        budget.setAmount(Double.parseDouble(budgetAmount.getText().toString()));

                        new DatabaseHelper().addBudget(budget, new DatabaseHelper.BudgetDataStatus() {

                            @Override
                            public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys, ArrayList<String> budgetNo) {

                            }

                            @Override
                            public void DataIsInserted() {
                                Snackbar.make(view, "Budget successfully added", Snackbar.LENGTH_SHORT).show();
                                Timer timer = new Timer();

                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        BudgetFormActivity.this.finish();
                                    }
                                }, 1000);

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {

                            }
                        });
                    }
                    else {
                        Snackbar.make(view, "Please check entered date values", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    protected  void openMaterialDatePicker(View view){

        //Instantiate Material Date Picker
        MaterialDatePicker.Builder<Long> materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();

        //Assign a title
        materialDatePickerBuilder.setTitleText("Pickup a date");

        //
        final MaterialDatePicker<Long> materialDatePicker = materialDatePickerBuilder.build();

        materialDatePicker.show(getSupportFragmentManager(), "Material_Date_Picker");

        //Set Value to EditText
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(selection);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String date = format.format(calendar.getTime());
                if(view.getId()==R.id.editTextDateFrom){
                    budgetDateFrom.setText(date);
                }
                else if(view.getId()==R.id.editTextDateTo){
                    budgetDateTo.setText(date);
                }
            }
        });

    }


    protected boolean compareDate(String dateFromString, String dateToString) {
        try {
            Date dateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(dateFromString);
            @SuppressLint("SimpleDateFormat") Date dateTo = new SimpleDateFormat("dd/MM/yyyy").parse(dateToString);

            if(dateTo!=null&&dateFrom!=null&&dateTo.compareTo(dateFrom)>0)
                return true;
            else return false;
        }
        catch (ParseException e){
            Log.i("Execption", String.valueOf(e));
            return false;
        }
    }
}