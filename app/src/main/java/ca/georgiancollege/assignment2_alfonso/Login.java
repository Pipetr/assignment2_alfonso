package ca.georgiancollege.assignment2_alfonso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import ca.georgiancollege.assignment2_alfonso.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                    Intent intentObj = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intentObj);
                    finish(); // Close the Login activity
                }else{
                    Log.d("tag", "singInWithEmail:failure ", task.getException());
                }

            }
        });
    }
}