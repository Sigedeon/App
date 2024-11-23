package com.example.apps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextIdentifiant, editTextMdp;
    private DataManager dataManager;

//    private SharedPreferences pref;
//    private SharedPreferences.Editor editor;
//    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        editTextIdentifiant = findViewById(R.id.EditTextId);
        editTextMdp = findViewById(R.id.EditTextMdp);

        dataManager = new DataManager(getApplicationContext());

        Button btnCon = findViewById(R.id.btnCon);
        TextView textViewInsc = findViewById(R.id.textViewInsc);

        textViewInsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InscriptionActivity.class);
                startActivity(intent);
            }
        });


        btnCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public void login(){
        String email = editTextIdentifiant.getText().toString().trim();
        String mdp = editTextMdp.getText().toString().trim();

        String url = "https://api.mascodeproduct.com/devApp/actions/login.php";
        Map<String, String> params = new HashMap<>();

        params.put("email", email);
        params.put("mdp", mdp);

        JSONObject jsonObject = new JSONObject(params);

        // Requête POST avec Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            // Récupérer les données utilisateur
                            String id = response.getString("id");
                            String noms = response.getString("nom");
                            String emailFetched = response.getString("email");
                            String prenom = response.getString("prenom");
                            String phone = response.getString("phone");
                            String equipe = response.getString("equipe");

                            String ac = response.getString("totalac");
                            String rc = response.getString("totalrc");
                            String rdv = response.getString("totalrdv");

                            // Afficher un message de bienvenue
                            Toast.makeText(getApplicationContext(), "Bienvenue, " + noms, Toast.LENGTH_SHORT).show();

                            // Rediriger vers ProfilActivity
                            Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
                            intent.putExtra("id_user", id);
                            intent.putExtra("nom", noms);
//                            intent.putExtra("email", emailFetched);
                            intent.putExtra("prenom", prenom);
//                            intent.putExtra("phone", phone);
                            intent.putExtra("equipe", equipe);
//
//                            intent.putExtra("totalac", ac);
//                            intent.putExtra("totalrc", rc);
//                            intent.putExtra("totalrdv", rdv);
                            startActivity(intent);
                            finish();
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
                    // Gestion des erreurs réseau ou serveur
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