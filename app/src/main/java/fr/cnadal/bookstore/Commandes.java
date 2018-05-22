package fr.cnadal.bookstore;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Commandes extends AppCompatActivity {


    public static class Global {
        public static int sizeComm;
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandes);

        final Commandes activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commandes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_commandes, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final TextView viewStatut = (TextView) rootView.findViewById(R.id.textViewStatut);
            final TextView viewPrix = (TextView) rootView.findViewById(R.id.textViewPrix);
            final TextView viewDate = (TextView) rootView.findViewById(R.id.textViewDate);
            final TextView viewContenu = (TextView) rootView.findViewById(R.id.textViewContenu);
            viewContenu.setMovementMethod(new ScrollingMovementMethod());
            Button button = (Button) rootView.findViewById(R.id.buttonRet);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.buttonRet:
                            getActivity().finish();
                            break;
                    }
                }
            });
            if(Global.sizeComm == 0) {
                textView.setText("Commande 0");
                viewContenu.setText("Vous n'avez passé aucune commande.");
            }else {

                final RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                try {
                    JSONArray arr = new JSONArray(Connexion.Global.commandes);
                    int numero = getArguments().getInt(ARG_SECTION_NUMBER);
                    String num;
                    if (numero == 1) {
                        JSONObject obj = arr.getJSONObject(numero);
                        num = obj.getString("idCommande");
                        textView.setText("Commande N°" + num);
                    } else {
                        int soustract = numero - 1;
                        JSONObject obj = arr.getJSONObject(numero + soustract);
                        num = obj.getString("idCommande");
                        textView.setText("Commande N°" + num);
                    }
                    String url = MenuAccueil.Global.ip + "apiUnContenu/" + num;
                    StringRequest stringRequestGet = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONArray arrContenu = new JSONArray(response);
                                        JSONObject obj1 = arrContenu.getJSONObject(0);
                                        JSONObject obj2 = obj1.getJSONObject("idcommande");
                                        String prixTotal = String.valueOf(BigDecimal.valueOf(obj2.getDouble("prixtotal")).floatValue());
                                        String date = obj2.getString("date");
                                        JSONObject stat = obj2.getJSONObject("statut");
                                        String statut = stat.getString("libellestatut");
                                        viewStatut.setText(viewStatut.getText() + statut);
                                        viewPrix.setText(viewPrix.getText() + prixTotal + " €");
                                        viewDate.setText(viewDate.getText() + date);
                                        for (int i = 0; i < arrContenu.length(); i++) {
                                            obj1 = arrContenu.getJSONObject(i);
                                            JSONObject obj3 = obj1.getJSONObject("idlivre");
                                            String titre = obj3.getString("titreLivre");
                                            JSONObject saga = obj3.getJSONObject("sagaLivres");
                                            String auteur = saga.getString("auteurSaga");
                                            String prix = String.valueOf(BigDecimal.valueOf(obj3.getDouble("prixLivre")).floatValue());
                                            viewContenu.setText(viewContenu.getText() + titre + ", de " + auteur + " à " + prix + " euros. \n \n");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {        // CAS d’ERREUR
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            viewContenu.setText("NOT OK");
                        }
                    });
                    queue.add(stringRequestGet);
                    int MY_SOCKET_TIMEOUT_MS = 40000;

                    stringRequestGet.setRetryPolicy(new DefaultRetryPolicy(
                            MY_SOCKET_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            int nbCommandes = Connexion.Global.nbCommandes;
            if(nbCommandes == 0){
                nbCommandes = 1;
                Global.sizeComm = 0;
            }else{
                Global.sizeComm = 1;
            }
            return nbCommandes;
        }

    }
}
