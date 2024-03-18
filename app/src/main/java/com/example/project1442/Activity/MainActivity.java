package com.example.project1442.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.project1442.Adapter.PopularListAdapter;
import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.Domain.PopularDomain;
import com.example.project1442.R;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Ajoutez une variable de classe pour stocker l'ID de l'utilisateur
    private int currentUserId = -1;

    private DatabaseHelper data;
    private EditText editTextSearch;
    private RecyclerView recyclerViewPopular;
    private PopularListAdapter adapterPopular;


    private EditText editTextText;
    private Button search_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new DatabaseHelper(this); // Correctement instancié ici

        setupClickListener();
        currentUserId = (int) getIntent().getLongExtra("USER_ID", -1);
       // createSampleItems();
        //data.deleteAllItems();
        updateUsernameTextView();

        List<Integer> topRatedItemIds = data.getTopRatedItemIds(5);
        initRecyclerView(topRatedItemIds); // Passe la liste des IDs en tant que paramètre

        bottomNavigation();
        // Vérifie si les permissions de localisation ont déjà été accordées
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si les permissions n'ont pas été accordées, les demander ici
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // Les permissions ont déjà été accordées, vous pouvez procéder avec la récupération de la localisation
        }

        editTextSearch = findViewById(R.id.editTextText);

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v.getText().toString());
                    return true; // L'événement est consommé
                }
                return false; // Laisse le système gérer l'événement
            }
        });
        currentUserId = getIntent().getIntExtra("USER_ID", -1);

    }

    private void updateUsernameTextView() {
        // Récupérer l'ID de l'utilisateur stocké dans SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        long userId = prefs.getLong("USER_ID", -1);

        // Vérifier si un ID utilisateur est stocké
        if (userId != -1) {
            // Utiliser l'ID pour récupérer les informations de l'utilisateur de la base de données
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            User user = databaseHelper.getUserById(userId);

            if (user != null) {
                // Mettre à jour le TextView avec le nom d'utilisateur
                TextView usernameTextView = findViewById(R.id.textView2);
                usernameTextView.setText(user.getUsername());
            } else {
                // Gérer le cas où l'utilisateur n'est pas trouvé dans la base de données
                Log.e("MainActivity", "Utilisateur non trouvé.");
            }
        } else {
            // Gérer le cas où aucun utilisateur n'est connecté
            Log.e("MainActivity", "Aucun utilisateur connecté.");
        }
    }

    private void performSearch(String searchTerm) {
        Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
        intent.putExtra("searchTerm", searchTerm.trim());
        startActivity(intent);
    }


    private void createSampleItems() {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());

 dbHelper.addItem(

    "MacBook Pro 13 M2 chip",
    "Découvrez le nouveau MacBook Pro 13 avec la puce M2. Un ordinateur portable de pointe qui redéfinit la performance et la portabilité.",
    "pic1", // Assurez-vous d'avoir une ressource drawable correspondante
    25, 5.0, 1299.99, // Notez que le prix semble incorrect dans votre exemple, ici j'ai mis un prix plus réaliste
    "Gaming");

dbHelper.addItem(
    "iPhone 13",
    "L'iPhone 13 offre des performances incroyables, un système de caméra révolutionnaire et une durabilité sans précédent.",
    "pic3",
    100, 4.8, 799.99,
    "Phone");


dbHelper.addItem(
    "Sony WH-1000XM4",
    "Découvrez le silence absolu avec les écouteurs Sony WH-1000XM4 qui combinent une suppression du bruit inégalée et une qualité audio exceptionnelle.",
    "pic4",
    75, 4.9, 349.99,
    "HeadPhone");

dbHelper.addItem(
        "PS5-Pro",
        "La PlayStation 5 est la dernière innovation de Sony, offrant une expérience de jeu de nouvelle génération. Avec des chargements quasi instantanés grâce à son SSD ultra-rapide, la PS5 propose des jeux en 4K à 120 FPS pour une immersion totale. La manette DualSense révolutionne l'interaction avec des retours haptiques précis et des gâchettes adaptatives, pour une expérience de jeu inégalée. Que vous soyez fan de jeux d'aventure, de sport ou de tout autre genre, la PS5 promet de repousser les limites du jeu vidéo.",
        "pic2",
        50, 4.7, 649.99,
        "Gaming");

dbHelper.close();
    }

    private void bottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout profile = findViewById(R.id.profile);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", currentUserId); // Passez l'ID de l'utilisateur à ProfileActivity
            startActivity(intent);
        });
    }


    private void initRecyclerView(List<Integer> topRatedItemIds) {
        ArrayList<PopularDomain> items = new ArrayList<>();
        //createSampleItems();
        // Pour chaque ID récupéré, ajoutez l'item correspondant dans la liste s'il existe
        for (int id : topRatedItemIds) {
            PopularDomain item = new PopularDomain(
                    data.getItemTitle(id),
                    data.getItemDescription(id),
                    data.getItemPicUrl(id),
                    data.getItemReview(id),
                    data.getItemScore(id),
                    data.getItemPrice(id),
                    data.getItemCategorie(id)
            );
            items.add(item);
        }

        recyclerViewPopular = findViewById(R.id.view1);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterPopular = new PopularListAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    private void setupClickListener() {
        // Trouvez imageView20 dans le layout
        ImageView imageView20 = findViewById(R.id.imageView20);

        // Définissez un gestionnaire de clics pour imageView20
        imageView20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créez un intent pour ouvrir RechercheActivity
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                // Ajoutez "PC" comme catégorie à rechercher dans RechercheActivity
                intent.putExtra("categorie", "PC");
                // Démarrez l'activité
                startActivity(intent);
            }
        });

        ImageView phoneImageView = findViewById(R.id.imageView22);
        phoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                intent.putExtra("categorie", "Phone");
                startActivity(intent);
            }
        });

        ImageView HeadphoneImageView = findViewById(R.id.imageView24);
        HeadphoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                intent.putExtra("categorie", "HeadPhone");
                startActivity(intent);
            }
        });

        ImageView GamingImageView = findViewById(R.id.imageView26);
        GamingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                intent.putExtra("categorie", "Gaming");
                startActivity(intent);
            }
        });

        ImageView allImageView = findViewById(R.id.imageView28);
        allImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                intent.putExtra("categorie", "Tous");
                startActivity(intent);
            }
        });

        TextView allView = findViewById(R.id.textView9);
        allView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechercheActivity.class);
                intent.putExtra("categorie", "Tous");
                startActivity(intent);
            }
        });
    }


}
