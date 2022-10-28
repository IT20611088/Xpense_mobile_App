package com.example.xpensemobileapp.income;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpensemobileapp.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "test.my.recyclerview.RecyclerViewAdapter";

    private ArrayList<String> expenseNo = new ArrayList<String>();
    private ArrayList<String> expenseDate = new ArrayList<>();
    private ArrayList<String> expenseID = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> expenseNo, ArrayList<String> expenseDate,
                               ArrayList<String> expenseID, Context mContext) {
        this.expenseNo = expenseNo;
        this.expenseDate = expenseDate;
        this.expenseID = expenseID;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        //Glide.with(mContext).asBitmap().load(mImage.get(position)).into(holder.image);

        holder.db_id.setText(expenseID.get(position));
        holder.expenseNo.setText(expenseNo.get(position));
        holder.date.setText(expenseDate.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on"+expenseNo.get(position));
                Toast.makeText(mContext,expenseNo.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return expenseNo.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        // ImageView imageView;
        TextView db_id;
        TextView expenseNo;
        ImageButton imgBtn;
        TextView date;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.imageView);
            db_id = itemView.findViewById(R.id.db_id);
            expenseNo = itemView.findViewById(R.id.expenseNo);
            date = itemView.findViewById(R.id.date);

            imgBtn = itemView.findViewById(R.id.imageButtonArrow);

            parentLayout = itemView.findViewById(R.id.parent_layout);

            imgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, IncomeOverviewActivity.class);
                    intent.putExtra("id", db_id.getText().toString());
//                    intent.putExtra("amount", )


                    mContext.startActivity(intent);
                }
            });
        }

    }
}