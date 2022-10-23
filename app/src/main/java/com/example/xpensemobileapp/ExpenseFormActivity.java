package com.example.xpensemobileapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExpenseFormActivity<FragmentContainerView> extends AppCompatActivity {
    private final Calendar myCalendar= Calendar.getInstance();

    //declare the spinner object for payment method
    private Spinner paymentMethod;

    //declare the spinner object for currency type
    private Spinner currencyType;

    //declare the spinner object for category type
    private Spinner categoryType;

    //declare objects for the rest of the inputs
    private EditText amount;
    private EditText date;
    private EditText payee;
    private EditText description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_form);

        this.amount = findViewById(R.id.amountValue);
        this.payee = findViewById(R.id.payeeValue);
        this.description = findViewById(R.id.descriptionValue);



        this.paymentMethod = findViewById(R.id.methodValue);
       //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.paymentTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        paymentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        this.paymentMethod.setAdapter(paymentAdapter);


        /*********************************************************************************************/

        this.currencyType = findViewById(R.id.currencyValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.currencyTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        currencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        this.currencyType.setAdapter(currencyAdapter);

        /*********************************************************************************************/

        this.categoryType = findViewById(R.id.categoryValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.categoryTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        this.categoryType.setAdapter(categoryAdapter);


        /*********************************************************************************************/


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
                new DatePickerDialog(ExpenseFormActivity.this, date,
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

    public void addExpense(View view) {
        //get the values of the input fields

        String amountTxt = this.amount.getText().toString();
        //double amountVal = Double.parseDouble(amountTxt);

        //this.currencyType = findViewById(R.id.currencyValue);
        String currencyTxt = this.currencyType.getSelectedItem().toString();

        //this.paymentMethod = findViewById(R.id.methodValue);
        String methodTxt = this.paymentMethod.getSelectedItem().toString();

       //this.date = findViewById(R.id.dateValue);
       String dateTxt = this.date.getText().toString();
       // Date dateVal = new SimpleDateFormat("dd/MM/yyyy").parse(dateTxt);

        //this.payee = findViewById(R.id.payeeValue);
        String payeeTxt = this.payee.getText().toString();

        //this.categoryType = findViewById(R.id.categoryValue);
        String categoryTxt = this.categoryType.getSelectedItem().toString();

        //this.description = findViewById(R.id.descriptionValue);
        String descriptionTxt = description.getText().toString();

       // checking whether any of the above fields are empty
        if(amountTxt.matches("") || dateTxt.matches("") || payeeTxt.matches("") || descriptionTxt.matches("")){
            String infomsg = "Please fill all the empty fields";

            Toast.makeText(this, infomsg, Toast.LENGTH_SHORT).show();
        }

        else{
            //
        }

    }


    public void next(View view){
        Intent intent = new Intent(ExpenseFormActivity.this, ExpenseReportActivity.class);
        startActivity(intent);
    }

}