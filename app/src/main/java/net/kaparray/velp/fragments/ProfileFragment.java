package net.kaparray.velp.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // View in fragment
    @BindView(R.id.tv_profilePoints) TextView mUserPoints;
    @BindView(R.id.tv_profileHelped) TextView mUserHelped;
    @BindView(R.id.tv_profileName) TextView mUserName;
    @BindView(R.id.tv_profileLevel) TextView mUserLevel;
    @BindView(R.id.iv_profilePhoto) ImageView mPhotoUser;
    @BindView(R.id.btn_ProfileCall) CardView mCall;
    @BindView(R.id.btn_ProfileMessage) CardView mMessage;

    View rootView;

    // Variables
    boolean usver = false;
    String userUID;
    String name;
    String level;
    String helped;
    String points;
    String photo;
    String phone;
    public static final String TAG = "Points";


    @OnClick(R.id.btn_ProfileCall)
    void call(){
        if((user.getUid()+"").equals(userUID)) {
            Toast.makeText(this.getActivity(), getResources().getString(R.string.callMe), Toast.LENGTH_SHORT).show();
        }else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 24);
                Log.d("0000", "WTF");

            } else {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        }

    }


    @OnClick(R.id.btn_ProfileMessage)
    void message(){
        if((user.getUid()+"").equals(userUID)) {
            Toast.makeText(this.getActivity(), getResources().getString(R.string.messageMe), Toast.LENGTH_SHORT).show();
        }else{
            // Open message fragmet and open chat with this man

            Intent smsMsgAppVar = new Intent(Intent.ACTION_VIEW);
            smsMsgAppVar.setData(Uri.parse("sms:" + phone));
            smsMsgAppVar.putExtra("sms_body", getResources().getString(R.string.smsText));
            startActivity(smsMsgAppVar);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fr_profile, container, false);

        ((MainActivity) getActivity()).setTitle(getString(R.string.ProfileTitle)); // Add title

        ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).setAddTask(false);



        // Check data in bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            usver = true;
            userUID = bundle.getString("UserUID");
        }


        if(!usver){
            getAndSetUserData(user.getUid());
            userUID = user.getUid();
        }else{
            getAndSetUserData(userUID);
        }

        return rootView;
    }




    void getAndSetUserData(final String userUid){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                name = (String) dataSnapshot.child("Users").child(userUid).child("name").getValue();
                phone = (String) dataSnapshot.child("Users").child(userUid).child("phone").getValue();
                level = (String) dataSnapshot.child("Users").child(userUid).child("level").getValue() + "";
                helped = (String) dataSnapshot.child("Users").child(userUid).child("helped").getValue() + "";
                points = (String) dataSnapshot.child("Users").child(userUid).child("points").getValue() + "";
                photo = (String) dataSnapshot.child("Users").child(userUid).child("photo").getValue() + "";




                // Set photo user
                try {
                    if (photo.equals("ic_boy")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
                    } else if (photo.equals("ic_boy1")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
                    } else if (photo.equals("ic_girl")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                    } else if (photo.equals("ic_girl1")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
                    } else if (photo.equals("ic_man1")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
                    } else if (photo.equals("ic_man2")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
                    } else if (photo.equals("ic_man3")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));
                    } else if (photo.equals("ic_man4")) {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
                    }else if(photo.equals("demo")){
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_image2vector));
                    }
                }catch (Exception w){
                    try {
                        mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                    }catch (Exception e){
                        Log.d("Error-Message", e.getStackTrace() + "");
                    }

                }
                // Set data to user
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
    }

}
