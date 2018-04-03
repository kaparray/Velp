package net.kaparray.velp.fragments;


import android.accounts.Account;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    TextView mUserPoints;
    TextView mUserHelped;
    TextView mUserName;
    TextView mUserLevel;
    String name;
    String level;
    String helped;
    String points;
    LinearLayout mLL;
    ImageView mPhotoUser;
    public static final String TAG = "Points";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_profile, container, false);
// Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.ProfileTitle));

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                name = (String) dataSnapshot.child("Users").child(user.getUid()).child("name").getValue();
                level = (String) dataSnapshot.child("Users").child(user.getUid()).child("level").getValue() + "";
                helped = (String) dataSnapshot.child("Users").child(user.getUid()).child("helped").getValue() + "";
                points = (String) dataSnapshot.child("Users").child(user.getUid()).child("points").getValue() + "";


                // Find all view in fragment
                mLL = rootView.findViewById(R.id.LL_profileBackground);
                mUserName = (TextView) rootView.findViewById(R.id.tv_profileName);
                mUserHelped = (TextView) rootView.findViewById(R.id.tv_profileHelped);
                mUserLevel = (TextView) rootView.findViewById(R.id.tv_profileLevel);
                mUserPoints = (TextView) rootView.findViewById(R.id.tv_profilePoints);
                mPhotoUser = rootView.findViewById(R.id.iv_profilePhoto);


                // Get theme
                SharedPreferences preferences = getActivity().getSharedPreferences("theme",MODE_PRIVATE);
                String theme = preferences.getString("THEME"," ");

                if (theme.equals("dark")){
                    mLL.setBackground(getResources().getDrawable(R.drawable.gradient_dark));
                    mUserName.setTextColor(getResources().getColor(R.color.white));
                    mUserHelped.setTextColor(getResources().getColor(R.color.white));
                    mUserLevel.setTextColor(getResources().getColor(R.color.white));
                    mUserPoints.setTextColor(getResources().getColor(R.color.white));
                } else if (theme.equals("light")){
                    mLL.setBackground(getResources().getDrawable(R.drawable.gradient));
                    mUserName.setTextColor(getResources().getColor(R.color.white));
                    mUserHelped.setTextColor(getResources().getColor(R.color.black));
                    mUserLevel.setTextColor(getResources().getColor(R.color.black));
                    mUserPoints.setTextColor(getResources().getColor(R.color.black));
                }

                mUserName.setText(name);
                mUserHelped.setText(helped);
                mUserLevel.setText(level);
                mUserPoints.setText(points);


                // Get theme
                SharedPreferences preferencesGoogle = getActivity().getSharedPreferences("userType",MODE_PRIVATE);
                String userType = preferencesGoogle.getString("userType"," ");
                // get photo in Google account and set in profile
                if (userType.equals("google")) {
                    String imgUrl = user.getPhotoUrl() + "";
                   // Glide.with(getContext()).load(imgUrl).into(mPhotoUser);
                } else {
                    // Get user photo in firebase
                }
                
                // Write in Log connection in Firebase
                Log.w("Connect to db", "Data user aad in Profile Fragment: " + name);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

        return rootView;
    }

}
