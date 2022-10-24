package com.example.xpensemobileapp.budget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xpensemobileapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateBudgetFormActivity extends AppCompatActivity {

    EditText budgetDateFrom, budgetDateTo, budgetAmount;
    Button btnUpdateBudget;
    FloatingActionButton deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget_form);

//        FloatingActionButton button = (Button)findViewById(R.id.btnUpdateBudget);
//        button.setOnClickListener(this::upBudget);

        //Edit text fields
        budgetDateFrom = findViewById(R.id.editTextDateFrom);
        budgetDateTo = findViewById(R.id.editTextDateTo);
        budgetAmount = findViewById(R.id.editTextAmount);
        //Buttons
        btnUpdateBudget = findViewById(R.id.btnUpdateBudget);
        deleteBtn = findViewById(R.id.btnDeleteBudget);
        deleteBtn.setOnClickListener(this::deleteBudget);

        if(getIntent().getExtras()!=null){
            budgetDateFrom.setText(getIntent().getExtras().getString("dateFrom"));
            budgetDateTo.setText(getIntent().getExtras().getString("dateTo"));
            budgetAmount.setText(getIntent().getExtras().getString("amount"));
            Log.i("sikhsoigfhoiahoigkhoiajhgoijhaiog", getIntent().getExtras().toString());
        }
        else{
            Log.i("sikhsoigfhoiahoigkhoiajhgoijhaiog", "NULLLLLLLLLLLLLLLLLLLLLLLLLL");
        }



        //Budget Object
        //budget = new Budget();

    }


    protected void deleteBudget(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(UpdateBudgetFormActivity.this);

        alert.setCancelable(true);
        alert.setTitle("Delete Budget");
        alert.setMessage("Are you sure ?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
            @Override
            public void  onClick(DialogInterface dialog, int i){
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void  onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });

        alert.show();

    }
}