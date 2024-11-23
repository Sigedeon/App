package com.example.apps;

public class User {
    private String nom;
    private String decision;
    private String phone;

    public User(String nom, String decision, String phone) {
        this.nom = nom;
        this.decision = decision;
        this.phone = phone;
    }

    public String getNom() {
        return nom;
    }

    public String getDecision() {
        return decision;
    }

    public String getPhone() {
        return phone;
    }
}
