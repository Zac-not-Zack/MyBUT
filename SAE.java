package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SAE extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private Intent output;
    private Future<String> todo;
    private String affichage;
    private EditText outputSae;
    private ArrayList<BUT> lf;
    private ListView vl;
    private ExecutorService exe;
    private ArrayAdapter<BUT> aaf;
    private List<String> tab = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sae);

        output = getIntent();
        String s;
        s = output.getStringExtra("Val1");
        JSONArray jsonArray, sonArray;
        lf = new ArrayList<>();
        vl = findViewById(R.id.listeSAE);
        try {//découpage objet JSON contenant les infos sur les ressources d'un SAE - récupéré de MainActivity3
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
                String projet = jsonArray.getJSONObject(i).getString("projet");
                String[] pr = projet.split(":");
                pr[0] = pr[0] + " heures";

                tab.add("ID SAE " + ": " + id + "\n" + "Nom " + ": " + nom + "\n"
                        + "Cours " + ": " + crs[0]  + "\n" + "TD " + ": " + td[0] + "\n"
                        + "TP " + ": " + tp[0] + "\n" + "Projet " + ": " + pr[0] + "\n"  + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int j = 0; j<tab.size();j++){
            lf.add(new BUT(tab.get(j)));
        }

        output = getIntent();
        affichage = output.getStringExtra("IdUE");
        outputSae= findViewById(R.id.outputSae);
        affichage = "Les SAE d'UE "+ affichage;
        outputSae.setText(affichage);
        aaf = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        vl.setAdapter(aaf);
        vl.setOnItemClickListener(this);
        registerForContextMenu(vl);
        aaf.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//affichage info plus détaillée d'un SAE

        String s;
        URL u;
        Intent action;
        if(i==0) {//Récupération de différentes infos selon où l'utilisateur clique
            s = "http://infort.gautero.fr/index2022.php?action=get&obj=sae&idSae=2";
            }
        else {
            s = "http://infort.gautero.fr/index2022.php?action=get&obj=sae&idSae=3";
            }

             action = new Intent(this, MainActivity4.class);
            try {
                u = new URL(s);
            } catch (MalformedURLException e) {

                e.printStackTrace();
                u = null;
            }

            exe = Executors.newSingleThreadExecutor();

            Future<String> todo = lireURL(u);

            try {
                s = todo.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(s);
            action.putExtra("Val1", s);
            this.startActivity(action);

            aaf.notifyDataSetChanged();

    }


    public Future<String> lireURL(URL u) {
        return exe.submit(() -> {
            URLConnection c;
            String inputline;
            StringBuilder codeHTML = new StringBuilder("");
            try {
                c = u.openConnection();

                c.setConnectTimeout(60000);

                c.setReadTimeout(60000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(c.getInputStream(), "UTF-8"));
                while ((inputline = in.readLine()) != null) {

                    codeHTML.append(inputline + "\n");
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codeHTML.toString();
        });
    }
}