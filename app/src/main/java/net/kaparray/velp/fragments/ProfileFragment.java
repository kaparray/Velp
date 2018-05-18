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
    String photo;
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
                photo = (String) dataSnapshot.child("Users").child(user.getUid()).child("photo").getValue() + "";


                // Find all view in fragment
                mLL = rootView.findViewById(R.id.LL_profileBackground);
                mUserName = (TextView) rootView.findViewById(R.id.tv_profileName);
                mUserHelped = (TextView) rootView.findViewById(R.id.tv_profileHelped);
                mUserLevel = (TextView) rootView.findViewById(R.id.tv_profileLevel);
                mUserPoints = (TextView) rootView.findViewById(R.id.tv_profilePoints);
                mPhotoUser = rootView.findViewById(R.id.iv_profilePhoto);



                if(photo.equals("ic_boy")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
                } else if(photo.equals("ic_boy1")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
                }else if(photo.equals("ic_girl")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                }else if(photo.equals("ic_girl1")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
                }else if(photo.equals("ic_man1")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
                }else if(photo.equals("ic_man2")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
                }else if(photo.equals("ic_man3")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));
                }else if(photo.equals("ic_man4")){
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
                }



                mUserName.setText(name);
                mUserHelped.setText(helped);
                mUserLevel.setText(level);
                mUserPoints.setText(points);



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
