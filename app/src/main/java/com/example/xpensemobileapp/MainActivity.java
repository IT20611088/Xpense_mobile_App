package com.example.xpensemobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnBudgetTool);
        button.setOnClickListener(this::budgetTool);
    }

    protected void budgetTool(View view){
        Intent intent = new Intent(MainActivity.this, BudgetReportActivity.class);
        startActivity(intent);
    }
}