package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

public class AddTaskFragment extends android.support.v4.app.Fragment{

    private FirebaseAuth mAuth;
    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Task");

    Button mAddTask;
    EditText mTask;
    EditText mValueTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ac_addtask, container, false);


       mAddTask = rootView.findViewById(R.id.btn_addTask);
       mTask = rootView.findViewById(R.id.et_NameTask);
       mValueTask = rootView.findViewById(R.id.et_valueTask);

       mAddTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DatabaseReference mUserAccount = myRef.push();
               mUserAccount.child("userUID").setValue(user.getUid());
               mUserAccount.child("taskUser").setValue(mTask.getText().toString());
               mUserAccount.child("nameTask").setValue(mValueTask.getText().toString());
           }
       });

        return rootView;
    }
}
