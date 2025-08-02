package ca.georgiancollege.assignment2_alfonso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import ca.georgiancollege.assignment2_alfonso.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    UserModel user;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Users");
        mAuth = FirebaseAuth.getInstance();

        binding.txtRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentObj = new Intent(getApplicationContext(), Register.class);
                startActivity(intentObj);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(binding.txtInEmail.getText()).toString().trim();
                String password = Objects.requireNonNull(binding.txtInPassword.getText()).toString().trim();
                // Validate email and password before proceeding
                if (email.isEmpty() || password.isEmpty()) {
                    binding.txtInEmail.setError("Email cannot be empty");
                    binding.txtInPassword.setError("Password cannot be empty");
                    return;
                }
                // Call the method to sign in with email and password
                singIn(email, password);
            }
        });
    }

    private void singIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("tag", "signInWithEmail:success");
                    String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    // Retrieve all the user details stored in Users collection
                    collectionReference.document(userid).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()){
                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                    if (user != null) {
                                        Log.d("tag", "User data retrieved: " + user.getUsername());
                                        // Make sure all user fields are properly set
                                        if (user.getUid() == null) {
                                            user.setUid(userid);
                                        }
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("UserModel", user);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.e("tag", "User object is null");
                                        Toast.makeText(Login.this, "Error: User data not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("tag", "User document does not exist");
                                    Toast.makeText(Login.this, "Error: User not found in database", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.w("tag", "Error getting user data", e);
                                Toast.makeText(Login.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                            });
                }else{
                    Log.d("tag", "singInWithEmail:failure ", task.getException());
                    Toast.makeText(Login.this, "Login fail", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}