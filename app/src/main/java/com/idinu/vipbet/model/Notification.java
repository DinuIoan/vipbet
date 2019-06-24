package com.idinu.vipbet.model;

public class Notification {
    private Long id;
    private String message;
    private String title;
    private String date;

    public Notification() {
    }

    public Notification(String message, String title, String date) {
        this.message = message;
        this.title = title;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
