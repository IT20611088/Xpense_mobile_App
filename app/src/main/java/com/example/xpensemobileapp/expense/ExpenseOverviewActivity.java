package com.example.xpensemobileapp.expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpensemobileapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ExpenseOverviewActivity extends AppCompatActivity {

    private String expenseID;
    private DatabaseReference dbRef;

    private TextView amount;
    private TextView payee;
    private TextView method;
    private TextView date;
    private TextView currency;
    private TextView category;
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_overview);


        this.expenseID = getIntent().getExtras().getString("id");

        this.amount = findViewById(R.id.overviewAmountLabelValue);
        this.amount.setEnabled(false);

        this.category = findViewById(R.id.overviewCategoryLabelValue);
        this.payee = findViewById(R.id.overviewPayeeLabelValue);
        this.method = findViewById(R.id.overviewMethodLabelValue);
        this.date = findViewById(R.id.overviewDateLabelValue);
        this.currency = findViewById(R.id.overviewCurrencyLabelValue);
        this.description= findViewById(R.id.overviewDescriptionLabelValue);


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
                        method.setText(expense.getMethod());
                        date.setText(expense.getDate());
                        currency.setText(expense.getCurrency());
                        category.setText(expense.getCategory());
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


    public void onClickDeleteBtn(View view){
        //ImageButton deleteBtn = findViewById(R.id.expenseOverviewDeleteBtn);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    new FirebaseDatabaseHelper().deleteExpense(expenseID, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {
                            Toast.makeText(getApplicationContext(), "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
//                switch (which){
//                    case DialogInterface.BUTTON_POSITIVE:
//                        //Yes button clicked
//                        break;
//
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        //No button clicked
//                        break;
//                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseOverviewActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void onClickEditBtn(View view){
        Intent intent = new Intent(ExpenseOverviewActivity.this, EditExpenseActivity.class);
        intent.putExtra("id", this.expenseID);

        startActivity(intent);

    }

}