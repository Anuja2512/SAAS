package com.example.pokedoc;

public class Doctor {

    private String username;
    private String names;

    public Doctor(String username,String names){
        this.username=username;
        this.names=names;
    }
    public Doctor(){

    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
