package com.example.xpensemobileapp.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;


import com.example.xpensemobileapp.databinding.ActivityAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
     private String title="" ,description="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle("Notes");

        binding.saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=binding.title.getText().toString();
                description=binding.description.getText().toString();
                saveNote();
            }
        });
    }

    private void saveNote(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("saving");
        progressDialog.setMessage("saving your note");
        progressDialog.show();
        String noteId= UUID.randomUUID().toString();//generate aid for the new note and convert it to string
        NotesModel notesModel = new NotesModel(noteId,title,description,firebaseAuth.getUid());//create a new instance of the notes model
        FirebaseFirestore  firebaseFirestore = FirebaseFirestore.getInstance();//get firebase instance
        firebaseFirestore.collection("notes").document(noteId).set(notesModel)//add to the collection in the database
                .addOnSuccessListener(new OnSuccessListener<Void>() {//if successful display success toast
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(AddActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }

}