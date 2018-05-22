package fr.cnadal.bookstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class AddSaga extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saga);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOk:
                final EditText editTitle = (EditText) findViewById(R.id.editTitre);
                final EditText editAuteur = (EditText) findViewById(R.id.editAuteur);
                final EditText editNbLivres = (EditText) findViewById(R.id.editNbLivres);

                final String titre = editTitle.getText().toString();
                final String auteur = editAuteur.getText().toString();
                final int livres = Integer.parseInt(editNbLivres.getText().toString());

                // Instantiate the RequestQueue. La requête sera gérée par Volley
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = MenuAccueil.Global.ip+"ajoutsaga/";

                JSONObject params = new JSONObject();
                try {
                    params.put("titreSaga",titre);
                    params.put("auteurSaga",auteur);
                    params.put("nbLivresSaga",livres);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, params,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                final TextView mTextView = (TextView) findViewById(R.id.textResult);
                                mTextView.setText("Ajout réussis ! Données de la nouvelle Saga : " + titre + " écrit par " + auteur + " contenant " + livres + " livres.");
                                editTitle.setText("");
                                editAuteur.setText("");
                                editNbLivres.setText("");
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                final TextView mTextView = (TextView) findViewById(R.id.textResult);
                                mTextView.setText(error.getMessage());
//                                mTextView.setText("Ajout échoué... Réessayez en étant sûr que cette saga n'est pas déjà présente dans notre bdd et que les paramètres envoyés sont correctes.");
                            }
                        });
                // Adding the request to the queue along with a unique string tag
                queue.add(jsonObjReq);
                break;
            case R.id.buttonRet:
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }


}

