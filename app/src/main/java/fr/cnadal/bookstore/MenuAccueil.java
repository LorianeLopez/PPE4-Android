package fr.cnadal.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

public class MenuAccueil extends AppCompatActivity implements View.OnTouchListener, OnClickListener  {

    public static class Global {
        public static String sagas;
        public static String livres;
        public static int nbSagas;
//        final public static String ip = "http://10.0.230.66:86/";
        final public static String ip = "http://192.168.1.62:86/";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_accueil);
        Connexion.Global.connected = Boolean.FALSE;
        Connexion.Global.role = "";
        Connexion.Global.prenom = "";
        Connexion.Global.nom = "";
        Connexion.Global.numero = "";
        Connexion.Global.commandes = "";
        Connexion.Global.nbCommandes = 0;
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.ip+"apiAllProduit";
        StringRequest stringRequestGet = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Global.sagas = response;
                        try {
                            JSONArray arr = new JSONArray(response);
                            Global.nbSagas = arr.length();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {        // CAS d’ERREUR
            @Override
            public void onErrorResponse(VolleyError error) {
                Global.sagas = "Erreur, " + error.getMessage();
            }
        });
        queue.add(stringRequestGet);
        String url2 = Global.ip+"apiAllBook";
        StringRequest stringRequestGetBook = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Global.livres = response;
                    }
                }, new Response.ErrorListener() {        // CAS d’ERREUR
            @Override
            public void onErrorResponse(VolleyError error) {
                Global.livres = "Erreur, " + error.getMessage();
            }
        });
        queue.add(stringRequestGetBook);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonConnexion:
                Intent intentConnexion = new Intent(this,
                        Connexion.class);
                startActivity(intentConnexion);
                break;
            case R.id.buttonStore:
                Intent intentStores = new Intent(this,
                        Stores.class);
                startActivity(intentStores);
                break;
            case R.id.buttonProd:
                Intent intentProd = new Intent(this,
                        Products.class);
                startActivity(intentProd);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}


