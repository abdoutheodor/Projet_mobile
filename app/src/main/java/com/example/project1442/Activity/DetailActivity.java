package com.example.project1442.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.Domain.PopularDomain;
import com.example.project1442.Helper.ManagmentCart;
import com.example.project1442.R;

public class DetailActivity extends AppCompatActivity {
    // Déclaration des vues et variables utilisées dans l'activité
    private TextView titleTxt, feeTxt, descriptionTxt, reviewTxt, scoreTxt;
    private ImageView picItem, backBtn;
    private Button addToCartBtn;
    private PopularDomain object; // Objet pour stocker les détails du produit
    private int numberOrder = 1; // Quantité initiale lors de l'ajout au panier
    private ManagmentCart managmentCart; // Gestionnaire du panier

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // Définit le layout de l'activité

        managmentCart = new ManagmentCart(this); // Initialisation du gestionnaire du panier
        initView(); // Initialisation des vues
        getBundle(); // Récupération et affichage des données du produit
    }

    private void initView() {
        // Association des vues avec leurs identifiants dans le fichier de layout
        addToCartBtn = findViewById(R.id.addToCartBtn);
        titleTxt = findViewById(R.id.titleTxt);
        feeTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        picItem = findViewById(R.id.itemPic);
        reviewTxt = findViewById(R.id.reviewTxt);
        scoreTxt = findViewById(R.id.scoreTxt);
        backBtn = findViewById(R.id.backBtn);
    }

    private void getBundle() {
        // Récupération de l'objet produit passé par l'intent
        object = (PopularDomain) getIntent().getSerializableExtra("object");
        // Chargement de l'image du produit en utilisant Glide
        int drawableResourceId = this.getResources().getIdentifier(object.getPicUrl(), "drawable", this.getPackageName());
        Glide.with(this).load(drawableResourceId).into(picItem);

        // Mise à jour de l'interface utilisateur avec les détails du produit
        titleTxt.setText(object.getTitle());
        feeTxt.setText("$" + object.getPrice());
        descriptionTxt.setText(object.getDescription());
        reviewTxt.setText(object.getReview() + "");
        scoreTxt.setText(object.getScore() + "");

        // Gestion du clic sur le bouton d'ajout au panier
        addToCartBtn.setOnClickListener(v -> {
            object.setNumberinCart(numberOrder);
            managmentCart.insertFood(object); // Ajout du produit au panier
        });
        // Gestion du clic sur le bouton de retour
        backBtn.setOnClickListener(v -> finish()); // Fermeture de l'activité
    }
}