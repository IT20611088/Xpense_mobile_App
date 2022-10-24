package com.example.xpensemobileapp.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpensemobileapp.R;
import com.example.xpensemobileapp.budget.Budget;

import java.util.ArrayList;

public class RecyclerViewAdapter {
    private Context mContext;
    private BudgetAdapter budgetAdapter;

    public void initRecyclerView(RecyclerView recyclerView, Context context, ArrayList<Budget> budgetArray){
        mContext = context;
        budgetAdapter = new BudgetAdapter(budgetArray);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(budgetAdapter);
    }

    class BudgetReportView extends RecyclerView.ViewHolder {

        //Create Table Row Object
        TableRow tableRow;


        //Create TextViews for each column
        String dateFrom, dateTo, amount;

        TextView editTxtRecordNo, editTxtDateFrom, editTxtDateTo, editTxtAmount;

        public BudgetReportView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).inflate(R.layout.table_row_item, parent, false));

            tableRow = itemView.findViewById(R.id.tableRowBudgetReport);
            editTxtRecordNo = itemView.findViewById(R.id.textViewRowNo);
            editTxtDateFrom = itemView.findViewById(R.id.textViewDateFromCell);
            editTxtDateTo = itemView.findViewById(R.id.textViewDateToCell);
            editTxtAmount = itemView.findViewById(R.id.textViewAmountCell);

            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, UpdateBudgetFormActivity.class);
                    Log.i("==================hgiuygiuygiuygiuygi===============================",editTxtDateFrom.getText().toString());
                    intent.putExtra("dateFrom", editTxtDateFrom.getText().toString());
                    intent.putExtra("dateTo", editTxtDateTo.getText().toString());
                    intent.putExtra("amount", editTxtAmount.getText().toString());
                    mContext.startActivity(intent);

                }
            });
        }

        public void bind(Budget budget) {
            editTxtRecordNo.setText("2");
            editTxtDateFrom.setText(budget.getDate_from());
            editTxtDateTo.setText(budget.getDate_to());
            editTxtAmount.setText(String.valueOf(budget.getAmount()));
        }
    }

    class BudgetAdapter extends RecyclerView.Adapter<BudgetReportView> {

        private ArrayList<Budget> budgetArray;

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
