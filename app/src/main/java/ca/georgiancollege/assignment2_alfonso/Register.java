package ca.georgiancollege.assignment2_alfonso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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

import ca.georgiancollege.assignment2_alfonso.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(binding.txtInEmail.getText()).toString().trim();
                String password = Objects.requireNonNull(binding.txtInPassword.getText()).toString().trim();
                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password) {
        if (!validateEmail(email) || !validatePassword(password)) {
            // If validation fails, do not proceed with registration, set toast
            binding.txtInEmail.setError("Invalid email or password");
            Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intentObj = new Intent(getApplicationContext(), Login.class);
                        startActivity(intentObj);

                    } else {
                        Toast.makeText(Register.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean validateEmail(String emailInput) {
        if (emailInput.isEmpty()) {
            binding.txtInEmail.setError("Field cannot be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            binding.txtInEmail.setError("Please enter a valid email address");
            return false;
        } else {
            binding.txtInEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword(String passwordInput) {
        if (passwordInput.isEmpty()) {
            binding.txtInPassword.setError("Field cannot be empty");
            return false;
        } else if (passwordInput.length() < 6) {
            binding.txtInPassword.setError("Password must be at least 6 characters");
            return false;
        } else {
            binding.txtInPassword.setError(null);
            return true;
        }
    }
}