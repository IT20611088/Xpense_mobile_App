package com.example.xpensemobileapp.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.firebasenotes.databinding.ActivityMainBinding;
//import com.example.xpensemobileapp.note.databinding.ActivityMainBinding;
import com.example.xpensemobileapp.note.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NotesAdapter notesAdapter;
    private List<NotesModel> notesModelListss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        notesModelListss = new ArrayList<>();

        notesAdapter= new NotesAdapter(this);
        binding.notesRecycler.setAdapter(notesAdapter);
        binding.notesRecycler.setLayoutManager(new LinearLayoutManager(this));

        binding.floatingAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                if(text.length()>0){
                    filter(text);
                }else{
                    notesAdapter.clear();
                    notesAdapter.filterList(notesModelListss);
                }

            }
        });

    }
    private void filter(String text){
       List<NotesModel> adapterList= notesAdapter.getList();
        List<NotesModel> notesModelList = new ArrayList<>();
        for (int i=0;i<adapterList.size();i++){
            NotesModel notesModel=adapterList.get(i);
            if (notesModel.getTitle().toLowerCase().contains(text.toLowerCase()) || notesModel.getDescription().toLowerCase().contains(text)){
                notesModelList.add(notesModel);
            }
        }
        notesAdapter.filterList(notesModelList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("checking user");
        progressDialog.setMessage("in process");

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            progressDialog.show();
            firebaseAuth.signInAnonymously()
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.cancel();
                        }


                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }
    private void getData(){
        FirebaseFirestore.getInstance()
                .collection("notess")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notesAdapter.clear();
                       List<DocumentSnapshot> dsList= queryDocumentSnapshots.getDocuments();
                       for (int i=0;i<dsList.size();i++){
                           DocumentSnapshot documentSnapshot = dsList.get(i);
                           NotesModel notesModel = documentSnapshot.toObject(NotesModel.class);
                           notesModelListss.add(notesModel);
                           notesAdapter.add(notesModel);
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}