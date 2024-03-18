package com.example.project1442.Activity;

// Importations nécessaires pour l'interface utilisateur, la localisation, etc.
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.ImageView;

import com.example.project1442.Adapter.CartListAdapter;
import com.example.project1442.Helper.ManagmentCart;
import com.example.project1442.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;
import java.io.IOException;
import java.util.List;
import android.location.Geocoder;
import android.location.Address;

public class CartActivity extends AppCompatActivity {
    // Déclaration des variables pour l'interface utilisateur et la gestion du panier
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private ScrollView scrollView;
    private ImageView backBtn;
    private double tax;

    // Client pour récupérer la dernière position connue de l'utilisateur
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart); // Associer le layout de l'activité

        managmentCart = new ManagmentCart(this); // Initialisation du gestionnaire de panier

        initView(); // Initialisation des vues
        setVariavle(); // Configurer les actions des éléments d'interface (par exemple, le bouton de retour)
        initList(); // Initialisation de la liste du panier avec RecyclerView
        calcualteCart(); // Calcul et affichage des totaux du panier

        // Initialisation du client de localisation pour récupérer la dernière position
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation(); // Récupération de la dernière position connue
    }

    private void initView() {
        // Association des vues avec leurs identifiants
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerView = findViewById(R.id.view3);
        scrollView = findViewById(R.id.scrollView2);
        backBtn = findViewById(R.id.backBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
    }

    private void setVariavle() {
        // Configuration du listener pour le bouton de retour
        backBtn.setOnClickListener(v -> finish());
    }

    private void initList() {
        // Configuration de RecyclerView avec un LinearLayoutManager et un adaptateur personnalisé
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managmentCart.getListCart(), this, () -> calcualteCart());
        recyclerView.setAdapter(adapter);

        // Gestion de l'affichage des vues en fonction du contenu du panier
        if (managmentCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calcualteCart() {
        // Calcul des totaux du panier, incluant les taxes et frais de livraison
        double percentTax = 0.02; // Pourcentage de taxe
        double delivery = 0; // Frais de livraison
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(managmentCart.getTotalFee() * 100) / 100;

        // Mise à jour de l'interface utilisateur avec les totaux calculés
        totalFeeTxt.setText("$" + itemTotal);
        taxTxt.setText("$" + tax);
        deliveryTxt.setText("$" + delivery);
        totalTxt.setText("$" + total);
    }

    private void getLastLocation() {
        // Vérification de la permission et récupération de la dernière position connue
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    getAddress(latitude, longitude); // Conversion de la position en adresse
                }
            });
        }
    }

    private void getAddress(double latitude, double longitude) {
        // Conversion de la latitude et longitude en adresse postale
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                String country = address.getCountryName();
                // Mise à jour de l'interface utilisateur avec l'adresse
                TextView locationTextView = findViewById(R.id.textView25);
                locationTextView.setText(city + ", " + country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
