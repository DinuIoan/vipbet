package com.idinu.vipbet.model;

public class Prediction {
    private Long id;
    private String home;
    private String away;
    private String prediction;
    private String date;
    private String odd;
    private String league;

    public Prediction() {
    }

    public Prediction(String home, String away, String prediction, String date, String odd, String league) {
        this.home = home;
        this.away = away;
        this.prediction = prediction;
        this.date = date;
        this.odd = odd;
        this.league = league;
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

    public String getOdd() {
        return odd;
    }

    public void setOdd(String odd) {
        this.odd = odd;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
