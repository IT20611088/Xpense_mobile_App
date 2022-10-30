package com.example.xpensemobileapp.income;

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

public class IncomeOverviewActivity extends AppCompatActivity {

    private String incomeID;
    private String incomeNo;
    private DatabaseReference dbRef;

    private TextView overviewTitle;

    private TextView amount;
    private TextView payer;

    private TextView date;
    private TextView currency;

    private TextView description;

    private ImageButton downloadBtn;
    private ConstraintLayout myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_overview);

        setTitle("Income overview");


        this.incomeID = getIntent().getExtras().getString("id");
        this.incomeNo = getIntent().getExtras().getString("incomeNo");

        this.overviewTitle = findViewById(R.id.expenseOverviewTitle);

        this.amount = findViewById(R.id.overviewAmountLabelValue);
        this.amount.setEnabled(false);

        this.overviewTitle.setText(this.incomeNo);


        this.payer = findViewById(R.id.overviewPayeeLabelValue);

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
        getIncomeData();
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
        String filename = "incomeOverview.jpg";

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
                        payer.setText(income.getPayee());

                        date.setText(income.getDate());
                        currency.setText(income.getCurrency());

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


    public void onClickDeleteBtn(View view){
        //ImageButton deleteBtn = findViewById(R.id.incomeOverviewDeleteBtn);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE){
                    new FirebaseDatabaseHelper().deleteIncome(incomeID, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<IncomeForm> incomes, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {
                            Toast.makeText(getApplicationContext(), "Income deleted successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), IncomeDashboardActivity.class);
                            startActivity(intent);
                        }
                    });
                }
//
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(IncomeOverviewActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    public void onClickEditBtn(View view){
        Intent intent = new Intent(IncomeOverviewActivity.this, EditIncomeActivity.class);
        intent.putExtra("id", this.incomeID);

        startActivity(intent);

    }

}