package com.example.xpensemobileapp.income;

import java.util.Date;

public class IncomeForm {

    private String amount;
    private String currency;

    private String date;
    private String payer;

    private String description;

    public IncomeForm(){

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //overloaded constructor
    public IncomeForm(String amount, String currency, String date, String payer, String description) {
        this.amount = amount;
        this.currency = currency;

        this.date = date;
        this.payer = payer;

        this.description = description;
    }

}
