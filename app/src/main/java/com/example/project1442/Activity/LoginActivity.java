package com.example.project1442.Activity;

// Importations nécessaires pour les éléments de l'interface utilisateur, l'intention et la persistance des données
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.example.project1442.BDD.DatabaseHelper; // Classe d'aide pour la base de données
import com.example.project1442.R; // Ressources de l'application

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    // Déclaration des vues et de la classe d'aide pour la base de données
    private EditText username, password;
    private Button loginButton;
    private TextView linkRegister;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Définit le layout de l'activité

        databaseHelper = new DatabaseHelper(this); // Initialisation de la classe d'aide pour la base de données

        // Association des vues avec leurs identifiants
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);
        linkRegister = findViewById(R.id.linkRegister);

        // Configuration du listener pour le bouton de connexion
        loginButton.setOnClickListener(v -> loginUser());

        // Configuration du listener pour le texte cliquable de redirection vers l'activité d'inscription
        linkRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }



    private void loginUser() {
        // Récupération des valeurs saisies par l'utilisateur
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        // Vérification des identifiants utilisateur dans la base de données
        long userId = databaseHelper.checkUser(user, pass);
        if (userId != -1) {
            // En cas de succès, affichage d'un message et stockage de l'ID utilisateur dans SharedPreferences
            Toast.makeText(this, "Connexion réussie", Toast.LENGTH_SHORT).show();

            getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .edit()
                    .putLong("USER_ID", userId)
                    .apply();

            // Redirection vers MainActivity et fermeture de LoginActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // En cas d'échec, affichage d'un message d'erreur
            Toast.makeText(this, "Échec de la connexion. Vérifiez vos identifiants", Toast.LENGTH_SHORT).show();
        }
    }
}
