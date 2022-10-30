package com.example.xpensemobileapp.budget;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Budget {
    private String date_from;
    private String date_to;
    private double amount;

    public Budget(){}

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate_from(){
        return  this.date_from;
    }

    public String getDate_to(){
        return  this.date_to;
    }

    public double getAmount(){
        return this.amount;
    }

    //Compare from date and to date
    public boolean compareDate() {
        try {

            Date dateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(this. date_from);
            Date dateTo = new SimpleDateFormat("dd/MM/yyyy").parse(this.date_to);

            if (dateTo != null && dateFrom != null && dateTo.compareTo(dateFrom) > 0)
                return true;
            else return false;
        } catch (ParseException e) {
            Log.i("Exception", String.valueOf(e));
            return false;
        }
    }
}
