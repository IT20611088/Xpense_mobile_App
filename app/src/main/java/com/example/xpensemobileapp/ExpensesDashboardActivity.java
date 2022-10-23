package com.example.xpensemobileapp;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExpensesDashboardActivity extends AppCompatActivity {

    private ArrayList<String> expenseNo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_dashboard);

        initializeContent();

    }

    private void initializeContent(){
        for(int i = 0; i < 15; i++){
            expenseNo.add("ex_00" + (i+1));
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(expenseNo,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickPlusBtn(View view){
        Intent intent = new Intent(ExpensesDashboardActivity.this, ExpenseFormActivity.class);

        startActivity(intent);
    }

   public void onClickArrow(View view){
        Intent intent = new Intent(ExpensesDashboardActivity.this, ExpenseOverviewActivity.class);

        startActivity(intent);
    }
}