package fr.cnadal.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class AcessUsers extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acess_users);
        final TextView mTextView = (TextView) findViewById(R.id.textViewFullName);
        mTextView.setText("Bienvenue " + Connexion.Global.prenom + " " + Connexion.Global.nom);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCom:
                Intent intentStores = new Intent(this,
                        Commandes.class);
                startActivity(intentStores);
                break;
            case R.id.buttonProd:
                Intent intentProd = new Intent(this,
                        Products.class);
                startActivity(intentProd);
                break;
            case R.id.buttonPanier:
                Intent intentPan = new Intent(this,
                        Panier.class);
                startActivity(intentPan);
                break;
            case R.id.buttonOut:
                Connexion.Global.connected = Boolean.FALSE;
                Connexion.Global.nom = "";
                Connexion.Global.prenom = "";
                Connexion.Global.role = "";
                Connexion.Global.numero = "";
                Connexion.Global.commandes = "";
                Connexion.Global.nbCommandes = 0;
                Intent intentAcc = new Intent(this,
                        MenuAccueil.class);
                startActivity(intentAcc);
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
