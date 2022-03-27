package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class SAE extends AppCompatActivity {
    private Intent output;
    private String affichage;
    private EditText outputSAE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sae);

        output=getIntent();
        affichage= output.getStringExtra("Val1");
        outputSAE= findViewById(R.id.outputSAE);
        outputSAE.setText(affichage);
    }
}