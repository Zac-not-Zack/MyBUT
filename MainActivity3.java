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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity3 extends AppCompatActivity {

    private Intent output;
    private String affichage;
    private EditText output3;
    private ArrayList<BUT> lf;
    private ListView vl;
    private ArrayAdapter<BUT> aaf;
    private ExecutorService exe;
    private List<String> tab = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        output = getIntent();

        Intent action;
        String s;
        String t;
        t = output.getStringExtra("Val2");
        s = output.getStringExtra("Val1");
        System.out.println(s);
        JSONArray jsonArray, sonArray;
        output3= findViewById(R.id.output3);
        output3.setText(t);
        lf = new ArrayList<>();

        vl = findViewById(R.id.listeUE);
        try {//découpage objet JSON récupéré de MainActivity2
            jsonArray = new JSONArray(s);
            sonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json=  new JSONObject();
                int id = jsonArray.getJSONObject(i).getInt("id");
                int semestre = jsonArray.getJSONObject(i).getInt("semestre");
                int nb = jsonArray.getJSONObject(i).getInt("numero");
                String idCom = jsonArray.getJSONObject(i).getString("idCompetence");
                String parcours = jsonArray.getJSONObject(i).getString("parcours");

                //traitement de cas particulier
                if (parcours == "null"){
                    parcours = "Tronc Commun";
                }
                if (idCom=="0"){
                    idCom = "Aucune information associée";

                }

                tab.add("\n" + "ID UE " + ": " + id + "\n" + "Semestre " + ": " + semestre + "\n"
                        + "Numéro dans le semestre " + ": " + nb + "\n" + "Id Compétence " + ": " + idCom + "\n"
                        + "Parcours " + ": " + parcours + "\n" );

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuilder h;

        h = new StringBuilder("");
        for(int j = 0; j<tab.size();j++){

            lf.add(new BUT(tab.get(j)));
        }

        aaf = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        vl.setAdapter(aaf);
        registerForContextMenu(vl);
        aaf.notifyDataSetChanged();

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Actions");
        contextMenu.add(Menu.NONE, view.getId(), 1, "Ressources");
        contextMenu.add(Menu.NONE, view.getId(), 2, "SAE");
    }

    public boolean onContextItemSelected(MenuItem item) {//gestion de MenuContextual
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String s;
        int idUE;
        URL u;
        Intent action;
        System.out.println(info.position);
        if(info.position==0) {//selon position de clique et hold, l'utilisateur va être redirigé vers différente activité
            if (item.getTitle() == "Ressources") {//open activity ressources
                s = "http://infort.gautero.fr/index2022.php?action=get&obj=res&idRes=2";
                action = new Intent(this, Ressources.class);
                String v = "3";
                action.putExtra("IdUE", v);
            } else {//open activity SAE
                s = "http://infort.gautero.fr/index2022.php?action=get&obj=sae&idUe=3";
                action = new Intent(this, SAE.class);
                String v = "3";
                action.putExtra("IdUE", v);
            }

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

            action.putExtra("Val1", s);

            this.startActivity(action);

            aaf.notifyDataSetChanged();
        }
        else {//gestion de cas information indisponible
            action = new Intent(this, Erreur.class);
            s="Pas d'information";
            action.putExtra("Val1", s);
            this.startActivity(action);

            aaf.notifyDataSetChanged();
        }



        return true;
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