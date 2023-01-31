package com.heady.betterweather.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.heady.betterweather.databinding.ActivityRegisterBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.tvLoginClick.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnSignIn.setOnClickListener(v -> {


            String name = binding.etName.getText().toString().trim();
            String zip = binding.etZipCode.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();
            String repass = binding.etRePassword.getText().toString().trim();


            if (name.isEmpty())
                binding.etName.setError("You must enter a name");
            else if (zip.isEmpty())
                binding.etZipCode.setError("You must enter a valid zip code");
            else if (email.isEmpty())
                binding.etEmail.setError("You must enter a valid email address");
            else if (isValidPassword(pass))
                binding.etPassword.setError("You must enter a password and must contain " +
                        "(8-20 characters, one digit, one uppercase and one lowercase letter, " +
                        "and one special character (!@#$%&*()_+^) ");
            else if (isValidPassword(repass) && repass.equals(pass))
                binding.etRePassword.setError("You must re-enter your password and it must " +
                        "match the password above");
            else {
                CreateUser(name, zip, email, pass);
            }
        });
    }

    private void CreateUser(String name, String zip, String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Create user hashmap
                        Map<String, Object> user = new HashMap<>();
                        user.put("name", name);
                        user.put("zip", zip);
                        user.put("email", email);

                        // Add user to firebase with generated ID
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(RegisterActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "OOPS! Something" +
                                        " went wrong, this user may already exist or something may be" +
                                        " wrong with your network connection. Try again later or check" +
                                        " if you already have an account.", Toast.LENGTH_SHORT).show());

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Function to validate the password.
    public static boolean
    isValidPassword(String password) {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(regex);

        if (password == null) {
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

}