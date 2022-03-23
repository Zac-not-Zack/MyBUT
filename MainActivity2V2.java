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

import java.time.temporal.ValueRange;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity  {

    private Intent output;
    private String affichage;
    private EditText output2;
    private ArrayList<BUT> lf;
    private ListView vl;
    private ArrayAdapter<BUT> aaf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        output=getIntent();
        affichage= output.getStringExtra("Table");
        lf.add(new BUT(affichage));
        String s;
        s = output.getStringExtra("Val");
        System.out.println(s);
        JSONArray jsonArray,sonArray;
        try {
            jsonArray = new JSONArray(s);
            sonArray = new JSONArray();
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject json=  new JSONObject();
                int id = jsonArray.getJSONObject(i).getInt("id");
                String spec = jsonArray.getJSONObject(i).getString("specialite");
                json.put("id" , id);
                json.put("spec",spec);
                lf.add(new BUT(json));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lf.add(new BUT(s));
        //output2= findViewById(R.id.output2);
        //output2.setText(affichage);
        lf= new ArrayList<>();
        vl= findViewById(R.id.listeFilms);
        aaf= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        vl.setAdapter(aaf);
       // vl.setOnItemClickListener(this);
        registerForContextMenu(vl);
        //lf.add(new BUT(affichage));
        aaf.notifyDataSetChanged();
            //titreFilm.setText("");
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Actions");
        contextMenu.add(Menu.NONE, view.getId(), 1, "Supprimer");
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        lf.remove(info.position);
        aaf.notifyDataSetChanged();
        return true;
    }
}
