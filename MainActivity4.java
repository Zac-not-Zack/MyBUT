package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity4 extends AppCompatActivity {

    private Intent output;
    private String affichage;
    private EditText output2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        output=getIntent();
        affichage= output.getStringExtra("Val1");
        output2= findViewById(R.id.output2);
        output2.setText(affichage);
    }
}