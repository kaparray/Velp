package net.kaparray.velp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;

import net.kaparray.velp.Auth.AuthActivity;
import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton mButtonTheme;
    Button mSignOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_settings);

        mSignOut = findViewById(R.id.btn_signOut);

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                logOut();
            }
        });



        mButtonTheme = findViewById(R.id.toggleButton);


        SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
        String side = preferences.getString("THEME"," ");

        if (side.equals("dark")){
          mButtonTheme.setChecked(false);
        } else if (side.equals("light")){
            mButtonTheme.setChecked(true);
        }

        mButtonTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getSharedPreferences("theme", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    // The toggle is enabled
                    editor.putString("THEME", "light");

                } else {
                    // The toggle is disabled
                    editor.putString("THEME", "dark");
                }
                editor.apply();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void logOut(){
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

}


//    SharedPreferences preferences = getSharedPreferences("theme", MODE_PRIVATE);
//    SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("THEME","dark");
//                        editor.apply();