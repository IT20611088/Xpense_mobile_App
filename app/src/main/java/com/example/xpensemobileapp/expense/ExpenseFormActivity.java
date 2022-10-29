package com.example.xpensemobileapp.expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.xpensemobileapp.budget.Budget;
import com.example.xpensemobileapp.budget.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

        //if()
        //Log.i("intentVal", getIntent().getExtras().getString("id"));

        //TextView amountLbl = findViewById(R.id.amountLabel);

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

    public void addExpense(View view) throws ParseException {


        //get the values of the input fields
        String amountTxt = this.amount.getText().toString();
        String currencyTxt = this.currencyType.getSelectedItem().toString();
        String methodTxt = this.paymentMethod.getSelectedItem().toString();
        String dateTxt = this.date.getText().toString();
        String payeeTxt = this.payee.getText().toString();
        String categoryTxt = this.categoryType.getSelectedItem().toString();
        String descriptionTxt = description.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateCurrent = new Date();




        // checking whether any of the above fields are empty
        if(amountTxt.matches("")){
            Snackbar.make(view, "Please input a value for amount", Snackbar.LENGTH_SHORT).show();
        }

        else if(dateTxt.matches("")){
            Snackbar.make(view, "Please input a value for date", Snackbar.LENGTH_SHORT).show();
        }
        else if(payeeTxt.matches("")){
            Snackbar.make(view, "Please input a value for payee", Snackbar.LENGTH_SHORT).show();
        }

        else if(descriptionTxt.matches("")){
            Snackbar.make(view, "Please input a value for description", Snackbar.LENGTH_SHORT).show();
        }

        else{

            Date date=formatter.parse(dateTxt);
            //Check if the entered date is a future date
            if(date.after(dateCurrent))
                Snackbar.make(view, "Date cannot be a future date", Snackbar.LENGTH_SHORT).show();

            else{

                //fetching details of the budget for the user if exists
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("user_budgets");
                dbRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //Log.i("----------------------------------------", String.valueOf(snapshot.hasChildren()));
                        boolean foundBudget = false;
                        Date dateFrom = null;
                        Date dateTo = null;
                        double budgetAmount = 0;
                        String budgetKey = "";
                        double enteredAmount = Double.parseDouble(amount.getText().toString());

                        if (snapshot.hasChildren()){
                            for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                try {
                                     dateFrom = formatter.parse(childSnapshot.child("date_from").getValue().toString());
                                     dateTo = formatter.parse(childSnapshot.child("date_to").getValue().toString());
                                } catch(ParseException e){
                                    //Log.i("Parse Exception===", e.toString());
                                }

                                if( (date.compareTo(dateFrom)==0  || date.after(dateFrom) ) && ( date.compareTo(dateTo)==0 || date.before(dateTo) ) ) {
                                    foundBudget = true;
                                    budgetKey = childSnapshot.getKey();
                                    budgetAmount = Double.parseDouble(childSnapshot.child("amount").getValue().toString());
                                    dbRef.removeEventListener(this);//break;
                                }

                                else
                                    continue;
                            }

                            Log.i("found Budget", String.valueOf(foundBudget));
                            if(foundBudget == true){
                                if(enteredAmount > budgetAmount){
                                    double finalBudgetAmount = budgetAmount;
                                    String finalBudgetKey = budgetKey;
                                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(which == DialogInterface.BUTTON_POSITIVE){
                                                ExpenseForm expense = new ExpenseForm(amountTxt, currencyTxt, methodTxt, dateTxt, payeeTxt, categoryTxt, descriptionTxt);

                                                new FirebaseDatabaseHelper().addExpense(expense, new FirebaseDatabaseHelper.DataStatus() {
                                                    @Override
                                                    public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {

                                                    }

                                                    @Override
                                                    public void DataIsInserted() {
                                                        Snackbar.make(view, "Expense added successfully", Snackbar.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void DataIsUpdated() {

                                                    }

                                                    @Override
                                                    public void DataIsDeleted() {

                                                    }
                                                });

                                                dbRef.child(userId).child(finalBudgetKey).child("amount").setValue(finalBudgetAmount -enteredAmount);
                                            }

                                        }
                                    };

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseFormActivity.this);

                                        builder.setMessage("Are you sure you want to exceed you budget of LKR " +
                                                        budgetAmount + " between " +
                                                        formatter.format(dateFrom) + " and " + formatter.format(dateTo) + "?").setPositiveButton("Yes", dialogClickListener)
                                                .setNegativeButton("No", dialogClickListener).show();
                                }

                                else{
                                    //update budget and add expense for entered amounts less than budget amount value
                                    double finalBudgetAmount = budgetAmount;
                                    String finalBudgetKey = budgetKey;

                                    ExpenseForm expense = new ExpenseForm(amountTxt, currencyTxt, methodTxt, dateTxt, payeeTxt, categoryTxt, descriptionTxt);

                                    new FirebaseDatabaseHelper().addExpense(expense, new FirebaseDatabaseHelper.DataStatus() {
                                        @Override
                                        public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {

                                        }

                                        @Override
                                        public void DataIsInserted() {
                                            Snackbar.make(view, "Expense added successfully", Snackbar.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void DataIsUpdated() {

                                        }

                                        @Override
                                        public void DataIsDeleted() {

                                        }
                                    });

                                    dbRef.child(userId).child(finalBudgetKey).child("amount").setValue(finalBudgetAmount -enteredAmount);
                                }
                            }

                            else{
                                ExpenseForm expense = new ExpenseForm(amountTxt, currencyTxt, methodTxt, dateTxt, payeeTxt, categoryTxt, descriptionTxt);

                                new FirebaseDatabaseHelper().addExpense(expense, new FirebaseDatabaseHelper.DataStatus() {
                                    @Override
                                    public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {

                                    }

                                    @Override
                                    public void DataIsInserted() {
                                        Snackbar.make(view, "Expense added successfully", Snackbar.LENGTH_SHORT).show();
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

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



//                //create new object of ExpenseForm class
//                ExpenseForm expense = new ExpenseForm(amountTxt, currencyTxt, methodTxt, dateTxt, payeeTxt,
//                        categoryTxt, descriptionTxt);
//
//                new FirebaseDatabaseHelper().addExpense(expense, new FirebaseDatabaseHelper.DataStatus() {
//                    @Override
//                    public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {
//
//                    }
//
//                    @Override
//                    public void DataIsInserted() {
//                        Snackbar.make(view, "Expense added successfully", Snackbar.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void DataIsUpdated() {
//
//                    }
//
//                    @Override
//                    public void DataIsDeleted() {
//
//                    }
//                });
            }

        }

    }


    public void next(View view){
        Intent intent = new Intent(ExpenseFormActivity.this, ExpenseReportActivity.class);
        startActivity(intent);
    }

}