package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Ressources extends AppCompatActivity {
    private Intent output;
    private String affichage;
    private EditText output3;
    private ArrayList<BUT> lf;
    private ListView vl;
    private ArrayAdapter<BUT> aaf;
    private ExecutorService exe;
    private Future<String> todo;
    private List<String> tab = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ressources);

        output = getIntent();
        String s;
        s = output.getStringExtra("Val1");
        System.out.println(s);
        JSONArray jsonArray, sonArray;
        lf = new ArrayList<>();
        vl = findViewById(R.id.listeUE);

        try {//découpage objet JSON contenant les infos sur les ressources d'une UE - récupéré de MainActivity3
            jsonArray = new JSONArray(s);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json=  new JSONObject();
                int id = jsonArray.getJSONObject(i).getInt("id");
                String nom = jsonArray.getJSONObject(i).getString("nom");
                String cours = jsonArray.getJSONObject(i).getString("cours");
                String [] crs = cours.split(":");
                crs[0] = crs[0] + " heures";
                String tdirige = jsonArray.getJSONObject(i).getString("td");
                String[] td = tdirige.split(":");
                td[0] = td[0] + " heures";
                String tpratique = jsonArray.getJSONObject(i).getString("tp");
                String[] tp = tpratique.split(":");
                tp[0] = tp[0] + " heures";
                String desc = jsonArray.getJSONObject(i).getString("description");
                if(desc == " "){
                    desc = "Pas de description";
                }
                System.out.println(desc);
                tab.add("\n" +"ID Res " + ": " + id + "\n" + "Nom " + ": " + nom + "\n"
                        + "Cours " + ": " + crs[0]  + "\n" + "TD " + ": " + td[0] + "\n"
                        + "TP " + ": " + tp[0] + "\n" + "Description " + ": " + desc +  "\n"  + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int j = 0; j<tab.size();j++){

            lf.add(new BUT(tab.get(j)));
        }

        output = getIntent();
        affichage = output.getStringExtra("IdUE");
        output3= findViewById(R.id.outputRes);
        affichage = "Les ressources d'UE "+ affichage;
        output3.setText(affichage);
        aaf = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        vl.setAdapter(aaf);
        registerForContextMenu(vl);
        aaf.notifyDataSetChanged();

    }

}
