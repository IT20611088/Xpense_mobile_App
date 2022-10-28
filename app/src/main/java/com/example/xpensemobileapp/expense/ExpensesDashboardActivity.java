package com.example.xpensemobileapp.expense;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.xpensemobileapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ExpensesDashboardActivity extends AppCompatActivity {

    private ArrayList<String> expenseNo = new ArrayList<String>();
    private ArrayList<String> expenseDate = new ArrayList<>();
    private ArrayList<String> expenseID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_dashboard);

          initializeContent();

    }


    private void initializeContent(){
        new FirebaseDatabaseHelper().readExpenses(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {
                //Log.i("length", String.valueOf(expenses.size()));

                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(GONE);

                for(int i = 0; i < expenses.size(); i++){
                    if(i < 10)
                        expenseNo.add("ex_0" + (i+1));

                    else
                        expenseNo.add("ex_" + (i+1));
                }

                for(ExpenseForm object : expenses){
                    expenseDate.add(object.getDate());
                }

                for(String key : keys){
                   // Log.i("key", key);
                    expenseID.add(key);
                }


                initRecyclerView();
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

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(expenseNo, expenseDate, expenseID, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickPlusBtn(View view){
        Intent intent = new Intent(ExpensesDashboardActivity.this, ExpenseFormActivity.class);

        startActivity(intent);
    }


}