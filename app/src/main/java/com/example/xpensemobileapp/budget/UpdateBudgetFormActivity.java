package com.example.xpensemobileapp.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xpensemobileapp.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class UpdateBudgetFormActivity extends AppCompatActivity {

    EditText budgetDateFrom, budgetDateTo, budgetAmount;

    FloatingActionButton btnDeleteBudget, btnUpdateBudget;
    private String key;
    private Budget budget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_budget_form);

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

        //Set on click listener
        btnUpdateBudget.setOnClickListener(this::updateBudget);
        btnDeleteBudget.setOnClickListener(this::deleteBudget);
        budgetDateFrom.setOnClickListener(this::openMaterialDatePicker);
        budgetDateTo.setOnClickListener(this::openMaterialDatePicker);
    };

    //Update function
    protected void updateBudget(View view){

        //Set user inputs to a Budget object
        budget = new Budget();
        budget.setDate_from(budgetDateFrom.getText().toString());
        budget.setDate_to(budgetDateTo.getText().toString());
        budget.setAmount(Double.parseDouble(budgetAmount.getText().toString()));


        //Alert Dialog box
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(view.getContext());

        materialAlertDialogBuilder
                .setTitle("Update Budget")
                .setMessage("Confirm Update ?")
                .setPositiveButton("Update", new DialogInterface.OnClickListener(){
                    @Override
                    public void  onClick(DialogInterface dialog, int i){
                        new DatabaseHelper().updateBudget(budget, key, new DatabaseHelper.BudgetDataStatus() {
                            @Override
                            public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys) {

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
                        });
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


    };

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
                            public void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys) {

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
                                //finish();

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



    };

    //View material Date Picker
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