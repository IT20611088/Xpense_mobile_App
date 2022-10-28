package com.example.xpensemobileapp.budget;

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
}
