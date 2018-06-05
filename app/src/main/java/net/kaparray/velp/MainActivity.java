package net.kaparray.velp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.fragments.AboutFragment;
import net.kaparray.velp.fragments.BonusFragment;
import net.kaparray.velp.fragments.ChatFragment;
import net.kaparray.velp.fragments.EventsFragments;
import net.kaparray.velp.fragments.MapFragment;
import net.kaparray.velp.fragments.ProfileFragment;
import net.kaparray.velp.fragments.RatingFragment;
import net.kaparray.velp.fragments.SettingsFragment;
import net.kaparray.velp.fragments.Task.AcceptedTaskFragment;
import net.kaparray.velp.fragments.TaskFragment;
import net.kaparray.velp.fragments.chandeDataUser.ChangeDataFragment;
import net.kaparray.velp.utils.FirebaseIntegration;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends FirebaseIntegration implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");


    ProfileFragment profileFragment;
    AboutFragment aboutFragment;
    BonusFragment bonusFragment;
    ChatFragment chatFragment;
    TaskFragment taskFragment;
    SettingsFragment settingsFragment;
    EventsFragments eventsFragment;
    RatingFragment ratingFragment;
    MapFragment mapFragment;
    ChangeDataFragment changeDataFragment;
    View mNavHeader;
    AcceptedTaskFragment acceptedTaskFragment;

    boolean fragmentCounter = true;
    String taskFragmentCounter = "true";
    boolean settingsFragmentCounter = true;
    boolean changeDataFragmentCounter = true;
    boolean mapCard = false;
    private int PERMISSION_CODE = 23;

    String lastFragment;

    @BindView(R.id.nav_view) NavigationView navigationView;


    Animation anim;

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


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);
            Log.d("0000", "WTF");
        }

        //Butter Knife
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


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
        chatFragment = new ChatFragment();
        taskFragment = new TaskFragment();
        settingsFragment = new SettingsFragment();
        profileFragment = new ProfileFragment();
        eventsFragment = new EventsFragments();
        ratingFragment = new RatingFragment();
        mapFragment = new MapFragment();


        // Set Fragment
        SharedPreferences preferencesView = getSharedPreferences("view",MODE_PRIVATE);
        String view = preferencesView.getString("VIEW"," ");

        if (view.equals("settings")){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, settingsFragment)
                    .commit();
            // Set item in navigation drawer
            navigationView.setCheckedItem(R.id.nav_settings);
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, taskFragment)
                    .commit();
            // Set item in navigation drawer
            navigationView.setCheckedItem(R.id.nav_task);
        }


        if(!hasConnection(getApplicationContext())){
            Toast.makeText(MainActivity.this, R.string.noInternet, Toast.LENGTH_LONG).show();
        }

        
    }

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if(mapCard) {
            CardView mCardInfo = findViewById(R.id.cv_TaskMap);
            anim = AnimationUtils.loadAnimation(this,R.anim.animate_top_bottom);
            mCardInfo.startAnimation(anim);

            mCardInfo.setVisibility(View.GONE);

            mapCard = false;
        }else{
            if (!settingsFragmentCounter) {

                changeDataFragment = new ChangeDataFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.container, changeDataFragment)
                        .commit();
                navigationView.setCheckedItem(R.id.nav_settings);

                settingsFragmentCounter = true;
            } else if (settingsFragmentCounter) {

                if (!changeDataFragmentCounter) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                            .replace(R.id.container, settingsFragment)
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_settings);

                    changeDataFragmentCounter = true;

                } else if (changeDataFragmentCounter) {
                    if (taskFragmentCounter.equals("false")) {
                        acceptedTaskFragment = new AcceptedTaskFragment();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.task, acceptedTaskFragment)
                                .commit();

                        taskFragmentCounter = "true";
                    } else if (taskFragmentCounter.equals("true")) {
                        super.onBackPressed();
                    }


                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (!fragmentCounter) {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                                .replace(R.id.container, taskFragment)
                                .commit();
                        navigationView.setCheckedItem(R.id.nav_task);
                        fragmentCounter = true;
                    } else {
                        super.onBackPressed();

                    }

                }
            }
        }

    }


    public boolean isMapCard() {
        return mapCard;
    }

    public void setMapCard(boolean mapCard) {
        this.mapCard = mapCard;
    }

    public boolean isFragmentCounter() {
        return fragmentCounter;
    }

    public void setFragmentCounter(boolean fragmentCounter) {
        this.fragmentCounter = fragmentCounter;
    }

    public String isTaskFragmentCounter() {
        return taskFragmentCounter;
    }

    public void setTaskFragmentCounter(String taskFragmentCounter) {
        this.taskFragmentCounter = taskFragmentCounter;
    }


    public boolean isSettingsFragmentCounter() {
        return settingsFragmentCounter;
    }

    public void setSettingsFragmentCounter(boolean settingsFragmentCounter) {
        this.settingsFragmentCounter = settingsFragmentCounter;
    }


    public boolean isChangeDataFragmentCounter() {
        return changeDataFragmentCounter;
    }

    public void setChangeDataFragmentCounter(boolean changeDataFragmentCounter) {
        this.changeDataFragmentCounter = changeDataFragmentCounter;
    }

    @SuppressWarnings("Stat ementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.nav_profile){
            // Profile
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, profileFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            taskFragmentCounter = "none";
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            // Set item in navigation drawer
//                navigationView.setCheckedItem();
        } else if (id == R.id.nav_task) {
            // RecyclerView Task
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, taskFragment)
                    .commit();
            mapCard = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;

        } else if (id == R.id.nav_map){
            // Map
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, mapFragment)
                    .commit();
            fragmentCounter = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            taskFragmentCounter = "none";
        } else if (id == R.id.nav_event) {
            // Events
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, eventsFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            taskFragmentCounter = "none";
        }else if (id == R.id.nav_rating) {
            // Rating
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, ratingFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            taskFragmentCounter = "none";
        } else if (id == R.id.nav_chat) {
            // Chat
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, chatFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            taskFragmentCounter = "none";
        } else if (id == R.id.nav_settings){
            // Settings
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, settingsFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            taskFragmentCounter = "none";
        } else if (id == R.id.nav_bonus) {
            // Bonus
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, bonusFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            taskFragmentCounter = "none";
        } else if (id == R.id.nav_info) {
            // Info
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, aboutFragment)
                    .commit();
            mapCard = false;
            fragmentCounter = false;
            settingsFragmentCounter = true;
            changeDataFragmentCounter = true;
            taskFragmentCounter = "none";
        } else if (id == R.id.nav_share){
            // Share
        } else if (id == R.id.nav_send) {
            // Send
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }



    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager()
                .beginTransaction()
                .remove(taskFragment)
                .commit();
        finish();
    }
}