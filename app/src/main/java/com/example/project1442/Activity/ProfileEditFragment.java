package com.example.project1442.Activity;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.R;


public class ProfileEditFragment extends Fragment {

    private EditText editFirstName, editLastName, editUsername;
    private Button btnSave;
    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate le layout pour ce fragment
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        db = new DatabaseHelper(getActivity());

        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editUsername = view.findViewById(R.id.editUsername);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserChanges();
            }
        });

        return view;
    }

    private void saveUserChanges() {
        long userId = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getLong("USER_ID", -1);

        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        String username = editUsername.getText().toString();

        if (userId != -1 && db.updateUser(userId, firstName, lastName, username) > 0) {
            Toast.makeText(getActivity(), "Informations mises à jour avec succès.", Toast.LENGTH_SHORT).show();
            // Rediriger ou rafraîchir les données affichées
        } else {
            Toast.makeText(getActivity(), "Erreur lors de la mise à jour des informations.", Toast.LENGTH_SHORT).show();
        }
    }
}
