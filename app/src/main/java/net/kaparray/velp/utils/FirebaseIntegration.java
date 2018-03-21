package net.kaparray.velp.utils;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.kaparray.velp.R;
import net.kaparray.velp.classes.TaskLoader;


@SuppressLint("Registered")
public class FirebaseIntegration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "Login";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String emailUser;
    public  String nameUser;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                nameUser = user.getDisplayName();
                emailUser = user.getEmail();




                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);

                TextView navName = (TextView) headerView.findViewById(R.id.tv_username);
                navName.setText(nameUser);
                TextView navEmail = (TextView) headerView.findViewById(R.id.tv_emainuser);
                navEmail.setText(emailUser);
                ImageView navPhoto =  headerView.findViewById(R.id.imageViewProfile);
                String imgUrl =  user.getPhotoUrl()+"";
                Glide.with(getApplicationContext()).load(imgUrl).into(navPhoto);



                Log.w("Connect to db", "Data user add to MainActivity: " + nameUser + " " + emailUser);

                SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editorView = preferences.edit();
                editorView.putString("USER", nameUser);
                editorView.apply();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);



    }

}
