package com.example.xpensemobileapp.income;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.xpensemobileapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.xpensemobileapp.R;

public class IncomeDashboardActivity extends AppCompatActivity {

    private ArrayList<String> incomeNo = new ArrayList<String>();
    private ArrayList<String> incomeDate = new ArrayList<>();
    private ArrayList<String> incomeID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_dashboard);
        setTitle("Incomes");
        initializeContent();

    }


    private void initializeContent(){
        new FirebaseDatabaseHelper().readIncomes(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<IncomeForm> incomes, List<String> keys) {


                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(GONE);


                for(IncomeForm object : incomes){
                    incomeNo.add(object.getDescription());
                    incomeDate.add(object.getDate());
                }

                for(String key : keys){
                    // Log.i("key", key);
                    incomeID.add(key);
                }


                initRecyclerView();
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(incomeNo, incomeDate, incomeID, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onClickPlusBtn(View view){
        Intent intent = new Intent(IncomeDashboardActivity.this, IncomeFormActivity.class);

        startActivity(intent);
    }


}