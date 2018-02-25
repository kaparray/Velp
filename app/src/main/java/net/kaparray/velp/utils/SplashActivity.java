package net.kaparray.velp.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.kaparray.velp.R;
import net.kaparray.velp.tutorial.TutorialActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
        // close splash activity
        finish();
    }
}
//    SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
//    String theme = preferences.getString("THEME"," ");
//
//        if (theme.equals("dark")){
//        setTheme(R.style.SplashTheme);
//    } else if (theme.equals("light")){
//        setTheme(R.style.SplashThemeDark);
