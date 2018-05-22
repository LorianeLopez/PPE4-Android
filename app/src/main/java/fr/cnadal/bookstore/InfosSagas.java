package fr.cnadal.bookstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InfosSagas extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_sagas);
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            JSONArray arraySaga = new JSONArray(MenuAccueil.Global.sagas);
            JSONObject laSaga = arraySaga.getJSONObject(i);
            String titre = laSaga.getString("titreSaga");
            String auteur = laSaga.getString("auteurSaga");
            int nbLivres = laSaga.getInt("nbLivresSaga");
            String livre;
            if(nbLivres == 1){
                livre = ". Cette saga ne possède qu'un seul livre.";
            }else{
                livre = ". De plus, cette saga possède " + nbLivres + " livres.";
            }
            final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
            mTextView.setText("L'auteur de la saga " + titre + " est " + auteur + livre);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
