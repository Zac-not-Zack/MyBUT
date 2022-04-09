package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText inputURL;
    private EditText output;
    private ExecutorService exe;
    private Future<String> todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v) {
        String s;
        URL u;
        Intent action;
        JSONObject jobj;
        JSONArray jsonArray,sonArray;

        StringBuilder r;

        r = new StringBuilder("");

        s = "http://infort.gautero.fr/index2022.php?action=get&obj=but";

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

        action = new Intent(this, MainActivity2.class) ;

        action.putExtra("Val1",s);
        this.startActivity(action) ;


    }

    public Future<String> lireURL(URL u) {//cx réseaux
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