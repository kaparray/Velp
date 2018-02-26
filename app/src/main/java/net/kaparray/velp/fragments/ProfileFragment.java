package net.kaparray.velp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

public class ProfileFragment extends Fragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    TextView mUserPosition;
    TextView mUserStatus;
    TextView mUserName;
    String name;
    String positions;
    String status;
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
                name = dataSnapshot.child("Users").child(user.getUid()).child("name").getValue().toString();
                positions = dataSnapshot.child("Users").child(user.getUid()).child("positions").getValue().toString();
                status = dataSnapshot.child("Users").child(user.getUid()).child("status").getValue().toString();


                mUserName = (TextView) rootView.findViewById(R.id.tv_profileName);
                mUserStatus = (TextView) rootView.findViewById(R.id.tv_profileStatus);
                mUserPosition = (TextView) rootView.findViewById(R.id.tv_profilePosition);

                mUserName.setText(name);
                mUserStatus.setText(status);
                mUserPosition.setText(positions);
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
