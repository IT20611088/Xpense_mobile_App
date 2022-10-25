package com.example.xpensemobileapp.budget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpensemobileapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class BudgetReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_report);

        recyclerView = findViewById(R.id.recyclerViewBudget);
        fab = findViewById(R.id.btnAddBudgetForm);
        fab.setOnClickListener(this::addBudget);

        new DatabaseHelper().readBudget(new DatabaseHelper.BudgetDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys) {
                Log.i("Is Empty", String.valueOf(budgetArrayList.isEmpty()));
               if(budgetArrayList.isEmpty() == false){
                   new RecyclerViewAdapter().initRecyclerView(recyclerView, BudgetReportActivity.this, budgetArrayList, keys);
               }
               else{
                   Snackbar.make(findViewById(android.R.id.content), "Budget data is empty.", Snackbar.LENGTH_SHORT).show();
               }
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

    };

    //Direct to add budget
    protected void addBudget(View view) {
        Intent intent = new Intent(BudgetReportActivity.this, BudgetFormActivity.class);
        startActivity(intent);
    }
}