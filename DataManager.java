package com.example.apps;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DataManager {
    private Context context;

    //Fil d'attente des requetes
    public RequestQueue queue;

    public DataManager(Context context){
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }
}
