package net.kaparray.velp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import net.kaparray.velp.fragments.AboutFragment;
import net.kaparray.velp.fragments.BonusFragment;
import net.kaparray.velp.fragments.SettingsFragment;
import net.kaparray.velp.fragments.TaskFragment;
import net.kaparray.velp.utils.FirebaseIntegration;


public class MainActivity extends FirebaseIntegration implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");


    AboutFragment aboutFragment;
    BonusFragment bonusFragment;
    TaskFragment taskFragment;
    SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        aboutFragment = new AboutFragment();
        bonusFragment = new BonusFragment();
        taskFragment = new TaskFragment();
        settingsFragment = new SettingsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, taskFragment)
                .addToBackStack(null)
                .commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, settingsFragment)
                    .addToBackStack(null)
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.nav_task) {
            // RecyclerView Task
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, taskFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_map){
            // Map
        } else if (id == R.id.nav_rating) {
            // Rating
        } else if (id == R.id.nav_chat) {
            // Chat
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.container, chatFragment)
//                    .addToBackStack(null)
//                    .commit();
        } else if (id == R.id.nav_bonus) {
            // Bonus
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, bonusFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            // Settings
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, settingsFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_info) {
            // Info
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, aboutFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_share){
            // Share
        } else if (id == R.id.nav_send){
            // Send
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
