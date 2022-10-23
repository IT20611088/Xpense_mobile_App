package com.example.xpensemobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BudgetReportActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_report);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewBudget);

        new DatabaseHelper().readBudget(new DatabaseHelper.BudgetDataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Budget> budgetArray) {
               new RecyclerViewAdapter().setConfig(recyclerView, BudgetReportActivity.this, budgetArray);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsLoaded() {

            }
        });


        FloatingActionButton fab;
        fab = findViewById(R.id.btnAddBudgetForm);
        fab.setOnClickListener(this::addBudget);
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