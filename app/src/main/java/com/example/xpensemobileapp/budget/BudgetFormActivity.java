package com.example.xpensemobileapp.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xpensemobileapp.R;
import com.example.xpensemobileapp.budget.Budget;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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

                dbRef = FirebaseDatabase.getInstance().getReference().child("Budget");

                if(TextUtils.isEmpty(budgetDateFrom.getText().toString()))
                    Snackbar.make(view, "Please Enter a date", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(budgetDateTo.getText().toString()))
                    Snackbar.make(view, "Please Enter a date", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(budgetAmount.getText().toString()))
                    Snackbar.make(view, "Please Enter a amount", Snackbar.LENGTH_SHORT).show();
                else{
                    //Get user inputs
                    budget.setDate_from(budgetDateFrom.getText().toString());
                    budget.setDate_to(budgetDateTo.getText().toString());
                    budget.setAmount(Double.parseDouble(budgetAmount.getText().toString()));

                    new DatabaseHelper().addBudget(budget, new DatabaseHelper.BudgetDataStatus(){

                        @Override
                        public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {
                            Snackbar.make(view, "Budget successfully added", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            }
        });
    }

    protected  void openMaterialDatePicker(View view){

        //Instantiate Material Date Picker
        MaterialDatePicker.Builder  materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();

        //Assign a title
        materialDatePickerBuilder.setTitleText("Pickup a date");

        //
        final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

        materialDatePicker.show(getSupportFragmentManager(), "Material_Date_Picker");

        //Set Value to EditText
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                if(view.getId()==R.id.editTextDateFrom){
                    budgetDateFrom.setText(materialDatePicker.getHeaderText());
                }
                else if(view.getId()==R.id.editTextDateTo){
                    budgetDateTo.setText(materialDatePicker.getHeaderText());
                }
            }
        });

    }
}