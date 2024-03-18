package com.example.project1442.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.R;

public class ProfileActivity extends AppCompatActivity {
    // Déclaration du bouton de retour
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Assurez-vous que ce layout contient les boutons

        // Initialisation des boutons pour voir et éditer les informations du profil
        Button btnViewInfo = findViewById(R.id.btnViewInfo);
        Button btnEditInfo = findViewById(R.id.btnEditInfo);

// Configuration des écouteurs de clic pour les boutons
        btnViewInfo.setOnClickListener(v -> showFragment(new ProfileViewFragment()));
        btnEditInfo.setOnClickListener(v -> showFragment(new ProfileEditFragment()));

        // Initialisation et configuration de l'écouteur de clic pour le bouton de retour

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

    }

    // Méthode pour afficher un fragment dans le conteneur de fragment de l'activité

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)// Remplace le contenu du conteneur de fragment par le nouveau fragment
                .commit();// Applique la transaction

    }}


