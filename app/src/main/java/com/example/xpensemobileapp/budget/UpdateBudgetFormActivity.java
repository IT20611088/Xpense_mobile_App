package com.example.xpensemobileapp.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.xpensemobileapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateBudgetFormActivity extends AppCompatActivity {

    EditText budgetDateFrom, budgetDateTo, budgetAmount;

    FloatingActionButton btnDeleteBudget, btnUpdateBudget;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget_form);

        setTitle("Budget");

        //Edit text fields
        budgetDateFrom = findViewById(R.id.editTextDateFrom);
        budgetDateTo = findViewById(R.id.editTextDateTo);
        budgetAmount = findViewById(R.id.editTextAmount);
        //Buttons
        btnUpdateBudget = findViewById(R.id.btnUpdateBudget);
        btnDeleteBudget = findViewById(R.id.btnDeleteBudget);


        //Retrieve data from intent
        key = getIntent().getExtras().getString("key");
        budgetDateFrom.setText(getIntent().getExtras().getString("dateFrom"));
        budgetDateTo.setText(getIntent().getExtras().getString("dateTo"));
        budgetAmount.setText(getIntent().getExtras().getString("amount"));

        budgetDateFrom.setEnabled(false);
        budgetDateTo.setEnabled(false);

        //Set on click listener
        btnUpdateBudget.setOnClickListener(this::updateBudget);
        btnDeleteBudget.setOnClickListener(this::deleteBudget);
    }

    //Update function
    protected void updateBudget(View view){
        //Alert Dialog box
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(view.getContext());

        materialAlertDialogBuilder
                .setTitle("Update Budget")
                .setMessage("Confirm Update ?")
                .setPositiveButton("Update", new DialogInterface.OnClickListener(){
                    @Override
                    public void  onClick(DialogInterface dialog, int i){

                        if(TextUtils.isEmpty(budgetAmount.getText().toString()))
                            Snackbar.make(view, "Please Enter a amount", Snackbar.LENGTH_SHORT).show();
                        else{
                                double amount = Double.parseDouble(budgetAmount.getText().toString());

                                new DatabaseHelper().updateBudget(amount, key, new DatabaseHelper.BudgetDataStatus() {
                                    @Override
                                    public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys, ArrayList<String> budgetNo) {

                                    }

                                    @Override
                                    public void DataIsInserted() {

                                    }

                                    @Override
                                    public void DataIsUpdated() {
                                        Snackbar.make(view, "Budget has been successfully updated", Snackbar.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void DataIsDeleted() {

                                    }

                                    @Override
                                    public void DisplayAlerts() {

                                    }
                                });
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void  onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
        //Show dialog box
        materialAlertDialogBuilder.show();
    }

    //Delete function
    protected void deleteBudget(View view){

        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(view.getContext());

        materialAlertDialogBuilder
                .setTitle("Delete Budget")
                .setMessage("Are you sure ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void  onClick(DialogInterface dialog, int i){

                        new DatabaseHelper().deleteBudget(key, new DatabaseHelper.BudgetDataStatus() {
                            @Override
                            public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys, ArrayList<String> budgetNo) {

                            }

                            @Override
                            public void DataIsInserted() {

                            }

                            @Override
                            public void DataIsUpdated() {

                            }

                            @Override
                            public void DataIsDeleted() {
                                Snackbar.make(view, "Budget has been successfully deleted", Snackbar.LENGTH_SHORT).show();
                                Timer timer = new Timer();

                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        UpdateBudgetFormActivity.this.finish();
                                    }
                                }, 800);


                            }

                            @Override
                            public void DisplayAlerts() {

                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    @Override
                    public void  onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });

        //Show dialog box
        materialAlertDialogBuilder.show();
    }

}