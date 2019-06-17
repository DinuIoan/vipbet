package com.example.vipbet.model;

public class Prediction {
    private Long id;
    private String home;
    private String away;
    private String prediction;
    private String date;

    public Prediction() {
    }

    public Prediction(String home, String away, String prediction, String date) {
        this.home = home;
        this.away = away;
        this.prediction = prediction;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
