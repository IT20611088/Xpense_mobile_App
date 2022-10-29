package com.example.xpensemobileapp.income;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xpensemobileapp.R;
import com.example.xpensemobileapp.expense.EditExpenseActivity;
import com.example.xpensemobileapp.expense.ExpenseForm;
import com.example.xpensemobileapp.expense.FirebaseDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditIncomeActivity extends AppCompatActivity {

    private String expenseID;
    private DatabaseReference dbRef;

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
        setContentView(R.layout.activity_edit_income);

        this.expenseID = getIntent().getExtras().getString("id");

        this.amount = findViewById(R.id.editAmountValue);
        this.payee = findViewById(R.id.editPayeeValue);
        this.description = findViewById(R.id.editDescriptionValue);

        //if()
        //Log.i("intentVal", getIntent().getExtras().getString("id"));

        //TextView amountLbl = findViewById(R.id.amountLabel);

        this.paymentMethod = findViewById(R.id.editMethodValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.paymentTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        paymentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        this.paymentMethod.setAdapter(paymentAdapter);


        /*********************************************************************************************/

        this.currencyType = findViewById(R.id.editCurrencyValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.currencyTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        currencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        this.currencyType.setAdapter(currencyAdapter);

        /*********************************************************************************************/

        this.categoryType = findViewById(R.id.editCategoryValue);
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
        this.date = findViewById(R.id.editDateValue);

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
                new DatePickerDialog(EditIncomeActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        getExpenseData();
    }


    private void getExpenseData(){
        this.dbRef = FirebaseDatabase.getInstance().getReference("expenses");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                    ExpenseForm expense = childSnapshot.getValue(ExpenseForm.class);

                    if(expenseID.equals(childSnapshot.getKey())){
                        amount.setText(expense.getAmount());
                        payee.setText(expense.getPayee());
                        //method.setText(expense.getMethod());
                        date.setText(expense.getDate());
                        //currency.setText(expense.getCurrency());
                        //category.setText(expense.getCategory());
                        description.setText(expense.getDescription());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // method for updating the date value in dd/MM/yyyy format
    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.UK);
        this.date.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void onClickSaveBtn(View view){
        //get the values of the input fields
        String amountTxt = this.amount.getText().toString();
        String currencyTxt = this.currencyType.getSelectedItem().toString();
        String methodTxt = this.paymentMethod.getSelectedItem().toString();
        String dateTxt = this.date.getText().toString();
        String payeeTxt = this.payee.getText().toString();
        String categoryTxt = this.categoryType.getSelectedItem().toString();
        String descriptionTxt = description.getText().toString();

        // checking whether any of the above fields are empty
        if(amountTxt.matches("") || dateTxt.matches("") || payeeTxt.matches("") || descriptionTxt.matches("")){
            String infomsg = "Please fill all the empty fields";

            Toast.makeText(this, infomsg, Toast.LENGTH_SHORT).show();
        }

        else{
            ExpenseForm expense = new ExpenseForm(amountTxt, currencyTxt, methodTxt, dateTxt, payeeTxt,
                    categoryTxt, descriptionTxt);

            new com.example.xpensemobileapp.expense.FirebaseDatabaseHelper().updateExpense(this.expenseID, expense, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {

                }

                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {
                    Toast.makeText(getApplicationContext(), "Expense updated successfully", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void DataIsDeleted() {

                }
            });
        }
    }
}