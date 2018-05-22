package fr.cnadal.bookstore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ActionsAdmin extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions_admin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRecupSaga:
                Intent intentRecupSaga = new Intent(this,
                        InfosSagas.class);
                startActivity(intentRecupSaga);
                break;
            case R.id.buttonRecupLivre:
                Intent intentRecupLivres = new Intent(this,
                        InfosLivres.class);
                startActivity(intentRecupLivres);
                break;
            case R.id.buttonModSaga:
                Intent intentModSaga = new Intent(this,
                        ModifierSaga.class);
                startActivity(intentModSaga);
                break;
            case R.id.buttonAddSaga:
                Intent intentAddSagas = new Intent(this,
                        AddSaga.class);
                startActivity(intentAddSagas);
                break;
            case R.id.buttonSuppLivre:
                Intent intentSuppLivres = new Intent(this,
                        SuppLivre.class);
                startActivity(intentSuppLivres);
                break;
            case R.id.buttonSuppSaga:
                Intent intentSuppSaga = new Intent(this,
                        SuppSaga.class);
                startActivity(intentSuppSaga);
                break;
            case R.id.buttonReturn:
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
