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
    private Future<String> todo;
    private List<String> tab = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        output = getIntent();
        //affichage = output.getStringExtra("Table");
        String s;
        s = output.getStringExtra("Val1");
        JSONArray jsonArray, sonArray;
        //output2= findViewById(R.id.output2);
        //output2.setText(affichage);
        lf = new ArrayList<>();
        //lf.add(new BUT(affichage));
        vl = findViewById(R.id.listeUE);
        try {
            jsonArray = new JSONArray(s);
            sonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json=  new JSONObject();
                int id = jsonArray.getJSONObject(i).getInt("id");
                int semestre = jsonArray.getJSONObject(i).getInt("semestre");
                int nb = jsonArray.getJSONObject(i).getInt("numero");
                int idCom = jsonArray.getJSONObject(i).getInt("idCompetence");
                String parcours = jsonArray.getJSONObject(i).getString("parcours");
                if (parcours == "null"){
                    parcours = "Tronc Commun";
                }
                tab.add("ID UE " + ": " + id + "\n" + "Semestre " + ": " + semestre + "\n"
                + "Numéro " + ": " + nb + "\n" + "IdCompétence " + ": " + idCom + "\n"
                + "Parcours " + ": " + parcours + "\n" + "\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int j = 0; j<tab.size();j++){
            lf.add(new BUT(tab.get(j)));
        }
        //vl.setText(h);
        aaf = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        vl.setAdapter(aaf);
        // vl.setOnItemClickListener(this);
        registerForContextMenu(vl);
        aaf.notifyDataSetChanged();
        //titreFilm.setText("");
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Actions");
        contextMenu.add(Menu.NONE, view.getId(), 1, "Ressources");
        contextMenu.add(Menu.NONE, view.getId(), 2, "SAE");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String s;
        URL u;
        Intent action;
        System.out.println(info.position);
        if(info.position==0) {
            if (item.getTitle() == "Ressources") {//open activity ressources
                s = "http://infort.gautero.fr/index2022.php?action=get&obj=res&idRes=2";
                action = new Intent(this, Ressources.class);
            } else {//open activity SAE
                s = "http://infort.gautero.fr/index2022.php?action=get&obj=sae&idUe=3";
                action = new Intent(this, SAE.class);
            }

            try {
                u = new URL(s);
            } catch (MalformedURLException e) {
                // Ce n'est qu'un exemple, pas de traitement propre de l'exception
                e.printStackTrace();
                u = null;
            }
            // On crée l'objet qui va gérer la thread
            exe = Executors.newSingleThreadExecutor();
            // On lance la thread
            Future<String> todo = lireURL(u);
            // On attend le résultat
            try {
                s = todo.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            action.putExtra("Val1", s);
            this.startActivity(action);
            //lf.remove(info.position);
            aaf.notifyDataSetChanged();
        }
        else {
            action = new Intent(this, Erreur.class);
            s="Pas d'information";
            action.putExtra("Val1", s);
            this.startActivity(action);
            //lf.remove(info.position);
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
                //temps maximun alloué pour se connecter
                c.setConnectTimeout(60000);
                //temps maximun alloué pour lire
                c.setReadTimeout(60000);
                //flux de lecture avec l'encodage des caractères UTF-8
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(c.getInputStream(), "UTF-8"));
                while ((inputline = in.readLine()) != null) {
                    //concaténation+retour à la ligne avec \n
                    codeHTML.append(inputline + "\n");
                }
                //il faut bien fermer le flux de lecture
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return codeHTML.toString();
        });
    }
}
