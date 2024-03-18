package com.example.project1442.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstName, lastName, username, password, confirmPassword;
    private Button registerButton;
    private TextView linkLogin;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        username = findViewById(R.id.usernameRegister);
        password = findViewById(R.id.passwordRegister);
        confirmPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        linkLogin = findViewById(R.id.linkLogin);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String first = firstName.getText().toString().trim();
        String last = lastName.getText().toString().trim();

        // Vérifie si l'un des champs est vide
        if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || first.isEmpty() || last.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return; // Stoppe l'exécution de la méthode si un champ est vide
        }

        // Vérifie si les mots de passe saisis correspondent
        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
            return; // Stoppe l'exécution de la méthode si les mots de passe ne correspondent pas
        }

        // Tente d'ajouter l'utilisateur à la base de données
        long userId = databaseHelper.addUser(first, last, user, pass);
        if (userId != -1) {
            Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();

            // Stockez l'ID utilisateur dans SharedPreferences
            getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .edit()
                    .putLong("USER_ID", userId)
                    .apply();


            // Redirigez vers MainActivity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Termine cette activité
        } else {
            Toast.makeText(this, "Échec de l'inscription ou utilisateur existant", Toast.LENGTH_SHORT).show();
        }
    }}

