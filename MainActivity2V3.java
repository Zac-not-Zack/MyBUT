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

import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity  {

    private Intent output;
    private String affichage;
    private EditText output2;
    private ArrayList<BUT> lf;
    private EditText vl;
    private ArrayAdapter<BUT> aaf;
    private List<String> lol = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        output=getIntent();
        //affichage= output.getStringExtra("Table");
        String s,val;
        s = output.getStringExtra("Val");
        vl= (EditText) findViewById(R.id.idText);

        JSONArray jsonArray,sonArray;
        lf= new ArrayList<>();
        //lf.add(new BUT(affichage));

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
                json.put("id" , id);
                json.put("semestre" , semestre);
                json.put("nb" , nb);
                json.put("idCom" , idCom);
                json.put("parcours",parcours);

                //System.out.println(json.getInt("id"));
                //lf.add(new BUT(json.toString()));
                lol.add(json.toString());
                System.out.println(lol);

                //vl.setText(lol.get(i));
                //vl.setText(lf.get(i).toString());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuilder h;

        h = new StringBuilder("");
        for(int j = 0; j<lol.size();j++){
            h.append(lol.get(j));
            h.append("\n");
        }
        vl.setText(h);
            /*for (int i = 0; i < jsonArray.length(); i++) {
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
             */
        //aaf= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lf);
        //vl.setAdapter(aaf);
       // vl.setOnItemClickListener(this);
        registerForContextMenu(vl);
        //aaf.notifyDataSetChanged();
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
