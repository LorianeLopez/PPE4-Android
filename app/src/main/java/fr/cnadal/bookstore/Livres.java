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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Livres extends AppCompatActivity {

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
        setContentView(R.layout.activity_livres);

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
        getMenuInflater().inflate(R.menu.menu_livres, menu);
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
            final View rootView = inflater.inflate(R.layout.fragment_livres, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final TextView textViewPrix = (TextView) rootView.findViewById(R.id.section_prix);
            final TextView textViewDesc = (TextView) rootView.findViewById(R.id.section_resume);
            textViewDesc.setMovementMethod(new ScrollingMovementMethod());
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
            String nameSag = Products.Global.nomSaga;
            try {
                String query = URLEncoder.encode(nameSag, "utf-8");
                final RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url2 = MenuAccueil.Global.ip + "apiLivresSagas/" + query;
                StringRequest stringRequestGetBook = new StringRequest(Request.Method.GET, url2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray arr = new JSONArray(response);
                                    int numero = getArguments().getInt(ARG_SECTION_NUMBER);
                                    JSONObject obj = arr.getJSONObject(numero - 1);
                                    final String titre = obj.getString("titreLivre");
                                    final String resume = obj.getString("resumeLivre");
                                    final String id = obj.getString("idLivre");
                                    final String prix = String.valueOf(BigDecimal.valueOf(obj.getDouble("prixLivre")).floatValue());
                                    textView.setText(titre);
                                    textViewDesc.setText(resume);
                                    textViewPrix.setText(prix + " Euros");
                                    Button button = (Button) rootView.findViewById(R.id.buttonAdd);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switch (v.getId()) {
                                                case R.id.buttonAdd:
                                                    if (Connexion.Global.role.equals("") || Connexion.Global.role.equals("[\"ROLE_ADMIN\"]")) {
                                                        CharSequence resultat = "Veuillez vous connecter en tant qu'Utilisateur pour commander ce livre.";
                                                        Toast monMessage = Toast.makeText(getActivity(), resultat, Toast.LENGTH_LONG);
                                                        monMessage.show();
                                                    } else {
                                                        String[] strings = new String[]{titre, resume, prix, id};
                                                        List<String> stringList = Arrays.asList(strings);
                                                        Connexion.Global.contPanier.add(stringList);
                                                        Boolean present = Boolean.FALSE;
                                                        if (Connexion.Global.idQteLivres.length() > 0) {
                                                            Iterator<String> iter = Connexion.Global.idQteLivres.keys();
                                                            while (iter.hasNext()) {
                                                                String key = iter.next();
                                                                if (key.equals(id)) {
                                                                    try {
                                                                        Integer value = Integer.parseInt(Connexion.Global.idQteLivres.get(key).toString());
                                                                        Integer qte = value + 1;
                                                                        present = Boolean.TRUE;
                                                                        Connexion.Global.idQteLivres.put(id, qte);
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        if (present == Boolean.FALSE) {
                                                            try {
                                                                Connexion.Global.idQteLivres.put(id, 1);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        CharSequence resultat = "Merci ! Cet article vient d'être ajouter à votre panier.";
                                                        Toast monMessage = Toast.makeText(getActivity(), resultat, Toast.LENGTH_LONG);
                                                        monMessage.show();
                                                    }
                                                    break;
                                            }
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {        // CAS d’ERREUR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewDesc.setText("Erreur, " + error.getMessage());
                    }
                });
                queue.add(stringRequestGetBook);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
            int nbLivres = Products.Global.nbLivres;
            return nbLivres;
        }
    }
}
