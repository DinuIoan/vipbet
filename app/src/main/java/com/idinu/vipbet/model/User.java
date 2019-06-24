package com.idinu.vipbet.model;

public class User {
    private String id;
    private String name;
    private Boolean admin;
    private String date;

    public User() {
    }

    public User(String id, String name, Boolean admin, String date) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
