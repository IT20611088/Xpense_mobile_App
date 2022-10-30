package com.example.xpensemobileapp.income;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpensemobileapp.R;
import com.example.xpensemobileapp.income.FirebaseDatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class IncomeFormActivity<FragmentContainerView> extends AppCompatActivity {
    private final Calendar myCalendar= Calendar.getInstance();


    //declare the spinner object for currency type
    private Spinner currencyType;


    //declare objects for the rest of the inputs
    private EditText amount;
    private EditText date;
    private EditText payer;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_form);
        setTitle("Add Income");

        this.amount = findViewById(R.id.amountValue);
        this.payer = findViewById(R.id.payerValue);
        this.description = findViewById(R.id.descriptionValue);



        this.currencyType = findViewById(R.id.currencyValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.currencyTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        currencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        this.currencyType.setAdapter(currencyAdapter);




        //for the date value
        //reference - https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext?page=1&tab=scoredesc#tab-top
        this.date = findViewById(R.id.dateValue);

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        this.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(IncomeFormActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    // method for updating the date value in dd/MM/yyyy format
    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.UK);
        this.date.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void addIncome(View view) throws ParseException {
        //get the values of the input fields
        String amountTxt = this.amount.getText().toString();
        String currencyTxt = this.currencyType.getSelectedItem().toString();
        //String methodTxt = this.paymentMethod.getSelectedItem().toString();
        String dateTxt = this.date.getText().toString();
        String payerTxt = this.payer.getText().toString();
        //String categoryTxt = this.categoryType.getSelectedItem().toString();
        String descriptionTxt = description.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateCurrent = new Date();

        Date date=formatter.parse(dateTxt);


        // checking whether any of the above fields are empty
        if(amountTxt.matches("")){
            Snackbar.make(view, "Please input a value for amount", Snackbar.LENGTH_SHORT).show();
        }

        else if(dateTxt.matches("")){
            Snackbar.make(view, "Please input a value for date", Snackbar.LENGTH_SHORT).show();
        }

        else if(date.after(dateCurrent)){
            Snackbar.make(view, "Date cannot be a future date", Snackbar.LENGTH_SHORT).show();
        }

        else if(payerTxt.matches("")){
            Snackbar.make(view, "Please input a value for payer", Snackbar.LENGTH_SHORT).show();
        }

        else if(descriptionTxt.matches("")){
            Snackbar.make(view, "Please input a value for description", Snackbar.LENGTH_SHORT).show();
        }

        else{
            //create new object of IncomeForm class
            IncomeForm income = new IncomeForm(amountTxt, currencyTxt, dateTxt, payerTxt, descriptionTxt);

            new FirebaseDatabaseHelper().addIncome(income, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<IncomeForm> incomes, List<String> keys) {

                }

                @Override
                public void DataIsInserted() {
                    Toast.makeText(IncomeFormActivity.this, "Income added successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), IncomeDashboardActivity.class);
                    startActivity(intent);
                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }
            });
        }

    }


    public void next(View view){
        Intent intent = new Intent(IncomeFormActivity.this, IncomeReportActivity.class);
        startActivity(intent);
    }

}