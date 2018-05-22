package fr.cnadal.bookstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Statistiques extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiques);
        final TextView clients = (TextView) findViewById(R.id.textViewClients);
        final TextView prods = (TextView) findViewById(R.id.textViewProds);
        final TextView commandes = (TextView) findViewById(R.id.textViewComVal);
        final TextView ca = (TextView) findViewById(R.id.textViewCA);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = MenuAccueil.Global.ip+"apiStats";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Integer nbClient = obj.getInt("nbClients");
                            Integer nbProd = obj.getInt("nbLivres");
                            Integer nbCommandes = obj.getInt("nbCommandesVal");
                            String CA = String.valueOf(BigDecimal.valueOf(obj.getDouble("CA")).floatValue());
                            clients.setText(clients.getText() + " " + nbClient);
                            prods.setText(prods.getText() + " " + nbProd);
                            commandes.setText(commandes.getText() + " " + nbCommandes);
                            ca.setText(ca.getText() + " " + CA + " €");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {        // CAS d’ERREUR
            @Override
            public void onErrorResponse(VolleyError error) {
                clients.setText("Erreur, " + error.getMessage());
                prods.setText("Erreur, " + error.getMessage());
                commandes.setText("Erreur, " + error.getMessage());
                ca.setText("Erreur, " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRet:
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
