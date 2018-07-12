package net.kaparray.velp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.Auth.AuthActivity;
import net.kaparray.velp.fragments.AboutFragment;
import net.kaparray.velp.fragments.BonusFragment;
import net.kaparray.velp.fragments.ChatFragment;
import net.kaparray.velp.fragments.EventsFragments;
import net.kaparray.velp.fragments.MapFragment;
import net.kaparray.velp.fragments.MessageFragment;
import net.kaparray.velp.fragments.ProfileFragment;
import net.kaparray.velp.fragments.RatingFragment;
import net.kaparray.velp.fragments.SettingsFragment;
import net.kaparray.velp.fragments.ShareFragment;
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
    ShareFragment shareFragment;
    MessageFragment messageFragment;
    ChangeDataFragment changeDataFragment;
    View mNavHeader;


    boolean mapCard = false;
    boolean addTask = true;
    private int PERMISSION_CODE = 23;

    String lastFragment;

    @BindView(R.id.nav_view) NavigationView navigationView;


    Animation anim;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

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
        shareFragment = new ShareFragment();
        messageFragment = new MessageFragment();


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


    // Back button algorithm
    @Override
    public void onBackPressed() {


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        }else{
            if (mapCard) {
                CardView mCardInfo = findViewById(R.id.cv_TaskMap);
                anim = AnimationUtils.loadAnimation(this, R.anim.animate_top_bottom);
                mCardInfo.startAnimation(anim);

                mCardInfo.setVisibility(View.GONE);

                mapCard = false;
            } else {

                if (!addTask) {

                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.container, taskFragment)
                            .commit();

                    navigationView.setCheckedItem(R.id.nav_task);

                    addTask = true;
                } else {
                    super.onBackPressed();
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


    public boolean isAddTask() {
        return addTask;
    }

    public void setAddTask(boolean addTask) {
        this.addTask = addTask;
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


        } else if (id == R.id.nav_map){
            // Map
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, mapFragment)
                    .commit();

        } else if (id == R.id.nav_event) {
            // Events
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, eventsFragment)
                    .commit();
            mapCard = false;

        }else if (id == R.id.nav_rating) {
            // Rating
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, ratingFragment)
                    .commit();
            mapCard = false;

        } else if (id == R.id.nav_chat) {
            // Chat
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, chatFragment)
                    .commit();
            mapCard = false;

        } else if (id == R.id.nav_settings){
            // Settings
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, settingsFragment)
                    .commit();
            mapCard = false;

        } else if (id == R.id.nav_bonus) {
            // Bonus
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, bonusFragment)
                    .commit();
            mapCard = false;

        } else if (id == R.id.nav_info) {
            // Info
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, aboutFragment)
                    .commit();
            mapCard = false;

        } else if (id == R.id.nav_share){
            // Share
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                    .replace(R.id.container, shareFragment)
                    .commit();
            mapCard = false;
        } else if (id == R.id.nav_send) {
            // Send
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
            mapCard = false;

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



}