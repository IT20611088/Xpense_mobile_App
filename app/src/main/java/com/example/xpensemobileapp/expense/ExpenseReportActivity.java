package com.example.xpensemobileapp.expense;

import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpensemobileapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExpenseReportActivity extends AppCompatActivity {
    private boolean customBtnClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);

        setCurrentDateValue();

    }

    //method to get current date and set date value based on user selection from the calender
    public void setCurrentDateValue() {

        //set the current date
        TextView currentDate = findViewById(R.id.currentDateValue);

        Calendar date = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");

        String curDate = sdf.format(date.getTime());

        currentDate.setText(curDate);


        //set the date as per selected by the user
        CalendarView calendarView = findViewById(R.id.calenderValue);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String selectedDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year);

                currentDate.setText(selectedDate);
            }
        });

    }

    public void setCustomFromDateValue() {
        //set the date as per selected by the user
        TextView fromDate = findViewById(R.id.currentDateValue);


        CalendarView calendarView = findViewById(R.id.calenderValue);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String selectedDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year);

                fromDate.setText(selectedDate);

            }
        });

    }

    public void setCustomToDateValue() {
        //set the date as per selected by the user
        TextView toDate = findViewById(R.id.toDateValue);

        CalendarView calendarView = findViewById(R.id.calenderValue);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String selectedDate = String.valueOf(dayOfMonth) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year);

                toDate.setText(selectedDate);

            }
        });


    }


    public void onClickDailyBtn(View view) {
        Button dailyBtn = findViewById(R.id.dailyBtn);
        dailyBtn.setBackgroundColor(getResources().getColor(R.color.light_blue));

        Button customBtn = findViewById(R.id.customBtn);
        customBtn.setBackgroundColor(getResources().getColor(R.color.white));

        TextView currentDateLabel = findViewById(R.id.currentDateLabel);
        currentDateLabel.setText(getResources().getText(R.string.currentDateText));

        TextView toDate = findViewById(R.id.toDateValue);
        toDate.setVisibility(View.INVISIBLE);

        TextView toDateLabel = findViewById(R.id.toLabel);
        toDateLabel.setVisibility(View.INVISIBLE);

        setCurrentDateValue();

    }

    public void onClickCustomBtn(View view) {

        this.customBtnClicked = true;

        Button customBtn = findViewById(R.id.customBtn);

        customBtn.setBackgroundColor(getResources().getColor(R.color.light_blue));

        Button dailyBtn = findViewById(R.id.dailyBtn);
        dailyBtn.setBackgroundColor(getResources().getColor(R.color.white));

        TextView currentDateLabel = findViewById(R.id.currentDateLabel);
        currentDateLabel.setText(getResources().getText(R.string.fromDateText));

        //TextView fromDate = findViewById(R.id.currentDateValue);
        //fromDate.setText(getResources().getText(R.string.currentDateValueHint));

        TextView toDate = findViewById(R.id.toDateValue);

        toDate.setVisibility(VISIBLE);

        TextView toDateLabel = findViewById(R.id.toLabel);
        toDateLabel.setVisibility(VISIBLE);

        setCustomFromDateValue(); //to set 'from' date value

        setCustomToDateValue(); //to set 'to' date value
    }


    public void onClickGenerateBtn(View view) throws ParseException {
        Button generateBtn = findViewById(R.id.generateBtn);

        TextView fromDate = findViewById(R.id.currentDateValue);
        TextView toDate = findViewById(R.id.toDateValue);

//        String fromDateTxt = fromDate.getText().toString();
        String toDateTxt = toDate.getText().toString();


//        Date fromDateValue = new SimpleDateFormat("dd/MM/yyyy").parse(fromDateTxt);
//        Date toDateValue = new SimpleDateFormat("dd/MM/yyyy").parse(toDateTxt);

        //to check whether custom button has been clicked
        if (this.customBtnClicked == true) {
            if (toDateTxt.matches(""))
                Toast.makeText(this, "Please select a second date", Toast.LENGTH_SHORT).show();

            else {
                //to be completed
            }

        }

    }

    public void moveToNext(View view){
        Intent myIntent = new Intent(ExpenseReportActivity.this, ExpenseReportOverviewActivity.class);
        startActivity(myIntent);
    }

}