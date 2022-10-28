package com.example.xpensemobileapp.expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpensemobileapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExpenseOverviewActivity extends AppCompatActivity {

    private String expenseID;
    private String expenseNo;
    private DatabaseReference dbRef;

    private TextView overviewTitle;

    private TextView amount;
    private TextView payee;
    private TextView method;
    private TextView date;
    private TextView currency;
    private TextView category;
    private TextView description;

    private ImageButton downloadBtn;
    private ConstraintLayout myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_overview);


        this.expenseID = getIntent().getExtras().getString("id");
        this.expenseNo = getIntent().getExtras().getString("expenseNo");

        this.overviewTitle = findViewById(R.id.expenseOverviewTitle);

        this.amount = findViewById(R.id.overviewAmountLabelValue);
        this.amount.setEnabled(false);

        this.overviewTitle.setText("Expense Overview - " + this.expenseNo);

        this.category = findViewById(R.id.overviewCategoryLabelValue);
        this.payee = findViewById(R.id.overviewPayeeLabelValue);
        this.method = findViewById(R.id.overviewMethodLabelValue);
        this.date = findViewById(R.id.overviewDateLabelValue);
        this.currency = findViewById(R.id.overviewCurrencyLabelValue);
        this.description= findViewById(R.id.overviewDescriptionLabelValue);

        this.downloadBtn = findViewById(R.id.expenseOverviewDownloadBtn);
        this.myLayout = findViewById(R.id.myLayout);

        this.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOverview();
            }
        });
        getExpenseData();
    }

    private void saveOverview(){
        this.myLayout.setDrawingCacheEnabled(true);
        this.myLayout.buildDrawingCache();
        this.myLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        Bitmap bitmap = this.myLayout.getDrawingCache();
        
        save(bitmap);
    }

    private void save(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(root+"/Download");
        String filename = "expense overview.jpg";

        File myFile = new File(file, filename);

        if(myFile.exists()){
            myFile.delete();
        }

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Snackbar.make(findViewById(R.id.myLayout), "Download successful", Snackbar.LENGTH_SHORT).show();

            this.myLayout.setDrawingCacheEnabled(false);

        } catch(Exception e){
            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void getExpenseData(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.dbRef = FirebaseDatabase.getInstance().getReference("user_expenses").child(userId);

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

                            Intent intent = new Intent(getApplicationContext(), ExpensesDashboardActivity.class);
                            startActivity(intent);
                        }
                    });
                }
//
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