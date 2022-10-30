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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper {

    private DatabaseReference dbRef;
    private ArrayList<Budget> budgetArrayList = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> budgetNo = new ArrayList<>();

    public DatabaseHelper() {
       // dbRef = FirebaseDatabase.getInstance().getReference().child("user_budgets");
    }

    //Get all budget records from database
    protected void readBudget(final BudgetDataStatus budgetDataStatus) {
        //Get Current User ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    budgetArrayList.clear();
                    budgetNo.clear();
                    keys.clear();

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
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Add new budget record to database
    public void addBudget(Budget budget, final BudgetDataStatus dataStatus) {
        //User Id
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dbRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                            String tempDateFrom = ds.child("date_from").getValue().toString();
                            String tempDateTo = ds.child("date_to").getValue().toString();
                            String dateFrom = budget.getDate_from();
                            String dateTo = budget.getDate_to();

                            //Budget that user is trying to adding is within given range
                            if(dateValidator(tempDateFrom, tempDateTo, dateFrom, dateTo)){
                                //Get current user id
                                String key = dbRef.child(userId).push().getKey();
                                dbRef.child(userId).child(key).setValue(budget).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dataStatus.DataIsInserted();
                                    }
                                });
                                break;
                            } //budget is outside of already available budgets
                            else{
                                dataStatus.DisplayAlerts();
                            }
                    }
                }
                else {
                    // if this is the first budget
                    String key = dbRef.child(userId).push().getKey();
                    dbRef.child(userId).child(key).setValue(budget).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dataStatus.DataIsInserted();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    //Update a budget record
    public void updateBudget(Double amount, String key, final BudgetDataStatus budgetDataStatus) {
        //User ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbRef.child(userId).child(key).child("amount").setValue(amount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                budgetDataStatus.DataIsUpdated();
            }
        });
    }

    //Delete a budget record
    public void deleteBudget(String key, BudgetDataStatus budgetDataStatus) {
        //User ID
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbRef.child(userId).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                budgetDataStatus.DataIsDeleted();
            }
        });
    }

    public interface BudgetDataStatus {
        void DataIsLoaded(ArrayList<Budget> budgetArrayList, ArrayList<String> keys, ArrayList<String> budgetNo);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();

        void DisplayAlerts();
    }

    //Date validator
    public boolean dateValidator(String tempFrom, String tempTo, String from, String to){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
        Date tempDateFrom = formatter.parse(tempFrom);
        Date tempDateTo = formatter.parse(tempTo);
        Date dateFrom = formatter.parse(from);
        Date dateTo = formatter.parse(to);
            return dateFrom.equals(tempDateTo) || dateTo.equals(tempDateFrom) || dateTo.before(tempDateFrom) || dateFrom.after(tempDateTo);
        } catch (ParseException e) {
            Log.e("__________________________________________________________________EXCEPTION", String.valueOf(e));
            return false;
        }
    }
}
