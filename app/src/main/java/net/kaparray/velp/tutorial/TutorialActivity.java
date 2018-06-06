package net.kaparray.velp.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import net.kaparray.velp.Auth.AuthActivity;
import net.kaparray.velp.R;


public class TutorialActivity extends AppCompatActivity {



    public class MyAdapter extends FragmentPagerAdapter {
        private Context context = null;

        public MyAdapter(Context context, FragmentManager mgr) {
            super(mgr);
            this.context = context;
        }

        @Override
        public int getCount() {
            return (6);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return (TutorialFragment1.newInstance(position));
                case 1:
                    return (TutorialFragment2.newInstance(position));
                case 2:
                    return (TutorialFragment3.newInstance(position));
                case 3:
                    return (TutorialFragment4.newInstance(position));
                case 4:
                    return (TutorialFragment5.newInstance(position));
                case 5:
                    return (TutorialFragment6.newInstance(position));
                default:
                    return PlaceholderFragment.newInstance(position);
            }



        }

    }



    public void startAuthActivity(View view){
        Intent intent = new Intent(TutorialActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set theme
        SharedPreferences preferencesTheme  = getSharedPreferences("theme",MODE_PRIVATE);
        String theme = preferencesTheme.getString("THEME"," ");

        if (theme.equals("dark")){
            setTheme(R.style.Theme_Design_NoActionBar);
        } else if (theme.equals("light")){
            setTheme(R.style.AppTheme_NoActionBar);
        }

        setContentView(R.layout.ac_tutorial);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            SharedPreferences preferences = getSharedPreferences("TUTORIAL", Context.MODE_PRIVATE);
            if (preferences.getBoolean("activity_executed", false)) {
                Intent intent = new Intent(TutorialActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activity_executed", true);
                editor.apply();
            }
        }


        // Adapter for
        ViewPager pager=(ViewPager)findViewById(R.id.pager);
        pager.setAdapter(new MyAdapter(this, getSupportFragmentManager()));
    }



    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }


        public PlaceholderFragment() {
        }

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
            return inflater.inflate(R.layout.fr_tutorial1, container, false);
        }
    }


}