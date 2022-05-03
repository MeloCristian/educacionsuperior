package com.saberpro.models;

public class Auth {
    private Boolean sigin;
    private String token;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getSigin() {
        return sigin;
    }

    public void setSigin(Boolean sigin) {
        this.sigin = sigin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
