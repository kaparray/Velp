package net.kaparray.velp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.transition.Fade;
import android.support.transition.Slide;
import android.support.transition.TransitionInflater;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.fragments.AboutFragment;
import net.kaparray.velp.fragments.AddTaskFragment;
import net.kaparray.velp.fragments.BonusFragment;
import net.kaparray.velp.fragments.EventsFragments;
import net.kaparray.velp.fragments.ProfileFragment;
import net.kaparray.velp.fragments.SettingsFragment;
import net.kaparray.velp.fragments.TaskFragment;
import net.kaparray.velp.utils.FirebaseIntegration;


public class MainActivity extends FirebaseIntegration implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");


    ProfileFragment profileFragment;
    AboutFragment aboutFragment;
    BonusFragment bonusFragment;
    TaskFragment taskFragment;
    SettingsFragment settingsFragment;
    EventsFragments eventsFragment;
    View mNavHeader;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // For settings
        SharedPreferences preferences = getSharedPreferences("view", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("VIEW", "null");
        editorView.apply();
        // For user
        SharedPreferences preferencesUser = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editorViewUser = preferencesUser.edit();
        editorViewUser.putString("USER", "null");
        editorViewUser.apply();
    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// Set Theme
        SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
        String theme = preferences.getString("THEME"," ");

        if (theme.equals("dark")){
            setTheme(R.style.Theme_Design_NoActionBar);
        } else if (theme.equals("light")){
            setTheme(R.style.AppTheme_NoActionBar);
        }

        setContentView(R.layout.ac_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View headerview = navigationView.getHeaderView(0);
        mNavHeader = headerview.findViewById(R.id.LL_profile);

        if (theme.equals("dark")){
            mNavHeader.setBackground(getResources().getDrawable(R.drawable.gradient_dark));
        } else if (theme.equals("light")){
            mNavHeader.setBackground(getResources().getDrawable(R.drawable.gradient));
        }

        aboutFragment = new AboutFragment();
        bonusFragment = new BonusFragment();
        taskFragment = new TaskFragment();
        settingsFragment = new SettingsFragment();
        profileFragment = new ProfileFragment();
        eventsFragment = new EventsFragments();


        mNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                        .replace(R.id.container, profileFragment)
                        .addToBackStack(null)
                        .commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                // Set item in navigation drawer
//                navigationView.setCheckedItem();
            }
        });

        // Set Fragment
        SharedPreferences preferencesView = getSharedPreferences("view",MODE_PRIVATE);
        String view = preferencesView.getString("VIEW"," ");

        if (view.equals("settings")){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, taskFragment)
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, settingsFragment)
                    .addToBackStack(null)
                    .commit();
            // Set item in navigation drawer
            navigationView.setCheckedItem(R.id.nav_settings);
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, taskFragment)
                    .commit();
        }

        // Set item in navigation drawer
        navigationView.setCheckedItem(R.id.nav_task);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("Stat ementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_task) {
            // RecyclerView Task
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, taskFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_map){
            // Map
        } else if (id == R.id.nav_event) {
            // Events
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, eventsFragment)
                    .addToBackStack(null)
                    .commit();
        }else if (id == R.id.nav_rating) {
            // Rating
        } else if (id == R.id.nav_chat) {
            // Chat
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
//                    .replace(R.id.container, chatFragment)
//                    .addToBackStack(null)
//                    .commit();
        } else if (id == R.id.nav_settings){
            // Settings
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, settingsFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_bonus) {
            // Bonus
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, bonusFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_info) {
            // Info
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, aboutFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_share){
            // Share
        } else if (id == R.id.nav_send) {
            // Send
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}