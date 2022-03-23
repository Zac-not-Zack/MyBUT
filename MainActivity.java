package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private EditText inputURL;
    private EditText output;
    private ExecutorService exe;
    private Future<String> todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inputURL = (EditText) findViewById(R.id.urlInput);
        output = (EditText) findViewById(R.id.output);
    }

    public void onClick(View v) {
        String s;
        URL u;
        Intent action;

        s = "http://infort.gautero.fr/index2022.php?action=get&obj=but";//inputURL.getText().toString();

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
        // On affiche le résultat sur une autre activité
        output.setText(s);

        action = new Intent(this, MainActivity2.class) ;
        action.putExtra("Val1",s);
        this.startActivity(action) ;
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