package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText url;
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText) findViewById(R.id.urlInput);
        output = (TextView) findViewById(R.id.textView);
    }

    public void onClick(View v) throws JSONException {
        this.decodeJSON(v);
    }

    public void decodeJSON(View v) {
        String s;
        JSONObject jobj1, jobj2, jobj;
        JSONArray jsonArray;

        int vi;
        StringBuilder r;

        s = url.getText().toString();
        r = new StringBuilder("");

        try {
            jsonArray = new JSONArray(s);
            for (int i= 0; i< jsonArray.length(); i++) {
                int id = jsonArray.getJSONObject(i).getInt("id");
                String spec = jsonArray.getJSONObject(i).getString("specialite");
                r.append("Id - "+ id + " ");
                r.append("Spécialité - " + spec + "\n");
            }
            output.setText(r.toString());

        } catch (JSONException e) {
            e.printStackTrace();

        }


/*
        jobj1 = new JSONObject();
        try {
            jobj1.put("id", 1);
            jobj1.put("specialite", "Reseaux");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jobj2 = new JSONObject();
        try {
            jobj2.put("id", 2);
            jobj2.put("specialite", "Info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray = new JSONArray();
        jsonArray.put(jobj1);
        jsonArray.put(jobj2);
        url.setText(jsonArray.toString());

        [{"id":1,"specialite":"Reseaux"},{"id":2,"specialite":"Info"}]

    }*/
    }
}

