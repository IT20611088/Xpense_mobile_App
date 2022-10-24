package com.example.xpensemobileapp.budget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpensemobileapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
               new RecyclerViewAdapter().initRecyclerView(recyclerView, BudgetReportActivity.this, budgetArrayList, keys);
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

    }



    protected void addBudget(View view) {
        Intent intent = new Intent(BudgetReportActivity.this, BudgetFormActivity.class);
        startActivity(intent);
    }

//    protected void updateBudget(View view) {
//
//        Intent intent = new Intent(BudgetReportActivity.this, UpdateBudgetFormActivity.class);
//        TableRow tableRow = new TableRow(this);
//        int rowId = tableRow.getId();
//        intent.putExtra(row_id, rowId);
//
//
//        startActivity(intent);
//
//
//    }
}