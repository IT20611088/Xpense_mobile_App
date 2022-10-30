package com.example.xpensemobileapp.income;

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
    private DatabaseReference mReferenceIncome;
    private List<IncomeForm> incomes = new ArrayList<>();
    private String userId;

    public interface DataStatus{
        void DataIsLoaded(List<IncomeForm> incomes, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mReferenceIncome = this.mDatabase.getReference("user_incomes").child(userId);
    }


    public void readIncomes(final DataStatus dataStatus){

        this.mReferenceIncome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                incomes.clear();

                Log.i("=====================", String.valueOf(incomes.isEmpty()));

                List<String> keys = new ArrayList<>();

                for(DataSnapshot keyNode : snapshot.getChildren()){
                    keys.add(keyNode.getKey());

                    //create new income
                    IncomeForm income = keyNode.getValue(IncomeForm.class);

                    //add new data to incomes array list
                    incomes.add(income);
                }

                dataStatus.DataIsLoaded(incomes, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addIncome(IncomeForm income, final DataStatus dataStatus){
        String key = mReferenceIncome.push().getKey();
        mReferenceIncome.child(key).setValue(income).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsInserted();
            }
        });
    }


    public void updateIncome(String key, IncomeForm income, final DataStatus dataStatus){
        mReferenceIncome.child(key).setValue(income).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteIncome(String key, final DataStatus dataStatus){
        mReferenceIncome.child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
    }


}
