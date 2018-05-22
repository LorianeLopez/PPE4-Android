package fr.cnadal.bookstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModifierSaga extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_saga);
        addItemsOnSpinner();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSaga);
        spinner.setOnItemSelectedListener(this);
    }

    public void addItemsOnSpinner() {

        spinner = (Spinner) findViewById(R.id.spinnerSaga);
        List<String> list = new ArrayList<String>();
        try {
            JSONArray arraySaga = new JSONArray(MenuAccueil.Global.sagas);

            for (int i = 0; i < arraySaga.length(); i++) {
                JSONObject saga = arraySaga.getJSONObject(i);
                list.add(saga.getString("titreSaga"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRet:
                finish();
                break;
            case R.id.buttonOk:
                spinner = (Spinner) findViewById(R.id.spinnerSaga);
                final String titre = spinner.getSelectedItem().toString();
                final EditText editAuteur = (EditText) findViewById(R.id.editAuteur);
                final EditText editNbLivres = (EditText) findViewById(R.id.editNbLivres);

                final String auteur = editAuteur.getText().toString();
                final int livres = Integer.parseInt(editNbLivres.getText().toString());

                RequestQueue queue = Volley.newRequestQueue(this);
                String url = MenuAccueil.Global.ip+"updatesaga";

                JSONObject params = new JSONObject();
                try {
                    params.put("titre_saga",titre);
                    params.put("auteur_saga",auteur);
                    params.put("nb_livres_saga",livres);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, params,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object response) {
                                final TextView mTextView = (TextView) findViewById(R.id.textResult);
                                mTextView.setText("Modification réussie ! Nouvelle données de la Saga " + titre + " : Ecrit par " + auteur + " contenant " + livres + " livres.");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final TextView mTextView = (TextView) findViewById(R.id.textResult);
        mTextView.setText("");
        try {
            JSONArray arraySaga = new JSONArray(MenuAccueil.Global.sagas);
            JSONObject laSaga = arraySaga.getJSONObject(i);
            String auteur = laSaga.getString("auteurSaga");
            Integer nbLivres = laSaga.getInt("nbLivresSaga");
            String lesLivres = nbLivres.toString();
            final EditText auteurEdit = (EditText) findViewById(R.id.editAuteur);
            auteurEdit.setText(auteur, TextView.BufferType.EDITABLE);
            final EditText nbLivresEdit = (EditText) findViewById(R.id.editNbLivres);
            nbLivresEdit.setText(lesLivres, TextView.BufferType.EDITABLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
