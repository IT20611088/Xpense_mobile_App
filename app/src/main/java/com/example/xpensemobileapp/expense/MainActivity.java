//package com.example.xpensemobileapp.expense;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import com.example.xpensemobileapp.R;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//
//    public void startNext(View view){
//        Intent intent = new Intent(MainActivity.this, ExpenseFormActivity.class);
//
//        startActivity(intent);
//    }
//
//    public void onClickBtn3(View view){
//        Intent intent = new Intent(MainActivity.this, ExpenseOverviewActivity.class);
//
//        startActivity(intent);
//    }
//
//    public void onClickLastBtn(View view){
//        Intent intent = new Intent(MainActivity.this, ExpensesDashboardActivity.class);
//
//       startActivity(intent);
//    }
//}