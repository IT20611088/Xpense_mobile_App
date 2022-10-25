package com.example.xpensemobileapp.expense;

import java.util.Date;

public class ExpenseForm {

    private String amount;
    private String currency;
    private String method;
    private String date;
    private String payee;
    private String category;
    private String description;

    public ExpenseForm(){

    }

    //overloaded constructor
    public ExpenseForm(String amount, String currency, String method, String date, String payee,
                       String category, String description) {
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.date = date;
        this.payee = payee;
        this.category = category;
        this.description = description;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
