package fr.cnadal.bookstore;

import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Products extends AppCompatActivity {

    public static class Global {
        public static String nomSaga;
        public static int nbLivres;
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
        setContentView(R.layout.activity_products);

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
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
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
            View rootView = inflater.inflate(R.layout.fragment_products, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_Titre);
            TextView textViewAuteur = (TextView) rootView.findViewById(R.id.section_auteur);
            TextView textViewDesc = (TextView) rootView.findViewById(R.id.section_description);
            textViewDesc.setMovementMethod(new ScrollingMovementMethod());
            Button button1 = (Button) rootView.findViewById(R.id.buttonRet);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.buttonRet:
                            getActivity().finish();
                            break;
                    }
                }
            });
            try {
                JSONArray arr = new JSONArray(MenuAccueil.Global.sagas);
                int numero = getArguments().getInt(ARG_SECTION_NUMBER);
                JSONObject obj = arr.getJSONObject(numero - 1);
                String titre = obj.getString("titreSaga");
                String auteur = obj.getString("auteurSaga");
                textViewAuteur.setText(auteur);
                textView.setText(titre);

                JSONArray arr2 = new JSONArray(MenuAccueil.Global.livres);
                JSONObject obj2 = arr2.getJSONObject(numero - 1);
                JSONObject titre2 = obj2.getJSONObject("sagaLivres");
                String title = titre2.getString("titreSaga");
                if (numero != 1) {
                    while (!title.equals(titre)) {
                        obj2 = arr2.getJSONObject(numero - 1);
                        titre2 = obj2.getJSONObject("sagaLivres");
                        title = titre2.getString("titreSaga");
                        numero += 1;
                    }
                } else {
                    obj2 = arr2.getJSONObject(numero - 1);
                }
                String description = obj2.getString("resumeLivre");
                textViewDesc.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Button button = (Button) rootView.findViewById(R.id.buttonLivres);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.buttonLivres:
                            int numSaga = getArguments().getInt(ARG_SECTION_NUMBER);
                            try {
                                JSONArray arr = new JSONArray(MenuAccueil.Global.sagas);
                                JSONObject obj = arr.getJSONObject(numSaga - 1);
                                Global.nomSaga = obj.getString("titreSaga");
                                Global.nbLivres = obj.getInt("nbLivresSaga");
                                Intent intentLivres = new Intent(getActivity(), Livres.class);
                                startActivity(intentLivres);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        break;
                }
            }
        });
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
        int nbSaga = MenuAccueil.Global.nbSagas;
        return nbSaga;
    }
}
}
