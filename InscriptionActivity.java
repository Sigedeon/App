package com.example.apps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {
    private Spinner spinnerTeams;
    private String selectedTeam;
    private static final int DEFAULT_POSITION = 0;

    private EditText editTextNom, editTextPrenom, editTextEmail, editTextPhone, editTextMdp1, editTextMdp;

    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        editTextNom = findViewById(R.id.EditTextNom);
        editTextPrenom = findViewById(R.id.EditTextPrenom);
        editTextEmail = findViewById(R.id.EditTextEmail);
        editTextPhone = findViewById(R.id.EditTextPhone);
        editTextMdp1 = findViewById(R.id.EditTextMdp1);
        editTextMdp = findViewById(R.id.EditTextMdp);

        dataManager = new DataManager(getApplicationContext());

        spinnerTeams = findViewById(R.id.spinner);

        // Charger les équipes depuis le fichier XML
        String[] teams = getResources().getStringArray(R.array.items_array);

        // Créer l'adaptateur pour le Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                teams
        );

        // Appliquer l'adaptateur au Spinner
        spinnerTeams.setAdapter(adapter);

        // Gérer la sélection d'un élément
        spinnerTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTeam = parent.getItemAtPosition(position).toString(); // Stocker l'équipe sélectionnée
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTeam = null; // Rien n'est sélectionné
            }
        });

        Button btnInscrip = findViewById(R.id.btnInscrip);
        TextView textViewCon = findViewById(R.id.textViewCon);

        btnInscrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        textViewCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(){
        String nom = editTextNom.getText().toString().trim();
        String prenom = editTextPrenom.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String equipe = selectedTeam;
        String mdp = editTextMdp.getText().toString().trim();

        String url = "https://api.mascodeproduct.com/devApp/actions/registeruser.php";
        Map<String, String> params = new HashMap<>();

        params.put("nom", nom);
        params.put("prenom", prenom);
        params.put("email", email);
        params.put("phone", phone);
        params.put("equipe", equipe);
        params.put("mdp", mdp);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                            Toast.makeText(getApplicationContext(), "Vous êtes inscrit avec succès", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            String errorMessage = response.getString("error");
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erreur de traitement des données", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error instanceof NetworkError || error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(), "Problème réseau ou serveur", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Ajouter la requête à la file d'attente de Volley
        dataManager.queue.add(jsonObjectRequest);
    }

}
