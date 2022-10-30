package com.example.xpensemobileapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.xpensemobileapp.budget.Budget;
import com.example.xpensemobileapp.budget.BudgetFormActivity;
import com.example.xpensemobileapp.budget.DatabaseHelper;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void fromToDateCheck(){
        Budget budget= new Budget();
        budget.setDate_from("25/10/2022");
        budget.setDate_to("20/10/2022");
        assertEquals(false, budget.compareDate());
    }

    @Test
    public void isInRange(){
        DatabaseHelper databaseHelper = new DatabaseHelper();
        Boolean expected = databaseHelper.dateValidator("12/10/2022", "18/10/2022", "20/10/2022", "25/10/2022");
        assertEquals(true, expected);
    }
}