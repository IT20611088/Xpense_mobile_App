package com.example.xpensemobileapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class ExpenseReportOverviewActivity extends AppCompatActivity {

    private ImageButton lineBtn;
    private ImageButton pieBtn;
    private ImageButton barBtn;

    private FrameLayout fragmentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report_overview);

        this.lineBtn = findViewById(R.id.lineBtn);
        this.pieBtn = findViewById(R.id.pieBtn);
        this.barBtn = findViewById(R.id.barBtn);

    }

   public void onClickLineGraphBtn(View view){
        //change graph
        this.fragmentLayout = findViewById(R.id.graphRepresentation);
        this.fragmentLayout.setBackground(getDrawable(R.drawable.line_graph_image));

        //change button color
        this.pieBtn.setBackground(getDrawable(R.drawable.btn_background));
        this.lineBtn.setBackground(getDrawable(R.drawable.graph_btn_background_clicked));
        this.barBtn.setBackground(getDrawable(R.drawable.btn_background));
    }

    public void onClickPieChartBtn(View view){
        //change graph
        this.fragmentLayout = findViewById(R.id.graphRepresentation);
        this.fragmentLayout.setBackground(getDrawable(R.drawable.pie_chart_image));

        //change button color
        this.pieBtn.setBackground(getDrawable(R.drawable.graph_btn_background_clicked));
        this.lineBtn.setBackground(getDrawable(R.drawable.btn_background));
        this.barBtn.setBackground(getDrawable(R.drawable.btn_background));
    }

    public void onClickBarGraphBtn(View view){
        //change graph
        this.fragmentLayout = findViewById(R.id.graphRepresentation);
        this.fragmentLayout.setBackground(getDrawable(R.drawable.bar_graph_image));

        //change button color
        this.lineBtn.setBackground(getDrawable(R.drawable.btn_background));
        this.pieBtn.setBackground(getDrawable(R.drawable.btn_background));
        this.barBtn.setBackground(getDrawable(R.drawable.graph_btn_background_clicked));
    }

    public void onClickDownloadBtn(View view){
        //complete the code
    }



}