package com.example.project1442.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project1442.Adapter.SearchResultAdapter;
import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.Domain.PopularDomain;
import com.example.project1442.R;
import java.util.ArrayList;

public class RechercheActivity extends AppCompatActivity {
    private ArrayList<PopularDomain> items;

    private RecyclerView recyclerViewSearchResults;
    private SearchResultAdapter adapter;
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);

        editTextSearch = findViewById(R.id.editTextSearch); // Déplacer cette ligne ici pour l'initialisation

        String searchTerm = getIntent().getStringExtra("searchTerm");
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SearchResultAdapter(new ArrayList<>());
        recyclerViewSearchResults.setAdapter(adapter);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            performSearch(searchTerm);
        }

        String categorie = getIntent().getStringExtra("categorie");
        if (categorie != null && !categorie.isEmpty()) {
            performSearchByCategorie(categorie);
        }

        if (categorie != null && !categorie.equals("Tous")) {
            performSearchByCategorie(categorie);
        } else {
            performSearchAllSortedByCategorie();
        }

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





    }

    private void performSearchAll() {
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<PopularDomain> searchResults = db.getAllItemsSortedByScore();
        adapter.updateData(searchResults);
    }

    private void performSearchByCategorie(String categorie) {
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<PopularDomain> searchResults = db.getItemsByCategorie(categorie.trim());
        adapter.updateData(searchResults); // Cette ligne suppose que SearchResultAdapter a une méthode updateData
    }

    private void performSearch(String searchTerm) {
        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<PopularDomain> searchResults = db.searchItems(searchTerm.trim());
        adapter.updateData(searchResults);
    }

    public void updateData(ArrayList<PopularDomain> newItems) {
        items.clear(); // Efface les données existantes
        items.addAll(newItems); // Ajoute les nouvelles données
        adapter.notifyDataSetChanged(); // Notifie le RecyclerView que les données ont changé
    }

    private void performSearchAllSortedByCategorie() {
        DatabaseHelper db = new DatabaseHelper(this);

        // Récupération de tous les items triés par catégorie
        ArrayList<PopularDomain> searchResults = db.getAllItemsSortedByCategorie();

        // Mise à jour de l'adapter avec les résultats de recherche
        adapter.updateData(searchResults);
    }
}
