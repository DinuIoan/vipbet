package com.example.vipbet.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private Boolean admin;

    public User() {
    }

    public User(String id, String name, Boolean admin) {
        this.id = id;
        this.name = name;
        this.admin = admin;
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
