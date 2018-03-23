package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.R;
import net.kaparray.velp.classes.secretKey;


public class OpenTaskFragment extends Fragment{


    private static final String TAG = "";
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    TextView mNameTask;
    TextView mValueTask;
    TextView mNameUser;

    DatabaseReference name;
    String valueTask;
    String nameTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_opentask, container, false);

        mNameTask = rootView.findViewById(R.id.tv_nameTaskInOpenTask);
        mValueTask = rootView.findViewById(R.id.tv_valueTaskInOpenTask);
        mNameUser = rootView.findViewById(R.id.tv_nameUserInOpenTask);


        secretKey sk = new secretKey();


        Query applesQuery = mDatabase.child("Task").orderByChild("uniqueIdentificator").equalTo(sk.getUniqueIdentificator()) ;


        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    name = appleSnapshot.getRef();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                Toast.makeText(getActivity(), "Ooops! Error database",
                        Toast.LENGTH_SHORT).show();
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                //name = (String) dataSnapshot.child("Task").child(user.getUid()).child("name").getValue();
                valueTask = (String) dataSnapshot.child("Users").child(user.getUid()).child("level").getValue() + "";
                nameTask = (String) dataSnapshot.child("Users").child(user.getUid()).child("helped").getValue() + "";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        };
        mDatabase.addValueEventListener(postListener);



        return rootView;
    }
}
