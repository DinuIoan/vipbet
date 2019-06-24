package com.idinu.vipbet.model;

public class AdminCode {
    private String id;
    private Boolean used;

    public AdminCode() {
    }

    public AdminCode(String id, Boolean used) {
        this.id = id;
        this.used = used;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}
