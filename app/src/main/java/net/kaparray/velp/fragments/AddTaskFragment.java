package net.kaparray.velp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    DatabaseReference myRef = database.getReference("task");

    Button mAddTask;
    EditText mTask;
    EditText mValueTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_addtask, container, false);
// Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.AddTaskTitle));

       mAddTask = rootView.findViewById(R.id.btn_addTask);
       mTask = rootView.findViewById(R.id.et_NameTask);
       mValueTask = rootView.findViewById(R.id.et_valueTask);

       mAddTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(mAddTask.getWindowToken(),
                       InputMethodManager.HIDE_NOT_ALWAYS);

               DatabaseReference mUserAccount = myRef.push();
               mUserAccount.child("userTask").setValue(user.getUid());
               mUserAccount.child("nameTask").setValue(mTask.getText().toString());
               mUserAccount.child("valueTask").setValue(mValueTask.getText().toString());
               Toast.makeText(getContext(),"Task add in database", Toast.LENGTH_LONG).show();
           }
       });

        return rootView;
    }
}
