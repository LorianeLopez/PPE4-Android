package fr.cnadal.bookstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PanierActions extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {


    public static class Global {
        public static float prixTotal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier_actions);
        TextView textViewResum = (TextView) findViewById(R.id.textViewContent);
        TextView textViewPrix = (TextView) findViewById(R.id.textViewPrixTotal);
        textViewResum.setMovementMethod(new ScrollingMovementMethod());
        String allBook = "";
        double prixTot = 0;
        for (List lisst : Connexion.Global.contPanier) {
            allBook = allBook + lisst.get(0).toString() + "\n" ;
            prixTot = prixTot + Double.parseDouble(lisst.get(2).toString());
        }
        textViewResum.setText(allBook);
        textViewPrix.setText(textViewPrix.getText() + " " + String.valueOf(prixTot) + " Euros");
        Global.prixTotal = Float.parseFloat(String.valueOf(prixTot));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRet:
                finish();
                break;
            case R.id.buttonVider:
                Connexion.Global.contPanier.clear();
                ArrayList<String> keys = new ArrayList<String>();
                if (Connexion.Global.idQteLivres.length() > 0) {
                    Iterator<String> iter = Connexion.Global.idQteLivres.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        keys.add(key);
                    }
                    for (String key: keys) {
                        Connexion.Global.idQteLivres.remove(key);
                    }
                }
                finish();
                Intent intentPan = new Intent(this,
                        Panier.class);
                startActivity(intentPan);
                break;
            case R.id.buttonVal:
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = MenuAccueil.Global.ip+"validerPanier";

                JSONObject params = new JSONObject();
                try {
                    params.put("numero_utilisateur",Connexion.Global.numero);
                    params.put("prix_total",Global.prixTotal);
                    params.put("livres",Connexion.Global.idQteLivres);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, params,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                Connexion.Global.contPanier.clear();
                                ArrayList<String> keys = new ArrayList<String>();
                                if (Connexion.Global.idQteLivres.length() > 0) {
                                    Iterator<String> iter = Connexion.Global.idQteLivres.keys();
                                    while (iter.hasNext()) {
                                        String key = iter.next();
                                        keys.add(key);
                                    }
                                    for (String key: keys) {
                                        Connexion.Global.idQteLivres.remove(key);
                                    }
                                }
                                finish();
                                Intent intentPan = new Intent(PanierActions.this, Panier.class);
                                startActivity(intentPan);
                                CharSequence resultat = "Merci pour votre achat ! Veuillez vous reconnecter pour voir la commande apparaître.";
                                Toast monMessage = Toast.makeText(PanierActions.this, resultat, Toast.LENGTH_LONG);
                                monMessage.show();
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                final TextView mTextView = (TextView) findViewById(R.id.textResult);
                                mTextView.setText(error.getMessage());
                            }
                        });
                queue.add(jsonObjReq);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
