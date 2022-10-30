package com.example.xpensemobileapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.xpensemobileapp.R;
import com.example.xpensemobileapp.User;
import com.example.xpensemobileapp.databinding.FragmentHomeBinding;
import com.example.xpensemobileapp.expense.ExpensesDashboardActivity;
import com.example.xpensemobileapp.income.IncomeDashboardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class HomeFragment extends Fragment {

    private TextView mTotal;


    private FragmentHomeBinding binding;

    private ImageButton expenseBtn;
    private ImageButton incomeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.expenseBtn = (ImageButton)root.findViewById(R.id.imageButton2) ;

        this.expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExpensesDashboardActivity.class));
            }
        });



        //income btn in home
        this.incomeBtn = (ImageButton)root.findViewById(R.id.imageButton) ;

        this.incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IncomeDashboardActivity.class));
            }
        });



        // Initialize Firebase Auth
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
//
//        textHome = getView().findViewById(R.id.textView17);
//
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_incomes").child(currentUser.getUid());
        //String key = reference.push().getKey();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                int sum=0;

                mTotal = getView().findViewById(R.id.textView9);

                 for(DataSnapshot ds:snapshot.getChildren()) {
                    Map<String,Object> map = (Map<String,Object>) ds.getValue();
                    Object price = map.get("amount");
                    int pValue = Integer.parseInt(String.valueOf(price));
                    sum += pValue;

                    mTotal.setText(String.valueOf(sum)+".00");

//                     Log.d("sum",String.valueOf(sum));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}