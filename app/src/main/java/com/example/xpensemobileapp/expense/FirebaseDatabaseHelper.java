package com.example.xpensemobileapp.expense;

import android.provider.ContactsContract;
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
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceExpense;
    private List<ExpenseForm> expenses = new ArrayList<>();
    private String userId;

    public interface DataStatus{
        void DataIsLoaded(List<ExpenseForm> expenses, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mReferenceExpense = this.mDatabase.getReference("user_expenses").child(userId);
    }


    public void readExpenses(final DataStatus dataStatus){

        this.mReferenceExpense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenses.clear();

                Log.i("=====================", String.valueOf(expenses.isEmpty()));

                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());

                    //create new expense
                    ExpenseForm expense = keyNode.getValue(ExpenseForm.class);

                    //add new data to expenses array list
                    expenses.add(expense);
                }

                dataStatus.DataIsLoaded(expenses, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addExpense(ExpenseForm expense, final DataStatus dataStatus){
        String key = mReferenceExpense.push().getKey();
        mReferenceExpense.child(key).setValue(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }


    public void updateExpense(String key, ExpenseForm expense, final DataStatus dataStatus){
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
