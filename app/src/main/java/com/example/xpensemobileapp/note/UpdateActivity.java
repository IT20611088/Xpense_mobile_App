package com.example.xpensemobileapp.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.firebasenotes.databinding.ActivityUpdateActitvityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateActivity extends AppCompatActivity {
    private String id,title, description;
    ActivityUpdateActitvityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateActitvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        title= intent.getStringExtra("title");
        description= intent.getStringExtra("description");

        binding.title.setText(title);
        binding.description.setText(description);
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                progressDialog.setTitle("deleting");
                FirebaseFirestore.getInstance()
                        .collection("notess")
                        .document(id)
                        .delete();
                finish();
            }
        });

        binding.updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=binding.title.getText().toString();
                description=binding.description.getText().toString();
                updateNote();
            }
        });

    }
    private void updateNote(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog =new ProgressDialog(this);
        progressDialog.setTitle("updating");
        progressDialog.setMessage("your note");
        progressDialog.show();
        NotesModel notesModel = new NotesModel(id,title,description,firebaseAuth.getUid());
        FirebaseFirestore  firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("notess").document(id).set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(UpdateActivity.this, "note saved", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                });
    }

}