package com.example.project1442.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1442.BDD.DatabaseHelper;
import com.example.project1442.R;


public class ProfileViewFragment extends Fragment {

    private TextView profileFirstName, profileLastName, profileUsername;
    private DatabaseHelper databaseHelper;

    public ProfileViewFragment() {
        // Required empty public constructor
    }

    public static ProfileViewFragment newInstance() {
        return new ProfileViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_view, container, false);

        profileFirstName = view.findViewById(R.id.firstNameTextView);
        profileLastName = view.findViewById(R.id.lastNameTextView);
        profileUsername = view.findViewById(R.id.usernameTextView);

        long userId = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getLong("USER_ID", -1);

        // Récupérez l'objet utilisateur de la base de données
        if (userId != -1) {
            User user = databaseHelper.getUserById(userId);
            if (user != null) {
                profileFirstName.setText(String.format("Prénom : %s", user.getFirstName()));
                profileLastName.setText(String.format("Nom : %s", user.getLastName()));
                profileUsername.setText(String.format("Nom d'utilisateur : %s", user.getUsername()));
            } else {
                // Gérer le cas où l'utilisateur n'est pas trouvé
                Toast.makeText(getActivity(), "Utilisateur non trouvé", Toast.LENGTH_LONG).show();
            }
        }

        return view;
    }
}
