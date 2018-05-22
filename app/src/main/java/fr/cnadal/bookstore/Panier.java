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
import android.widget.Toast;

import java.util.List;

public class Panier extends AppCompatActivity {


    public static class Global {
        public static int sizeArr;
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
        setContentView(R.layout.activity_panier);

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
        getMenuInflater().inflate(R.menu.menu_panier, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_panier, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            TextView textViewResum = (TextView) rootView.findViewById(R.id.section_resume);
            textViewResum.setMovementMethod(new ScrollingMovementMethod());
            TextView textViewPrix = (TextView) rootView.findViewById(R.id.section_prix);
            Button buttonSupp = (Button) rootView.findViewById(R.id.buttonSupp);
            Button buttonAct = (Button) rootView.findViewById(R.id.buttonActions);
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
            if(Global.sizeArr == 0) {
                buttonSupp.setVisibility(View.GONE);
                buttonAct.setVisibility(View.GONE);
                textView.setText("Votre panier est vide !");
            }else{
                buttonSupp.setVisibility(View.VISIBLE);
                buttonAct.setVisibility(View.VISIBLE);
                final int numeroArticle = getArguments().getInt(ARG_SECTION_NUMBER);
                List<String> listStr = Connexion.Global.contPanier.get(numeroArticle - 1);
                final String id = listStr.get(3).toString();
                textView.setText(listStr.get(0));
                textViewResum.setText(listStr.get(1));
                textViewPrix.setText(listStr.get(2) + " Euros.");
                buttonSupp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.buttonSupp:
                                Connexion.Global.contPanier.remove(numeroArticle - 1);
                                Connexion.Global.idQteLivres.remove(id);
                                getActivity().finish();
                                Intent intentPan = new Intent(getActivity(),
                                        Panier.class);
                                startActivity(intentPan);
                                CharSequence resultat = "Cet article vient d'être retirer de votre panier.";
                                Toast monMessage = Toast. makeText (getActivity(), resultat, Toast.LENGTH_LONG);
                                monMessage.show();
                                break;
                        }
                    }
                });
                buttonAct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.buttonActions:
                                getActivity().finish();
                                Intent intentPanAct = new Intent(getActivity(),
                                        PanierActions.class);
                                startActivity(intentPanAct);
                                break;
                        }
                    }
                });
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
            int taille = Connexion.Global.contPanier.size();
            if(taille == 0){
                taille = 1;
                Global.sizeArr = 0;
            }else{
                Global.sizeArr = 1;
            }
            return taille;
        }
    }
}
