package com.example.xpensemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BudgetFormActivity extends AppCompatActivity {
    DatabaseReference dbRef;
    EditText budgetDateFrom, budgetDateTo, budgetAmount;
    Button btnAddBudget;

    Budget budget;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_form);

        //Edit Text Fields
        budgetDateFrom = findViewById(R.id.editTextDateFrom);
        budgetDateTo = findViewById(R.id.editTextDateTo);
        budgetAmount = findViewById(R.id.editTextAmount);

        //Buttons
        btnAddBudget = (Button) findViewById(R.id.btnAddBudget);

        //Budget object
        budget = new Budget();

        btnAddBudget.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                dbRef = FirebaseDatabase.getInstance().getReference().child("Budget");

                try{
                    if(TextUtils.isEmpty(budgetDateFrom.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please Enter a date", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(budgetDateTo.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please Enter a date", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(budgetAmount.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please Enter a amount", Toast.LENGTH_SHORT).show();
                    else{
                        //Get user inputs
                        budget.setDate_from(budgetDateFrom.getText().toString().trim());
                        budget.setDate_to(budgetDateTo.getText().toString().trim());
                        budget.setAmount(Double.parseDouble(budgetAmount.getText().toString().trim()));

                        //Insert to database
                        dbRef.push().setValue(budget);
                        Log.i("Insertion", "Successfully Inserted");
                        Toast.makeText(getApplicationContext(),"Budget saved successfully", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.e("My exception", String.valueOf(e));
                }
            }
        });


        Intent intent = getIntent();
        //String rowId = intent.getStringExtra(BudgetReportActivity.row_id);

       // Log.d("Row ID", String.valueOf(rowId));

        //TableRow tr = (TableRow) findViewById(Integer.valueOf(rowId));
        //if(tr.getStateDescription())

    }
}