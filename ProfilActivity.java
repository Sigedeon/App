package com.example.apps;

import static android.text.TextUtils.substring;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity {
    private TextView textViewAc, textViewRc, textViewRdv, textViewTotal;
    private String id_users;
    private String ac, rc, rdv;

    DataManager dataManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnForm = findViewById(R.id.btnForm);
        Button btnListCont = findViewById(R.id.btnList);
        Button btnPres = findViewById(R.id.btnPres);

        TextView textViewDec = findViewById(R.id.textViewDec);
        textViewRc = findViewById(R.id.textViewRc);
        textViewAc = findViewById(R.id.textViewAc);
        textViewRdv = findViewById(R.id.textViewRdv);
        textViewTotal = findViewById(R.id.textViewTotal);

        TextView textViewNoms = findViewById(R.id.textViewNoms);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        Intent intent = getIntent();

        id_users = intent.getStringExtra("id_user");
        String nom = majuscul(intent.getStringExtra("nom"));
        String prenom = majuscul(intent.getStringExtra("prenom"));

        if (nom == null){
            Intent inten = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(inten);
            finish();
        }else {
            textViewNoms.setText(nom + " " + prenom);
        }

        btnForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                intent.putExtra("id_user", id_users);
                startActivity(intent);
            }
        });

        btnPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PresenceActivity.class);
                startActivity(intent);
            }
        });

        btnListCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MesContactsActivity.class);
                intent.putExtra("id_user", id_users);
                startActivity(intent);
            }
        });
    }

    private void profilUser() {

        String id_user = id_users;
        String url = "https://api.mascodeproduct.com/devApp/actions/listecontact.php";

        Map<String, String> params = new HashMap<>();

        params.put("id_user", id_user);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            ac = response.getString("totalac");
                            rc = response.getString("totalrc");
                            rdv = response.getString("totalrdv");

                            textViewAc.setText(ac + " AC");
                            textViewRc.setText(rc + " RC");
                            textViewRdv.setText(rdv + " RDV");

                            textViewTotal.setText(String.valueOf(Integer.parseInt(ac) + "ggd" + Integer.parseInt(rc) + Integer.parseInt(rdv)));

                        } else {
                            Toast.makeText(this, response.getString("error"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Erreur JSON", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Erreur réseau ou serveur", Toast.LENGTH_LONG).show()
        );

        // Ajouter la requête à la file d'attente de Volley
        dataManager.queue.add(jsonObjectRequest);

    }


    public static String majuscul(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

}