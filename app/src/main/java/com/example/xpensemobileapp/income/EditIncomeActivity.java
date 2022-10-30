package com.example.xpensemobileapp.income;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.xpensemobileapp.R;
import com.example.xpensemobileapp.income.EditIncomeActivity;
import com.example.xpensemobileapp.income.IncomeForm;
import com.example.xpensemobileapp.income.FirebaseDatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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

    private String incomeID;
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

        setTitle("Edit Income");

        this.incomeID = getIntent().getExtras().getString("id");

        this.amount = findViewById(R.id.editAmountValue);
        this.payee = findViewById(R.id.editPayeeValue);
        this.description = findViewById(R.id.editDescriptionValue);

        //if()
        //Log.i("intentVal", getIntent().getExtras().getString("id"));

        //TextView amountLbl = findViewById(R.id.amountLabel);

        //this.paymentMethod = findViewById(R.id.editMethodValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                //R.array.paymentTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
       // paymentAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
        //this.paymentMethod.setAdapter(paymentAdapter);


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

        //this.categoryType = findViewById(R.id.editCategoryValue);
        //create an ArrayAdapter using the string array and a default spinner layout
        //ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
               // R.array.categoryTypes, R.layout.spinner_item);

        //specify the layout to use when the list of choices appears
        //categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list);

        //apply the adapter to the spinner
       // this.categoryType.setAdapter(categoryAdapter);

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



        getIncomeData();
    }


    private void getIncomeData(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.dbRef = FirebaseDatabase.getInstance().getReference("user_incomes").child(userId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnapshot: snapshot.getChildren()) {

                    IncomeForm income = childSnapshot.getValue(IncomeForm.class);

                    if(incomeID.equals(childSnapshot.getKey())){
                        amount.setText(income.getAmount());
                        payee.setText(income.getPayee());
                        //method.setText(income.getMethod());
                        date.setText(income.getDate());
                        //currency.setText(income.getCurrency());
                        //category.setText(income.getCategory());
                        description.setText(income.getDescription());
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

        String dateTxt = this.date.getText().toString();
        String payeeTxt = this.payee.getText().toString();

        String descriptionTxt = description.getText().toString();

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
            IncomeForm income = new IncomeForm(amountTxt, currencyTxt, dateTxt, payeeTxt, descriptionTxt);

            new FirebaseDatabaseHelper().updateIncome(this.incomeID, income, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<IncomeForm> incomes, List<String> keys) {

                }

                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {

                    Toast.makeText(EditIncomeActivity.this, "Income updated successfully", Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(getApplicationContext(), IncomeOverviewActivity.class);
                    startActivity(intent);

                }

                @Override
                public void DataIsDeleted() {

                }
            });
        }
    }
}