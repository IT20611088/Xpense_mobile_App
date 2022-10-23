package com.example.xpensemobileapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseHelper {

    private DatabaseReference dbRef;
    private ArrayList<Budget> budgetArray = new ArrayList<>();
    private Budget budget;

    public interface BudgetDataStatus{
        void DataIsLoaded(ArrayList<Budget> budgetArrayList);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsLoaded();
    }

    public DatabaseHelper(){
        dbRef = FirebaseDatabase.getInstance().getReference().child("Budget");
    }


    protected void readBudget(final BudgetDataStatus budgetDataStatus) {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Log.i("Retrieved data", String.valueOf(ds.getKey()));
                        budget = new Budget();

                        budget.setDate_from(ds.child("date_to").getValue().toString());
                        budget.setDate_to(ds.child("date_to").getValue().toString());
                        budget.setAmount(Double.parseDouble(ds.child("amount").getValue().toString()));
                        budgetArray.add(budget);

                    }

                   budgetDataStatus.DataIsLoaded(budgetArray);

                } else {
                    //Toast.makeText(getApplicationContext(), "Budget record not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
