package com.example.myapplication;

public class BUT {
    private String titre;

    public BUT () {
        titre= "";
    }

    public BUT (String t) {
        titre= new String(t);
    }

    public void setTitre (String t) {
        titre= new String(t);
    }

    public String getTitre () {
        return titre;
    }

    public String toString () {
        return titre;
    }
}

