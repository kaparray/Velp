package net.kaparray.velp.Auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.utils.ProgressDialogActivity;

public class AuthActivity extends ProgressDialogActivity{

    private FirebaseAuth mAuth;
    AuthEmailFragment emailFragment;
    AuthGoogleFragment GoogleFragment;
    AuthStartFragment startFragment;



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null ){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
        String side = preferences.getString("THEME"," ");

        if (side.equals("dark")){
            setTheme(R.style.Theme_Design_NoActionBar);
        } else if (side.equals("light")){
            setTheme(R.style.AppTheme_NoActionBar);
        }

        setContentView(R.layout.ac_signin);


        startFragment = new AuthStartFragment();
        GoogleFragment = new AuthGoogleFragment();
        emailFragment = new AuthEmailFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                .add(R.id.containerAuth, startFragment)
                .addToBackStack(null)
                .commit();

    }


}