package fr.cnadal.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connexion extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    public static class Global {
        public static Boolean connected = Boolean.FALSE;
        public static String nom;
        public static String prenom;
        public static String role;
        public static String numero;
        public static String commandes;
        public static int nbCommandes;
        public static ArrayList<List> contPanier = new ArrayList<List>();
        public static JSONObject idQteLivres = new JSONObject();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonFinish:
                Global.connected = Boolean.FALSE;
                Global.nom = "";
                Global.prenom = "";
                Global.role = "";
                Global.numero = "";
                Global.commandes = "";
                Global.nbCommandes = 0;
                finish();
                break;
            case R.id.buttonConnect:
                final EditText editNum = (EditText) findViewById(R.id.editTextNum);
                final EditText editCode = (EditText) findViewById(R.id.editTextCode);

                final String num = editNum.getText().toString();
                final String code = editCode.getText().toString();

                final RequestQueue queue = Volley.newRequestQueue(this);
                String url = MenuAccueil.Global.ip+"Utilisateurs";

                final TextView mTextView = (TextView) findViewById(R.id.textResult);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Sucess")) {
                            editNum.setText("");
                            editCode.setText("");
                            mTextView.setText("Connexion effectuée !");
                            Global.connected = Boolean.TRUE;
                        }else{
                            mTextView.setText("Identifiants Incorrects, veuillez réessayer.");
                        }
                        if(Global.connected.equals(Boolean.TRUE)){
                            String url = MenuAccueil.Global.ip+"apiUser/"+num;
                            StringRequest stringRequestGet = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                Global.role = obj.getString("roles");
                                                Global.nom = obj.getString("nom");
                                                Global.prenom = obj.getString("prenom");
                                                Global.numero = obj.getString("numero");
                                                if(Global.role.equals("[\"ROLE_USER\"]")){
                                                        String url = MenuAccueil.Global.ip+"getCommandes/"+Global.numero;
                                                        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                                                                new Response.Listener<JSONArray>(){
                                                                    @Override
                                                                    public void onResponse(JSONArray response) {
                                                                        Global.commandes = response.toString();
                                                                        try {
                                                                            JSONObject nb = response.getJSONObject(0);
                                                                            Global.nbCommandes = Integer.parseInt(nb.getString("nbCommandes"));
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                },
                                                                new Response.ErrorListener() {
                                                                    @Override
                                                                    public void onErrorResponse(VolleyError error) {
                                                                        Log.d("Error.Response", error.toString());
                                                                    }
                                                                }
                                                        );
                                                        queue.add(getRequest);
                                                    Intent intentProducts = new Intent(Connexion.this, AcessUsers.class);
                                                    startActivity(intentProducts);
                                                }else if(Global.role.equals("[\"ROLE_ADMIN\"]")){
                                                    Intent intentAdmin = new Intent(Connexion.this, AcessAdmin.class);
                                                    startActivity(intentAdmin);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {        // CAS d’ERREUR
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mTextView.setText("Erreur, " + error.getMessage());
                                }
                            });
                            queue.add(stringRequestGet);
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTextView.setText("Erreur, " + error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("_username", num);
                        params.put("_password", code);
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);

                break;
        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }



}

