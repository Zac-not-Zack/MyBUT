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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Intent output;
    private ExecutorService exe;
    private Future<String> todo;
    private String affichage;
    private EditText output2;
    private ArrayList<BUT> lf;
    private ListView vl;
    private ArrayAdapter<BUT> aaf;
    private List<String> lol = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        output=getIntent();
        affichage= output.getStringExtra("Table");
        String s,val;
        s = output.getStringExtra("Val1");
        System.out.println(s);
        //vl= (EditText) findViewById(R.id.idText);

        JSONArray jsonArray,sonArray;
        lf= new ArrayList<>();
        lf.add(new BUT(affichage));
        vl= findViewById(R.id.listeBUT);
        try {
            jsonArray = new JSONArray(s);
            sonArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json=  new JSONObject();
                int id = jsonArray.getJSONObject(i).getInt("id");
                String spec = jsonArray.getJSONObject(i).getString("specialite");
                json.put("id" , id);
                json.put("spec",spec);
                lf.add(new BUT(String.valueOf(json.getInt("id")) + "               " + json.get("spec")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        aaf= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        vl.setAdapter(aaf);
        vl.setOnItemClickListener(this);
        registerForContextMenu(vl);
        //aaf.notifyDataSetChanged();
        //titreFilm.setText("");
    }
    /*@Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Actions");
        contextMenu.add(Menu.NONE, view.getId(), 1, "Supprimer");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        lf.remove(info.position);
        aaf.notifyDataSetChanged();
        return true;
    }*/

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        String s;
        URL u;
        Intent action;
        StringBuilder r;


        if( i == 1) {
            r = new StringBuilder("");
            //Automatisation de choix utilisateur
            //String idBut;
            //String tmp = idBut;

            s = "http://infort.gautero.fr/index2022.php?action=get&obj=ue&idBut=1";//inputURL.getText().toString();
            //s = s + idBut
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
            todo = lireURL(u);
            // On attend le résultat
            try {
                s = todo.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            action = new Intent(this, MainActivity3.class);
            action.putExtra("Val1", s);
            this.startActivity(action);

            //lf.remove(info.position);
            aaf.notifyDataSetChanged();
            //return true;
        }
        else{
            s="Pas d'information";
            action = new Intent(this, Erreur.class);
            action.putExtra("Val1", s);
            this.startActivity(action);
        }

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
