package com.example.xpensemobileapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "test.my.recyclerview.RecyclerViewAdapter";

    private ArrayList<String> expenseNo = new ArrayList<String>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> expenseNo, Context mContext) {
        this.expenseNo = expenseNo;
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

        holder.textView.setText(expenseNo.get(position));
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
        TextView textView;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.expenseNo);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}