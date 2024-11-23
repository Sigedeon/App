package com.example.apps;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MesContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> userList;

    private String id_users;

    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mes_contacts);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        id_users = intent.getStringExtra("id_user");

        dataManager = new DataManager(getApplication());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        Toast.makeText(getApplicationContext(), "id :" + id_users, Toast.LENGTH_SHORT).show();

        fetchUsers();
    }

    private void fetchUsers() {

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
                            JSONArray users = response.getJSONArray("data");

                            userList.clear();
                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                String nom = user.getString("nom");
                                String phone = user.getString("phone");
                                String decision = user.getString("decision");
                                String id = user.getString("id");

                                userList.add(new User(nom, decision, phone));
                            }
                            userAdapter.notifyDataSetChanged();
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
}