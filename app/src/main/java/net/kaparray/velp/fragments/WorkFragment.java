package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;


public class WorkFragment extends Fragment {


    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    private EditText mEmail;
    private EditText mName;
    private EditText mAbout;
    private Button mButton;
    private TextView mText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_work, container, false);
        ((MainActivity) getActivity()).setTitle(getString(R.string.title_work));

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        mEmail = rootView.findViewById(R.id.et_emailWork);
        mName = rootView.findViewById(R.id.et_nameWork);
        mAbout = rootView.findViewById(R.id.et_aboutWork);
        mText = rootView.findViewById(R.id.tv_TextWork);

        mButton = rootView.findViewById(R.id.btnApplayWork);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbRef = mDatabase.child("Work").push();
                dbRef.child(mEmail.getText().toString());
                dbRef.child(mName.getText().toString());
                dbRef.child(mAbout.getText().toString());
            }
        });


        return rootView;
    }
}
