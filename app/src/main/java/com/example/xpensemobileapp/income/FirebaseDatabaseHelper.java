package com.example.xpensemobileapp.income;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceExpense;
    private List<IncomeForm> expenses = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<IncomeForm> Incomes, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mReferenceExpense = this.mDatabase.getReference("expenses");
    }


    public void readExpenses(final DataStatus dataStatus){
        this.mReferenceExpense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenses.clear();
                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());

                    //create new expense
                    IncomeForm income = keyNode.getValue(IncomeForm.class);

                    //add new data to expenses array list
                    expenses.add(income);
                }

                dataStatus.DataIsLoaded(expenses, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addExpense(IncomeForm expense, final DataStatus dataStatus){
        String key = mReferenceExpense.push().getKey();
        mReferenceExpense.child(key).setValue(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }


    public void updateExpense(String key, IncomeForm expense, final DataStatus dataStatus){
        mReferenceExpense.child(key).setValue(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteExpense(String key, final DataStatus dataStatus){
        mReferenceExpense.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
    }


}
