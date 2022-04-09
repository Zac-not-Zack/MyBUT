package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class Erreur extends AppCompatActivity {

    private Intent output;
    private String affichage;
    private EditText output4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erreur);

        output=getIntent();
        affichage= output.getStringExtra("Val1");
        output4= findViewById(R.id.output4);
        output4.setText(affichage);
    }
    public void onClick(View v) {
        Intent action;
        action = new Intent(this, MainActivity.class);
        this.startActivity(action);
    }

}