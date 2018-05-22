package fr.cnadal.bookstore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class SuppLivre extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {

    private Spinner spinner;
    private Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supp_livre);
        addItemsOnSpinner();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSaga);
        spinner.setOnItemSelectedListener(this);
        final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void addItemsOnSpinner() {
        spinner = (Spinner) findViewById(R.id.spinnerSaga);
        List<String> list = new ArrayList<String>();
        try {
            JSONArray arraySaga = new JSONArray(MenuAccueil.Global.livres);

            for (int i = 0; i < arraySaga.length(); i++) {
                JSONObject saga = arraySaga.getJSONObject(i);
                list.add(saga.getString("titreLivre"));
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
            case R.id.buttonSupp:
                spinner = (Spinner) findViewById(R.id.spinnerSaga);
                final String titre = spinner.getSelectedItem().toString();
                try {
                    JSONArray arraySaga = new JSONArray(MenuAccueil.Global.livres);
                    for (int i = 0; i < arraySaga.length(); i++) {
                        JSONObject saga = arraySaga.getJSONObject(i);
                        if(saga.getString("titreLivre").equals(titre)){
                            id = saga.getInt("idLivre");
                        }
                    }
                    RequestQueue queue = Volley.newRequestQueue(this);
                    String url = MenuAccueil.Global.ip+"supplivre";

                    JSONObject params = new JSONObject();
                    try {
                        params.put("id_livre",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            url, params,
                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {
                                    final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
                                    mTextView.setText("Suppression réussie ! Le livre " + titre + " à bien été supprimé.");
                                    spinner.setSelection(0);
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
                                    mTextView.setText(error.getMessage());
                                }
                            });
                    queue.add(jsonObjReq);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
        mTextView.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
