package net.kaparray.velp.utils;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.R;


@SuppressLint("Registered")
public class FirebaseIntegration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "Login";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String emailUser;
    public  String nameUser;



    String photo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {




                photo = dataSnapshot.child("Users").child(user.getUid()).child("photo").getValue() + "";

                String name = dataSnapshot.child("Users").child(user.getUid()).child("name").getValue() + "";

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name).build();
                user.updateProfile(profileUpdates);


                nameUser = user.getDisplayName();
                emailUser = user.getEmail();

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);

                TextView navName = (TextView) headerView.findViewById(R.id.tv_username);
                navName.setText(name);
                TextView navEmail = (TextView) headerView.findViewById(R.id.tv_emainuser);
                navEmail.setText(emailUser);
                ImageView navPhoto =  headerView.findViewById(R.id.imageViewProfile);

                if(photo.equals("ic_boy")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
                } else if(photo.equals("ic_boy1")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
                }else if(photo.equals("ic_girl")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                }else if(photo.equals("ic_girl1")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
                }else if(photo.equals("ic_man1")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
                }else if(photo.equals("ic_man2")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
                }else if(photo.equals("ic_man3")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));
                }else if(photo.equals("ic_man4")){
                    navPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
                }



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
