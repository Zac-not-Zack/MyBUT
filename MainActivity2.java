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
import android.widget.TextView;
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
    private TextView txt;
    private ArrayAdapter<BUT> aaf;
    private List<String> lol = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        output=getIntent();
        String s,val;
        StringBuilder r;
        r = new StringBuilder("");
        s = output.getStringExtra("Val1");
        r.append(" ID     |        Spécialité     " + "\n");
        r.append("-------------------------------------------------------------------------------------" + "\n");
        txt = (TextView)findViewById(R.id.textView3);

        JSONArray jsonArray,sonArray;
        lf= new ArrayList<>();
        txt.setText(r);
        vl= findViewById(R.id.listeBUT);
        try {//découpage objetJSON - listeBUT
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

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String s, t;
        URL u;
        Intent action;

        if( i == 0) {//récupération des infos sur UEs et envoyer à MainActivity3
            t = "Les Unités d'Enseignement (UE) de ";

            t = t + "R&T";
            s = "http://infort.gautero.fr/index2022.php?action=get&obj=ue&idBut=1";
            try {
                u = new URL(s);
            } catch (MalformedURLException e) {

                e.printStackTrace();
                u = null;
            }


            exe = Executors.newSingleThreadExecutor();

            todo = lireURL(u);

            try {
                s = todo.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            action = new Intent(this, MainActivity3.class);
            action.putExtra("Val1", s);
            action.putExtra("Val2", t);
            this.startActivity(action);


            aaf.notifyDataSetChanged();

        }
        else{//gestion de cas information indisponible
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}