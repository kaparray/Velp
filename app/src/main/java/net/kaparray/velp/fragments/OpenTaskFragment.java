package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.R;



public class OpenTaskFragment extends Fragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    Button mTakeTask;

    TextView mNameTask;
    TextView mValueTask;
    TextView mNameUser;

    String KEY_Task;
    String AcceptedTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_opentask, container, false);

        mNameTask = rootView.findViewById(R.id.tv_nameTaskInOpenTask);
        mValueTask = rootView.findViewById(R.id.tv_valueTaskInOpenTask);
        mNameUser = rootView.findViewById(R.id.tv_nameUserInOpenTask);
        mTakeTask  =rootView.findViewById(R.id.btn_takeTask);

        // Check data in bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNameTask.setText(bundle.getString("NameTask"));
            mValueTask.setText(bundle.getString("ValueTask"));
            mNameUser.setText(bundle.getString("NameUser"));
            KEY_Task = bundle.getString("TaskKey");

        }


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Fire base
                AcceptedTask =  dataSnapshot.child("Task").child(KEY_Task).child("accept").child("accepted").getValue()+"";


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);

        mTakeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (AcceptedTask.equals("true") ){
                    Toast.makeText(getActivity(), "Занято", Toast.LENGTH_LONG).show();
                }else if (AcceptedTask.equals("false")){
                    mDatabase.child("Task").child(KEY_Task).child("accept").child("accepted").setValue("true");
                    mDatabase.child("Task").child(KEY_Task).child("accept").child("userUID").setValue(user.getUid());
                    Toast.makeText(getActivity(), "Задание взять вами", Toast.LENGTH_SHORT).show();
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

        }



    }
}
