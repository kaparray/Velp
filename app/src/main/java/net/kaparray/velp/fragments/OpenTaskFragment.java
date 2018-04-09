package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import net.kaparray.velp.classes_for_data.OpenTaskLoader;

import static net.kaparray.velp.fragments.ProfileFragment.TAG;


public class OpenTaskFragment extends Fragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    Button mTakeTask;

    TextView mNameTask;
    TextView mValueTask;
    TextView mNameUser;

    String KEY_Task;
    String userUID;
    String points;
    String userTakeUID;
    String accepted;

    int pointsUserInt;
    int pointsInt;


    int counter = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_opentask, container, false);






        mNameTask = rootView.findViewById(R.id.tv_nameTaskInOpenTask);
        mValueTask = rootView.findViewById(R.id.tv_valueTaskInOpenTask);
        mNameUser = rootView.findViewById(R.id.tv_nameUserInOpenTask);
        mTakeTask  =rootView.findViewById(R.id.btn_universalButtonTask);







        // Check data in bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNameTask.setText(bundle.getString("NameTask"));
            mValueTask.setText(bundle.getString("ValueTask"));
            mNameUser.setText(bundle.getString("NameUser"));
            KEY_Task = bundle.getString("TaskKey");
            userUID = bundle.getString("userUID");
            points = bundle.getString("points");
            userTakeUID = bundle.getString("userTakeUID");
            accepted = bundle.getString("accepted");

        }






            // On click
            mTakeTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(userUID.equals(user.getUid())){
                        mTakeTask.setText(getString(R.string.Finish));

                        if(accepted.equals("true")) {
                            ValueEventListener postListener = new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (counter != 1) {
                                        // Get data in fire base🔥, calculate and post in database
                                        String pointsUser = dataSnapshot.child("Users").child(userTakeUID).child("points").getValue(String.class);
                                        pointsUserInt = Integer.parseInt(pointsUser);
                                        pointsInt = Integer.parseInt(points);

                                        mDatabase.child("Users").child(userTakeUID).child("points").setValue(pointsUserInt + pointsInt + "");

                                        counter++;

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            mDatabase.addValueEventListener(postListener);


                        }else if(accepted.equals("false")){
                            Toast.makeText(getActivity(), "Данную задачу никто не подтвердил",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("true");
                        mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue(user.getUid());
                        Toast.makeText(getActivity(), "Вы взяли данную задачу",Toast.LENGTH_SHORT).show();

                    }
                }
            });




        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle.putString("NameTask", "");
            bundle.putString("ValueTask", "");
            bundle.putString("NameUser", "");
            bundle.putString("TaskKey", "");
            bundle.putString("userTakeUID", "");
            bundle.putString("accepted", "");
        }



    }
}
