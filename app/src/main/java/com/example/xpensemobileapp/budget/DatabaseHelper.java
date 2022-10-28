package com.example.xpensemobileapp.budget;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseHelper {

    private DatabaseReference dbRef;
    private ArrayList<Budget> budgetArrayList = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> budgetNo = new ArrayList<>();

    public interface BudgetDataStatus{
        void DataIsLoaded(ArrayList<Budget> budgetArrayList,  ArrayList<String> keys, ArrayList<String> budgetNo);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public DatabaseHelper(){

        dbRef = FirebaseDatabase.getInstance().getReference().child("user_budgets");
    }

    //Get all budget records from database
    protected void readBudget(final BudgetDataStatus budgetDataStatus) {

        //User ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    budgetArrayList.clear();
                    int count = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                            //Assign retrieved values to a budget object

                        Budget budget = new Budget();
                        budget.setDate_from(ds.child("date_from").getValue().toString());
                        budget.setDate_to(ds.child("date_to").getValue().toString());
                        budget.setAmount(Double.parseDouble(ds.child("amount").getValue().toString()));

                        //Insert created budget object to ArrayList
                        budgetNo.add(String.valueOf(++count));
                        budgetArrayList.add(budget);
                        keys.add(String.valueOf(ds.getKey()));
                    }

                   budgetDataStatus.DataIsLoaded(budgetArrayList, keys, budgetNo);

                } else {
                    budgetArrayList.clear();
                    keys.clear();
                    budgetDataStatus.DataIsLoaded(budgetArrayList, keys, budgetNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    };

    //Add new budget record to database
    public void addBudget(Budget budget, final BudgetDataStatus dataStatus){
        //User Id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String key = dbRef.child(userId).push().getKey();
        dbRef.child(userId).child(key).setValue(budget).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });

    };

    //Update a budget record
    public void updateBudget(Budget budget, String key, final BudgetDataStatus budgetDataStatus){
        //User ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbRef.child(userId).child(key).setValue(budget).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                budgetDataStatus.DataIsUpdated();
            }
        });
    };

    //Delete a budget record
    public void  deleteBudget(String key, BudgetDataStatus budgetDataStatus){
        //User ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbRef.child(userId).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                budgetDataStatus.DataIsDeleted();
            }
        });
    }
}
