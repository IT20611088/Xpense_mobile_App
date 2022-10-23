package com.example.xpensemobileapp;

import java.util.Date;

public class ExpenseForm {

    private double amount;
    private String currency;
    private String method;
    private Date date;
    private String payee;
    private String category;
    private String description;

    public ExpenseForm(double amount, String currency, String method, Date date, String payee,
                       String category, String description){
        this.amount = amount;
        this.currency = currency;
        this.method = method;
        this.date = date;
        this.payee = payee;
        this.category = category;
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
