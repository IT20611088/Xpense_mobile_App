package com.example.xpensemobileapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter {
    private Context mContext;
    private BudgetAdapter budgetAdapter;

    public void setConfig(RecyclerView recyclerView, Context context, ArrayList<Budget> budgetArray){
        mContext = context;
        budgetAdapter = new BudgetAdapter(budgetArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(budgetAdapter);
    }

    class BudgetReportView extends RecyclerView.ViewHolder {

        //Create Table Row Object
        TableRow tableRow;


        //Create TextViews for each column
        TextView recordNo;
        TextView dateFrom;
        TextView dateTo;
        TextView amount;

        public BudgetReportView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.table_row_item, parent, false));

            tableRow = itemView.findViewById(R.id.tableRowBudgetReport);
            recordNo = itemView.findViewById(R.id.textViewRowNo);
            dateFrom = itemView.findViewById(R.id.textViewDateFromCell);
            dateTo = itemView.findViewById(R.id.textViewDateToCell);
            amount = itemView.findViewById(R.id.textViewAmountCell);
        }

        public void bind(Budget budget) {
            //Remove these logs
            Log.i("kldgjlkdgjkldgj", String.valueOf(recordNo));
            Log.i("jshfjshf", "I hate this shit");

            recordNo.setText("2");
            dateFrom.setText(budget.getDate_from());
            dateTo.setText(budget.getDate_to());
            amount.setText(String.valueOf(budget.getAmount()));
        }
    }

    class BudgetAdapter extends RecyclerView.Adapter<BudgetReportView> {

        private ArrayList<Budget> budgetArray = new ArrayList<>();
        private Budget budget;

        public BudgetAdapter(ArrayList<Budget> budgetArray) {
            this.budgetArray = budgetArray;
        }


        @NonNull
        @Override
        public BudgetReportView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BudgetReportView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BudgetReportView holder, int position) {
            holder.bind(budgetArray.get(position));
        }

        @Override
        public int getItemCount() {
            return budgetArray.size();
        }
    }

}
