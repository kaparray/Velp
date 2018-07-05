package net.kaparray.velp.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.kaparray.velp.fragments.ProfileFragment.TAG;


public class BonusFragment extends android.support.v4.app.Fragment {

    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    long timeSec;
    int points;
    Date date;

    @BindView(R.id.btn_freePoints) Button mFreePoints;


    @OnClick(R.id.btn_freePoints)
    void clickFreePoints(){
        date = new Date();

        long timeSecCalc = (date.getTime() - timeSec) / 1000;
        if(timeSecCalc < 864000){
            Toast.makeText(getActivity(), getString(R.string.littleTime), Toast.LENGTH_SHORT).show();
        }else if(points >= 10){
            Toast.makeText(getActivity(), getString(R.string.manyPoints), Toast.LENGTH_SHORT).show();
        }else if(timeSecCalc > 864000 && points < 10) {
            points = points + 10;
            mDatabase.child("Users").child(user.getUid()).child("lastFreePoints").setValue(date.getTime() + "");
            mDatabase.child("Users").child(user.getUid()).child("points").setValue(points + "");
        }
    }


    // go to Firebase and check data about free points
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                timeSec = Long.parseLong((String)dataSnapshot.child("Users").child(user.getUid()).child("lastFreePoints").getValue());
                points = Integer.parseInt((String)dataSnapshot.child("Users").child(user.getUid()).child("points").getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_bonus, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.BonusTitle));


        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        ButterKnife.bind(this, rootView);




        return rootView;
    }
}
