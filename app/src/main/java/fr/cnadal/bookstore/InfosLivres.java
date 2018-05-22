package fr.cnadal.bookstore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InfosLivres extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_livres);
        addItemsOnSpinner();
        Spinner spinner = (Spinner) findViewById(R.id.spinnerSaga);
        spinner.setOnItemSelectedListener(this);
        final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void addItemsOnSpinner() {

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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            JSONArray arraySaga = new JSONArray(MenuAccueil.Global.livres);
            JSONObject laSaga = arraySaga.getJSONObject(i);
            String titre = laSaga.getString("titreLivre");
            String resum = laSaga.getString("resumeLivre");
            String prix = String.valueOf(BigDecimal.valueOf(laSaga.getDouble("prixLivre")).floatValue());
            int stock = laSaga.getInt("stockLivre");
            JSONObject leNom = laSaga.getJSONObject("sagaLivres");
            String auteur = leNom.getString("auteurSaga");
            String livre;
            if(stock == 0){
                livre = ". Malheureusement, nous n'en possèdons plus en stock.";
            }else if (stock == 1){
                livre = ". Il ne reste qu'un seul livre en stock.";
            }else{
                livre = ". Il reste " + stock + " livres en stock.";
            }
            final TextView mTextView = (TextView) findViewById(R.id.textViewSaga);
            mTextView.setText("Le livre '" + titre + "', écrit par " + auteur + ", est disponible à " + prix + "€ sur notre site" + livre + " En voici le resumé : \n \n " + resum);
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

